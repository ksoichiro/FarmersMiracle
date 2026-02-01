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

## Requirements

- Minecraft 1.21.1 or 1.20.1
- Fabric, NeoForge (1.21.1), or Forge (1.20.1)

## Building

```sh
# Build for default version (1.21.1)
./gradlew build

# Build for a specific version
./gradlew build -Ptarget_mc_version=1.20.1
./gradlew build -Ptarget_mc_version=1.21.1
```

## Project Structure

| Module | Description |
|---|---|
| `common-shared` | Loader/version-independent common code |
| `common-1.21.1` | Common resources and Mixins for MC 1.21.1 |
| `common-1.20.1` | Common resources and Mixins for MC 1.20.1 |
| `fabric-base` | Fabric-specific base code |
| `fabric-1.21.1` | Fabric entrypoint for MC 1.21.1 |
| `fabric-1.20.1` | Fabric entrypoint for MC 1.20.1 |
| `neoforge-base` | NeoForge-specific base code |
| `neoforge-1.21.1` | NeoForge entrypoint for MC 1.21.1 |
| `forge-base` | Forge-specific base code |
| `forge-1.20.1` | Forge entrypoint for MC 1.20.1 |

## License

LGPL-3.0-only
