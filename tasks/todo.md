# InvLib Project Tracking

## 🛠️ Recent Accomplishments

### Foundation & Cleanup
- [x] Configure JUnit 5 dependencies in `build.gradle.kts`
- [x] Remove legacy `DemoPlugin` and `ShowcasePlugin` code
- [x] Initialize Git repository and perform initial commits

### Framework Evolution
- [x] **Layout System**: Implement string-pattern-based inventory design (e.g., `layout("###...###")`).
- [x] **Architecture Refactor**: Separate `Gui` (BaseMenu) from `Window` (MenuWindow) to support dynamic titles and better session management.
- [x] **Specialized Menus**: Refactor `PagedMenu` and `MerchantMenu` to align with the new Window system.

## 🚀 Future Roadmap

### Enhancements
- [ ] Implement specialized inventory types (Anvil, Crafting Table, etc.)
- [ ] Add support for shared menus (multiple viewers per BaseMenu)
- [ ] Implement animation framework for items and titles
- [ ] Add built-in button presets (e.g., Close, Back, Next Page)

### Documentation & Quality
- [ ] Create a comprehensive `README.md` with usage examples
- [ ] Implement unit tests for `Layout` and `BaseMenu` logic
- [ ] Add Javadoc to all public API methods

## 📋 Technical Context
- **Base Directory**: `/home/sayan/IdeaProjects/InvLib/`
- **Current Build Status**: **SUCCESSFUL**
- **Last Verified**: 2026-04-22
