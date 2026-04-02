# Changelog

## 1.1.0
- Added localization manager and locale files support (`config/locales/locale_(countrycode).loc`)
- Added default locale file (`config/locales/locale_en.loc`)
- Added `/locale set <code>` and `/locale reload`
- Added `/help adminmod` and `/adminhelp`
- Added punishment commands: `/ban`, `/mute`, `/tempban`, `/tempmute`
- Added unpunish commands: `/unban`, `/unmute`, `/untempban`, `/untempmute`
- Added punishment storage in separate files:
  - `config/adminmod/punishments/bans.json`
  - `config/adminmod/punishments/mutes.json`
  - `config/adminmod/punishments/tempbans.json`
  - `config/adminmod/punishments/tempmutes.json`
- Added chat/message blocking for muted players
- Added tab-complete for `/allowcommand` and `/denycommand`
- Added custom broadcast tags for `/bc`
- Unified permission checks so granted permissions apply consistently
