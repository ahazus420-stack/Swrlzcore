# APP-SHELL-GATE-010 — CORE_BASE, Keyboard, and Launcher Source Topology Audit

- **Status:** Complete audit; implementation not authorized
- **Date:** 2026-07-21
- **Repository:** `ahazus420-stack/Swrlzcore`
- **Branch:** `checkpoint/app-shell-gate-010`
- **Base commit:** `961e92907acb6a3158f6da982902f07acbfba019`
- **Governing decisions:** ADR-0001, ADR-0003, SWRLZ Constitution
- **Scope:** Repository inspection and documentation only

## 1. Objective

Determine the actual source/package relationship among CORE_BASE, Keyboard, and Launcher before any archive removal, source copying, Gradle restructuring, app implementation, or APK build.

The audit answers:

1. which source is currently active for CORE_BASE;
2. what the Keyboard and Launcher ZIPs actually represent;
3. whether nested archives exist;
4. which code is shared, Core-shell-owned, Keyboard-owned, or Launcher-owned;
5. the safest bounded path to a real SWRLZ Keyboard application.

## 2. Facts

### 2.1 Current authoritative repository state

At audit start, `main` is:

```text
961e92907acb6a3158f6da982902f07acbfba019
```

No source, archive, workflow, build request, application code, Gradle file, or manifest was changed by this audit.

### 2.2 CORE_BASE has two materially different package generations

#### Active reduced CORE_BASE source

The current build request and Core workflow identify the active source as:

```text
SOURCES/CORE_BASE/source/
SOURCES/CORE_BASE/SWRLZ_CORE_ANDROID_CORE_REDUCE_003_SOURCE.zip
SOURCES/CORE_BASE/SWRLZ_CORE_ANDROID_CORE_REDUCE_003_SOURCE.sha256
```

The sibling checksum declares:

```text
bd4909fce635d60d4a984a521503ed1b53ecb451f41281885112d92f9ce528e1
```

The active unpacked Gradle project contains only:

```text
:app
:core
```

The settings file no longer includes `:designsystem` or `:featurehome`.

The Core build workflow explicitly requires:

```text
featurehome absent
designsystem absent
HomeScreen absent
FeatureRepository absent
CoreFeature absent
```

The active `:core` module currently exposes only the minimal `CoreKernel` boundary. It is a foundation seam, not a mature shared-platform implementation.

#### Historical four-module foundation package

A separate older package remains under:

```text
SOURCES/CORE_BASE/packages/SWRLZ_CORE_ANDROID_CFv1.0.1.zip
SOURCES/CORE_BASE/packages/SWRLZ_CORE_ANDROID_CFv1.0.1.sha256
```

Its SHA-256 is:

```text
8fcf9a29a4dc0b75e166da2b5522b8fab90274353d610fd52b80dcc7c1bc5d40
```

The historical source manifest records the earlier modules and demonstration code:

```text
:app
:core
:designsystem
:featurehome
```

including `FeatureRepository.kt`, `DomainModels.kt`, `SwrlzTheme.kt`, and `HomeScreen.kt`.

This historical package is not the active reduced CORE_BASE build source.

### 2.3 Keyboard and Launcher ZIPs are exact inheritance seeds

Keyboard:

```text
SOURCES/KEYBOARD/SWRLZ_KEYBOARD_BASE_CFv1.0.1.zip
SOURCES/KEYBOARD/SWRLZ_KEYBOARD_BASE_CFv1.0.1.sha256
```

Launcher:

```text
SOURCES/LAUNCHER/SWRLZ_LAUNCHER_BASE_CFv1.0.1.zip
SOURCES/LAUNCHER/SWRLZ_LAUNCHER_BASE_CFv1.0.1.sha256
```

Both checksum files declare:

```text
8fcf9a29a4dc0b75e166da2b5522b8fab90274353d610fd52b80dcc7c1bc5d40
```

All three historical ZIP paths—Core package, Keyboard seed, and Launcher seed—resolve to the same Git blob:

```text
a8cec2ab4889d53055b73aa18cf60423ec315f6a
```

Therefore byte identity is proven at both the declared SHA-256 layer and repository blob layer.

The Keyboard and Launcher READMEs accurately classify the archives as byte-identical inheritance seeds. They are not distinct product source, implementations, builds, or accepted releases.

### 2.4 No nested source archive model is present

The Keyboard and Launcher lane-root ZIPs are the source archives themselves. They are not ZIPs nested inside a checked-in Keyboard or Launcher source project.

Evidence:

- neither lane currently contains a `source/` Gradle project;
- each workflow performs one `unzip` operation directly into a temporary build workspace;
- each workflow immediately executes `:app:assembleDebug` from that extracted root;
- the historical source manifest lists Gradle/project files, not a second inner source ZIP.

The phrase “remove the ZIPs inside Keyboard and Launcher” therefore maps to replacing the current inheritance-seed source model. It does not map to deleting an inner archive from an existing application source tree.

### 2.5 Current Core Android shell identity

The active Core `:app` declares:

```text
namespace:     com.swrlz.core.app
applicationId: com.swrlz.core.app
versionCode:   1
versionName:   1.0.0
label:         SWRLZ Core
```

Its manifest declares a standard exported launcher activity using:

```text
android.intent.action.MAIN
android.intent.category.LAUNCHER
```

This is a Core app shell identity. It is not a Keyboard IME identity and not an Android HOME/Launcher role identity.

### 2.6 Current Keyboard and Launcher workflows build Core-shaped payloads

The Keyboard workflow:

1. verifies the historical seed checksum;
2. extracts the byte-identical Core seed;
3. regenerates the wrapper;
4. runs `:app:assembleDebug`.

