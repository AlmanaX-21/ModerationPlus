# Database Notes

## Performance & Tuning
- **WAL Mode:** The database automatically uses WAL (Write-Ahead Logging) for better concurrency.
- **Flush Interval:**
  - Default: 600s (10 minutes).
  - **Tuning:** If you have very high punishment volume (spam bots), you might lower this to `300` (5 minutes) in `configuration.json` to reduce the journal size.
  - **Warning:** Setting this too low (e.g., `< 60`) may cause excessive disk I/O.

## Limitations
- **Single Server Only:** Because SQLite is a file-based database, **ModerationPlus cannot share data across multiple servers** (e.g., a network).
- **Concurrency:** SQLite handles strictly sequential writes. While fine for a single server, extremely high concurrent write loads (hundreds per second) might see latency.

## Troubleshooting
- **Locked Database:**
  - Symptom: `[SQLITE_BUSY] The database file is locked`
  - Cause: Another process (or a file viewer) has the `.db` file open in write mode.
  - Fix: Close any external SQLite viewers while the server is running.
- **Corruption:**
  - If the file becomes corrupted (0 bytes or invalid header), stop the server, delete `moderation.db`, and restart. **Warning: All data will be lost.**

## Todo / Future Plans
- [ ] Add MySQL Support for cross-server synchronization.
- [ ] Add backup command to export data to JSON.
