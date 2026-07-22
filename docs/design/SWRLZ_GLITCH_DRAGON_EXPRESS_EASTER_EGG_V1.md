# SWRLZ Glitch Dragon Express Easter Egg

**Document ID:** INT-DOC-013A  
**Version:** 1.0  
**Status:** Approved design specification  
**Scope:** CLIENT-facing hidden cinematic, reward progression, and supporting SERVER authority  
**Repository role:** Canonical product, UX, security, and implementation design reference

---

## 1. Purpose

The **Glitch Dragon Express** is a hidden SWRLZ cinematic and legacy artifact. It is not a normal launch screen, an advertisement, or a required onboarding step. It is a private-feeling reward for users who deliberately explore the application and repeatedly return to a piece of SWRLZ history.

The experience carries three meanings:

1. The train represents forward motion, endurance, and the years spent near the Leavenworth, Kansas train tracks and old station while SWRLZ existed first as a vision and later as an engineered system.
2. The dragon represents transformation, resilience, creative force, and the Glitch Dragon visual identity.
3. The rails represent the foundations beneath the visible product: architecture, contracts, checkpoints, lineage, trust, and disciplined implementation.

The central project motto appears within the cinematic:

> **Stay glitchy. Keep engineering. Keep swurlzing.**

A secondary phrase may appear as a hidden lore element:

> **Bugs are not always squashed. Some are choo'd away.**

---

## 2. Product Principles

The feature shall preserve the following SWRLZ principles:

- **Integrate; do not overwrite.**
- **Offline-first behavior remains intact.**
- **Normal User Mode must remain complete.** The Easter egg must not require Dev Mode.
- **Dev Mode remains for diagnostics, compatibility, experimentation, and testing only.**
- **No hidden trigger may silently change trust, identity, mission authority, permissions, provider routing, or protocol state.**
- **The Truth Firewall remains armed.**
- **Local and remote reward state must remain distinguishable.**
- **All reward claims require explicit user action and auditable lineage.**
- **No source disclosure is implied by the Easter egg.** The implementation may remain restricted to trusted employees and authorized maintainers.

---

## 3. Discovery and Activation

### 3.1 Location

The trigger shall be available from:

```text
Settings
  -> About
    -> Version
```

### 3.2 Trigger

The user taps the displayed version number **eight times** within a bounded time window.

Recommended trigger behavior:

- Required taps: `8`
- Tap window: `5 seconds`
- Reset on timeout: yes
- Reset when leaving the screen: yes
- Ignore accidental multi-touch duplication: yes
- Haptic progression: optional and accessibility-aware
- Visible instructions: none by default

### 3.3 Trigger Feedback

Intermediate taps should remain subtle. Suggested feedback:

- taps 1-4: no visible text
- taps 5-7: increasingly distinct haptic or glow pulse
- tap 8: short confirmation message such as `Swurlzing...`

The trigger must not unlock developer options, diagnostics, privileged settings, or hidden control surfaces.

---

## 4. Cinematic Experience

### 4.1 Opening

The screen fades to black. A low rail vibration begins, followed by a distant rhythm:

```text
...chug...
.....chug...
.........chug...
```

The rhythm grows into a locomotive cadence mixed with a restrained dragon growl.

### 4.2 Arrival

A **Glitch Dragon Train** enters at speed along rails assembled from cyan-violet energy, sparks, fractured light, and digital shards.

The dragon-locomotive emits a long:

```text
CHOOOOOOOOOOOO!
```

The sound should feel triumphant rather than startling. Audio must respect device volume, mute state, accessibility settings, and reduced-sensory preferences.

### 4.3 Flame and Credits

The dragon breathes stylized glitch flame across the rails and into a field of floating text. The flame reveals the cinematic credits and project story.

The sequence may use a receding cinematic crawl, but must remain visually original and must not reproduce the protected presentation, typography, music, timing, or exact visual language of any existing film franchise.

