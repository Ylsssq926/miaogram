# MiaoGram 0.1 测试清单 / Testing checklist

**中文** | [English](#english)

这份清单给早期测试者和后续维护者使用。0.1 还没有完整人工真机 QA，如果你要帮忙测试，请优先覆盖下面这些路径。

## 测试前准备

- 使用自己的 Telegram API ID/HASH 构建。
- 建议先使用测试账号或不重要账号。
- 保留官方 Telegram 作为备用客户端。
- 记录设备型号、Android 版本、构建变体和复现步骤。
- 如果遇到崩溃，请尽量附上 logcat。

## 基础启动与登录

- [ ] 应用能冷启动，不闪退。
- [ ] 能进入登录流程。
- [ ] 使用自备 API_ID/HASH 后能正常连接 Telegram。
- [ ] 登录后基础聊天列表加载正常。
- [ ] 退出再打开后账号状态保持正常。

## MiaoGram 设置入口

- [ ] 能进入 MiaoGram 设置页。
- [ ] 设置页各分区能正常滚动。
- [ ] 开关状态能保存，重启后仍保持。
- [ ] 关闭某个 MiaoGram 功能后，对应行为能回退。

## 多账号与聚合收件箱

- [ ] 能添加多个账号。
- [ ] 账号备注能设置、显示和修改。
- [ ] 聚合收件箱能显示各账号未读对话。
- [ ] @ 提及页能显示提及内容。
- [ ] 点击聚合项能切换到正确账号并打开正确对话。
- [ ] 某个账号离线/未激活时不会导致聚合页崩溃。

## 频道阅读器

- [ ] 频道阅读器能打开。
- [ ] 已加入频道能被列出。
- [ ] 长按频道能设置阅读优先级。
- [ ] 必读/补读/归档/其他分组显示正确。
- [ ] 重启后分级仍然保留。

## 翻译

- [ ] 单条消息长按后能看到翻译入口。
- [ ] 默认 Google 免费源能返回译文。
- [ ] 翻译源能切换为 Telegram 官方源。
- [ ] 整段对话翻译开关对非 Premium 账号不弹付费页。
- [ ] 关闭 MiaoGram 内置翻译后，行为回退到上游逻辑。
- [ ] 网络失败时不会崩溃，能看到失败提示或保持可恢复状态。

## 日常体验开关

- [ ] 隐藏 Stories 生效，并可恢复。
- [ ] 隐藏「所有聊天」tab 生效，并可恢复。
- [ ] 外链追踪参数清理不会破坏普通链接打开。
- [ ] Streamer Mode / 截图保护开关生效。
- [ ] Profile 能显示用户 ID / DC，点击复制不崩溃。
- [ ] 消息时间显示到秒的开关生效。
- [ ] 关键词通知静默不会删除消息，只影响通知。
- [ ] 安全默认值不会阻断正常删除流程。

## 隐私/社区向开关

这些功能默认关闭，请按需谨慎测试：

- [ ] 广告屏蔽开关不会影响普通消息加载。
- [ ] Ghost Mode 不应导致聊天列表或发送消息异常。
- [ ] 受限内容相关开关关闭时，应保持上游行为。

## 发布前最低门槛

如果未来要从源码预览变成可推荐 APK，至少需要：

- [ ] 一台 Android 12+ 真机完整跑通登录和基础聊天。
- [ ] 一台 Android 14/15 真机跑通权限、截图保护和通知路径。
- [ ] 至少两个账号验证聚合收件箱。
- [ ] 至少一个频道场景验证频道阅读器。
- [ ] 至少一次 Google 源和 Telegram 官方源翻译验证。
- [ ] 无启动即崩、登录即崩、打开设置即崩。

---

# English

This checklist is for early testers and future maintainers. MiaoGram 0.1 has not gone through full manual device QA yet. If you want to help test it, start with these paths.

## Before testing

- Build with your own Telegram API ID/HASH.
- Prefer test or non-critical accounts first.
- Keep official Telegram installed as a backup client.
- Record device model, Android version, build variant, and reproduction steps.
- If something crashes, attach logcat if possible.

## Basic launch and login

- [ ] App cold-starts without crashing.
- [ ] Login flow opens.
- [ ] Telegram connection works with your own API credentials.
- [ ] Chat list loads after login.
- [ ] Account state survives app restart.

## MiaoGram settings

- [ ] MiaoGram settings page opens.
- [ ] Sections scroll normally.
- [ ] Toggles persist after restart.
- [ ] Turning a feature off restores the expected upstream behavior.

## Multi-account and unified inbox

- [ ] Multiple accounts can be added.
- [ ] Account remarks can be set, shown, and edited.
- [ ] Unified inbox shows unread chats from multiple accounts.
- [ ] Mentions tab shows mention entries.
- [ ] Tapping an entry switches to the correct account and opens the correct dialog.
- [ ] Inactive accounts do not crash the unified view.

## Channel reader

- [ ] Channel reader opens.
- [ ] Joined channels are listed.
- [ ] Long-pressing a channel changes its reading priority.
- [ ] Must-read / catch-up / archived / other groups display correctly.
- [ ] Grades persist after restart.

## Translation

- [ ] Single-message translate action appears in the long-press menu.
- [ ] Default Google free source returns translations.
- [ ] Source can be switched to Telegram official.
- [ ] Whole-chat translation does not show the Premium paywall on non-Premium accounts when MiaoGram translation is enabled.
- [ ] Turning off MiaoGram translation restores upstream behavior.
- [ ] Network failures do not crash the app.

## Everyday toggles

- [ ] Hide Stories works and can be reverted.
- [ ] Hide All Chats tab works and can be reverted.
- [ ] Link tracking cleanup does not break ordinary links.
- [ ] Streamer Mode / screenshot protection works.
- [ ] Profile ID/DC display works and copy action does not crash.
- [ ] Seconds in message timestamps toggle works.
- [ ] Keyword notification muting affects notifications only, not message storage.
- [ ] Safer defaults do not block normal deletion flows.

## Minimum bar before recommending APKs

Before moving from source preview to recommended APK builds, test at least:

- [ ] One Android 12+ device for login and basic chat.
- [ ] One Android 14/15 device for permissions, screenshot protection, and notifications.
- [ ] Two accounts for unified inbox.
- [ ] One channel-heavy case for channel reader.
- [ ] One Google-source and one Telegram-official translation path.
- [ ] No startup/login/settings crash.
