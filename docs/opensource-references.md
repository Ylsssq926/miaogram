# 开源参考与边界 / Open-source references and boundaries

**中文** | [English](#english)

MiaoGram 站在 Telegram Android 和第三方 Telegram 客户端生态的肩膀上。

这份文档是面向公开仓库的友好说明：我们参考了哪些普遍做法，以及这个仓库坚持哪些边界。

## 第三方 Telegram 客户端的常见做法

公开的 Telegram Android fork 通常会遵循类似模式：

- 保持开源，并使用 GPL 兼容许可证。
- 明确说明自己是非官方客户端。
- 要求构建者提供自己的 Telegram API ID/HASH。
- 在 README 前面列出可见的用户侧改进。
- 使用不同于官方 Telegram 的名称和品牌。
- 对可选增强功能优先使用本地设置开关。

MiaoGram 也遵循这套基本做法。

## API 凭据

公开发布的 Telegram 客户端应使用自己的 API 凭据。MiaoGram 不内置维护者凭据；如果没有有效的 `.private/api-keys.properties` 或 `MIAO_APP_ID` / `MIAO_APP_HASH` 环境变量，构建会主动失败。

具体设置方式见 README。

## 许可证兼容性

MiaoGram 使用 GPLv2。参考其他开源项目时，代码许可证兼容性很重要：

- GPLv2 兼容代码可以在遵守署名和许可证条款的前提下复用。
- GPLv3-only 代码不应直接复制进 GPLv2 项目，除非整个项目的授权策略发生变化。
- 可以参考想法和 UI 模式，但源码必须按对应许可证处理。

不确定时，优先自己重写功能，并在文档中说明灵感来源，而不是直接复制代码。

## 隐私和网络行为

MiaoGram 应明确说明哪些功能会访问非 Telegram 服务。

例如，MiaoGram 的翻译增强在用户选择 Google 源时，会使用 Google 的免费翻译端点；设置中也提供切回 Telegram 官方翻译源的选项。

隐藏遥测、凭据收集、静默账号采集不应出现在这个仓库里。

## 公开沟通风格

MiaoGram 的公开文档应当：

- 准确；
- 冷静；
- 对构建者和测试者有用；
- 可以有一点轻松的猫味；
- 不包含私有策略或戏剧化竞争表达。

我们可以做一个有猫主题的 fork，但 README 不应该变成猫抓板。

---

# English

MiaoGram stands on the shoulders of Telegram Android and the broader third-party Telegram client ecosystem.

This file is a public, friendly summary of what we learned from other clients and what boundaries we keep for this repository.

## What other third-party clients commonly do

Public Telegram Android forks usually follow a similar pattern:

- Keep the project open-source under a GPL-compatible license.
- Make it clear that the app is unofficial.
- Require builders to provide their own Telegram API ID/HASH.
- List visible user-facing improvements near the top of the README.
- Keep branding distinct from official Telegram.
- Prefer local settings for optional enhancements.

MiaoGram follows the same broad pattern.

## API credentials

Published Telegram clients should use their own API credentials. MiaoGram does not ship maintainer credentials and intentionally fails the build if neither `.private/api-keys.properties` nor `MIAO_APP_ID` / `MIAO_APP_HASH` provides valid credentials.

See the README for setup instructions.

## License compatibility

MiaoGram is GPLv2. When borrowing ideas from other projects, code license compatibility matters:

- GPLv2-compatible code can be reused if attribution and license terms are followed.
- GPLv3-only code should not be copied into this GPLv2 project unless the whole licensing strategy changes.
- Ideas and UI patterns can inspire implementation, but source code must be handled according to its license.

When in doubt, rewrite the feature from scratch and document the inspiration rather than copying code.

## Privacy and network behavior

MiaoGram should be clear about features that talk to non-Telegram services.

For example, MiaoGram's translation enhancement can use Google's free translation endpoint. This is exposed as a setting, and users can switch back to Telegram's official translation source.

Hidden telemetry, credential collection, and silent data harvesting are not acceptable in this repository.
