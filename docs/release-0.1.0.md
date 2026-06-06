# MiaoGram 0.1.0 🐾 / 喵Gram 0.1.0

**English** | [中文](#中文)

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

---

# 中文

这是 MiaoGram 的第一个公开源码版本。

MiaoGram 是一个基于 Telegram Android 源码的非官方 Android 客户端。它保留熟悉的 Telegram 体验，同时加入多账号、阅读管理、翻译、隐私和日常体验增强工具。

## 构建前请先看这里

MiaoGram 0.1.0 **不包含** Telegram API 凭据，也不提供官方发布 APK。

如果你想构建可登录的应用，请先在这里注册自己的 Telegram 应用：

https://my.telegram.org/apps

然后创建：

```properties
.private/api-keys.properties
APP_ID=your_numeric_api_id
APP_HASH=your_32_character_api_hash
```

或设置环境变量：

```bash
MIAO_APP_ID=your_numeric_api_id
MIAO_APP_HASH=your_32_character_api_hash
```

没有有效凭据时，构建会故意失败。请不要复用其他项目或截图里的 API 凭据。

## 亮点

- 更多本地账号槽位。
- 账号备注，方便区分账号。
- 跨账号聚合收件箱，汇总未读对话和 @ 提及。
- 频道阅读器，支持手动阅读优先级分组。
- 内置翻译：默认 Google 免费源，可选 Telegram 官方源。
- 启用 MiaoGram 翻译后，整段对话翻译在客户端侧解锁。
- 外链追踪参数清理。
- Streamer Mode / 截图录屏保护开关。
- 隐藏 Stories 和「所有聊天」文件夹 tab。
- Profile 显示用户 ID 和头像 DC。
- 消息时间可选显示到秒。
- 关键词通知静默。
- 更安全的危险确认对话框默认值。

## 稳定性说明

0.1.0 是早期公开源码版本。它已在维护者环境中通过编译，但还没有经过完整人工真机 QA。

如果你是来测试的：欢迎，记得带日志。

如果你需要绝对稳定的 Telegram 客户端：请同时保留官方 Telegram。小猫已经醒了，但还在熟悉家具摆放。

## 许可证与声明

MiaoGram 使用 GPLv2。请查看 `LICENSE` 和 `NOTICE.md`。

MiaoGram 与 Telegram FZ-LLC 或官方 Telegram 开发者没有从属、背书或关联关系。
