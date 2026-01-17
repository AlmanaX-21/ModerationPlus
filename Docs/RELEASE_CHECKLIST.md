# Release Checklist

Use this checklist before publishing a new version of **ModerationPlus**.

## Pre-Requisites
- [ ] Code compiles without errors (`gradle build`).
- [ ] All tests pass (if applicable).
- [ ] `README.md` and `Docs/` are up to date.

## Testing
- [ ] **Fresh Install:** Delete `mods/data/` and verify clean startup.
- [ ] **Data Persistence:** Punish a player, restart server, verify punishment persists.
- [ ] **Migrations:** (If schema changed) Test upgrade from previous version database.
- [ ] **Permissions:** Verify a non-op player cannot use commands.
- [ ] **Jail:** Verify a jailed player cannot walk out of the zone.

## Packaging
- [ ] Update `pluginVersion` in `build.gradle.kts`.
- [ ] Update `CHANGELOG.md` with new version header.
- [ ] Run `gradle shadowJar` to build the release artifact.
- [ ] Access artifact in `build/libs/ModerationPlus-<version>.jar`.

## Publishing
- [ ] Tag the release on GitHub (`git tag v1.0.0`).
- [ ] Upload JAR to Modrinth.
- [ ] Upload JAR to CurseForge.
- [ ] Post announcement in Discord.
