# Setup Guide

Follow these steps to install and configure **ModerationPlus** on your Hytale server.

## Prerequisites

- A running Hytale Server.
- Java 21 or newer (bundled with Hytale server).

## Installation

1.  **Download:** Get the latest `ModerationPlus.jar` release.
2.  **Install:** Copy the `.jar` file into the `mods` folder of your server directory.
    - Example path: `MyHytaleServer/mods/ModerationPlus.jar`
3.  **Start Server:** Run your server start script.
4.  **Verify:** Check the console for the startup message:
    ```
    [INFO] [ModerationPlus] StorageManager initialized successfully.
    [INFO] [ModerationPlus] ModerationPlus has been enabled!
    ```

## Post-Installation Configuration

### 1. Set Permissions
Assign the permission nodes listed in `PERMISSIONS.md` to your staff ranks. This is usually done through your permission management system or a permissions file if Hytale provides one by default.

### 2. Set Jail Location
1.  Login to the server as an Admin.
2.  Build a secure jail area (ensure players cannot escape).
3.  Stand inside the jail area.
4.  Run `/setjail`.
    - *You should see a confirmation message.*

### 3. Customize Config (Optional)
If you wish to change the database flush interval:
1.  Stop the server.
2.  Edit `mods/data/moderationplus_config.json`.
3.  Change `flush_interval_seconds`.
4.  Start the server.

## Updating

 To update ModerationPlus:
1.  Stop the server.
2.  Replace the old `ModerationPlus.jar` with the new version.
3.  Start the server.
    - *The plugin will automatically run any necessary database migrations.*
