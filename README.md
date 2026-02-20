# Farmer's Miracle

A Minecraft mod that adds three temple structures with collectible orbs. Gathering all orbs grants permanent crop growth buffs.

> Collect three sacred orbs from small shrines to slightly boost crop growth around you.

![Farmer's Miracle Overview](docs/screenshots/featured-for-readme.png)

## Features

- **3 Temple Structures** — Temple of Golden Wheat, Temple of Pumpkins, Temple of Melons
  - All types generate within 2000 blocks of the initial spawn point
  - Minimum 300+ blocks distance between temples
- **Collectible Orbs** — Each temple chest contains a unique orb (Wheat Orb, Pumpkin Orb, Melon Orb)
- **Advancements** — Triggered by obtaining each orb, with a final advancement for collecting all three
- **Permanent Crop Growth Buffs** — Each advancement grants "Blessing of Grains" buffs:
  - **Growth I–IV**: Increases crop growth speed (stacks with each orb collected, +3% per level)
  - **Spread I**: Expands buff range from 16 to 24 blocks (granted by collecting all 3 orbs)
  - Target crops: Wheat, Pumpkin, Melon, Potato, Carrot, Beetroot
- **Mod Compatibility** — Built-in support for crops from popular mods:
  - [Farmer's Delight](https://modrinth.com/mod/farmers-delight) (`farmersdelight`)
  - [Croptopia](https://modrinth.com/mod/croptopia) (`croptopia`)
  - [Pam's HarvestCraft 2 - Crops](https://www.curseforge.com/minecraft/mc-mods/pams-harvestcraft-2-crops) (`pamhc2crops`)
  - [Cultural Delights](https://modrinth.com/mod/cultural-delights) (`culturaldelights`)
  - Only grain and vegetable crops are included; fruits, herbs, and beverages are excluded
  - These mods are **not required** — compatibility entries are ignored if the mod is not installed
  - You can add support for other mods by creating a datapack that appends entries to the `farmersmiracle:grains` block tag

## Requirements

- Minecraft 1.21.7, 1.21.6, 1.21.5, 1.21.4, 1.21.3, 1.21.1, or 1.20.1
- Fabric, NeoForge (1.21.7/1.21.6/1.21.5/1.21.4/1.21.3/1.21.1), or Forge (1.20.1)

## Building

```sh
# Build for default version (1.21.7)
./gradlew build

# Build for a specific version
./gradlew build -Ptarget_mc_version=1.20.1
./gradlew build -Ptarget_mc_version=1.21.1
./gradlew build -Ptarget_mc_version=1.21.3
./gradlew build -Ptarget_mc_version=1.21.4
./gradlew build -Ptarget_mc_version=1.21.5
./gradlew build -Ptarget_mc_version=1.21.6
./gradlew build -Ptarget_mc_version=1.21.7
```

## Project Structure

| Module | Description |
|---|---|
| `common-shared` | Loader/version-independent common code |
| `common-1.21.7` | Common resources and Mixins for MC 1.21.7 |
| `common-1.21.6` | Common resources and Mixins for MC 1.21.6 |
| `common-1.21.5` | Common resources and Mixins for MC 1.21.5 |
| `common-1.21.4` | Common resources and Mixins for MC 1.21.4 |
| `common-1.21.3` | Common resources and Mixins for MC 1.21.3 |
| `common-1.21.1` | Common resources and Mixins for MC 1.21.1 |
| `common-1.20.1` | Common resources and Mixins for MC 1.20.1 |
| `fabric-base` | Fabric-specific base code |
| `fabric-1.21.7` | Fabric entrypoint for MC 1.21.7 |
| `fabric-1.21.6` | Fabric entrypoint for MC 1.21.6 |
| `fabric-1.21.5` | Fabric entrypoint for MC 1.21.5 |
| `fabric-1.21.4` | Fabric entrypoint for MC 1.21.4 |
| `fabric-1.21.3` | Fabric entrypoint for MC 1.21.3 |
| `fabric-1.21.1` | Fabric entrypoint for MC 1.21.1 |
| `fabric-1.20.1` | Fabric entrypoint for MC 1.20.1 |
| `neoforge-base` | NeoForge-specific base code |
| `neoforge-1.21.7` | NeoForge entrypoint for MC 1.21.7 |
| `neoforge-1.21.6` | NeoForge entrypoint for MC 1.21.6 |
| `neoforge-1.21.5` | NeoForge entrypoint for MC 1.21.5 |
| `neoforge-1.21.4` | NeoForge entrypoint for MC 1.21.4 |
| `neoforge-1.21.3` | NeoForge entrypoint for MC 1.21.3 |
| `neoforge-1.21.1` | NeoForge entrypoint for MC 1.21.1 |
| `forge-base` | Forge-specific base code |
| `forge-1.20.1` | Forge entrypoint for MC 1.20.1 |

## License

LGPL-3.0-only
