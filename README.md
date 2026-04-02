# Admin Mod (Fabric 1.21.1)

Language: English | [Русский](README_ru.md)

Admin Mod is a server-side Fabric mod focused on moderation, command permissions, quality-of-life admin tools, and localization.

## Features

- Permission-based command system (`/allowcommand`, `/denycommand`) with tab-complete.
- Moderation commands: `/ban`, `/mute`, `/tempban`, `/tempmute`.
- Unpunish commands: `/unban`, `/unmute`, `/untempban`, `/untempmute`.
- Utility/admin commands: `/bc`, `/tps`, `/invsee`, `/near`, `/vanish` (`/v`), `/rename`, `/lore`.
- Locales from files in `config/locales/locale_(countrycode).loc`.
- `/locale set <code>` and `/locale reload`.
- `/help adminmod` (and `/adminhelp`) for command overview.

## Commands

### Utilities

- `/bc <message>`
- `/bc <tag> <message>`
- `/tps`
- `/invsee <player>`
- `/near [radius]`
- `/vanish` or `/v`
- `/rename <name>`
- `/lore <text>` (`\\n` for new lines)

### Permission Management

- `/allowcommand <player> <permission>`
- `/denycommand <player> <permission>`

### Moderation

- `/ban <player> [reason]`
- `/mute <player> [reason]`
- `/tempban <player> <duration> [reason]`
- `/tempmute <player> <duration> [reason]`
- `/unban <player>`
- `/unmute <player>`
- `/untempban <player>`
- `/untempmute <player>`

Duration format examples:

- `10m`
- `2h`
- `3d`
- `1w`
- `1d12h`

### Localization & Help

- `/locale set <code>`
- `/locale reload`
- `/help adminmod`
- `/adminhelp`

## Configuration

Main config:

- `config/adminmod/config.json`

Permissions:

- `config/adminmod/permissions.json`

Punishments:

- `config/adminmod/punishments/bans.json`
- `config/adminmod/punishments/mutes.json`
- `config/adminmod/punishments/tempbans.json`
- `config/adminmod/punishments/tempmutes.json`

Locales:

- `config/locales/locale_en.loc`
- custom files: `config/locales/locale_(countrycode).loc`

## Requirements

- Minecraft 1.21.1
- Fabric Loader 0.16.9+
- Fabric API (matching 1.21.1)
- Java 21+

## Build

Windows:

```cmd
gradlew.bat clean build
```

Linux/macOS:

```bash
./gradlew clean build
```

Output jar:

- `build/libs/adminmod-<version>.jar`

## Installation

1. Install Fabric Loader for Minecraft 1.21.1.
2. Put Fabric API and Admin Mod jar in your server `mods/` folder.
3. Start the server.

## License

MIT
