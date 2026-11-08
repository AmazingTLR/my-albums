# Evolution plan

This document lists what should come next for the app, in priority order, and why.

### P0: robustness and performance

- **Continuous integration.** The project has no CI configuration today. Add a pipeline that runs test checks on every push and pull request, so regressions and stale screenshots are caught before merge. Later, add a release build job.
- **Refresh policy.** Pull-to-refresh exists, but every time the albums screen is subscribed again, the full payload is re-downloaded. Add a freshness window: skip the network if the last successful sync is under N minutes old.
- **Pagination or incremental loading in the UI.** The payload is about 5000 photos. Room already serves grouped queries, so the UI is cheap. However, a detail screen with thousands of rows should use paged loading if the data set grows.
- **Image loading tuning.** Define a Coil `ImageLoader` with memory and disk cache. placeholders and error images rules. Should be part of the design system.
- **Room schema migrations.** Before the first schema change, add a `MigrationTestHelper` test and stop relying on destructive fallback.
- **Validate the R8 release build.** Minification is on, but the release APK has only been built, not exercised. Add a smoke test on the minified build (launch, list, detail, favorite) and review keep rules if anything reflective is added.

### P1: product features

- **Search and filter** on the albums list.
- **Large-screen layouts.** The favorites and detail screens could use a list-detail layout on tablets and foldables. Actionable quickly since Navigation 3 supports adaptive scenes.
- **End-to-end instrumented test** in `:app`: launch, open an album, favorite a photo, check the Favorites tab. Run it on a Gradle managed device in a separate, slower CI job.
- **Feature flags** for risky features.
- **Accessibility pass:** content descriptions, touch target sizes, font scaling and TalkBack.

### P2: platform and maintainability

- **Dependency and version hygiene:** automated updates (Renovate or Dependabot) and a version catalog check in CI.
- **Static analysis:** detekt and ktlint, wired into the convention plugins so modules stay free of boilerplate.
- **Module dependency checks:** a test or Gradle rule that fails the build if a feature depends on `*:impl` of another module, to enforce the architecture rules.
- **Crash and analytics reporting:** `core:analytics:impl` is the single place to plug a real provider in. Should we need a new implementation, the architecture allows it.
- **Localisation:** strings are English only today. Move to resource-based translations when other languages support is needed.