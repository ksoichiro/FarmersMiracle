---
name: mc-version-upgrade
description: Checklist and procedures for adding support for a new Minecraft version. Use when adding a new MC version target to this mod.
---

# Minecraft Version Upgrade Procedure

## Prerequisites

Before starting, gather the following information for the new MC version:

- `minecraft_version`
- `pack_format` (check [Minecraft Wiki: Pack format](https://minecraft.wiki/w/Pack_format))
- `java_version`
- `fabric_loader_version`
- `neoforge_version` (or `forge_version` for 1.20.x)
- `fabric_api_version`
- Base version to copy from (usually the latest existing version)

## Investigate Version-Specific Changes

Check for breaking changes between the base version and the target version:

- **Java API changes**: renamed/removed methods, new required method calls
- **Resource format changes**: new required resource files, changed JSON schemas
- **Mixin compatibility**: verify mixin targets still exist
- **Build tooling**: Architectury Loom compatibility

Past examples of version-specific changes:
- 1.21.2+: `registryOrThrow()` → `lookupOrThrow()`, `Item.Properties` requires `.setId(ResourceKey)`
- 1.21.4: `assets/<namespace>/items/<id>.json` item model definitions required
- 26.x: unobfuscated MC, built with `dev.architectury.loom-no-remap` (see "MC 26.x Notes" in `CLAUDE.md`). Copy new 26.x versions from the latest 26.x module, not from 1.21.x

## File Creation Checklist

### 1. `props/<version>.properties`

Create from base version, update all version fields.

### 2. `common/<version>/` module

Copy from base version's common module:

```
rsync -a --exclude build --exclude .gradle --exclude run common/<base>/ common/<version>/
```

- Remove `.DS_Store` files: `find common/<version> -name '.DS_Store' -delete`
- Apply any version-specific Java code changes
- Apply any version-specific resource changes (new resource types, format changes)
- `build.gradle`: Usually no changes needed (references are self-contained)

### 3. `fabric/<version>/` module

Copy from base version:

```
rsync -a --exclude build --exclude .gradle --exclude run fabric/<base>/ fabric/<version>/
```

Update these files:
- **`build.gradle`**: Change `commonModule` variable to `':common-<version>'`
- **`src/main/resources/fabric.mod.json`**: Update `depends` section:
  - `minecraft`: `~<new_mc_version>`

### 4. `neoforge/<version>/` module (or `forge/<version>/` for 1.20.x)

Copy from base version:

```
rsync -a --exclude build --exclude .gradle --exclude run neoforge/<base>/ neoforge/<version>/
```

Update these files:
- **`build.gradle`**: Change `commonModule` variable to `':common-<version>'`
- **`src/main/resources/META-INF/neoforge.mods.toml`**: Update 2 dependency entries:
  - `neoforge` versionRange: `[<major>.<minor>.0,)`
  - `minecraft` versionRange: `[<new_mc_version>]`
- **`gradle.properties`**: Must contain `loom.platform=neoforge`

## Configuration & Documentation Update Checklist

### 5. `gradle.properties` (root) and `gradle/multi-version-tasks.gradle`

- Update `target_mc_version` to the new version
- Add the new version to `supportedVersions` in `gradle/multi-version-tasks.gradle` (used by `buildAll`, `cleanAll`, `collectJars`)
- Add an entry to `CHANGELOG.md` under `Unreleased`

### 6. Documentation (all 4 files below)

| File | Sections to update |
|---|---|
| `CLAUDE.md` | Overview (target list), Module Structure table, add MC Notes section, Build & Test commands |
| `README.md` | Requirements, Building commands, Project Structure table |
| `docs/curseforge_description.md` | Multi-Loader Support list, Requirements section (add new version block), footer version list |
| `docs/modrinth_description.md` | Same as curseforge_description.md |

## Build Verification

```sh
./gradlew build -Ptarget_mc_version=<version>
```

Check for:
- Build success
- Deprecation warnings (note in CLAUDE.md if significant)
- No errors in mixin application

## Server Smoke Test

Compiling does not validate data JSON (advancements, loot tables, worldgen). Start a dev server with RCON enabled (`fabric/<version>/run/server.properties`: `enable-rcon=true`, `rcon.port`, `rcon.password`, plus `eula.txt`), run `./gradlew :fabric:runServer -Ptarget_mc_version=<version> --args=nogui` (and `:neoforge:runServer`), then check:

- No `ERROR`/`Couldn't parse` lines for `farmersmiracle` during startup
- `locate structure farmersmiracle:temple_of_golden_wheat` (and `temple_of_pumpkin`, `temple_of_melons`) finds a structure
- `loot spawn 0 200 0 loot farmersmiracle:chests/temple_of_golden_wheat` drops the orb

The dev server JVM may not exit after `stop`. Kill it once the world is saved.

## Notes

- `settings.gradle` does NOT need modification — it dynamically reads `props/<version>.properties` and resolves modules
- The `enabled_platforms` field in props file controls which platform modules are included
- For NeoForge, `gradle.properties` with `loom.platform=neoforge` is critical — without it, the `neoForge` dependency configuration won't be created
