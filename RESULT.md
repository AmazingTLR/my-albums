# RESULT

This document answers the requirements of `README.md`. For each topic it states **what was done**, **why**, and **what alternative could be used** if the context changes. The forward-looking roadmap lives in [`EVOLUTION_PLAN.md`](EVOLUTION_PLAN.md).

## 1. Architecture and patterns

**MVVM with unidirectional data flow**, in a layered chain:

```
Screen -> ViewModel -> UseCase -> Repository (api) -> LocalDataSource (Room) / NetworkDataSource (Retrofit)
```

- **UiState** is a sealed interface (`Loading`, `Success`, `LoadFailed`). The ViewModel exposes it as a `StateFlow` built with `stateIn(viewModelScope, WhileSubscribed(5_000), Loading)`.
- **Offline-first, single source of truth.** The UI only reads from the database. The network only refreshes it. A successful fetch replaces the stored data, so the cache never drifts from the server.
- **Use cases** are single-purpose and return a nested sealed `Result`.
- **Stateful/stateless screens.** Each screen has a stateful overload and an `internal` stateless overload. The stateless one is used for UI and screenshot tests.
- **Errors.** An `AppException` hierarchy is established. Library calls are wrapped (`safeApiCall`, `SafeDb`) and `CancellationException` is always rethrown.

*Why:* a thin and testable layering where each class has one reason to change. The ViewModel never knows about Room or Retrofit.

*Alternatives:*
- **MVI** with a reducer, if screens get several interacting events. Here the screens are simple enough that MVVM stays lighter.
- **Skip use cases** and call repositories from ViewModels. Less code, but the use cases are cheap here and keep business rules (e.g. toggling a favorite) out of the ViewModels and reusable across features.

## 2. Bugs and traps found and fixed

Found in the initial project:

| Problem in the original code | Why it is a problem | Fix |
|---|---|---|
| `GlobalScope.launch` in `AlbumsViewModel` | Work outlives the ViewModel and cannot be cancelled. Leaks and wasted work. | `viewModelScope` through `stateIn`. `GlobalScope` is banned by project rules. |
| `catch (_: Exception) { /* TODO */ }` | Errors silently swallowed. Cancellation also swallowed. The user sees an empty screen. | Typed `AppException`, `LoadFailed` state with an error screen, `CancellationException` rethrown. |
| `MutableSharedFlow` without replay, collected with `emptyList()` as initial value | After rotation or resubscription the list is lost and the screen is empty until another emission. | `StateFlow` with an explicit `Loading` initial state. |
| Logging interceptor guarded by `if (!BuildConfig.DEBUG)` | Logging was inverted: full response `BODY` logged in **release**, nothing in debug. A leak and a perf cost. | Logging only when `BuildConfig.DEBUG` is passed to `createNetworkModule`. |
| `AnalyticsHelper` stores a `Context` in a long-lived singleton | Leaks an Activity. Also writes the selected item to SharedPreferences on every click (disk IO in a tracking call). | `:core:analytics` split into `api`/`impl` (`AnalyticsLogger`, stub implementation). No Context held. |
| `DetailsActivity` started with a bare `Intent` | No item id was passed, so a detail screen could not show anything. | Navigation 3 with a typed `NavKey` carrying the album id. |
| Duplicate `LAUNCHER` intent filter | Ambiguous entry point. | Single launcher activity. |
| Unused `androidTest` dependencies and template instrumented test | Dead weight in the build. | Removed. |
| Hardcoded strings and dp | Not themable, not localizable. | `strings.xml` per module and `AppTheme` tokens. |
| Manual DI (`AppDependencies` with `lazy` singletons) | Not easily testable, no scoping, tight coupling. | Koin modules. |

*Note:* the original `AlbumItem` also passed the parent `modifier` to the inner `AsyncImage`, applying size and padding modifiers twice. The new `AlbumItem` in `:core:ui` does not do this.

## 3. Multi-module structure

```
:app                                 wiring only (Application, Activity, DI start, navigation graph)
:core:model | common | testing       pure Kotlin/Android basics
:core:network:{api,retrofit}         NetworkDataSource contract / Retrofit implementation
:core:database:{api,room}            local data source contracts / Room implementation
:core:datastore:{api,preferences}    user preferences contract / DataStore implementation
:core:repository:{api,impl}
:core:usecase
:core:analytics:{api,impl}
:core:navigation | designsystem | ui
:feature:{albums,details,favorites,settings}:{api,impl}
```

Rules:
- Features depend on `*:api` modules only.
- Only `:app` depends on `repository:impl`, `network:retrofit`, `database:room`, `analytics:impl`.
- Spark is used only inside `:core:designsystem`. Other modules use the `App*` wrappers.
- Build config lives in **convention plugins**.

*Why:*
- **Enforced boundaries.** An `impl` is not on the classpath of its consumers, so a feature cannot reach into Room or Retrofit by accident. The compiler enforces it.
- **Build speed.** Changing an `impl` does not recompile its dependents. Modules build in parallel and are cached.
- **Swappable implementations.** Retrofit could be replaced by Ktor, or Room by SQLDelight, by touching one module plus `:app` wiring. Future-proof, long-term evolutions are easier to handle.
- **Demonstration.** The README asks to observe the multi-module aspect. The project is deliberately more split than a feature of this size strictly needs.

