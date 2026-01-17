# Database Schema

This document details the database structure used by **ModerationPlus**.

**Dialect:** SQLite

## Tables

### `migrations`
Tracks which schema updates have been applied.

| Field | Type | Description |
| :--- | :--- | :--- |
| `version` | `INTEGER` | Primary Key. The migration version number. |
| `applied_at` | `INTEGER` | Unix timestamp (ms) when the migration was run. |

### `players`
Stores persistent data about players who have joined the server.

| Field | Type | Description |
| :--- | :--- | :--- |
| `id` | `INTEGER` | Primary Key (Auto Increment). Internal ID. |
| `uuid` | `TEXT` | Unique UUID of the player. |
| `username` | `TEXT` | Last known username. |
| `first_seen` | `INTEGER` | Timestamp of first join. |
| `last_seen` | `INTEGER` | Timestamp of last join. |

### `punishment_types`
Enum-like table defining available punishment types.

| Field | Type | Description |
| :--- | :--- | :--- |
| `id` | `INTEGER` | Primary Key. |
| `name` | `TEXT` | Unique name (BAN, KICK, MUTE, WARN, JAIL). |

### `punishments`
Stores all active and inactive punishments.

| Field | Type | Description |
| :--- | :--- | :--- |
| `id` | `INTEGER` | Primary Key. |
| `player_id` | `INTEGER` | Foreign Key -> `players.id`. |
| `type_id` | `INTEGER` | Foreign Key -> `punishment_types.id`. |
| `issuer_uuid` | `TEXT` | UUID of the staff member (or "CONSOLE"). |
| `reason` | `TEXT` | Reason for punishment. |
| `created_at` | `INTEGER` | Timestamp when issued. |
| `expires_at` | `INTEGER` | Timestamp when it expires (NULL for permanent). |
| `active` | `INTEGER` | Boolean (0/1). Whether the punishment is currently enforced. |
| `extra_data` | `TEXT` | JSON string for additional metadata (unused currently). |

### `staff_notes`
Stores notes added by staff members.

| Field | Type | Description |
| :--- | :--- | :--- |
| `id` | `INTEGER` | Primary Key. |
| `player_id` | `INTEGER` | Foreign Key -> `players.id`. |
| `issuer_uuid` | `TEXT` | UUID of the staff member. |
| `message` | `TEXT` | Content of the note. |
| `created_at` | `INTEGER` | Timestamp when created. |

## Data Flow

1.  **Join:** When a player joins, their UUID is looked up in `players`. If missing, a row is inserted. `last_seen` is updated.
2.  **Punishment Check:** Active punishments are queried from `punishments` joined with `punishment_types`.
    - Expired punishments found during this check are auto-deactivated.
3.  **Punishment Issue:** A new row is inserted into `punishments` with `active = 1`.
4.  **Punishment Revoke:** The specific row in `punishments` is updated to set `active = 0`.
