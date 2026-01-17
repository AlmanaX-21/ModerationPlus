# Features & Mechanics

**ModerationPlus** introduces several game-play mechanics and systems to help manage your server.

## Punishment System

The punishment system is the core of ModerationPlus. It supports both temporary and permanent durations for bans and mutes.

- **Storage:** All punishments are stored in a local SQLite database. active punishments are checked on player login (for bans) or chat (for mutes).
- **Expiry:** Temporary punishments automatically expire. Use `0` or simple duration formats (e.g., `1d`, `12h`, `30m`) when issuing commands.
- **History:** All past punishments are kept for record-keeping. You can view a player's history using `/history <player>`.

## Jail System

The Jail system allows you to physically confine a player to a specific location.

- **Containment:** When a player is jailed, they are teleported to the configured jail location. A background task runs every 250ms to check if they have moved away. If they stray too far (> 1 block) from the jail point, they are teleported back.
- **Persistence:** Jail status is saved in the database. If a jailed player disconnects and reconnects, they are immediately sent back to jail.
- **Setup:** You must set the jail location using `/setjail` before you can jail anyone.

## Freeze System

The Freeze system is useful for pausing a player's actions, typically for screensharing or confronting them about rule-breaking.

- **Immobilization:** Similar to jailing, a frozen player is locked to their current position. They cannot move freely.
- **Reconnection:** Freeze status is not currently persisted across restarts; however, it is active as long as the player remains online.

## Vanish Mode

Vanish mode allows staff members to spectate without being seen.

- **Invisibility:** When vanished, you are hidden from all players who do not have the `moderation.vanish.see` permission.
- **Chat Redirection:** If you try to speak in chat while vanished, your message is blocked from global chat and instead sent only to other staff members. This prevents accidental "ghost speaking".
- **Join/Quit:** (TODO) Vanish state should ideally hide join/quit messages (This feature depends on server event cancellation support).

## Staff Chat

A dedicated channel for staff communication.

- **Usage:** Use `/sc <message>` or `/staffchat <message>` to send a message.
- **Visibility:** Only players with `moderation.staffchat` permission can see these messages.

## Data Persistence

ModerationPlus handles data reliability seriously.

- **SQLite:** Uses a self-contained SQLite database. No external database server is required.
- **Auto-Flush:** The database is checkpointed (flushed) to disk at a configurable interval (default: 10 minutes) to minimize data loss in case of a crash.
- **Migrations:** The plugin automatically handles database schema updates when you update the plugin version.
