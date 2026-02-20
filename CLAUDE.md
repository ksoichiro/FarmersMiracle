# Project Notes

## Overview

- Mod ID: `farmersmiracle`
- Package: `com.farmersmiracle`
- Architectury-based Minecraft mod targeting MC 1.20.1 (Fabric + Forge), MC 1.21.1 (Fabric + NeoForge), MC 1.21.3 (Fabric + NeoForge), MC 1.21.4 (Fabric + NeoForge), MC 1.21.5 (Fabric + NeoForge), MC 1.21.6 (Fabric + NeoForge), and MC 1.21.7 (Fabric + NeoForge), and MC 1.21.8 (Fabric + NeoForge)
- Gradle multi-project setup

## Module Structure

| Module | Role |
|---|---|
| `common-shared` | Mod main class, registry, buff logic. Not a Gradle subproject; included as srcDir from version-specific modules |
| `common-1.20.1` | Version-specific code (events, data, mixins) and resources for MC 1.20.1. NBT structures are generated at build time from 1.21.1 sources via `convertNbt` task |
| `common-1.21.1` | Version-specific code (events, data, mixins) and resources for MC 1.21.1 |
| `common-1.21.3` | Version-specific code (events, data, mixins) and resources for MC 1.21.3. `registryOrThrow` → `lookupOrThrow` API change |
| `common-1.21.4` | Version-specific code (events, data, mixins) and resources for MC 1.21.4. Adds `assets/<namespace>/items/` item model definitions |
| `common-1.21.5` | Version-specific code (events, data, mixins) and resources for MC 1.21.5. NBT API changes (`getCompoundOrEmpty`, default-value getters) |
| `common-1.21.6` | Version-specific code (events, data, mixins) and resources for MC 1.21.6. `Entity.server` field became private (`getServer()` required) |
| `common-1.21.7` | Version-specific code (events, data, mixins) and resources for MC 1.21.7. No API changes from 1.21.6 |
| `common-1.21.8` | Version-specific code (events, data, mixins) and resources for MC 1.21.8. No API changes from 1.21.7 |
| `fabric-base` | Fabric-specific code (currently empty) |
| `fabric-1.20.1` | Fabric entrypoint for MC 1.20.1 |
| `fabric-1.21.1` | Fabric entrypoint for MC 1.21.1 |
| `fabric-1.21.3` | Fabric entrypoint for MC 1.21.3 |
| `fabric-1.21.4` | Fabric entrypoint for MC 1.21.4 |
| `fabric-1.21.5` | Fabric entrypoint for MC 1.21.5 |
| `fabric-1.21.6` | Fabric entrypoint for MC 1.21.6 |
| `fabric-1.21.7` | Fabric entrypoint for MC 1.21.7 |
| `fabric-1.21.8` | Fabric entrypoint for MC 1.21.8 |
| `forge-base` | Forge-specific code (currently empty) |
| `forge-1.20.1` | Forge entrypoint for MC 1.20.1 |
| `neoforge-base` | NeoForge-specific code (currently empty) |
| `neoforge-1.21.1` | NeoForge entrypoint for MC 1.21.1 |
| `neoforge-1.21.3` | NeoForge entrypoint for MC 1.21.3 |
| `neoforge-1.21.4` | NeoForge entrypoint for MC 1.21.4 |
| `neoforge-1.21.5` | NeoForge entrypoint for MC 1.21.5 |
| `neoforge-1.21.6` | NeoForge entrypoint for MC 1.21.6 |
| `neoforge-1.21.7` | NeoForge entrypoint for MC 1.21.7 |
| `neoforge-1.21.8` | NeoForge entrypoint for MC 1.21.8 |

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

## MC 1.21.5 Notes

- **SavedData**: `SavedData.Factory` removed. Use `SavedDataType` with `Codec` for serialization. `save()` override no longer exists; encode/decode handled by codec. `DimensionDataStorage.computeIfAbsent(SavedDataType<T>)` is the new API
- **CompoundTag**: getter methods (`getCompound`, `getInt`, `getBoolean`, etc.) now return `Optional`. Use `getCompoundOrEmpty(key)` / `getIntOr(key, default)` / `getBooleanOr(key, default)` for old behavior. `getAllKeys()` renamed to `keySet()`
- **NeoForge**: `@EventBusSubscriber(bus = Bus.MOD)` deprecated for removal. Use `IEventBus.addListener()` in mod constructor instead (see `FarmersMiracleNeoForge.java` in neoforge-1.21.5)
- **Advancement background**: `DisplayInfo.background` changed from `ResourceLocation` to `ClientAsset`. JSON format: `"minecraft:block/farmland_moist"` instead of `"minecraft:textures/block/farmland_moist.png"` (`textures/` prefix and `.png` suffix are auto-derived)
- Java code otherwise identical to 1.21.4 (`setId()`, `lookupOrThrow()`, `items/` definitions unchanged)
- Mixin `refmap` platform split and `loom.platform=neoforge` settings carry over from 1.21.3/1.21.4

## MC 1.21.6 Notes

- **Entity.server field**: `Entity.server` field became private. Use `player.getServer()` instead of `player.server`
- **NBT ValueInput/ValueOutput**: Entity/BlockEntity save/load methods now use `ValueInput`/`ValueOutput` instead of `CompoundTag`. This mod uses Codec-based `SavedDataType`, so no direct impact on SavedData
- Java code otherwise identical to 1.21.5 (`setId()`, `lookupOrThrow()`, `items/` definitions, `SavedDataType` with Codec unchanged)
- Mixin `refmap` platform split and `loom.platform=neoforge` settings carry over from 1.21.3/1.21.4/1.21.5

## MC 1.21.7 Notes

- No breaking Java API changes from 1.21.6
- Java code identical to 1.21.6 (`setId()`, `lookupOrThrow()`, `items/` definitions, `SavedDataType` with Codec, `getServer()` unchanged)
- Mixin `refmap` platform split and `loom.platform=neoforge` settings carry over from 1.21.3/1.21.4/1.21.5/1.21.6

## MC 1.21.8 Notes

- No breaking Java API changes from 1.21.7
- Java code identical to 1.21.7 (`setId()`, `lookupOrThrow()`, `items/` definitions, `SavedDataType` with Codec, `getServer()` unchanged)
- Mixin `refmap` platform split and `loom.platform=neoforge` settings carry over from 1.21.3/1.21.4/1.21.5/1.21.6/1.21.7

## Architectury API

The Fabric implementation of `ParticleProviderRegistry` (Architectury 13.0.8) has `register(ParticleType, DeferredParticleProvider)` as a no-op. Use each platform's API directly for particle provider registration:
  - Fabric: `net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry`
  - NeoForge: `RegisterParticleProvidersEvent`

## Build & Test

```sh
# Build for default version (1.21.8)
./gradlew build

# Build for a specific version
./gradlew build -Ptarget_mc_version=1.20.1
./gradlew build -Ptarget_mc_version=1.21.1
./gradlew build -Ptarget_mc_version=1.21.3
./gradlew build -Ptarget_mc_version=1.21.4
./gradlew build -Ptarget_mc_version=1.21.5
./gradlew build -Ptarget_mc_version=1.21.6
./gradlew build -Ptarget_mc_version=1.21.7
./gradlew build -Ptarget_mc_version=1.21.8
```

- MC 1.20.1 build requires Python 3 with `nbtlib` package (for NBT structure conversion)
- In-game verification is required for structure generation, advancement triggers, and buff behavior

## Reference Projects

- ChronoDawn: Architectury patterns, registry, structures, effects, advancements, tags
- BeginnersDelight: Structure placement, SavedData, loot tables
