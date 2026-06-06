# MiaoGram 0.1 design notes

MiaoGram's 0.1 goal is simple:

> Keep Telegram familiar, then add a few sharp claws for people who live in Telegram all day.

This is not a promise list for every future feature. It is a public snapshot of the current product direction.

## Product feel

MiaoGram should feel like Telegram, not like a different app wearing Telegram's coat.

- Keep upstream navigation and visual language where possible.
- Put MiaoGram features in a dedicated settings area.
- Prefer small, reversible toggles over surprising global behavior.
- Make power features discoverable but not noisy.
- Keep sensitive or community-style features off by default.

## 0.1 feature pillars

### 1. Multi-account comfort

MiaoGram is designed for users with more than a couple of accounts.

Current direction:

- More local account slots.
- Account remarks for easier switching.
- Unified cross-account inbox for unread chats and mentions.

Why it matters: official Telegram is good for a few accounts; MiaoGram tries to be better when your account list starts looking like a tiny office building.

### 2. Reading and channel management

Heavy Telegram users often follow many channels. MiaoGram adds local organization tools:

- Channel reader.
- Manual reading priority: must-read, catch-up, archived, other.
- Local-only grouping without a backend service.

The goal is not to replace folders; it is to give high-volume readers a calmer place to catch up.

### 3. Translation as a baseline expectation

Many third-party Telegram clients include message translation. MiaoGram 0.1 treats it as a baseline feature:

- Single-message translation.
- Whole-chat translation unlock.
- Google free source by default.
- Telegram official source as an option.

Translation involves network requests to the selected translation provider. The source is visible in settings.

### 4. Small quality-of-life improvements

MiaoGram includes a group of small toggles that make daily use smoother:

- Hide visual clutter like Stories or the All Chats tab.
- Strip common tracking parameters from outbound links.
- Show technical profile details such as user ID and photo DC.
- Optional seconds in message timestamps.
- Safer defaults for destructive confirmation dialogs.
- Keyword-based notification muting.

These are not headline features, but they are the little paw prints users notice after a day or two.

## What 0.1 is not

0.1 is not a polished stable release.

It is also not:

- an official Telegram client,
- a service with bundled API credentials,
- a backend-powered analytics product,
- a promise that every planned feature already exists,
- a place to hide secret network behavior.

## Future direction

Future work should prioritize:

1. real-device testing and crash fixes,
2. polishing the unified inbox and channel reader,
3. translation reliability and fallback behavior,
4. clearer settings and onboarding,
5. stable release packaging.

Nice-to-have ideas can wait. The cat should land on its feet first.
