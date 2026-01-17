# Database System

**ModerationPlus** runs on a local **SQLite** database. This ensures valid data persistence without the need for setting up external database servers like MySQL or PostgreSQL.

## Supported Backends

| Backend | Status | Description |
| :--- | :--- | :--- |
| **SQLite** | ✅ Active | Default and only specific backend. Stores data in `mods/data/moderation.db`. |
| MySQL | ❌ Unsupported | Planned for future release. |
| MongoDB | ❌ Unsupported | No plans for implementation. |
| JSON/Flatfile | ❌ Unsupported | Not used for core data (only config). |

## High-Level Behaviors

### Connection Lifecycle
- **Startup:** The plugin initializes the JDBC connection to the SQLite file during the server startup phase.
- **Runtime:** The connection remains open.
- **Shutdown:** The connection is explicitly closed when the plugin is disabled to prevent data corruption.

### Data Safety
- **WAL Mode:** The database implementation triggers `PRAGMA wal_checkpoint` periodically to ensure Write-Ahead Logging integrity.
- **Auto-Flush:** A scheduled task runs every 10 minutes (configurable) to fully checkpoint the WAL to the main database file.

### Migrations
- The system includes an automatic migration engine.
- On startup, it checks the current schema version and applies any missing patches sequentially.
- This ensures that updating the plugin is safe and does not require manual database scripts.
