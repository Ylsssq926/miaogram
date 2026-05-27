# MiaoTelegram - 开源参考资源库

> 目的：从现有开源 fork 中"抄最好的作业"，聚合到 MiaoTelegram
> 原则：GPL 兼容的代码可以直接复用（注明来源即可）

---

## 1. 最值得参考的开源 fork（按价值排序）

### 1.1 exteraGram（已归档但代码仍可用）
- **仓库**：https://github.com/exteraSquad/exteraGram
- **值得抄的**：
  - Material 3 UI 适配（整套主题系统）
  - 下载速度加速（experimental speed boost）
  - 设置页面架构（BasePreferencesEntry 扩展框架）
  - CameraX 集成
- **License**：GPLv2 ✅ 可直接复用
- **注意**：已归档（2026-01-28），代码基于较旧的上游版本，需要手动 merge 到新上游

### 1.2 AyuGram4A
- **仓库**：https://github.com/AyuGram/AyuGram4A
- **值得抄的**：
  - Ghost Mode 完整实现（per-chat 粒度）
  - Anti-recall（Room 数据库保存已删消息）
  - AyuForward（绕过 saveRestrictions）
  - Local Premium 解锁逻辑
  - 消息过滤器
- **License**：GPLv2 ✅ 可直接复用
- **注意**：GitHub 仓库代码较旧（2023-07），实际新版通过 TG 频道分发

### 1.3 Nekogram
- **仓库**：https://github.com/Nekogram/Nekogram
- **值得抄的**：
  - 免费语音转文字（Cloudflare Workers AI 集成）
  - Google Lens 搜图集成
  - 消息翻译多引擎（Google/Yandex/DeepL）
  - 文件夹图标自定义
  - 底部导航栏隐藏
- **License**：GPLv2 ✅ 可直接复用
- **注意**：有后门历史，代码需要审查后再用（不要用 Extra.java 相关的任何东西）

### 1.4 Mercurygram
- **仓库**：https://github.com/Mercurygram/Mercurygram
- **值得抄的**：
  - 可复现构建配置（F-Droid 标准）
  - UnifiedPush 替代 FCM 的实现
  - Anti-Delete/Anti-Edit 消息历史
  - DoH 泄漏修复
  - 去 CDN 追踪
- **License**：GPLv2 ✅ 可直接复用
- **注意**：最活跃的隐私导向 fork，代码质量高

### 1.5 Nagram
- **仓库**：https://github.com/NextAlone/Nagram
- **值得抄的**：
  - 合并消息功能
  - 可编辑文本样式
  - 强制复制（绕过复制限制）
  - 噪音抑制/语音增强
  - 快速回复（长按菜单）
- **License**：GPLv3（注意：与 GPLv2 上游有兼容性问题，需要确认）

### 1.6 Forkgram
- **仓库**：https://github.com/forkgram/TelegramAndroid
- **值得抄的**：
  - "全员删除"默认勾选
  - 原始转发日期保留
  - 未读聊天置顶逻辑
  - F-Droid 上架的完整配置
  - Snap/Chocolatey/Homebrew 多渠道分发脚本
- **License**：GPLv2 ✅ 可直接复用

### 1.7 Materialgram（Desktop）
- **仓库**：https://github.com/kukuruzka165/materialgram
- **值得抄的**：
  - Material You 主题 + Colorizer 系统
  - 降低 JPEG 压缩（94% 照片质量）
  - 语音消息码率提升（32→256）
  - 隐藏非联系人手机号
- **License**：GPLv3+OpenSSL ✅

### 1.8 64Gram（Desktop）
- **仓库**：https://github.com/TDesktop-x64/tdesktop
- **值得抄的**：
  - 聊天 ID 显示
  - 多账户支持（10 个）
  - 语音聊天 Radio mode
  - 截图隐私模式
  - 恢复已移除功能列表
- **License**：GPLv3+OpenSSL ✅

---

## 2. 非 TG fork 但值得参考的开源项目

### 2.1 Telegram-Speed-Hook（Xposed 模块）
- **仓库**：搜索 GitHub "Telegram Speed Hook"
- **价值**：多倍率下载的最简实现参考（不需要改源码，通过 Hook 实现）
- **用途**：理解多连接下载的核心逻辑后移植到客户端内

### 2.2 Layerex/telegram-desktop-patches
- **仓库**：https://github.com/Layerex/telegram-desktop-patches
- **价值**：5 个独立 patch 文件，每个解决一个痛点
  - 0001-Disable-sponsored-messages.patch
  - 0002-Disable-saving-restrictions.patch
  - 0003-Disable-invite-peeking-restrictions.patch
  - 0004-Disable-accounts-limit.patch
  - 0005-Option-to-disable-stories.patch
- **用途**：直接看 patch 内容就知道改哪几行代码

### 2.3 AyuGramDesktop-PLEngine（插件引擎）
- **仓库**：https://github.com/MrCheatEugene/AyuGramDesktop-PLEngine
- **价值**：展示了如何给 TG 客户端加插件系统
- **用途**：如果未来想做"用户自定义功能模块"可以参考

