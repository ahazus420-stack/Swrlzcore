# CLIENT CFv2.0.13 SWRLZ Delivery Receipt

Checkpoint: INT-GRP-024A-REV2
Baseline: CLIENT CFv2.0.12
Target: CLIENT CFv2.0.13
ZIP: CLIENT_CFv2.0.13_SWRLZ.zip
SHA-256: 803865c220e7348b8b0b52d9183c59f8209db0c4ee8f0fc31c88587082bd18d4
Size: 4666362 bytes

Implemented source-only:
- full CLIENT-to-SERVER theme parity, including Jester-family and Inverse presentation;
- SERVER-owned persistent group foundation;
- unique leader-selected group names;
- rotatable invite codes with prior-code invalidation;
- pending leader approval after invite submission;
- SERVER group count;
- CLIENT and SERVER node action menus;
- secondary SERVER disconnect confirmation with registration, trust, membership, and lineage retention.

Authority boundary:
- SERVER remains canonical for validation, storage, routing, memberships, and audit-relevant state.
- Group leader has final approval or denial authority for membership after SERVER validation.
- Invite possession alone does not grant active membership.

Verification:
- STATIC BOUNDED VERIFICATION PASS
- ZIP INTEGRITY PASS
- CHECKSUM PAIR VERIFIED
- BUILD NOT RUN
- RUNTIME NOT TESTED
- GITHUB NOT MODIFIED
