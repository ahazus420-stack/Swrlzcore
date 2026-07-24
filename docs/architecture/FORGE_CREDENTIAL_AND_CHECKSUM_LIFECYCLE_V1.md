# Forge Credential and Checksum Lifecycle v1

**Status:** Accepted CFv2.0.x architecture; runtime acceptance remains evidence-gated.  
**Updated:** 2026-07-24

## 1. Purpose

This document defines how SWRLZ CLIENT and SWURLZER SERVER acquire, normalize, persist, validate, refresh, reject, and apply GitHub credentials, and how either Forge obtains the exact checksum receipt paired with a selected source ZIP.

It supplements `CLIENT_FORGE_IDENTITY_AND_BUBBLE_FOUNDATION_V1.md` and supersedes that document's earlier assumption that a persistent folder grant or manual checksum picker is the normal fallback.

## 2. Credential states

Forge must not collapse these states:

```text
manual candidate text
validated active access token
refresh token
known access-token expiry
known refresh-token expiry
connected account identity
rejected/retired credential
```

Typing text never changes the active credential. Connected state is published only after GitHub validates the complete normalized candidate.

## 3. Token normalization

Before validation, encrypted persistence, or Authorization-header creation, Forge:

- trims surrounding whitespace;
- removes an accidentally pasted `Bearer ` prefix;
- removes an accidentally pasted `token ` prefix;
- removes remaining whitespace characters, because GitHub token values contain no whitespace.

The normalized token value is never logged.

## 4. Credential classes

Forge recognizes non-secret token classification by prefix:

```text
github_pat_   fine-grained personal access token
ghp_          classic personal access token
gho_          OAuth App access token
ghu_          GitHub App user access token
ghr_          GitHub App refresh token
```

Prefix classification is diagnostic metadata only. It does not grant authority.

## 5. Device-flow lifecycle

When GitHub's device-flow response supplies lifecycle metadata, Forge persists:

- access token;
- refresh token;
- `expires_in` converted to an absolute access expiry;
- `refresh_token_expires_in` converted to an absolute refresh expiry;
- connected login and repository target.

CLIENT stores lifecycle secrets in its Android Keystore-backed encrypted secret store and may include them only in the existing eligible encrypted Android backup/device-transfer envelope. SERVER stores them in its own Android Keystore-backed encrypted secret store.

The two applications never expose or copy token material through UI, notifications, logs, repository commits, or cross-app intents.

## 6. Pre-transfer authentication gate

Before source bytes stream, each Forge resolves one immutable credential snapshot and validates:

```text
GET /user
GET /repos/<owner>/<repository>/git/ref/heads/<branch>
```

Only that validated snapshot may enter the upload transaction.

A local ZIP/SHA validation success does not bypass this gate. A visually connected account does not bypass this gate.

## 7. Refresh policy

When refresh metadata is available, Forge refreshes a GitHub App user access token:

- before the known expiry, using a five-minute safety window;
- after a 401 received during a long transfer;
- at most once for the affected upload transaction.

A successful refresh rotates both access and refresh tokens and persists the new expiration metadata before connected state is republished.

A non-refreshable OAuth or personal-access token cannot be silently replaced. It requires explicit reconnection or a new manual token.

## 8. Rejection and transient failure

Forge distinguishes authoritative rejection from temporary failure:

```text
401 Bad credentials / rejected refresh token
    -> retire encrypted credential
    -> clear connected account projection
    -> require one reconnection

network timeout / temporary GitHub outage / DNS failure
    -> retain credential
    -> show retryable error
```

Retirement after explicit GitHub rejection is a confirmed invalidation policy, not an unapproved user disconnect.

## 9. Reauthorization pressure

Forge must not generate unnecessary device-flow tokens. Saved valid credentials and refresh tokens are preferred over a new authorization request. CLIENT and SERVER maintain separate Android sandboxes, so each app owns its own credential lifecycle.

## 10. Checksum acquisition

For a selected:

```text
<base>.zip
```

Forge derives:

```text
<base>.sha256
```

Resolution order:

1. use an exact readable sibling receipt when the document provider exposes it;
2. attempt provider-safe parent/document/MediaStore resolution;
3. when sibling access remains blocked, hash the selected ZIP locally and generate a canonical receipt:

```text
<64 lowercase hexadecimal digest>  <exact ZIP filename>
```

The generated receipt is staged automatically beside the ZIP and passes through the same local pair validation and repository integrity workflow as a user-supplied receipt.

`LOCATE SHA-256` is reserved for exceptional failure of both sibling resolution and local receipt creation.

## 11. Diagnostics

Redacted Forge diagnostics may record:

- credential kind;
- whether preflight passed;
- whether refresh occurred;
- whether a 401 caused one bounded retry;
- whether the credential was retired;
- sibling versus generated checksum provenance.

Diagnostics must never record access tokens, refresh tokens, Authorization headers, OAuth device codes, or token fingerprints that materially aid reconstruction.

## 12. Invariants

- No source streaming begins without exact credential preflight.
- No token candidate becomes active before validation.
- Refresh rotation persists before connected state is republished.
- Explicit 401 rejection prevents silent credential reuse.
- Transient failures do not delete credentials.
- One upload receives at most one refresh retry.
- A selected ZIP always targets the exact-basename `.sha256` receipt.
- Generated receipts are computed from the selected ZIP bytes, never guessed.
- CLIENT and SERVER remain separate credential authorities.
