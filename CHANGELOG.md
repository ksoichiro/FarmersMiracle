# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.3.0] - 2026-02-22

### Added

- Minecraft 1.21.11 support (Fabric + NeoForge)
- Minecraft 1.21.10 support (Fabric + NeoForge)
- Minecraft 1.21.9 support (Fabric + NeoForge)
- Minecraft 1.21.8 support (Fabric + NeoForge)
- Minecraft 1.21.7 support (Fabric + NeoForge)
- Minecraft 1.21.6 support (Fabric + NeoForge)
- Minecraft 1.21.5 support (Fabric + NeoForge)

### Fixed

- Reapply buff effects on player respawn across all MC versions

### Removed

- Architectury API runtime dependency (replaced with platform-native APIs)

## [0.2.0] - 2026-02-06

### Added

- Minecraft 1.21.4 support (Fabric + NeoForge)
- Minecraft 1.21.3 support (Fabric + NeoForge)
- Optional crop compatibility with external mods (Farmer's Delight, Croptopia, Pam's HarvestCraft 2, Cultural Delights)
- Sub-tag composition for `farmersmiracle:grains` block tag for modular external mod support

## [0.1.0] - 2026-02-02

### Added

#### Core Features
- Grain Growth buff system with crop growth acceleration via Mixin-based bonus random ticks
- Non-linear scaling and custom grain growth particle effects
- MobEffect display for grain buffs (Grain Growth and Grain Spread)
- World-level SavedData for persistent player buff storage

#### Items & Advancements
- Grain Orb items (Growth Orb, Spread Orb) and creative tab
- Advancements for orb collection (Pilgrimage of Grains progression)
- Custom icons for root and pilgrimage_of_grains advancements

#### World Generation
- Grain Temple structures with jigsaw placement across plains, forest, and other biomes
- Auto-assigned loot tables to chests in generated structures
- Biome tags for structure placement control

#### Minecraft Version Support
- Minecraft 1.21.1 support (Fabric + NeoForge)
- Minecraft 1.20.1 support (Fabric + Forge)

### Fixed

- Offset structure templates Y+1 to prevent 1-block burial
- Add missing `spawn_overrides` to structure definitions (fixes MC 1.21.1 crash)
- Always use overworld for PlayerBuffData storage
- Include mixin config, refmap, and access widener in Fabric JAR
- Bump Fabric Loader minimum to 0.16.10 for MC 1.20.1
- Resolve Forge 1.20.1 crash by removing intermediary refmap and registering mod event bus

[Unreleased]: https://github.com/ksoichiro/FarmersMiracle/compare/v0.3.0...HEAD
[0.3.0]: https://github.com/ksoichiro/FarmersMiracle/compare/v0.2.0...v0.3.0
[0.2.0]: https://github.com/ksoichiro/FarmersMiracle/compare/v0.1.0...v0.2.0
[0.1.0]: https://github.com/ksoichiro/FarmersMiracle/releases/tag/v0.1.0
