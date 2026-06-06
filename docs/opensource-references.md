# Open-source references and boundaries

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

Published Telegram clients should use their own API credentials. MiaoGram does not ship maintainer credentials and intentionally fails the build if `.private/api-keys.properties` is missing or invalid.

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

## Public communication style

MiaoGram's public docs should be:

- accurate,
- calm,
- useful to builders and testers,
- a little playful where appropriate,
- free of private strategy or drama.

We can be a cat-themed fork without turning the README into a scratching post.
