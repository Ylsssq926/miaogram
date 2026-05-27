# AI Collaboration Guidelines for MiaoGram

## 🤖 For AI Assistants Working on This Project

This is a **public open-source repository**. Follow these rules strictly:

### DO NOT include in any committed file:
- API keys, secrets, tokens, or credentials
- Internal business strategy or monetization details
- References to data collection for commercial purposes
- Aggressive competitive language ("kill", "destroy", "monopolize")
- Details about gray-area feature motivations
- Phone number collection strategies
- Feature flag server-side decision logic

### DO use neutral, user-facing language:
- "Privacy protection features" (not "ghost mode to avoid detection")
- "Notification preferences" (not "phone number collection trigger")
- "Usage analytics for product improvement" (not "behavior tracking for monetization")
- "Enhanced download experience" (not "bypass speed limits")

### Code comments should be:
- Technical and factual
- Focused on HOW, not WHY (from business perspective)
- Free of any internal strategy references

### Feature flag keys should be:
- Non-descriptive: `miao_pf_7`, `miao_exp_3`
- NOT self-explanatory: ~~`ghost_mode_enabled`~~, ~~`collect_phone`~~

### When in doubt:
- Ask the project owner before committing
- Default to NOT including sensitive information
- Place uncertain content in `.private/` (gitignored)

## Project Structure

```
miaogram/
├── .private/          ← GITIGNORED - sensitive docs, keys, strategy
├── docs/              ← Public documentation
├── TMessagesProj/     ← Main Android project (will be added)
├── .gitignore
├── LICENSE            ← GPLv2
└── README.md
```
