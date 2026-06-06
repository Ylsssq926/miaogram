# MiaoGram Notice 🐾 / 喵Gram 声明

**English** | [中文](#中文)

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

---

# 中文

这份文档说明一些无聊但重要的事情：署名、许可证、商标、API 凭据、隐私预期，以及再分发责任。

MiaoGram 想做一只友好的猫，不想做一个神秘盒子。

## 1. 非官方 Telegram 客户端

MiaoGram 是一个基于 Telegram Android 源码的非官方 Telegram 客户端。

它与 Telegram FZ-LLC 或官方 Telegram 开发者**没有**从属、背书、赞助或关联关系。

"Telegram" 名称及相关标识属于其对应权利人。MiaoGram 使用自己的名称、图标和品牌，以避免让用户误以为这是官方应用。

## 2. 许可证

MiaoGram 使用 **GNU General Public License v2.0** 分发。详见 [LICENSE](LICENSE)。

简单说：

- 你可以在 GPLv2 条款下学习、修改和再分发源码。
- 如果你分发修改后的二进制文件，必须提供对应源码。
- 必须保留许可证声明。
- 本项目不提供任何担保。如果猫跳上键盘，请记得备份。

以上只是友好摘要，实际以许可证正文为准。

## 3. Telegram API 凭据

MiaoGram **不包含**维护者的 Telegram API 凭据。

任何想构建可用客户端的人，都必须在这里注册自己的 Telegram 应用：

<https://my.telegram.org/apps>

并通过以下方式之一提供本地凭据：

```properties
.private/api-keys.properties
APP_ID=your_numeric_api_id
APP_HASH=your_32_character_api_hash
```

或环境变量：

```bash
MIAO_APP_ID=your_numeric_api_id
MIAO_APP_HASH=your_32_character_api_hash
```

不要复用其他项目、截图、博客、泄露构建或上游示例里的 API 凭据。共享凭据可能被限流、封禁，或给无关用户带来麻烦。

## 4. 隐私预期

除非明确说明，MiaoGram 的自定义功能都应尽量在本地运行。

需要注意的联网行为：

- Telegram 基础功能必须连接 Telegram 服务器，这是任何 Telegram 客户端都无法避免的。
- 内置翻译增强在用户选择 Google 源时，会使用 Google 的免费翻译端点。用户可以在 MiaoGram 设置中切回 Telegram 官方翻译源。

MiaoGram 不应故意包含隐藏遥测、凭据收集或静默账号收集。如果你看到可疑行为，请报告。

## 5. 再分发清单

如果你发布自己的构建或 fork，请把事情做干净：

- 如果你的改动可能让用户混淆，请使用自己的应用名和图标。
- 使用你自己的 Telegram API ID/HASH。
- 分发二进制文件时，发布对应源码。
- 不要移除 GPL 声明。
- 不要声称自己是官方 Telegram。
- 告诉用户你改了什么。
- 不要隐藏网络行为。

一个好的 fork 应该让用户安心，而不是让他们像可疑小猫一样嗅 APK。

## 6. 无担保

MiaoGram 按「原样」提供，不附带任何形式的担保。使用风险由你自行承担。

如果稳定性很重要，请同时保留官方 Telegram。MiaoGram 0.1 是早期公开版本，还不是久经沙场的稳定构建。