Recommended canonical text:

```text
SWRLZ

Started as a vision.

Built through persistence.

Engineered one checkpoint at a time.

Every mission.
Every late night.
Every bug choo'd away.
Every breakthrough.

Brought us here.

Stay glitchy.
Keep engineering.
Keep swurlzing.
```

### 4.4 Hidden Trackside Details

The train may pass subtle station signs or rail markers carrying SWRLZ-specific references:

- `INTEGRATE, DON'T OVERWRITE`
- `TRUTH FIREWALL`
- `CHECKPOINT`
- current compatible protocol version
- current application version
- `BUG CROSSING`
- a sign that flips from `BUG CROSSING` to `BUG CLEARED`

These details are decorative. They must not expose secrets, credentials, internal branch names, private infrastructure addresses, employee identities, or restricted implementation details.

### 4.5 Final Approach

The train curves toward the viewer. The headlight expands into a controlled white flash, then resolves into the SWRLZ or SWURLZER core mark.

Final text:

```text
Thank you for riding
The Glitch Dragon Express.

Destination:
The Future.
```

A final optional voice or text beat may say:

```text
...Swurlz.
```

The experience then returns to the same Settings screen and preserves prior scroll position and state.

---

## 5. Ride Progression

Each completed cinematic increments a **Ride Count**. The count is part of the user's local experience and may later participate in an authenticated reward claim.

Recommended milestones:

| Ride Count | Experience |
|---:|---|
| 1 | Welcome aboard the Glitch Dragon Express. |
| 8 | Regular Passenger acknowledgement. |
| 25 | Additional dragon-eye animation or hidden lore line. |
| 50 | Engineer's Platform sequence and reward eligibility. |

Suggested milestone text:

### Ride 8

```text
You've become a regular passenger.
```

### Ride 25

```text
The rails remember those who return.
```

### Ride 50

The train stops at a hidden station labeled:

```text
ENGINEER'S PLATFORM
```

A reward container appears and introduces **Founder's Appreciation**.

Milestones are product defaults, not permanent commercial commitments. Exact counts and rewards may be versioned or configured by authorized policy.

---

## 6. Reward and Subscription Discount

### 6.1 Intent

After sufficient genuine engagement, the user may unlock a first-time low-cost SWRLZ subscription offer or another approved appreciation reward.

The reward must feel like recognition, not coercion. It must not use countdown pressure, deceptive scarcity, forced sharing, ad viewing, or dark-pattern purchasing.

### 6.2 Example Reward

```text
You've unlocked
Founder's Appreciation

A special first-time SWRLZ subscription offer is available.
```

The exact percentage, price, duration, region, eligibility, tax treatment, and redemption window must be supplied by an authorized commercial policy rather than permanently embedded in UI copy.

### 6.3 Reward Authority

The CLIENT may track local ride progress offline, but the CLIENT must not be the sole authority for monetary discounts.

Recommended authority split:

```text
CLIENT
- renders the cinematic
- stores local ride observations
- displays local milestones
- requests reward eligibility
- presents an explicit claim action

SWURLZER / authorized commerce service
- validates account eligibility
- validates first-time subscription status
- applies current reward policy
- issues or rejects a single-use offer
- records claim lineage
- prevents duplicate redemption
```

### 6.4 Offline Behavior

While offline:

- the cinematic remains playable
- local ride count may continue
- non-monetary milestones may display
- monetary eligibility remains `pending verification`
- no fake or guessed discount code may be shown

When connectivity returns, the user may explicitly request verification.

### 6.5 Claim Requirements

A valid claim flow should include:

- authenticated account or approved anonymous-to-account upgrade path
- signed policy version
- offer identifier
- eligibility decision timestamp
- claim timestamp
- redemption status
- device and account lineage appropriate to privacy policy
- idempotency key
- explicit confirmation before purchase

### 6.6 Code Presentation

