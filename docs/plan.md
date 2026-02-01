# Farmer's Miracle - Implementation Plan

## Overview

Minecraft mod (Architectury: Fabric + NeoForge, MC 1.21.1) that adds 3 types of temple structures containing collectible orbs. Collecting orbs grants advancements and permanent crop growth buffs.

Package: `com.farmersmiracle`
Mod ID: `farmersmiracle`

## Reference Projects

- ChronoDawn: Architectury patterns, registry, structures, effects, advancements, tags
- BeginnersDelight: Structure placement, SavedData, loot tables

## Module Placement Strategy

| Module | Contents |
|---|---|
| common-shared | Mod main class, registry classes, event handlers, buff logic, player data |
| common-1.21.1 | Resources (advancements, loot tables, tags, structure NBTs, worldgen JSONs, translations, item models), Mixin classes |
| fabric-base | (Empty) |
| fabric-1.21.1 | Fabric entrypoint (`FarmersMiracleFabric`), fabric.mod.json |
| neoforge-base | (Empty) |
| neoforge-1.21.1 | NeoForge entrypoint (`FarmersMiracleNeoForge`), neoforge.mods.toml |

---

## Phase 1: Project Foundation ✅

- [x] `common-shared/src/main/java/com/farmersmiracle/FarmersMiracle.java`
- [x] `fabric-1.21.1/src/main/java/com/farmersmiracle/fabric/FarmersMiracleFabric.java`
- [x] `neoforge-1.21.1/src/main/java/com/farmersmiracle/neoforge/FarmersMiracleNeoForge.java`
- [x] `fabric-1.21.1/src/main/resources/fabric.mod.json`
- [x] `neoforge-1.21.1/src/main/resources/META-INF/neoforge.mods.toml`
- [x] `common-1.21.1/src/main/resources/pack.mcmeta`
- [x] `common-1.21.1/build.gradle` — added common-shared srcDir
- [x] Build passes for both Fabric and NeoForge

---

## Phase 2: Custom Items (Orbs) ✅

- [x] `common-shared/.../registry/ModItems.java` — 3 orb items (Rarity.UNCOMMON, stacksTo(1))
- [x] `common-shared/.../registry/ModCreativeTabs.java` — creative tab using CreativeTabRegistry.create
- [x] Item models: `wheat_orb.json`, `pumpkin_orb.json`, `melon_orb.json`
- [x] Item textures: placeholder PNGs (pre-existing)
- [x] Translations: `en_us.json`, `ja_jp.json`

---

## Phase 3: Structures (Temples) ✅

- [x] Structure NBT files moved to `data/farmersmiracle/structure/`
- [x] Worldgen structure definitions (jigsaw type, terrain_adaptation: beard_thin)
- [x] Template pools (one per temple, using minecraft:empty fallback)
- [x] Structure sets (spacing=24 chunks/384 blocks, separation=19 chunks/304 blocks)
- [x] Biome tags under `data/farmersmiracle/tags/worldgen/biome/`
  - Targets: plains, sunflower_plains, forest, flower_forest, birch_forest, dark_forest, taiga, savanna, meadow
- [x] Loot tables with guaranteed 1 orb per temple chest

---

## Phase 4: Advancements ✅

- [x] Root advancement (background: farmland_moist)
- [x] `the_first_harvest.json` — wheat orb, task frame
- [x] `fruit_of_the_earth.json` — pumpkin orb, task frame
- [x] `a_juicy_reward.json` — melon orb, task frame
- [x] `pilgrimage_of_grains.json` — all 3 orbs required, challenge frame
- [x] EN/JP translations for all advancement titles and descriptions

---

## Phase 5: Buff System ✅

### 5.1 Player data management

- [x] `PlayerBuffData.java` — world-level SavedData (not getPersistentData, which is NeoForge-only)
  - Maps player UUID → {growthLevel (0-4), rangeExpanded (boolean)}
  - Stored in overworld's data storage as `farmersmiracle_player_buffs`

### 5.2 Buff application logic

- [x] `GrainGrowthHandler.java` — bonus growth chance: `level * 3%`
- [x] `BuffedPlayerCache.java` — per-tick cache of buffed player positions
  - Rebuilt every server tick via `TickEvent.SERVER_POST`
  - Reads from SavedData, no login/logout handling needed
  - Single-threaded MC server guarantees cache consistency within a tick
- [x] `CropBlockMixin.java` / `StemBlockMixin.java`
  - Inject at TAIL of `randomTick`, re-entrancy guard via static boolean flag
  - Extends `Block` to access protected `randomTick` method
- [x] `farmersmiracle.mixins.json` — registered in fabric.mod.json and neoforge.mods.toml

### 5.3 Advancement → Buff integration

- [x] `FarmersMiracleEvents.java`
  - `PlayerEvent.PLAYER_ADVANCEMENT` listener
  - Each orb advancement increments growth level
  - Pilgrimage advancement grants range expansion

### 5.4 Crop tag

- [x] `data/farmersmiracle/tags/block/grains.json`
  - wheat, pumpkin_stem, melon_stem, potatoes, carrots, beetroots

---

## Phase 6: Translations and Polish ✅

- [x] All item names translated (EN/JP)
- [x] All advancement titles and descriptions translated (EN/JP)
- [x] Creative tab name translated (EN/JP)
- [x] Item textures: placeholder PNGs exist (to be replaced with final art)

---

## Phase 7: Testing and Verification

- [x] Build succeeds for Fabric and NeoForge
- [ ] Items appear in creative tab (requires in-game verification)
- [ ] Structures generate within 2000 blocks of spawn (requires in-game verification)
- [ ] Chests in structures contain correct orbs (requires in-game verification)
- [ ] Picking up orbs triggers advancements (requires in-game verification)
- [ ] Advancements grant correct buff levels (requires in-game verification)
- [ ] Crop growth acceleration works within correct radius (requires in-game verification)
- [ ] Buff persists across login/logout (requires in-game verification)
- [ ] All 3 orbs → pilgrimage advancement → range expansion buff (requires in-game verification)

---

## Known Implementation Notes

- **SavedData approach**: Used world-level SavedData instead of `getPersistentData()` (NeoForge-only API). Data stored in overworld, accessible from any dimension.
- **Mixin re-entrancy**: Static boolean guard prevents infinite recursion when bonus randomTick calls trigger the mixin again.
- **Structure placement**: `random_spread` with spacing=24/separation=19 chunks ensures ~304 block minimum distance. All 3 types use the same biome list to guarantee they can generate near each other.
- **Buff persistence**: SavedData is automatically saved/loaded by MC. No special login/logout handling needed for data persistence; BuffedPlayerCache is rebuilt every tick from online players.
- **Crop tag**: Currently defined but not checked in Mixin (Mixin targets CropBlock/StemBlock classes directly, which already covers the target crops). Tag is available for future use if needed.
