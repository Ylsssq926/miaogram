# MiaoGram 🐾

MiaoGram is an unofficial, open-source Android client based on Telegram's Android source code.

It keeps the Telegram experience familiar, then adds a layer of "power-user but still cozy" tools: more accounts, cross-account reading, cleaner links, built-in translation, privacy toggles, and a few small quality-of-life scratches behind the ear.

> **0.1 status**: early public source release. The code compiles in the maintainer environment, but this tag is not a fully hand-tested stable build yet. No API credentials or release APK are included. If you build it, bring your own Telegram API ID/HASH.

## What is different from official Telegram?

### Signature MiaoGram features

- **More local account slots** — designed for people who actually use multiple Telegram identities.
- **Unified cross-account inbox** — one view for unread chats and mentions across logged-in accounts.
- **Channel reader** — group channels by reading priority: must-read, catch-up, archived, and other.
- **Built-in translation** — defaults to the free Google translation endpoint used by many third-party clients, with an option to switch back to Telegram's official source. Whole-chat translation is unlocked client-side when the feature is enabled.

### Everyday comfort features

- Account remarks, so same-looking accounts are less confusing.
- Hide the "All Chats" folder tab when you do not want it taking space.
- Hide the Stories strip.
- Strip common tracking parameters from outbound links.
- Streamer mode / screenshot protection toggle.
- Show user ID and photo DC on profiles.
- Optional seconds in message timestamps.
- Keyword notification muting: matching messages still exist, they just do not ring the bell.
- Safer defaults for some destructive confirmation dialogs.

Some community/privacy toggles are intentionally off by default. MiaoGram tries to be useful without turning the app into a surprise machine.

## 0.1 release notes

This release is mainly for:

- people who want to inspect the source,
- developers who want to build their own unofficial Telegram client,
- early testers who are comfortable with local credentials and rough edges.

It is **not** yet a polished app-store-style release. Please do not treat 0.1 as "production stable". It is more like a curious kitten that already knows a few tricks.

## Telegram API credentials

MiaoGram does **not** ship Telegram API credentials.

Before the app can connect to Telegram servers, you must register your own application at:

<https://my.telegram.org/apps>

Then either create this local, gitignored file:

```properties
# .private/api-keys.properties
APP_ID=123456
APP_HASH=0123456789abcdef0123456789abcdef
```

or provide environment variables for CI/local shells:

```bash
MIAO_APP_ID=123456
MIAO_APP_HASH=0123456789abcdef0123456789abcdef
```

The build intentionally fails if neither source contains valid credentials. Do not copy API credentials from Telegram, Telegram-FOSS, AyuGram, Nekogram, MiaoGram maintainers, screenshots, random blog posts, or your friend's cat. Register your own.

Why so strict?

- Telegram requires published apps to use their own API credentials.
- Shared API credentials can be rate-limited or blocked for everyone.
- A source release should not leak secrets. Cats knock over cups, not API keys.

## Building from source

### Prerequisites

- JDK 17 or newer
- Android Studio / Android SDK with **compileSdk 35**
- Android Build Tools **35.0.0**
- Android NDK **27.2.12479018**
- CMake supported by the Android Gradle plugin
- Enough disk space for a Telegram Android build (it is not tiny)

### Steps

```bash
git clone https://github.com/Ylsssq926/miaogram.git
cd miaogram
mkdir -p .private
```

Create `.private/api-keys.properties` as described above, or export `MIAO_APP_ID` / `MIAO_APP_HASH` before running Gradle.

If you build outside Android Studio, make sure `local.properties` points to your Android SDK, for example:

```properties
sdk.dir=C\:\\Users\\you\\AppData\\Local\\Android\\Sdk
```

Then build the regular app variant, for example:

```bash
./gradlew :TMessagesProj_App:assembleAfatRelease
```

For quick Java-level checks during development:

```bash
./gradlew :TMessagesProj:compileDebugJavaWithJavac
```

## Repository layout

```text
miaogram/
├── TMessagesProj/              Telegram Android library module + MiaoGram hooks
├── TMessagesProj_App/          Main Android app module
├── docs/                       Public notes and design docs
├── .github/AI_GUIDELINES.md    Rules for AI-assisted contributions
├── LICENSE                     GPLv2
└── README.md
```

Private planning docs, signing material, API credentials, logs, and strategy notes live under `.private/` locally and are intentionally ignored by git.

## Open-source and license

MiaoGram is licensed under the **GNU General Public License v2.0**. See [LICENSE](LICENSE).

This repository is a modified work based on Telegram's Android source code. If you distribute a modified binary, you must also provide the corresponding source code under the GPLv2 terms.

Please also read [NOTICE.md](NOTICE.md) for attribution, trademark, API credential, privacy, and redistribution notes.

## Unofficial project notice

MiaoGram is not affiliated with, endorsed by, or connected to Telegram FZ-LLC or the official Telegram developers.

"Telegram" is a trademark of its respective owner. MiaoGram uses its own name and branding because pretending to be the official app would be rude, confusing, and not very meow.

## Contributing

Contributions are welcome, especially bug reports from real-device testing. If you work with AI assistants, follow [.github/AI_GUIDELINES.md](.github/AI_GUIDELINES.md): no secrets, no private strategy, no credential leaks, no weird surprises.

## Disclaimer

Use unofficial clients at your own risk. MiaoGram may contain bugs, may break after upstream Telegram changes, and may behave differently from official Telegram.

If in doubt, keep official Telegram installed too. Cats like backup boxes.