A raw reusable coupon code should be avoided where possible. Prefer an account-bound, single-use offer applied through an authenticated checkout flow.

If a display code is required, it must be:

- short-lived
- single-use
- account-bound where feasible
- generated server-side
- revocable
- excluded from logs and analytics payloads
- protected from screenshots only through policy and UX expectations, not false security claims

---

## 7. Persistence and Data Model

### 7.1 Local State

Suggested local fields:

```text
GlitchDragonExpressState
- schemaVersion
- completedRideCount
- lastCompletedAt
- highestMilestoneSeen
- pendingRewardVerification
- lastKnownRewardStatus
- reducedMotionPreferenceSnapshot
```

The local record is evidence of observed playback, not authoritative evidence of commercial eligibility.

### 7.2 Completion Rule

A ride should count only after a meaningful completion threshold, for example:

- at least 80% of the cinematic watched, or
- the final frame reached through normal playback, or
- accessibility-mode equivalent completed

Skipping immediately should not increment the count unless product policy explicitly allows it.

### 7.3 Integrity

Do not pretend a local counter is tamper-proof. The system should instead treat it as low-trust local evidence and combine it with server-side eligibility rules.

Avoid invasive device fingerprinting. Do not weaken offline-first identity or create an undisclosed surveillance mechanism merely to protect a promotional reward.

---

## 8. Accessibility

The Easter egg must be fully optional and accessibility-aware.

Required accommodations:

- skip or close control available after activation
- captions for all spoken or meaningful audio
- no essential information conveyed only through color
- reduced-motion presentation
- reduced-flash presentation
- screen-reader summary and milestone announcement
- respect system mute and volume
- no forced vibration
- no rapid flashing that violates accepted accessibility thresholds
- focus returns predictably to the Version element after playback

### 8.1 Reduced-Motion Variant

The reduced-motion version may use:

- static illustrated dragon train
- slow crossfades
- trackside text cards
- restrained particle effects
- no camera rush toward the viewer

Completion in reduced-motion mode must count identically to the standard experience.

---

## 9. Performance and Packaging

The feature must not degrade ordinary startup or Settings performance.

Requirements:

- do not preload full cinematic assets during normal launch
- lazy-load only after the eighth tap
- provide graceful fallback when assets are unavailable
- cap memory usage on lower-end Android devices
- release media resources when playback ends
- avoid blocking the main thread
- preserve battery-conscious behavior
- support deterministic asset versioning and checksums

A lightweight fallback may present text, sound, and a static illustration if the full cinematic cannot run safely.

---

## 10. Privacy, Telemetry, and Logging

Default local-only events may include:

- trigger activated
- playback started
- playback completed
- milestone reached
- reward verification requested

Remote telemetry, if later enabled, must follow explicit privacy policy and user settings.

Never log:

- discount codes
- payment tokens
- API keys
- private account credentials
- raw device secrets
- unrestricted personally identifying playback history

Communication logs should record correlation and outcome without leaking reward secrets.

---

## 11. Security and Abuse Resistance

The reward system should defend against casual duplication without compromising core SWRLZ values.

Recommended controls:

- server-authoritative offer issuance
- account-bound eligibility
- first-subscription validation
- idempotent claim endpoint
- signed offer policy
- replay protection
- rate limiting at the claim boundary
- explicit local-versus-remote status
- no privilege escalation through the Easter egg

The cinematic itself is not a security boundary. Obscurity of the eight-tap trigger is part of discovery, not authorization.

---

## 12. Employee and Source Access

The design is meaningful to the project founder and may be implemented within a restricted code area.

Recommended governance:

- least-privilege repository access
- protected branches
- required review for Easter egg and commerce changes
- CODEOWNERS for sensitive paths
- documented employee confidentiality obligations
- separated production secrets
- audited reward-policy changes
- no credentials or pricing secrets committed to source

