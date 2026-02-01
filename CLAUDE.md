# Project Notes

## Overview

- Mod ID: `farmersmiracle`
- Package: `com.farmersmiracle`
- Architectury-based Minecraft mod targeting MC 1.20.1 (Fabric + Forge) and MC 1.21.1 (Fabric + NeoForge)
- Gradle multi-project setup

## Module Structure

| Module | Role |
|---|---|
| `common-shared` | Mod main class, registry, buff logic. Not a Gradle subproject; included as srcDir from version-specific modules |
| `common-1.20.1` | Version-specific code (events, data, mixins) and resources for MC 1.20.1. NBT structures are generated at build time from 1.21.1 sources via `convertNbt` task |
| `common-1.21.1` | Version-specific code (events, data, mixins) and resources for MC 1.21.1 |
| `fabric-base` | Fabric-specific code (currently empty) |
| `fabric-1.20.1` | Fabric entrypoint for MC 1.20.1 |
| `fabric-1.21.1` | Fabric entrypoint for MC 1.21.1 |
| `forge-base` | Forge-specific code (currently empty) |
| `forge-1.20.1` | Forge entrypoint for MC 1.20.1 |
| `neoforge-base` | NeoForge-specific code (currently empty) |
| `neoforge-1.21.1` | NeoForge entrypoint for MC 1.21.1 |

## Key Implementation Details

### Buff System

- **SavedData**: World-level `SavedData` stored in overworld as `farmersmiracle_player_buffs`. Maps player UUID to `{growthLevel (0-4), rangeExpanded (boolean)}`. Do not use `getPersistentData()` (NeoForge-only API).
- **BuffedPlayerCache**: Rebuilt every server tick from online players via `TickEvent.SERVER_POST`. No special login/logout handling needed.
- **Mixin re-entrancy**: `CropBlockMixin` / `StemBlockMixin` use a static boolean guard to prevent infinite recursion when bonus `randomTick` calls re-trigger the mixin.
- **Crop tag**: `farmersmiracle:grains` tag exists but Mixins currently target `CropBlock`/`StemBlock` classes directly.

### Structure Placement

- `random_spread` with spacing=24 / separation=19 chunks (~304 block minimum distance)
- Biome targets: plains, sunflower_plains, forest, flower_forest, birch_forest, dark_forest, taiga, savanna, meadow

## MC 1.21.1 Worldgen

- Jigsaw structure JSON requires `spawn_overrides` field (even if empty `{}`). Omitting it causes a crash at startup.

## Architectury API

The Fabric implementation of `ParticleProviderRegistry` (Architectury 13.0.8) has `register(ParticleType, DeferredParticleProvider)` as a no-op. Use each platform's API directly for particle provider registration:
  - Fabric: `net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry`
  - NeoForge: `RegisterParticleProvidersEvent`

## Build & Test

```sh
# Build for default version (1.21.1)
./gradlew build

# Build for a specific version
./gradlew build -Ptarget_mc_version=1.20.1
./gradlew build -Ptarget_mc_version=1.21.1
```

- MC 1.20.1 build requires Python 3 with `nbtlib` package (for NBT structure conversion)
- In-game verification is required for structure generation, advancement triggers, and buff behavior

## Reference Projects

- ChronoDawn: Architectury patterns, registry, structures, effects, advancements, tags
- BeginnersDelight: Structure placement, SavedData, loot tables
