# KEYBOARD-IMP-011A Lineage

## Implementation branch

- Repository: `ahazus420-stack/Swrlzcore`
- Branch: `checkpoint/keyboard-imp-011a`
- Parent planning branch: `checkpoint/app-shell-gate-010`
- Planning commit: `2fdc76cd8f8e4a6619a5a61eb5c2dcca2a99a0d8`
- Main baseline inherited by the planning branch: `961e92907acb6a3158f6da982902f07acbfba019`

## Preserved predecessor seed

```text
SOURCES/KEYBOARD/SWRLZ_KEYBOARD_BASE_CFv1.0.1.zip
SHA-256: 8fcf9a29a4dc0b75e166da2b5522b8fab90274353d610fd52b80dcc7c1bc5d40
Git blob: a8cec2ab4889d53055b73aa18cf60423ec315f6a
```

The predecessor is a byte-identical inheritance seed from the historical Core Android Foundation package. It is preserved unchanged and is not selected as the new Keyboard implementation source.

## Successor source

```text
SOURCES/KEYBOARD/source/
SOURCES/KEYBOARD/SWRLZ_KEYBOARD_IME_CFv0.1.0_SOURCE.zip
SOURCES/KEYBOARD/SWRLZ_KEYBOARD_IME_CFv0.1.0_SOURCE.sha256
SHA-256: 7281f083e4776004bd59f4973cc33a25e788b36c8175f42a15a4fc90ccd50442
```

The successor is a new role-specific Keyboard source project. It derives compatible toolchain settings from CORE_BASE but does not copy Core's app shell, application ID, activity, label, manifest role, or retired demonstration modules.

## Retirement boundary

The predecessor seed remains at the Keyboard lane root. Moving it to `OLD_PATCHES/` requires a separate checkpoint after a verification build proves the successor can compile and package as `com.swrlz.keyboard.app` with recoverable evidence.
