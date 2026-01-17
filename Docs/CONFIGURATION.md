# Configuration

**ModerationPlus** uses a JSON configuration file located at `mods/data/moderationplus_config.json`.

## Default Configuration

```json
{
  "database": {
    "flush_interval_seconds": 600
  },
  "jail": {
    "x": 0.0,
    "y": 64.0,
    "z": 0.0
  }
}
```

## Configuration Keys

### `database`
Settings related to the SQLite database storage.

- `flush_interval_seconds` (Integer): How often (in seconds) the database WAL (Write-Ahead Log) is fully checkpointed to the main file. Default is `600` (10 minutes). Lower values increase disk I/O but reduce potential data loss on hard crashes.

### `jail`
Stores the coordinates for the jail location.

- `x` (Double): X coordinate.
- `y` (Double): Y coordinate.
- `z` (Double): Z coordinate.

> **Note:** It is recommended to set the jail location using the in-game command `/setjail` rather than editing this file manually to ensure precision.
