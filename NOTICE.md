# MiaoGram Notice 🐾

This document explains the boring-but-important parts: attribution, licensing, trademarks, API credentials, privacy expectations, and redistribution responsibilities.

MiaoGram wants to be a friendly cat, not a mystery box.

## 1. Unofficial Telegram client

MiaoGram is an unofficial Telegram client based on Telegram's Android source code.

It is **not** affiliated with, endorsed by, sponsored by, or connected to Telegram FZ-LLC or the official Telegram developers.

The name "Telegram" and related marks belong to their respective owners. MiaoGram uses its own name, icon, and branding to avoid pretending to be the official app.

## 2. License

MiaoGram is distributed under the **GNU General Public License v2.0**. See [LICENSE](LICENSE).

In short:

- You may study, modify, and redistribute the source code under GPLv2.
- If you distribute a modified binary, you must provide the corresponding source code.
- You must keep the license notices intact.
- There is no warranty. If the cat jumps on the keyboard, keep backups.

This is only a friendly summary; the actual license text controls.

## 3. Telegram API credentials

MiaoGram does **not** include maintainer API credentials.

Anyone building a usable client must register their own Telegram application at:

<https://my.telegram.org/apps>

and provide local credentials through either:

```properties
.private/api-keys.properties
APP_ID=your_numeric_api_id
APP_HASH=your_32_character_api_hash
```

or environment variables:

```bash
MIAO_APP_ID=your_numeric_api_id
MIAO_APP_HASH=your_32_character_api_hash
```

Do not reuse API credentials from other projects, screenshots, blog posts, leaked builds, or upstream examples. Shared credentials can be limited, blocked, or create problems for unrelated users.

## 4. Privacy expectations

MiaoGram's custom features are designed to run locally unless explicitly described otherwise.

Notable network-facing behavior:

- Telegram functionality connects to Telegram's servers, as any Telegram client must.
- The built-in translation enhancement can use Google's free translation endpoint when the user selects the Google source. Users can switch the source back to Telegram official translation in MiaoGram settings.

MiaoGram does not intentionally include hidden telemetry, credential collection, or silent account harvesting. Please report anything that looks suspicious.

## 5. Redistribution checklist

If you publish your own build or fork, please do the tidy thing:

- Use your own app name/icon if your changes could confuse users.
- Use your own Telegram API ID/HASH.
- Publish corresponding source code when distributing binaries.
- Do not remove GPL notices.
- Do not claim to be official Telegram.
- Tell users what changed.
- Do not hide network behavior.

A good fork should make users feel safe, not make them sniff the APK like a suspicious cat.

## 6. No warranty

MiaoGram is provided "as is", without warranty of any kind. Use it at your own risk.

Keep official Telegram installed if reliability matters. MiaoGram 0.1 is an early public release, not a battle-hardened stable build.
