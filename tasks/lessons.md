# InvLib Development Lessons

## 🛠️ Compilation & Refactoring
*   **Lesson**: When adding Javadocs or refactoring multiple files simultaneously, always verify that field names and method signatures match the current state of the codebase.
*   **Context**: Broken `MenuBuilder.java` by using a mix of "BaseMenu-as-field" and "BaseMenu-as-local-variable" logic in the same class.
*   **Prevention**: Use `gradle build` frequently during refactoring phases, especially before adding documentation or final polish.

## 📦 Paper API Compatibility
*   **Lesson**: Prefer `InventoryView` methods over `Inventory` methods for specialized inventory data (like Anvil rename text) to avoid deprecation warnings.
*   **Context**: `AnvilInventory.getRenameText()` is deprecated in Paper 1.21.2+; use `event.getView().getRenameText()` instead.

## 🚀 CI/CD Automation
*   **Lesson**: Always include the Gradle wrapper (`gradlew`) in the repository to ensure CI environments (GitHub Actions) can execute builds without requiring pre-installed global Gradle versions.
*   **Context**: Initial GitHub Action failed because `gradlew` was missing from the repository.

## 📦 Shading & Distribution
*   **Lesson**: When creating a standalone plugin that depends on a library (like InvLib), you must **shade** (bundle) the library into the plugin JAR using a tool like the Gradle Shadow plugin.
*   **Context**: The example plugin failed with `NoClassDefFoundError` because the `InvLib` classes weren't included in the final JAR.