### 2.4 Telegram-FOSS
- **仓库**：https://github.com/Telegram-FOSS-Team/Telegram-FOSS
- **价值**：去 Google 服务的完整方案
  - OSM 地图替代 Google Maps
  - Noto emoji 替代 Google emoji
  - UnifiedPush 替代 FCM
- **用途**：F-Droid 版必须参考

---

## 3. "拼凑"策略建议

### 3.1 推荐的"抄作业"顺序

| 阶段 | 从哪抄 | 抄什么 |
|------|--------|--------|
| Week 1-2 | Layerex patches | 去广告 + 隐藏 Stories + 多账户（看 patch 就知道改哪行） |
| Week 2-3 | AyuGram4A | Local Premium 解锁逻辑 |
| Week 3-4 | exteraGram | 下载加速代码 |
| Week 4-5 | Nekogram | 翻译多引擎集成 |
| Week 5-6 | AyuGram4A | Ghost Mode + Anti-recall |
| Week 6-8 | exteraGram | Material 3 主题系统 |
| Week 8+ | Mercurygram | 可复现构建 + F-Droid 配置 |

### 3.2 GPL 复用规则

- GPLv2 代码可以直接复制到你的 GPLv2 项目中 ✅
- 必须保留原始版权声明（文件头的 Copyright）
- 必须在你的项目中也使用 GPLv2（你已经是了）
- 建议在 README 或 CREDITS 文件中列出参考来源（不是法律要求，是社区礼仪）
- **GPLv3 代码不能直接复制到 GPLv2 项目**（不兼容）— Nagram 的代码要注意

### 3.3 怎么高效"抄"

1. **看 Layerex patches 最快**：每个 patch 就是一个 diff 文件，直接告诉你改哪个文件的哪一行
2. **看 exteraGram 的 commit history**：按功能搜 commit message（如 "speed boost"），找到对应 commit 看 diff
3. **看 AyuGram 的文档**：docs.ayugram.one 列出了每个功能的实现原理
4. **让 Claude Opus 帮你读代码**：把某个 fork 的特定文件喂给它，问"这段代码实现了什么功能，我怎么移植到我的项目"

---

## 4. 我们的计划可以优化的地方（基于开源生态观察）

### 4.1 不需要从零实现的功能（直接复用）

| 功能 | 最佳参考源 | 复用难度 |
|------|-----------|---------|
| 去 Sponsored | Layerex patch #1 | ★（看 patch 改 1 行） |
| 隐藏 Stories | Layerex patch #5 | ★ |
| 多账户 | Layerex patch #4 | ★ |
| 翻译 | Nekogram TranslateHelper.java | ★★ |
| Ghost Mode | AyuGram GhostConfig.java | ★★★ |
| Anti-recall | AyuGram/Mercurygram | ★★★ |
| 下载加速 | exteraGram | ★★★ |
| Material 3 主题 | exteraGram/Materialgram | ★★★★ |
| 可复现构建 | Mercurygram CI 配置 | ★★★ |

### 4.2 需要自己原创的功能（无现成参考）

| 功能 | 为什么没人做过 | 我们的机会 |
|------|--------------|-----------|
| 频道排行榜内置 | 需要自建后端 | 92% 空白市场 |
| 视频流页（TikTok 风格） | UI 工作量大 | Turrit 仅 iOS，Android 空白 |
| 频道健康度仪表盘 | 需要数据积累 | 无竞品 |
| 智能通知规则引擎 | 产品设计复杂 | 无竞品做到位 |
| 阅读进度追踪 | 需要本地数据库设计 | 无竞品 |

### 4.3 开发计划优化建议

基于"拼凑"策略，原计划可以加速：

**原计划 Week 1-2**：Fork 设置 + api_id + 品牌替换
**优化后**：同时把 Layerex 的 5 个 patch 应用上去 → Week 2 结束时已经有一个"去广告+隐藏Stories+多账户"的可用版本

**原计划 Week 3-4**：多倍率下载
**优化后**：直接参考 exteraGram 的 speed boost 代码移植 → 可能 1 周搞定

**原计划 Week 5-6**：翻译
**优化后**：直接复制 Nekogram 的 TranslateHelper → 半周搞定

**总结**：通过"拼凑"策略，原计划 8 周的 Phase 1 可能压缩到 4-5 周。

---

## 5. 需要注意的"坑"

1. **上游版本对齐**：不同 fork 基于不同版本的官方源码，直接复制代码可能因为 API 变化而编译失败。需要先确认你的 base 版本，再找对应版本的 fork 代码。

2. **代码冲突**：从多个 fork 抄代码时，它们可能修改了同一个文件的同一个位置。需要手动 merge。

3. **Nekogram 的 Extra.java**：绝对不要复制这个文件的任何内容（后门代码的载体）。

4. **AyuGram 的官方 API keys**：不要复制它的 api_id/api_hash 配置（APP_ID=6 是官方 keys，用了等于违规）。

5. **GPLv3 vs GPLv2**：Nagram (GPLv3) 的代码不能直接放进 GPLv2 项目。如果要用，需要把整个项目升级到 GPLv3（但这会影响与其他 GPLv2 fork 的兼容性）。
