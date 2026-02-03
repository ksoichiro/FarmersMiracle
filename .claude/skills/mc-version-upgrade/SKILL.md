---
name: mc-version-upgrade
description: Checklist and procedures for adding support for a new Minecraft version. Use when adding a new MC version target to this Architectury mod.
---

# Minecraft Version Upgrade Procedure

## Prerequisites

Before starting, gather the following information for the new MC version:

- `minecraft_version`
- `pack_format` (check [Minecraft Wiki: Pack format](https://minecraft.wiki/w/Pack_format))
- `java_version`
- `fabric_loader_version`
- `neoforge_version` (or `forge_version` for 1.20.x)
- `architectury_api_version`
- `fabric_api_version`
- Base version to copy from (usually the latest existing version)

## Investigate Version-Specific Changes

Check for breaking changes between the base version and the target version:

- **Java API changes**: renamed/removed methods, new required method calls
- **Resource format changes**: new required resource files, changed JSON schemas
- **Mixin compatibility**: verify mixin targets still exist
- **Build tooling**: Architectury Loom, Fabric Loom compatibility

Past examples of version-specific changes:
- 1.21.2+: `registryOrThrow()` → `lookupOrThrow()`, `Item.Properties` requires `.setId(ResourceKey)`
- 1.21.4: `assets/<namespace>/items/<id>.json` item model definitions required

## File Creation Checklist

### 1. `props/<version>.properties`

Create from base version, update all version fields.

### 2. `common-<version>/` module

Copy from base version's common module:

```
cp -R common-<base> common-<version>
```

- Remove `.DS_Store` files: `find common-<version> -name '.DS_Store' -delete`
- Apply any version-specific Java code changes
- Apply any version-specific resource changes (new resource types, format changes)
- `build.gradle`: Usually no changes needed (references are self-contained)

### 3. `fabric-<version>/` module

Copy from base version:

```
cp -R fabric-<base> fabric-<version>
```

Update these files:
- **`build.gradle`**: Change `commonModule` variable to `':common-<version>'`
- **`src/main/resources/fabric.mod.json`**: Update `depends` section:
  - `architectury`: `>=<new_architectury_version>`
  - `minecraft`: `~<new_mc_version>`

### 4. `neoforge-<version>/` module (or `forge-<version>/` for 1.20.x)

Copy from base version:

```
cp -R neoforge-<base> neoforge-<version>
```

Update these files:
- **`build.gradle`**: Change `commonModule` variable to `':common-<version>'`
- **`src/main/resources/META-INF/neoforge.mods.toml`**: Update 3 dependency entries:
  - `neoforge` versionRange: `[<major>.<minor>.0,)`
  - `minecraft` versionRange: `[<new_mc_version>]`
  - `architectury` versionRange: `[<new_architectury_version>,)`
- **`gradle.properties`**: Must contain `loom.platform=neoforge`

## Configuration & Documentation Update Checklist

### 5. `gradle.properties` (root)

- Update `target_mc_version` to the new version

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

## Notes

- `settings.gradle` does NOT need modification — it dynamically reads `props/<version>.properties` and resolves modules
- The `enabled_platforms` field in props file controls which platform modules are included
- For NeoForge, `gradle.properties` with `loom.platform=neoforge` is critical — without it, the `neoForge` dependency configuration won't be created