The Launcher workflow performs the same sequence against the same Git blob.

Neither workflow patches:

- `applicationId`;
- namespace;
- app label;
- manifest role;
- service declarations;
- capabilities;
- version lineage.

Consequently those workflows prove independent build lanes only. They do not prove distinct Keyboard or Launcher applications.

Building the current seeds again would reproduce Core-shaped APK payloads and would not satisfy ADR-0001’s distinct-app-shell requirements.

### 2.7 Keyboard has an accepted role contract but no production source

The accepted Keyboard contract establishes a dedicated Android Input Method Editor surface with these hard boundaries:

- ordinary typing remains local and available offline;
- the Keyboard is a surface, not a physical device or NODE_HOST;
- sensitive editor contexts disable AI/content processing;
- remote processing requires deliberate action and visible route disclosure;
- discovery cannot grant authority;
- Truth Firewall objection, refusal, pause, and safer-alternative behavior remains active;
- CLIENT enrollment grants only scoped Keyboard-surface authority.

The reserved implementation lane under `core-swrlz-board/keyboard/` contains documentation only and explicitly contains no build files or production source.

### 2.8 Launcher has identity intent but no accepted role contract

ADR-0001 requires Launcher to have a unique permanent Android identity and independent update lineage. The device-surface identity contract recognizes `surfaceType=launcher`.

However, no dedicated accepted Launcher behavior, trust, role, lifecycle, HOME-role, app-indexing, widget, search, or CLIENT-integration contract was found.

Launcher implementation should not begin by copying the Core app because its role boundary is less defined than Keyboard’s.

## 3. Requirements

The next source checkpoints must preserve:

1. the active reduced CORE_BASE source and package lineage;
2. the historical four-module seed and all three checksum identities;
3. unique permanent Android identities for Core, Keyboard, and Launcher;
4. separate signing and version progressions;
5. ordinary Keyboard typing without CLIENT, NODE_HOST, LAN, or internet;
6. surface identity beneath one physical-device identity without device-count inflation;
7. explicit local, LAN, and remote routes;
8. Truth Firewall objection, refusal, pause, and safer-alternative behavior;
9. protocol/schema version discipline;
10. rollback from every source transition.

## 4. Classification

### 4.1 Shared-platform candidates

Current immediately reusable foundation:

```text
Core toolchain profile
- AGP 8.2.2
- Kotlin Android 1.9.24
- Java/JVM 17
- compileSdk 34
- targetSdk 34
- minSdk 24

CoreKernel contract seam
```

Potential later shared components, only after bounded extraction or accepted implementation:

```text
identity and surface contracts
lineage models
Truth Firewall interfaces
protocol/version models
route disclosure models
audit/redaction envelopes
design-system tokens
capsules such as swrlz.discovery.contract where role-compatible
```

The removed historical `designsystem` and `featurehome` modules are lineage evidence, not automatically active shared code.

### 4.2 Core-shell-owned

```text
com.swrlz.core.app identity
Core MainActivity
SwrlzApplication
Core label, icon, theme, launcher presentation
Core-specific navigation and UI
Core version and signer lineage
```

These must not be copied into another shell as authoritative identity.

### 4.3 Keyboard-shell-owned

```text
unique Keyboard application identity
InputMethodService and BIND_INPUT_METHOD declaration
IME metadata and subtype configuration
InputConnection/editor-context adapter
ordinary key dispatch
protected-field classification
Keyboard UI, candidate strip, layouts, themes, accessibility
settings/onboarding surface
enrollment and scoped credential storage
Keyboard-specific version and signer lineage
```

### 4.4 Launcher-shell-owned

```text
unique Launcher application identity
HOME/DEFAULT role and activity declarations
home workspace, app grid, search, widgets, wallpaper, navigation
launcher-specific permissions and package visibility
Launcher enrollment/scoped surface state if later accepted
Launcher-specific version and signer lineage
```

Launcher-specific trust and capability boundaries require a dedicated accepted contract before implementation.

## 5. Risks

### 5.1 Deleting the seed ZIPs now

Would remove explicit provenance before a successor Keyboard or Launcher source package exists.

### 5.2 Copying the full current Core app into each lane

Would duplicate Core shell identity and presentation, create manual synchronization debt, and risk application-ID collisions.

### 5.3 Using the historical seed as the new baseline

Would revive modules and demonstration code intentionally removed by CORE_REDUCE_003.

### 5.4 Building before accepting unique identity

Would create disposable APKs with no durable update path and could repeat the existing same-package collision.

### 5.5 Treating package inclusion as authority

Would violate identity, trust, capability, and Truth Firewall boundaries.

## 6. Audit conclusion

The user’s desired destination is correct: real, separately installable SWRLZ Keyboard and Launcher apps should be created.

The safe implementation method is not “delete ZIP, then copy Core wholesale.” It is:

```text
preserve seed ZIP lineage
→ derive a new role-specific shell from the current Core toolchain
→ assign a unique permanent app identity
→ implement only role-owned behavior
→ reference or attach shared modules/capsules explicitly
→ package and verify a successor ZIP/SHA
→ archive the old seed in OLD_PATCHES through a later retirement checkpoint
```

Keyboard is the correct first shell because it already has an accepted role and trust contract. Launcher must receive a dedicated behavior/role contract before implementation.

## 7. Explicitly not performed

- no archive removed, moved, renamed, or replaced;
- no source copied;
- no source directory created;
- no Gradle or manifest change;
- no workflow or build-request change;
- no APK build;
- no workflow trigger;
- no merge, release, deployment, installation, or branch deletion.
