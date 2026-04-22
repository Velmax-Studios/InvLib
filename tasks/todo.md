# InvLib Project Roadmap

InvLib is a modern, lightweight inventory management library for Paper 1.21.2+.

## ✅ Completed Tasks

### Core Architecture
- [x] **Gui & Window Separation**: Decoupled menu logic (`BaseMenu`) from player view (`MenuWindow`).
- [x] **Multi-Viewer Support**: Real-time synchronization across multiple players viewing the same menu.
- [x] **Layout System**: Visual, string-pattern-based inventory design.

### Specialized Menus
- [x] **Paged Menu**: Support for large content sets with built-in navigation.
- [x] **Merchant Menu**: Support for virtual villager trading.
- [x] **Anvil Menu**: Support for user input via virtual anvils.
- [x] **Crafting Menu**: Support for 3x3 virtual crafting grids.

### Features & Polish
- [x] **Animation Framework**: Dynamic, frame-based item animations with an optimized global ticker.
- [x] **Button Presets**: Built-in `Close`, `Back`, and `Navigation` buttons.
- [x] **Item Builder**: Fluent API for creating and modifying ItemStacks.
- [x] **Example Plugin**: Comprehensive showcase in `example/` folder.
- [x] **API Polish**: Added `filler` preset and `setButton` convenience methods.
- [x] **Stability Fixes**: Resolved MenuBuilder inconsistencies and updated Anvil API usage.

### Documentation
- [x] **README.md**: Comprehensive guide with code examples.
- [x] **Javadocs**: Document all public API methods and classes.
- [x] **Wiki**: Premium HTML + Tailwind CSS documentation in `wiki/` folder.

### CI/CD & Distribution
- [x] **JitPack Support**: Configured maven-publishing for easy distribution.
- [x] **GitHub Actions**: Automated CI builds and Wiki deployment.

## 🚀 Future Roadmap

### Advanced Features
- [ ] **Title Animations**: Support for animated inventory titles.
- [ ] **Inventory Persistence**: Options to save/restore menu states.
- [ ] **Remote Menus**: Open menus for players from other servers (Redis/Velocity).

### Testing & Stability
- [ ] **Unit Tests**: Coverage for Layout mapping and Page logic.
- [ ] **Integration Tests**: Verify event routing under heavy load.
