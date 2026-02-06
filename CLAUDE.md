# Project Notes

## Overview

- Mod ID: `farmersmiracle`
- Package: `com.farmersmiracle`
- Architectury-based Minecraft mod targeting MC 1.20.1 (Fabric + Forge), MC 1.21.1 (Fabric + NeoForge), MC 1.21.3 (Fabric + NeoForge), and MC 1.21.4 (Fabric + NeoForge)
- Gradle multi-project setup

## Module Structure

| Module | Role |
|---|---|
| `common-shared` | Mod main class, registry, buff logic. Not a Gradle subproject; included as srcDir from version-specific modules |
| `common-1.20.1` | Version-specific code (events, data, mixins) and resources for MC 1.20.1. NBT structures are generated at build time from 1.21.1 sources via `convertNbt` task |
| `common-1.21.1` | Version-specific code (events, data, mixins) and resources for MC 1.21.1 |
| `common-1.21.3` | Version-specific code (events, data, mixins) and resources for MC 1.21.3. `registryOrThrow` → `lookupOrThrow` API change |
| `common-1.21.4` | Version-specific code (events, data, mixins) and resources for MC 1.21.4. Adds `assets/<namespace>/items/` item model definitions |
| `fabric-base` | Fabric-specific code (currently empty) |
| `fabric-1.20.1` | Fabric entrypoint for MC 1.20.1 |
| `fabric-1.21.1` | Fabric entrypoint for MC 1.21.1 |
| `fabric-1.21.3` | Fabric entrypoint for MC 1.21.3 |
| `fabric-1.21.4` | Fabric entrypoint for MC 1.21.4 |
| `forge-base` | Forge-specific code (currently empty) |
| `forge-1.20.1` | Forge entrypoint for MC 1.20.1 |
| `neoforge-base` | NeoForge-specific code (currently empty) |
| `neoforge-1.21.1` | NeoForge entrypoint for MC 1.21.1 |
| `neoforge-1.21.3` | NeoForge entrypoint for MC 1.21.3 |
| `neoforge-1.21.4` | NeoForge entrypoint for MC 1.21.4 |

## Key Implementation Details

### Buff System

- **SavedData**: World-level `SavedData` stored in overworld as `farmersmiracle_player_buffs`. Maps player UUID to `{growthLevel (0-4), rangeExpanded (boolean)}`. Do not use `getPersistentData()` (NeoForge-only API).
- **BuffedPlayerCache**: Rebuilt every server tick from online players via `TickEvent.SERVER_POST`. No special login/logout handling needed.
- **Mixin re-entrancy**: `CropBlockMixin` / `StemBlockMixin` use a static boolean guard to prevent infinite recursion when bonus `randomTick` calls re-trigger the mixin.
- **Crop tag**: `farmersmiracle:grains` tag exists but Mixins currently target `CropBlock`/`StemBlock` classes directly.
- **External mod compatibility**: `grains` tag uses sub-tag composition (`grains_vanilla`, `grains_farmersdelight`, `grains_croptopia`, `grains_pamhc2crops`, `grains_culturaldelights`). Sub-tags are referenced with `"required": false` so missing mods are safely ignored. Only grain/vegetable crops are included; fruits, herbs, beverages, and fiber crops are excluded.

### Structure Placement

- `random_spread` with spacing=24 / separation=19 chunks (~304 block minimum distance)
- Biome targets: plains, sunflower_plains, forest, flower_forest, birch_forest, dark_forest, taiga, savanna, meadow

## MC 1.21.1 Worldgen

- Jigsaw structure JSON requires `spawn_overrides` field (even if empty `{}`). Omitting it causes a crash at startup.

## MC 1.21.3 Notes

- `RegistryAccess.registryOrThrow()` was renamed to `lookupOrThrow()` in 1.21.2+
- `Item.Properties` requires `.setId(ResourceKey)` since 1.21.2+. The `common-1.21.3` module overrides `ModItems.java` from `common-shared` via `java.filter.exclude` in build.gradle
- Mixin `refmap` handling differs by platform: Fabric needs `refmap` in mixin JSON (intermediary mappings), NeoForge must NOT have it (causes intermediary name resolution). Common mixin JSON includes `refmap` for Fabric; NeoForge modules have their own mixin JSON without `refmap` in `src/main/resources/` (takes precedence over common's version via ShadowJar merge order)
- `neoforge-1.21.3/gradle.properties` must contain `loom.platform=neoforge` for Architectury Loom to create the `neoForge` dependency configuration

## MC 1.21.4 Notes

- Java code is identical to 1.21.3 (`setId()`, `lookupOrThrow()` unchanged)
- `assets/<namespace>/items/<id>.json` item model definitions are required (new in 1.21.4). These reference existing `models/item/` models
- Mixin `refmap` platform split and `loom.platform=neoforge` settings carry over from 1.21.3

## Architectury API

The Fabric implementation of `ParticleProviderRegistry` (Architectury 13.0.8) has `register(ParticleType, DeferredParticleProvider)` as a no-op. Use each platform's API directly for particle provider registration:
  - Fabric: `net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry`
  - NeoForge: `RegisterParticleProvidersEvent`

## Build & Test

```sh
# Build for default version (1.21.4)
./gradlew build

# Build for a specific version
./gradlew build -Ptarget_mc_version=1.20.1
./gradlew build -Ptarget_mc_version=1.21.1
./gradlew build -Ptarget_mc_version=1.21.3
./gradlew build -Ptarget_mc_version=1.21.4
```

- MC 1.20.1 build requires Python 3 with `nbtlib` package (for NBT structure conversion)
- In-game verification is required for structure generation, advancement triggers, and buff behavior

## Reference Projects

- ChronoDawn: Architectury patterns, registry, structures, effects, advancements, tags
- BeginnersDelight: Structure placement, SavedData, loot tables
