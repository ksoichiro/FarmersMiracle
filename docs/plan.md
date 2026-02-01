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
| common-shared | Mod main class, registry classes (ModItems, ModEffects, etc.), ID definitions, event handlers, buff logic, platform abstraction |
| common-1.21.1 | Resources: advancements, loot tables, tags, structure NBTs, worldgen JSONs, translations, item models |
| fabric-base | (Empty or minimal) |
| fabric-1.21.1 | Fabric entrypoint (`FarmersMiracleFabric`), fabric.mod.json, mixin config if needed |
| neoforge-base | (Empty or minimal) |
| neoforge-1.21.1 | NeoForge entrypoint (`FarmersMiracleNeoForge`), neoforge.mods.toml |

---

## Phase 1: Project Foundation

### 1.1 Main mod class and entrypoints

- [ ] `common-shared/.../FarmersMiracle.java`
  - MOD_ID, LOGGER, `init()` method
- [ ] `fabric-1.21.1/.../FarmersMiracleFabric.java`
  - `ModInitializer`, calls `FarmersMiracle.init()`
- [ ] `neoforge-1.21.1/.../FarmersMiracleNeoForge.java`
  - NeoForge mod class, calls `FarmersMiracle.init()`
- [ ] Resource files
  - `fabric-1.21.1/src/main/resources/fabric.mod.json`
  - `neoforge-1.21.1/src/main/resources/META-INF/neoforge.mods.toml`
  - `common-1.21.1/src/main/resources/pack.mcmeta`

### 1.2 Build verification

- [ ] `./gradlew build` passes for both Fabric and NeoForge

---

## Phase 2: Custom Items (Orbs)

### 2.1 Item registration

- [ ] `common-shared/.../registry/ModItems.java`
  - Register 3 orb items using Architectury `DeferredRegister`
  - `wheat_orb`, `pumpkin_orb`, `melon_orb`
  - Simple items (no special behavior), possibly with Rarity.UNCOMMON or custom
- [ ] `common-shared/.../registry/ModItemId.java` (optional, for ID constants)

### 2.2 Creative tab

- [ ] `common-shared/.../registry/ModCreativeTabs.java`
  - Register a creative tab for Farmer's Miracle items

### 2.3 Item resources

- [ ] Item models: `common-1.21.1/src/main/resources/assets/farmersmiracle/models/item/`
  - `wheat_orb.json`, `pumpkin_orb.json`, `melon_orb.json`
- [ ] Item textures: `common-1.21.1/src/main/resources/assets/farmersmiracle/textures/item/`
  - Placeholder textures for 3 orbs (can use solid color initially)
- [ ] Translations: `common-1.21.1/src/main/resources/assets/farmersmiracle/lang/`
  - `en_us.json`, `ja_jp.json`

---

## Phase 3: Structures (Temples)

### 3.1 Structure NBT files

- [ ] Create 3 temple structures using Structure Block in-game or programmatically
  - `common-1.21.1/src/main/resources/data/farmersmiracle/structure/`
    - `temple_of_golden_wheat.nbt`
    - `temple_of_pumpkin.nbt`
    - `temple_of_melons.nbt`
  - Each temple contains a chest (to be filled with loot table)

### 3.2 Worldgen configuration (JSON-driven)

- [ ] Structure definitions: `data/farmersmiracle/worldgen/structure/`
  - `temple_of_golden_wheat.json`
  - `temple_of_pumpkin.json`
  - `temple_of_melons.json`
  - Type: `minecraft:jigsaw` or custom
- [ ] Template pools: `data/farmersmiracle/worldgen/template_pool/`
  - One pool per temple type
- [ ] Structure sets: `data/farmersmiracle/worldgen/structure_set/`
  - Configure placement: all 3 types within 2000 blocks of spawn
  - Minimum distance between temples: 300 blocks
  - Use `minecraft:random_spread` or `minecraft:concentric_rings` placement
- [ ] Processor lists (if needed): `data/farmersmiracle/worldgen/processor_list/`
- [ ] Biome tags for structure placement: `data/minecraft/tags/worldgen/biome/`
  - Tag to define biomes where temples can generate (e.g., `has_structure/temple_of_golden_wheat`)

### 3.3 Loot tables

- [ ] `data/farmersmiracle/loot_table/chests/`
  - `temple_of_golden_wheat.json` → Contains `wheat_orb`
  - `temple_of_pumpkin.json` → Contains `pumpkin_orb`
  - `temple_of_melons.json` → Contains `melon_orb`
  - Each chest guarantees exactly 1 orb

### 3.4 Structure placement constraints

- Ensure all 3 temple types spawn within 2000 blocks of world origin
- Minimum 300 blocks between different temple types
- Consider using structure set with appropriate spacing/separation parameters

---

## Phase 4: Advancements

### 4.1 Advancement definitions

