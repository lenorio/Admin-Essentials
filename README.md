# Admin Mod для Fabric 1.21.1

Полнофункциональный мод для администраторов серверов Minecraft с поддержкой команд в чате, консоли и системой прав доступа.

## 🚀 Быстрый старт

### Сборка мода

**ВАЖНО: Перед сборкой удалите старую папку!**
```cmd
rd /s /q "src\main\java\net\russianphonks\adminmod\command"
```

**Вариант 1 - Python скрипт (рекомендуется):**
```cmd
python build.py
```

**Вариант 2 - Batch скрипт:**
```cmd
build-mod.bat
```

**Вариант 3 - Вручную:**
```cmd
gradlew.bat clean build
```

Готовый JAR файл будет в `build\libs\adminmod-1.0.0.jar`

---

## Команды

### /bc (Broadcast)
Отправляет сообщение всем игрокам на сервере.
```
/bc &aСервер перезагружается через 5 минут!
```

### /tps
Показывает информацию о TPS, MSPT, CPU и RAM сервера в красивом формате.
```
/tps
```

### /invsee <player>
Позволяет просмотреть и редактировать инвентарь игрока (только через сервер-сайд GUI).
```
/invsee nicolay
```

### /near [radius]
Показывает список игроков рядом в указанном радиусе (по умолчанию 50 блоков).
```
/near
/near 100
```

### /vanish или /v
Делает игрока невидимым и скрывает его из TAB-листа.
```
/vanish
/v
```

### /rename <name>
Переименовывает предмет в руке с поддержкой цветов и переносов строк.
```
/rename &c&lЛегендарный меч
```

### /lore <lore>
Добавляет описание (lore) к предмету в руке. Используйте `\n` для переносов строк.
```
/lore &7Легендарное оружие\n&7Обладает магической силой
```

### /allowcommand <player> <permission>
Выдает игроку разрешение на выполнение команды (требует op level 4).
```
/allowcommand nicolay adminmod.tps
/allowcommand nicolay adminmod.vanish
```

### /denycommand <player> <permission>
Отнимает разрешение на выполнение команды (требует op level 4).
```
/denycommand nicolay adminmod.tps
```

### /ban <player> [reason]
Перманентный бан игрока с киком.

### /mute <player> [reason]
Перманентный мут (блокирует чат и команды личных сообщений).

### /tempban <player> <duration> [reason]
Временный бан. Форматы времени: `10m`, `2h`, `3d`, `1w`, `1d12h`.

### /tempmute <player> <duration> [reason]
Временный мут с тем же форматом времени.

### /unban /unmute /untempban /untempmute <player>
Снятие соответствующих наказаний.

### /locale set <code>
Смена локали из папки `config/locales`.

### /help adminmod
Справка по командам мода и permission-нодам.

## Коды цветов

Поддерживаются стандартные Minecraft коды цветов:
- `&0` - Черный
- `&1` - Темно-синий
- `&2` - Темно-зеленый
- `&3` - Темно-голубой
- `&4` - Темно-красный
- `&5` - Темно-фиолетовый
- `&6` - Золотой
- `&7` - Серый
- `&8` - Темно-серый
- `&9` - Синий
- `&a` - Зеленый
- `&b` - Голубой
- `&c` - Красный
- `&d` - Магента
- `&e` - Желтый
- `&f` - Белый
- `&l` - Жирный
- `&n` - Подчеркнутый
- `&o` - Курсив
- `&r` - Сброс форматирования

## Конфигурация

Конфиг находится в `config/adminmod/config.json` и создается автоматически при первом запуске.

```json
{
  "console_prefix": "&8[&6CONSOLE&8]&r",
  "default_player_prefix": "&8[&6ADMIN&8]&r",
  "near_default_radius": 50,
  "enable_vanish_particles": false,
  "enable_vanish_sounds": false,
  "player_prefixes": {
    "russianphonks": "&8[&9Admin&8]&r"
  }
}
```

## Система прав

Права хранятся в `config/adminmod/permissions.json` и загружаются при старте сервера.

```json
{
  "nicolay": [
    "adminmod.tps",
    "adminmod.vanish",
    "adminmod.near"
  ]
}
```

## Требования

- Minecraft 1.21.1
- Fabric Loader 0.16.9+
- Fabric API 0.100.3+1.21.1
- Java 21+

## Установка

1. Скачайте Fabric Loader с [fabricmc.net](https://fabricmc.net)
2. Скачайте Fabric API с [CurseForge](https://www.curseforge.com/minecraft/mc-mods/fabric-api) или [Modrinth](https://modrinth.com/mod/fabric-api)
3. Поместите `adminmod-1.0.0.jar` и `fabric-api.jar` в папку `mods` вашего сервера
4. Запустите сервер

## Сборка

### Перед сборкой (ВАЖНО!)
Удалите устаревшую папку с дубликатами:
```cmd
rd /s /q "src\main\java\net\russianphonks\adminmod\command"
```

### Способы сборки

**Python скрипт (автоматически удалит старую папку):**
```cmd
python build.py
```

**Batch скрипт:**
```cmd
build-mod.bat
```

**Вручную через Gradle:**
```bash
./gradlew build
```
или на Windows:
```cmd
gradlew.bat clean build
```

Готовый JAR файл будет находиться в `build/libs/`

## Возможные проблемы при сборке

### "Cannot find symbol: VanishCommand/RenameCommand/LoreCommand"
**Причина:** Старая папка `command` не удалена
**Решение:** Удалите `src\main\java\net\russianphonks\adminmod\command`

### "TabListMixin not found"
**Решение:** Уже исправлено в последней версии

### "PowerShell not found"
**Решение:** Используйте `python build.py` или установите [PowerShell 7+](https://aka.ms/powershell)

## <mixin не работает полностью>

Этот мод использует Mixins для перехвата событий игрока и команд. Убедитесь, что Fabric Loader версии 0.16.9 или выше установлен.

### Текущие Mixins:
- `PlayerEntityMixin` - расширяемый mixin для модификации игрока
- `ServerPlayNetworkHandlerMixin` - перехват выполнения команд

## Структура проекта

```
adminmod/
├── src/main/java/net/russianphonks/adminmod/
│   ├── AdminMod.java              # Главный класс мода
│   ├── commands/                  # Все команды
│   │   ├── AllowCommandCommand.java
│   │   ├── BroadcastCommand.java
│   │   ├── DenyCommandCommand.java
│   │   ├── InvSeeCommand.java
│   │   ├── LoreCommand.java
│   │   ├── NearCommand.java
│   │   ├── RenameCommand.java
│   │   ├── TpsCommand.java
│   │   └── VanishCommand.java
│   ├── config/                    # Система конфигурации
│   │   └── ConfigManager.java
│   ├── mixin/                     # Mixins
│   │   ├── PlayerEntityMixin.java
│   │   └── ServerPlayNetworkHandlerMixin.java
│   └── util/                      # Утилиты
│       ├── ColorFormatter.java
│       ├── ColorUtil.java
│       ├── PermissionChecker.java
│       ├── PermissionManager.java
│       └── TPSMonitor.java
└── src/main/resources/
    ├── adminmod.mixins.json       # Конфигурация mixins
    └── fabric.mod.json            # Метаданные мода
```

## Лицензия

MIT

## Публикация на Modrinth

В репозитории добавлены файлы для релиза:
- `CHANGELOG.md`
- `MODRINTH_RELEASE.md`

Перед публикацией проверьте:
1. Актуальность ссылок в `src/main/resources/fabric.mod.json`
2. Наличие иконки мода (опционально)
3. Сборку `gradlew.bat clean build`
