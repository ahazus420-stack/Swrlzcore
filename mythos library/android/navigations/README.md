# Android Navigations Library

This directory is the living SWRLZ reference for Android navigation knowledge.

## Purpose

Build a grounded, version-aware map of how Android devices, Android apps, and Android UI systems are navigated from a human perspective, so SWRLZ can reason about what is on screen, where controls usually live, and how users move through menus and app flows.

## Scope

Store knowledge about:

- System-level Android navigation
- App-level navigation patterns
- Menu structures and common destination layouts
- Settings hierarchies and control locations
- Navigation bars, drawers, app bars, tabs, and back behavior
- Accessibility-assisted navigation patterns
- Version and form-factor differences across Android releases, manufacturers, and device classes

## Reference frame

The goal is not just to know isolated UI facts. The goal is to maintain a practical navigation blueprint that helps SWRLZ infer:

- where a user is likely to find a setting or action
- how to move between screens safely
- what visual structure to expect on different Android versions
- how navigation changes across phone, tablet, foldable, TV, and other Android surfaces

## Initial Android navigation anchors

- System bars are the status bar, caption bar, and navigation bar. They are always relevant when reasoning about screen layout and device interaction. 
- Top app bars provide screen-level title, navigation, and action affordances.
- Navigation drawers organize deeper destinations and feature groups.
- Bottom navigation is best suited for a small set of equally important destinations.
- Navigation frameworks in Android support consistent movement across destinations and can integrate with app bars, drawers, and bottom navigation.
- Accessibility services can inspect screen content and interact with apps on behalf of the user, but they are specialized tools and should be treated with care.

## Planned substructure

Populate this directory with files such as:

- `system-bars.md`
- `settings-paths.md`
- `top-app-bar.md`
- `bottom-navigation.md`
- `navigation-drawer.md`
- `tabs-and-rails.md`
- `accessibility-navigation.md`
- `version-notes.md`
- `manufacturer-notes.md`
- `app-flow-patterns.md`
- `gesture-vs-button-nav.md`

## Working rule

When adding a new note, record:

1. Android version or API level if known
2. Device class if relevant
3. Screen or menu context
4. The concrete navigation pattern
5. Any caveats, exceptions, or accessibility implications

## Source discipline

Prefer official Android documentation and versioned references when recording facts. Treat this directory as an engineering blueprint, not a pile of guesses.