Restricted source access may protect the implementation, but long-term maintainability still requires clear internal documentation, code review, recovery procedures, and more than one authorized maintainer when operationally necessary.

---

## 13. Configuration Boundaries

The following may be remotely configurable through an authenticated, versioned policy:

- reward availability
- reward type
- milestone threshold for eligibility
- eligible regions
- offer expiration
- subscription product identifier
- campaign copy identifier

The following should remain local application behavior unless separately approved:

- eight-tap discovery mechanism
- cinematic identity and core story
- accessibility controls
- Truth Firewall behavior
- user-mode availability

Remote configuration must never silently turn the Easter egg into an advertisement, enable hidden permissions, or remove required accessibility behavior.

---

## 14. Suggested Module Boundaries

Illustrative CLIENT boundaries:

```text
feature/about
- VersionTapDetector
- EasterEggEntryPoint

feature/glitchdragon
- GlitchDragonExpressScreen
- GlitchDragonExpressViewModel
- RideProgressRepository
- MilestoneEvaluator
- AccessibilityPresentationPolicy
- CinematicAssetLoader

feature/rewards
- RewardEligibilityClient
- RewardClaimCoordinator
- RewardStatusUiModel
```

Illustrative SERVER or SWURLZER boundaries:

```text
rewards
- EligibilityPolicy
- OfferIssuer
- ClaimLedger
- RedemptionVerifier
- RewardAuditEvent
```

These names are recommendations, not authorization to modify existing architecture or overwrite canonical module naming.

---

## 15. Acceptance Criteria

The design is implemented correctly when all of the following are true:

1. Eight taps on the Version element activate the experience without entering Dev Mode.
2. Normal app launch remains unchanged.
3. The cinematic is optional, skippable, and accessible.
4. Playback does not alter permissions, trust, routing, mission authority, or identity.
5. Local ride progress works offline.
6. Commercial rewards are not granted solely from a local counter.
7. Reward verification clearly distinguishes pending, eligible, claimed, expired, and unavailable states.
8. Duplicate reward claims are idempotently rejected.
9. Sensitive codes and credentials are absent from logs.
10. The user returns to the prior Settings state after playback.
11. Reduced-motion playback receives equal ride credit.
12. The implementation preserves protocol-version discipline and accepted SWRLZ contracts.

---

## 16. Verification Plan

Minimum verification should include:

- unit tests for tap timing and reset behavior
- unit tests for milestone progression
- process-death restoration tests
- offline playback tests
- accessibility scanner review
- reduced-motion verification
- low-memory device test
- orientation and lifecycle tests
- reward API idempotency tests
- duplicate-account and already-subscribed tests
- log-redaction tests
- protocol compatibility tests
- manual visual review of the full cinematic

No build, workflow, release, or deployment is authorized by this document alone.

---

## 17. Future Extensions

Potential future additions, each requiring separate approval:

- alternate train skins tied to major releases
- founder or employee-only hidden stations
- collectible non-monetary tickets
- anniversary routes
- local voice narration
- synchronized multi-device presentation
- optional AR Glitch Dragon Express scene
- a physical founder certificate or digital badge

Future extensions must not dilute the original story or convert the experience into repetitive promotional content.

---

## 18. Canonical Experience Summary

```text
Settings
  -> About
    -> tap Version 8 times
      -> screen fades to black
      -> distant chug rhythm
      -> Glitch Dragon Express arrives
      -> glitch flame reveals project story
      -> hidden SWRLZ rail markers pass
      -> motto appears
      -> ride count advances
      -> milestone may unlock
      -> reward eligibility may be verified
      -> user returns to Settings
```

The Glitch Dragon Express exists to preserve a piece of SWRLZ history inside the product itself: a reminder that strong systems are built on foundations most users never see, and that forward motion can begin beside the tracks long before the destination is visible.

> **Stay glitchy. Keep engineering. Keep swurlzing.**
