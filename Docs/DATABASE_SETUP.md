# Database Setup

**ModerationPlus** uses **SQLite**, which requires zero zero manual setup. The system is designed to "Just Work".

## Prerequisites
- No external database server (MySQL/MariaDB) is needed.
- The plugin bundles the `sqlite-jdbc` driver, so no extra library installation is required.

## First Run
1.  Start the server.
2.  The plugin will automatically create the folder `mods/data/`.
3.  It will generate the file `mods/data/moderation.db`.
4.  It will run all necessary migrations to create the tables.

## Verification
To verify the database is working:
- Check the console logs for:
  ```
  [INFO] [StorageManager] StorageManager initialized successfully.
  ```
- Use a SQLite viewer (like **DB Browser for SQLite**) to open `mods/data/moderation.db` if you want to inspect data manually.

## Connection Configuration
Currently, there are no configurable connection strings because the path is hardcoded to the local file system.
- **Path:** `mods/data/moderation.db`
- **Driver:** `org.sqlite.JDBC`