*Alternatives and cost:*
- A single `:feature` + `:core` + `:data` split would be far less boilerplate (25 modules is heavy for 4 screens). It is the better option if the team is small and the app will not grow.
- `api`/`impl` doubles the module count. The roadmap proposes an automated check of these dependency rules (`EVOLUTION_PLAN.md`, P2).

## 4. Dependency injection: Koin

One `val xModule` per module in `di/`, all started in `PhotoApp`. ViewModels use `koinViewModel()`.

*Why Koin:*
- No code generation, faster builds and simpler module setup.
- First-class Compose and ViewModel support, easy to override in tests.
- Kotlin Multiplatform ready, in case the data and domain layers are shared later.

*Alternatives:*
- **Hilt/Dagger:** compile-time graph validation, stronger on very large teams and codebases. The price is KSP/kapt time and more boilerplate with multi-module `api`/`impl`.
- **Manual DI** (what the original code did): fine for tiny apps, but it does not scale or scope.
- **Koin risk:** the graph is checked at runtime. Mitigation: Koin's `verify()` check can be added to `:app` tests (not done in this project).

## 5. Libraries

| Need | Choice | Why | Alternative |
|---|---|---|---|
| Design system | **Spark**, wrapped in `:core:designsystem` | The starting project used Spark. Wrapping it (`AppTheme`, `AppChip`, `AppIconToggleButton`, ...) keeps vendor code in one module, so a theme change or a swap touches one place. | Use Material 3 directly: less indirection, but ties every module to one library. |
| Navigation | **Navigation 3** | The back stack is plain state owned by the app. Typed `NavKey`s live in `feature:x:api`, so features navigate without depending on each other's `impl`. Supports per-tab back stacks (`NavigationState`) and adaptive scenes later. | Navigation Compose 2.x: more mature, string/route based, and feature modules must share a graph. Activities: what the original code did, which makes config changes and shared state harder. |
| Network | **Retrofit 3 + OkHttp 5 + kotlinx-serialization** | Retrofit gives a typed interface with a tiny surface. | Ktor client (better if going KMP), Moshi, Gson... |
| Images | **Coil 3** | Compose-native, coroutine-based, lifecycle-aware. | Glide (heavier, larger API), Picasso. |
| Persistence | **Room** (albums, favorites) + **DataStore Preferences** (theme) | See section 6. | SQLDelight (KMP friendly), Realm. |
| Async | Coroutines + Flow | Standard. | RxJava, discutable for a new project in 2026 |
| DI | Koin | See section 4. | Hilt |
| Build | Version catalog + convention plugins | One place for versions and boilerplate. | `buildSrc` (invalidates the whole build on change). |

## 6. Persistence, favorites, details, configuration changes

Albums are cached in **Room** (grouping done in SQL) and shown immediately on launch, then refreshed in the background. 
A failed refresh is not an error while stored data is displayed. 

Favorites live in a **separate table keyed by photo id** so they survive each `replaceAll` sync. 

A DataStore-backed dark mode setting complete the features. 

State is held in `ViewModel`s as `StateFlow` with `WhileSubscribed(5_000)`, with saveable navigation state, rotation and process death keep the screen without refetching.

## 7. Tests

A pyramid, from the base up:

| Level | Tools | What is covered |
|---|---|---|
| Unit | JUnit4, mockk, Turbine, coroutines-test, shared `CoroutinesRule` (`:core:testing`) | ViewModels, all use cases, repositories, app states |
| Data | MockWebServer, Robolectric + in-memory Room | `RetrofitNetworkDataSource`, Room data sources, DataStore preferences |
| Component UI | Compose UI tests (on the JVM via Robolectric) | Screens and shared components |
| Screenshot | Roborazzi with committed screenshots | Design-system components, list/detail/favorites screens |

*Why:*
- Most logic sits in ViewModels, use cases and the repository, so that is where the most tests are.
- Running Room and Compose tests on the JVM (Robolectric) avoids a device or emulator, so `./gradlew test` is enough.
- Screenshot tests protect the design-system wrappers against visual regressions.

Run everything with: `./gradlew test lint assembleDebug`.

**Known gaps:**
- No end-to-end instrumented test (launch -> open album -> favorite -> Favorites tab).
- No CI pipeline, so nothing runs the tests or checks that screenshots are up to date on a push.
- No static analysis (detekt/ktlint) yet.

## 8. Trade-offs and limitations

- **Module count vs. app size.** 25 modules for 4 screens is more structure than the problem needs, chosen to show the architecture.
- **`replaceAll` on every sync** is simple and consistent, but it rewrites ~5000 rows each time. An incremental upsert-and-prune would be cheaper.
- **No pagination** on the details screen. Fine for current album sizes.
- **Analytics is a stub** (`StubAnalyticsLogger`). Needs a real provider.
- **English strings only.**

## 9. Planning the evolutions

See the roadmap in [`EVOLUTION_PLAN.md`](EVOLUTION_PLAN.md)