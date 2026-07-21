# KEYBOARD-IMP-011A — Minimal Standalone SWRLZ Keyboard IME Implementation

- **Status:** Implemented as source; Android build not executed
- **Date:** 2026-07-21
- **Branch:** `checkpoint/keyboard-imp-011a`
- **Identity:** `com.swrlz.keyboard.app`
- **Version:** `0.1.0` / `versionCode=1`

## Architecture

```text
KeyboardSetupActivity
  -> Android input settings / input-method picker

KeyboardImeService
  -> EditorInfo adapter
  -> EditorContextClassifier
  -> local InputConnection key commits
```

The source is one Android application module. It is intentionally small so the first verification build tests the real IME role without prematurely introducing enrollment, networking, shared authority, or complex UI dependencies.

## Keyboard-owned surfaces

- permanent Keyboard application identity;
- launcher-visible setup activity;
- `InputMethodService`;
- `BIND_INPUT_METHOD`-protected service declaration;
- IME metadata and `en_US` ASCII subtype;
- keyboard icon and strings;
- local key layout and editor commits;
- editor privacy classifier.

## Basic typing

The input view provides lowercase `a-z`, space, backspace, and enter.

Backspace deletes selected text first, then one surrounding character, with a delete-key fallback. Enter performs a supported editor action when accepted by the target editor, otherwise inserts a newline.

## Protected-editor seam

The classifier is pure Kotlin and receives a neutral descriptor. It recognizes:

- ordinary and multiline text;
- search or URL;
- email or recipient;
- numeric and phone;
- password or secret;
- payment or financial secret;
- one-time code;
- unknown sensitive.

Password, payment, one-time-code, and unknown-sensitive contexts set `protected=true`, disable future SWRLZ actions, and keep content telemetry disabled. Unknown input classes fail closed.

## Scope exclusions

This checkpoint does not implement CLIENT enrollment, NODE_HOST routing, discovery, remote processing, AI actions, missions, clipboard history, voice, content telemetry, dictionaries, autocorrect, suggestions, symbols, shift/caps, or durable settings.

## Package

```text
SWRLZ_KEYBOARD_IME_CFv0.1.0_SOURCE.zip
SHA-256: 7281f083e4776004bd59f4973cc33a25e788b36c8175f42a15a4fc90ccd50442
```

The archive contains the standalone source project and source-verification tools. It is deterministic, path-safe, and contains no duplicate entries.
