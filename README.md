# MiaoGram

An unofficial open-source Android client built on the Telegram API, designed for everyday users who want a smoother, more powerful messaging experience.

> ⚠️ **Status**: Early development. The project is just starting; there is no usable APK yet. Star the repo to follow progress.

## Planned Features

- ⚡ Multi-speed download with parallel chunked transfers
- 👥 Multi-account support beyond the official limit
- 📂 Enhanced folder & tab management
- 🌐 Multi-engine message translation
- 🎬 Immersive video player with gestures (speed, PiP)
- 🎨 Material 3 theming with dynamic colors
- 🔍 In-app channel discovery & rankings
- 🔔 Smart notification rules

## Building from source

### Prerequisites

- **JDK 17** (Eclipse Temurin recommended)
- **Android Studio** Ladybug (2024.2.x) or later
- **Android SDK 34** with Build-Tools 34
- **Android NDK** r26b
- About **20 GB** of free disk space

### Setup

1. Clone the repository
   ```bash
   git clone https://github.com/Ylsssq926/miaogram.git
   cd miaogram
   ```
2. Create `local.properties` in the project root:
   ```
   APP_ID=your_api_id
   APP_HASH=your_api_hash
   sdk.dir=C\:\\path\\to\\Android\\Sdk
   ```
3. Open the project in Android Studio (use **Open**, not Import)
4. Wait for Gradle sync
5. Build → Make Project

### Telegram API credentials

You must obtain your own `APP_ID` and `APP_HASH` from <https://my.telegram.org/apps> before the app can connect to Telegram servers.

**Do NOT use someone else's API credentials.** Each developer should register their own.

## Project structure

```
miaogram/
├── TMessagesProj/        Main Android module (will be added in Stage 1)
├── docs/                 Public documentation
├── LICENSE               GPLv2
└── README.md             You are here
```

## License

Licensed under the **GNU General Public License v2.0**. See [LICENSE](LICENSE) for details.

This project uses the Telegram API and is part of the Telegram ecosystem. It is **not affiliated with Telegram FZ-LLC** or its developers. The Telegram name and logo are trademarks of their respective owners.

## Contributing

Contributions are welcome. Please read the [AI collaboration guidelines](.github/AI_GUIDELINES.md) if you are working on this project with AI assistants.

## Disclaimer

This is an unofficial Telegram client. Use at your own risk. The maintainers are not responsible for any account restrictions or data loss that may result from using this application.

Conversations between MiaoGram users and users of other Telegram clients may display a security notice on the official Telegram client. This is a server-side behavior introduced by Telegram in April 2026 for all unofficial clients and cannot be disabled.
