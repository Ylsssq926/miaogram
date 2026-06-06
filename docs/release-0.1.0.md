# MiaoGram 0.1.0 🐾

This is the first public source release of MiaoGram.

MiaoGram is an unofficial Android client based on Telegram's Android source code. It keeps the familiar Telegram experience and adds a layer of multi-account, reading, translation, privacy, and quality-of-life tools.

## Important before you build

MiaoGram 0.1.0 does **not** include Telegram API credentials or an official release APK.

To build a usable app, register your own Telegram application at:

https://my.telegram.org/apps

Then create:

```properties
.private/api-keys.properties
APP_ID=your_numeric_api_id
APP_HASH=your_32_character_api_hash
```

or set environment variables:

```bash
MIAO_APP_ID=your_numeric_api_id
MIAO_APP_HASH=your_32_character_api_hash
```

The build intentionally fails without valid credentials. Please do not reuse API credentials from other projects or screenshots.

## Highlights

- More local account slots.
- Account remarks for easier account switching.
- Unified cross-account inbox for unread chats and mentions.
- Channel reader with manual reading priority groups.
- Built-in translation: Google free source by default, Telegram official source optional.
- Whole-chat translation unlocked client-side when MiaoGram translation is enabled.
- Link tracking-parameter cleanup.
- Streamer mode / screenshot protection toggle.
- Hide Stories and the All Chats folder tab.
- Profile user ID + photo DC display.
- Optional seconds in message timestamps.
- Keyword notification muting.
- Safer destructive-dialog defaults.

## Stability note

0.1.0 is an early public source release. It compiles in the maintainer environment, but it has not gone through a full manual device QA pass yet.

If you are here to test: welcome, and please bring logs.

If you need a guaranteed stable Telegram client: keep official Telegram installed too. The cat is awake, but still learning where all the furniture is.

## License and notice

MiaoGram is GPLv2. See `LICENSE` and `NOTICE.md`.

MiaoGram is not affiliated with, endorsed by, or connected to Telegram FZ-LLC or the official Telegram developers.
