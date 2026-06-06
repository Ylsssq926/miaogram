# MiaoGram 🐾 / 喵Gram

**中文** | [English](#english)

MiaoGram（喵Gram）是一个基于 Telegram Android 源码的非官方开源 Android 客户端。

它尽量保留 Telegram 原本熟悉的体验，同时加上一层「重度用户也能轻松上手」的增强：更多账号、跨账号阅读、链接清理、内置翻译、隐私开关，以及一些日常使用里会让人觉得顺手的小功能。猫爪不大，但挠得很准。

> **0.1 状态**：这是早期公开源码版本。代码已在维护者环境中通过编译，但这个 tag 还没有做完整人工真机验收。仓库不包含 Telegram API 凭据，也不提供正式 APK。你如果要构建/登录，需要自备 Telegram API ID/HASH。

## 和官方 Telegram 有什么不同？

### MiaoGram 的招牌功能

- **更多本地账号槽位** —— 面向确实需要管理多个 Telegram 身份的用户。
- **跨账号聚合收件箱** —— 把已登录账号的未读对话和 @ 提及聚合到一个视图。
- **频道阅读器** —— 按阅读优先级整理频道：必读、补读、归档、其他。
- **内置翻译** —— 默认使用许多第三方客户端也在用的免费 Google 翻译端点，同时可以切回 Telegram 官方翻译源。启用 MiaoGram 翻译增强后，整段对话翻译会在客户端侧解锁。

### 日常舒适功能

- 账号备注，避免多个账号看起来一模一样。
- 隐藏不想占位置的「所有聊天」文件夹 tab。
- 隐藏 Stories 横条。
- 清理外链里的常见追踪参数。
- Streamer Mode / 截图录屏保护开关。
- Profile 显示用户 ID 和头像 DC。
- 消息时间可选显示到秒。
- 关键词通知静默：消息仍然存在，只是不响铃。
- 部分危险确认对话框使用更安全的默认值。

一些社区/隐私向开关默认关闭。MiaoGram 想做的是有用的小猫，不是突然扑你脸上的惊吓盒子。

## 0.1 发布说明

这个版本主要面向：

- 想查看源码的人；
- 想自己构建非官方 Telegram 客户端的开发者；
- 能接受本地凭据和早期粗糙边缘的测试用户。

它**还不是**一个成熟的应用商店式稳定版本。请不要把 0.1 当作「生产稳定版」。它更像是一只已经会几个动作、但还在熟悉家具摆放的小猫。

## Telegram API 凭据

MiaoGram **不内置** Telegram API 凭据。

在应用能连接 Telegram 服务器之前，你必须在这里注册自己的 Telegram 应用：

<https://my.telegram.org/apps>

然后创建本地、被 git 忽略的文件：

```properties
# .private/api-keys.properties
APP_ID=123456
APP_HASH=0123456789abcdef0123456789abcdef
```

或者为 CI / 本地 shell 提供环境变量：

```bash
MIAO_APP_ID=123456
MIAO_APP_HASH=0123456789abcdef0123456789abcdef
```

如果两种来源都没有有效凭据，构建会故意失败。不要复制 Telegram、Telegram-FOSS、AyuGram、Nekogram、MiaoGram 维护者、截图、随机博客，或者你朋友家猫的 API 凭据。请注册自己的。

为什么这么严格？

- Telegram 要求发布应用使用自己的 API 凭据。
- 共享凭据可能被限流或封禁，影响所有使用者。
- 源码发布不应该泄露密钥。猫可以打翻水杯，但不能打翻 API key。

## 从源码构建

### 前置要求

- JDK 17 或更高版本
- Android Studio / Android SDK，`compileSdk 35`
- Android Build Tools **35.0.0**
- Android NDK **27.2.12479018**
- Android Gradle 插件支持的 CMake
- 足够磁盘空间（Telegram Android 构建体积不小）

### 步骤

```bash
git clone https://github.com/Ylsssq926/miaogram.git
cd miaogram
mkdir -p .private
```

按上面的说明创建 `.private/api-keys.properties`，或者在运行 Gradle 前导出 `MIAO_APP_ID` / `MIAO_APP_HASH`。

如果你不通过 Android Studio 构建，请确保 `local.properties` 指向你的 Android SDK，例如：

```properties
sdk.dir=C\:\\Users\\you\\AppData\\Local\\Android\\Sdk
```

然后构建常规 app 变体，例如：

```bash
./gradlew :TMessagesProj_App:assembleAfatRelease
```

开发时快速做 Java 编译检查：

```bash
./gradlew :TMessagesProj:compileDebugJavaWithJavac
```

## 仓库结构

```text
miaogram/
├── TMessagesProj/              Telegram Android library module + MiaoGram hooks
├── TMessagesProj_App/          Main Android app module
├── docs/                       Public notes and design docs
├── .github/AI_GUIDELINES.md    Rules for AI-assisted contributions
├── LICENSE                     GPLv2
└── README.md
```

私有规划文档、签名材料、API 凭据、日志和策略笔记放在本地 `.private/` 下，并被 git 忽略。

## 开源与许可证

MiaoGram 使用 **GNU General Public License v2.0** 授权。详见 [LICENSE](LICENSE)。

本仓库是基于 Telegram Android 源码的修改作品。如果你分发修改后的二进制文件，也必须按照 GPLv2 条款提供对应源码。

请同时阅读 [NOTICE.md](NOTICE.md)，里面有署名、商标、API 凭据、隐私和再分发说明。

## 非官方项目声明

MiaoGram 与 Telegram FZ-LLC 或官方 Telegram 开发者没有从属、背书、赞助或关联关系。

"Telegram" 是其对应权利人的商标。MiaoGram 使用自己的名称和品牌，因为假装自己是官方应用既容易误导用户，也不够喵。

## 贡献

欢迎贡献，尤其欢迎真机测试反馈和 bug report。如果你使用 AI 助手参与开发，请遵守 [.github/AI_GUIDELINES.md](.github/AI_GUIDELINES.md)：不要提交密钥、私有策略、凭据泄露或奇怪惊喜。

## 免责声明

使用非官方客户端需自行承担风险。MiaoGram 可能有 bug，可能会因 Telegram 上游变化而失效，也可能与官方 Telegram 行为不同。

如果你愿意帮忙测试，请从 [0.1 测试清单](docs/testing-checklist.md) 开始。这样反馈更容易复现，也更容易修。

如果你追求稳定，请同时保留官方 Telegram。猫也喜欢备用纸箱。

---

# English

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

## Building from source

Prerequisites:

- JDK 17 or newer
- Android SDK with **compileSdk 35**
- Android Build Tools **35.0.0**
- Android NDK **27.2.12479018**
- CMake supported by the Android Gradle plugin

```bash
git clone https://github.com/Ylsssq926/miaogram.git
cd miaogram
mkdir -p .private
```

Create `.private/api-keys.properties` as described above, or export `MIAO_APP_ID` / `MIAO_APP_HASH` before running Gradle.

```bash
./gradlew :TMessagesProj_App:assembleAfatRelease
```

For quick Java-level checks during development:

```bash
./gradlew :TMessagesProj:compileDebugJavaWithJavac
```

## Testing

If you want to help test MiaoGram, start with the [0.1 testing checklist](docs/testing-checklist.md).

## License and notice

MiaoGram is licensed under the **GNU General Public License v2.0**. See [LICENSE](LICENSE).

This repository is a modified work based on Telegram's Android source code. If you distribute a modified binary, you must also provide the corresponding source code under the GPLv2 terms.

Please also read [NOTICE.md](NOTICE.md) for attribution, trademark, API credential, privacy, and redistribution notes.

MiaoGram is not affiliated with, endorsed by, or connected to Telegram FZ-LLC or the official Telegram developers.
