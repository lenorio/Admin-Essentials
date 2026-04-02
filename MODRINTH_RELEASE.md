# Modrinth Release Checklist

## 1. Build
```bash
gradlew.bat clean build
```
Upload jar from `build/libs/`.

## 2. Required Modrinth Metadata
- Name: Admin Mod
- Summary: Server-side Fabric moderation and admin utility mod
- Categories: Administration, Utility
- Environment: Server
- Loaders: Fabric
- Game versions: 1.21.1
- License: MIT

## 3. Links (set in project + fabric.mod.json)
- Homepage: https://modrinth.com/mod/adminmod
- Source: https://github.com/russianphonks/adminmod
- Issues: https://github.com/russianphonks/adminmod/issues

## 4. Release Notes Template
- Added localization system with `config/locales/locale_en.loc`
- Added moderation commands: `/ban`, `/mute`, `/tempban`, `/tempmute`
- Added unpunish commands: `/unban`, `/unmute`, `/untempban`, `/untempmute`
- Added `/locale` and Admin help commands
- Added permission-based tab-complete for `/allowcommand` and `/denycommand`
- Added custom broadcast tags for `/bc <tag> <message>`

## 5. Verify before publish
- Server starts without errors
- Permission checks work for non-op players
- Temp punishments expire correctly
- Locale switch works and custom locale files are loaded
- Mixins load successfully

## 6. Optional improvements
- Add `icon.png` and `"icon": "assets/adminmod/icon.png"` in `fabric.mod.json`
- Add screenshots to Modrinth gallery
- Add changelog in both EN/RU
