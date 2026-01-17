# Publishing Guide

This document outlines the strategy and requirements for distributing **ModerationPlus**.

## Distribution Platforms

We target the following platforms for release:
1.  **CurseForge** (Primary)
2.  **GitHub Releases** (For dev builds)

## Versioning Policy

We use **Semantic Versioning** (`MAJOR`.`MINOR`.`PATCH`).
- **MAJOR**: Breaking changes (e.g., database schema overhaul without auto-migration, API break).
- **MINOR**: New features (e.g., new commands, new bypass permissions).
- **PATCH**: Bug fixes, performance improvements, internal refactoring.

## Release Cadence

- **Stable Releases:** When a milestone of features is complete and tested.
- **Hotfixes:** Immediately upon discovery of critical bugs (dupes, crashes, security).

## Update Communication

- **Discord:** Post in `#announcements` with a link to the changelog.
- **Marketplaces:** Update the "What's New" section on Modrinth/CurseForge.
- **GitHub:** Create a comprehensive release note with tag `vX.Y.Z`.

## Identity & Branding

- **Name:** ModerationPlus
- **Tagline:** "Essential Moderation Tools for Hytale Servers"
- **Color Palette:** Red (`#FF5555`) for punishments, Green (`#55FF55`) for success.

## Legal

- **License:** All Rights Reserved.
- **EULA:** Users must agree to the Hytale EULA.
