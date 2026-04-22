# InvLib 📦

A modern, super-lightweight, and high-performance inventory management library exclusively for **Paper 1.21.2+**.

InvLib is designed to leverage the latest Paper APIs, providing a clean, fluent interface for developers to create complex, dynamic, and animated GUIs with minimal overhead.

## ✨ Key Features

*   🎨 **Visual Layout System**: Design your menus using string patterns (e.g., `layout("###...###")`).
*   👥 **Shared Menus**: Built-in support for multiple players viewing and interacting with the same menu instance in real-time.
*   ⚡ **High Performance**: Decoupled Gui/Window architecture with optimized event routing.
*   🎬 **Animation Framework**: Frame-based item animations with a global ticker that only runs when needed.
*   🛠️ **Specialized Menus**: Simplified APIs for **Anvils** (rename tracking), **Crafting Grids** (3x3), **Merchants**, and **Paged** content.
*   🚀 **Developer Friendly**: Fluent `MenuBuilder` and `ItemBuilder` APIs with built-in presets for common buttons (Close, Back, Navigation).

## 🚀 Getting Started

### 1. Initialize the Library
Initialize InvLib in your plugin's `onEnable`:

```java
@Override
public void onEnable() {
    InvLib.init(this);
}
```

### 2. Create a Simple Menu
Use the `MenuBuilder` to design a menu fluently:

```java
MenuBuilder.chest(3)
    .title(Component.text("My Menu", NamedTextColor.GOLD))
    .layout(
        "#########",
        "#.......#",
        "####C####"
    )
    .bind('.', MenuButton.of(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)))
    .bind('C', MenuPresets.close())
    .onClick(event -> player.sendMessage("You clicked the menu!"))
    .open(player);
```

## 🏗️ Specialized Menus

### Anvil Menus (Input)
Easily capture player input via virtual anvils:

```java
AnvilMenu menu = new AnvilMenu();
menu.onRename(text -> player.sendMessage("Typing: " + text));
menu.onComplete((text, event) -> {
    player.sendMessage("Final text: " + text);
    player.closeInventory();
});
menu.open(player, Component.text("Rename Item"));
```

### Paged Menus
Display large sets of items with automatic pagination:

```java
PagedMenu paged = new PagedMenu(6, Layout.createContentSlots(6));
for (int i = 0; i < 100; i++) {
    paged.addContent(MenuButton.of(new ItemStack(Material.DIAMOND, i + 1)));
}
paged.setButton(49, paged.createNextPageButton());
paged.open(player, Component.text("Big Collection"));
```

## 🎞️ Animations
Create eye-catching animated buttons:

```java
ItemAnimation rainbow = tick -> new ItemStack(Material.values()[(int)(tick % 100)]);
AnimatedButton animated = new AnimatedButton(rainbow);
menu.setButton(0, animated);
```

## 📸 Example

<img src="./assets/example.gif" width="100%" alt="Example GIF">

[![JitPack](https://jitpack.io/v/Velmax-Studios/InvLib.svg)](https://jitpack.io/#Velmax-Studios/InvLib)
[![Wiki](https://img.shields.io/badge/Wiki-Documentation-cyan)](https://Velmax-Studios.github.io/InvLib/)


## 📦 Installation

Add the JitPack repository and the InvLib dependency to your `build.gradle` or `pom.xml`.

### Gradle (Groovy)
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.Velmax-Studios:InvLib:Tag'
}
```

### Gradle (Kotlin)
```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.Velmax-Studios:InvLib:Tag")
}
```

## ⚖️ License
InvLib is licensed under the MIT License.
