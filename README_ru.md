# Admin Mod (Fabric 1.21.1)

Язык: [English](README.md) | Русский

Admin Mod - это серверный Fabric-мод для модерации, управления правами на команды, админ-утилит и локализации.

## Возможности

- Система прав команд (`/allowcommand`, `/denycommand`) с tab-complete.
- Команды модерации: `/ban`, `/mute`, `/tempban`, `/tempmute`.
- Команды снятия наказаний: `/unban`, `/unmute`, `/untempban`, `/untempmute`.
- Утилиты администратора: `/bc`, `/tps`, `/invsee`, `/near`, `/vanish` (`/v`), `/rename`, `/lore`.
- Локализация через файлы `config/locales/locale_(countrycode).loc`.
- Команды `/locale set <code>` и `/locale reload`.
- Справка `/help adminmod` (и `/adminhelp`) по командам.

## Команды

### Утилиты

- `/bc <message>`
- `/bc <tag> <message>`
- `/tps`
- `/invsee <player>`
- `/near [radius]`
- `/vanish` или `/v`
- `/rename <name>`
- `/lore <text>` (`\\n` для переноса строк)

### Управление правами

- `/allowcommand <player> <permission>`
- `/denycommand <player> <permission>`

### Модерация

- `/ban <player> [reason]`
- `/mute <player> [reason]`
- `/tempban <player> <duration> [reason]`
- `/tempmute <player> <duration> [reason]`
- `/unban <player>`
- `/unmute <player>`
- `/untempban <player>`
- `/untempmute <player>`

Примеры формата времени:

- `10m`
- `2h`
- `3d`
- `1w`
- `1d12h`

### Локализация и справка

- `/locale set <code>`
- `/locale reload`
- `/help adminmod`
- `/adminhelp`

## Конфигурация

Основной конфиг:

- `config/adminmod/config.json`

Права:

- `config/adminmod/permissions.json`

Наказания:

- `config/adminmod/punishments/bans.json`
- `config/adminmod/punishments/mutes.json`
- `config/adminmod/punishments/tempbans.json`
- `config/adminmod/punishments/tempmutes.json`

Локали:

- `config/locales/locale_en.loc`
- пользовательские файлы: `config/locales/locale_(countrycode).loc`

## Требования

- Minecraft 1.21.1
- Fabric Loader 0.16.9+
- Fabric API (для 1.21.1)
- Java 21+

## Сборка

Windows:

```cmd
gradlew.bat clean build
```

Linux/macOS:

```bash
./gradlew clean build
```

Готовый jar:

- `build/libs/adminmod-<version>.jar`

## Установка

1. Установите Fabric Loader для Minecraft 1.21.1.
2. Поместите Fabric API и jar Admin Mod в папку `mods/` сервера.
3. Запустите сервер.

## Лицензия

MIT