- [ ] `data/farmersmiracle/advancement/`
  - Root advancement (hidden, grants nothing, used as parent)
  - `the_first_harvest.json`
    - Trigger: `minecraft:inventory_changed` for `wheat_orb`
    - Parent: root
  - `fruit_of_the_earth.json`
    - Trigger: `minecraft:inventory_changed` for `pumpkin_orb`
    - Parent: root
  - `a_juicy_reward.json`
    - Trigger: `minecraft:inventory_changed` for `melon_orb`
    - Parent: root
  - `pilgrimage_of_grains.json`
    - Trigger: Requires all 3 orbs (use `minecraft:inventory_changed` with requirements for all 3, or chain as child of the other 3)
    - Parent: root

### 4.2 Advancement reward: Buff granting

- Advancements alone cannot grant permanent buffs → need event-based approach
- [ ] Listen for advancement events and grant/update buffs on achievement
  - Use `PlayerEvent` or equivalent to detect when advancement is completed
  - Alternatively, use advancement reward functions

---

## Phase 5: Buff System

### 5.1 Player data management

- [ ] `common-shared/.../data/FarmersMiraclePlayerData.java`
  - Track per-player buff levels (growth speed level 0-4, range expansion level 0-1)
  - Persist via player capability (NeoForge) / Architectury approach / SavedData
  - Consider using player's persistent data (CompoundTag from `getPersistentData()`)

### 5.2 Buff application logic

- [ ] `common-shared/.../effects/GrainGrowthHandler.java`
  - Intervene in crop growth determination with Mixin on `CropBlock.randomTick()` / `StemBlock.randomTick()`
  - When a crop (matching tag `farmersmiracle:grains`) receives random tick:
    1. Check nearby players (within radius) for buff
    2. If buffed player nearby, apply additional growth chance
    3. Radius: 16 blocks (base), 24 blocks (with spread buff)
  - Growth probability: `level * 3%` (base rate configurable)
- [ ] Player position cache for performance
  - Build a coordinate list of players with buffs at the start of server tick (using `ServerTickEvents.END_SERVER_TICK` etc.)
  - In Mixin, only perform distance calculations against the cached list, without calling `level.getEntitiesOfClass()`
  - Since the cache is rebuilt every tick, individual processing for login/logout is unnecessary
  - Since MC server is single-threaded, there is no possibility of cache and actual positions being out of sync within the same tick

### 5.3 Advancement → Buff integration

- [ ] When advancement is achieved:
  - Increment `grain_growth_speed` level (1 per orb advancement)
  - `pilgrimage_of_grains` → grant `grain_growth_range` (spread) buff
- [ ] On player login: restore buff state from saved data

### 5.4 Crop tag

- [ ] `data/farmersmiracle/tags/block/grains.json`
  ```json
  {
    "values": [
      "minecraft:wheat",
      "minecraft:pumpkin_stem",
      "minecraft:melon_stem",
      "minecraft:potatoes",
      "minecraft:carrots",
      "minecraft:beetroots"
    ]
  }
  ```

### 5.5 Implementation approach for crop growth acceleration

Options (choose one):
1. **Mixin on CropBlock.randomTick()** — intercept random tick and add extra growth
2. **Server tick event** — periodically scan crops near buffed players and trigger growth
3. **RandomTickEvent (if available)** — listen for block random tick events

Recommended: **Mixin approach** for precision and performance. Apply mixin in common-1.21.1 (version-specific due to MC internals).

---

## Phase 6: Translations and Polish

### 6.1 Translations

- [ ] `en_us.json` and `ja_jp.json`
  - Item names (3 orbs)
  - Advancement titles and descriptions (4 advancements)
  - Buff names (if displayed)
  - Creative tab name

### 6.2 Textures and assets

- [ ] Item textures for 3 orbs (16x16 pixel art)
- [ ] Advancement icons

---

## Phase 7: Testing and Verification

- [ ] Build succeeds for Fabric and NeoForge
- [ ] Items appear in creative tab
- [ ] Structures generate within 2000 blocks of spawn
- [ ] Chests in structures contain correct orbs
- [ ] Picking up orbs triggers advancements
- [ ] Advancements grant correct buff levels
- [ ] Crop growth acceleration works within correct radius
- [ ] Buff persists across login/logout
- [ ] All 3 orbs → pilgrimage advancement → range expansion buff

---

## Implementation Order (Recommended)

1. Phase 1: Foundation (entrypoints, build verification)
2. Phase 2: Items (orbs registration and resources)
3. Phase 4: Advancements (JSON-based, can test with `/give`)
4. Phase 3: Structures (NBT creation, worldgen config, loot tables)
5. Phase 5: Buff system (player data, growth handler, advancement integration)
6. Phase 6: Translations and polish
7. Phase 7: Testing

Rationale: Items are needed before advancements, advancements before buff integration. Structures require in-game NBT creation which can be done in parallel. Buff system is the most complex and benefits from having other systems in place first.

---

## Technical Notes

- Use Architectury `DeferredRegister` for all registrations
- Follow ChronoDawn patterns for registry organization
- NBT structure files must be created in-game using Structure Blocks — placeholder files or programmatic generation can be used initially
- Player buff data: use `player.getPersistentData()` (simplest cross-platform approach) or Architectury's attachment API if available
- Mixin for crop growth: target `CropBlock.randomTick()` and `StemBlock.randomTick()`
- Structure placement guarantees (within 2000 blocks, 300 block spacing) may require custom placement logic or careful structure set configuration
