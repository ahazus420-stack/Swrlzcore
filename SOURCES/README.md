# SWRLZ Sources

Upload the active SWRLZ source sets here. Keep each lane mobile-readable and uncluttered.

## Lane structure

```text
SOURCES/
├── CLIENT/
│   ├── OLD_PATCHES/
│   └── README.md
├── SERVER/
│   ├── OLD_PATCHES/
│   └── README.md
├── SWRLZ-BOARD/
│   ├── OLD_PATCHES/
│   └── README.md
├── SCORE-LAUNCHER/
│   ├── OLD_PATCHES/
│   └── README.md
└── README.md
```

`SWRLZ-BOARD` is the independent source lane for the SWRLZ Keyboard application.
`SCORE-LAUNCHER` is the independent source lane for the future sCore Launcher application.
Neither lane contains CLIENT or SERVER source material.

## Current naming standard

```text
CLIENT_CFvX.Y.Z_SWRLZ.zip
CLIENT_CFvX.Y.Z_SWRLZ.sha256

SERVER_CFvX.Y.Z_SWRLZ.zip
SERVER_CFvX.Y.Z_SWRLZ.sha256

KEYBOARD_CFvX.Y.Z_SWRLZ.zip
KEYBOARD_CFvX.Y.Z_SWRLZ.sha256

SCORE_LAUNCHER_CFvX.Y.Z_SWRLZ.zip
SCORE_LAUNCHER_CFvX.Y.Z_SWRLZ.sha256
```

## Folder hygiene rule

Each lane should normally contain only its current active source ZIP, matching checksum, current notes, and its own `OLD_PATCHES/` archive.

Older source ZIPs, checksum files, reports, upload notes, and legacy files must be retained in the lane's `OLD_PATCHES/` directory as lineage receipts. Do not delete old source outputs by default.

## Build request rule

`BUILD_REQUESTS/000_CURRENT.request` records the four independent application lanes and their build flags. Dedicated workflows resolve the newest matching versioned source ZIP from their own lane and verify the matching SHA-256 before extraction.

The request file does not trigger a build by itself.

## Promotion rule

A Keyboard or sCore Launcher source ZIP must be created only from an accepted source checkpoint. Place the ZIP and matching `.sha256` file in the appropriate lane after source acceptance. A workflow build must never rewrite that canonical source ZIP.

Core law: integrate, do not overwrite.