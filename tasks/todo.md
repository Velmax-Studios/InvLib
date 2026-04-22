# Task: Fix JUnit Build Failure

The build is failing because `useJUnitPlatform()` is enabled in `build.gradle.kts` but no JUnit dependencies are provided.

## Plan

- [x] Add JUnit 5 dependencies to `build.gradle.kts`
- [x] Verify build with `gradle build`
- [x] Document results
- [x] Remove `DemoPlugin.java`
- [x] Remove `ShowcasePlugin.java` and showcase package
- [x] Remove `paper-plugin.yml`

## Implementation Details

### build.gradle.kts
- Added JUnit BOM (5.11.2) and Jupiter dependency.
- Added `junit-platform-launcher` to resolve executor issues.

### Deletions
- `src/test/java/me/velmax/invlib/DemoPlugin.java`
- `src/main/java/me/velmax/invlib/showcase/ShowcasePlugin.java`
- `src/main/resources/paper-plugin.yml`

## Verification Plan

### Automated Tests
- Ran `gradle build`.
- Status: SUCCESSFUL.

## Review
- Build success: [x]
- Cleanup complete: [x]
- Lessons learned updated: [n/a]
