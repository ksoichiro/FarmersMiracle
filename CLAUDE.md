# Project Notes

## Overview

- Mod ID: `farmersmiracle`
- Package: `com.farmersmiracle`
- Architectury-based Minecraft mod (Fabric + NeoForge), targeting MC 1.21.1
- Gradle multi-project setup

## Module Structure

| Module | Role |
|---|---|
| `common-shared` | Mod main class, registry, event handlers, buff logic, player data. Not a Gradle subproject; included as srcDir from version-specific modules |
| `common-1.21.1` | Resources (advancements, loot tables, tags, structure NBTs, worldgen JSONs, translations, item models), Mixin classes |
| `fabric-base` | Fabric-specific code (currently empty) |
| `fabric-1.21.1` | Fabric entrypoint (`FarmersMiracleFabric`), `fabric.mod.json` |
| `neoforge-base` | NeoForge-specific code (currently empty) |
| `neoforge-1.21.1` | NeoForge entrypoint (`FarmersMiracleNeoForge`), `neoforge.mods.toml` |

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
./gradlew build
```

- Build is limited to MC 1.21.1 only
- In-game verification is required for structure generation, advancement triggers, and buff behavior

## Reference Projects

- ChronoDawn: Architectury patterns, registry, structures, effects, advancements, tags
- BeginnersDelight: Structure placement, SavedData, loot tables
