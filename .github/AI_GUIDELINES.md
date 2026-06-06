# MiaoGram AI 协作指南 / AI Collaboration Guidelines

**中文** | [English](#english)

本仓库是**公开开源仓库**。任何 AI 助手或 AI 辅助贡献都必须严格遵守以下规则。

## 不要提交这些内容

- API keys、secrets、tokens、凭据。
- 内部商业策略或变现细节。
- 商业数据收集相关表述。
- 过激竞争语言。
- 灰色功能的内部动机说明。
- 手机号收集策略。
- feature flag 服务端决策逻辑。

## 使用中性、面向用户的表达

推荐：

- 「隐私保护功能」
- 「通知偏好」
- 「用于产品改进的使用分析」
- 「增强下载体验」

避免：

- 把隐私功能写成规避检测。
- 把通知功能写成采集触发器。
- 把体验优化写成绕过限制。
- 把竞品或上游描述成攻击目标。

## 代码注释要求

代码注释应：

- 技术、事实、克制；
- 解释如何工作，而不是解释商业动机；
- 不包含私有策略；
- 不暴露敏感映射或真实付费/灰度规则。

## Feature flag 命名

公开代码里的 flag key 应保持非描述性，例如：

- `miao_pf_7`
- `miao_exp_3`

不要使用一眼暴露功能含义的 key，例如：

- `ghost_mode_enabled`
- `collect_phone`

## 不确定时

- 先询问项目所有者。
- 默认不要提交不确定内容。
- 可能敏感的内容放到 `.private/`（已被 git 忽略）。

## 项目结构

```text
miaogram/
├── .private/          ← GITIGNORED，本地敏感文档、密钥、策略
├── docs/              ← 公开文档
├── TMessagesProj/     ← Telegram Android library module + MiaoGram hooks
├── TMessagesProj_App/ ← Main Android app module
├── .gitignore
├── LICENSE            ← GPLv2
└── README.md
```

---

# English

This is a **public open-source repository**. Any AI assistant or AI-assisted contribution must follow these rules strictly.

## Do not commit

- API keys, secrets, tokens, or credentials.
- Internal business strategy or monetization details.
- References to commercial data collection.
- Aggressive competitive language.
- Internal motivations behind gray-area features.
- Phone number collection strategies.
- Feature flag server-side decision logic.

## Use neutral, user-facing language

Prefer:

- "Privacy protection features"
- "Notification preferences"
- "Usage analytics for product improvement"
- "Enhanced download experience"

Avoid framing privacy features as evasion, notification features as collection triggers, or product work as attacks on competitors/upstream.

## Code comments

Code comments should be:

- technical and factual,
- focused on how the code works, not business motivations,
- free of private strategy,
- free of sensitive mappings or real paid/rollout rules.

## Feature flag keys

Public feature flag keys should be non-descriptive, for example:

- `miao_pf_7`
- `miao_exp_3`

Avoid self-explanatory keys such as:

- `ghost_mode_enabled`
- `collect_phone`

## When in doubt

- Ask the project owner.
- Default to not committing uncertain content.
- Put potentially sensitive material under `.private/` (gitignored).
