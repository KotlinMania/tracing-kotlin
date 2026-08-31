# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 8/8 (100.0%)
- **Function parity:** 56/65 matched (target 86) — 86.2%
- **Class/type parity:** 13/15 matched (target 18) — 86.7%
- **Combined symbol parity:** 69/80 matched (target 104) — 86.2%
- **Average inline-code cosine:** 0.40 (function body across 7 matched files)
- **Average documentation cosine:** 0.52 (doc text across 7 matched files)
- **Cheat-zeroed Files:** 1
- **Critical Issues:** 6 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. span

- **Target:** `tracing.Span [PROVENANCE-FALLBACK]`
- **Similarity:** 0.41
- **Dependents:** 1
- **Priority Score:** 1074305.9
- **Functions:** 30/36 matched (target 45)
- **Missing functions:** `log`, `eq`, `hash`, `fmt`, `drop`, `deref`
- **Types:** 6/7 matched
- **Missing types:** `PhantomNotSend`
- **Tests:** 1/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tracing/src/span.rs` vs expected `span.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:tracing/src/span.rs` vs expected `span.rs`
- **Proposed provenance header:** `// port-lint: source span.rs` (current: `// port-lint: source tracing/src/span.rs`)
- **Proposed provenance header:** `// port-lint: tests span.rs` (current: `// port-lint: tests tracing/src/span.rs`)
- **Lint issues:** 2

### 2. instrument

- **Target:** `tracing.Instrument [PROVENANCE-FALLBACK]`
- **Similarity:** 0.52
- **Dependents:** 1
- **Priority Score:** 1041804.8
- **Functions:** 12/15 matched (target 19)
- **Missing functions:** `span`, `inner`, `dispatcher`
- **Types:** 2/3 matched (target 4)
- **Missing types:** `Output`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tracing/src/instrument.rs` vs expected `instrument.rs`
- **Proposed provenance header:** `// port-lint: source instrument.rs` (current: `// port-lint: source tracing/src/instrument.rs`)
- **Lint issues:** 1

### 3. lib

- **Target:** `tracing.Lib [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 1310.0
- **Functions:** 9/9 matched (target 16)
- **Missing functions:** _none_
- **Types:** 4/4 matched (target 6)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tracing/src/lib.rs` vs expected `lib.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:tracing/src/lib.rs` vs expected `lib.rs`
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source tracing/src/lib.rs`)
- **Proposed provenance header:** `// port-lint: tests lib.rs` (current: `// port-lint: tests tracing/src/lib.rs`)
- **Lint issues:** 2

### 4. subscriber

- **Target:** `tracing.Subscriber [PROVENANCE-FALLBACK]`
- **Similarity:** 0.84
- **Dependents:** 0
- **Priority Score:** 301.6
- **Functions:** 3/3 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tracing/src/subscriber.rs` vs expected `subscriber.rs`
- **Proposed provenance header:** `// port-lint: source subscriber.rs` (current: `// port-lint: source tracing/src/subscriber.rs`)
- **Lint issues:** 1

### 5. field

- **Target:** `tracing.Field [PROVENANCE-FALLBACK]`
- **Similarity:** 0.60
- **Dependents:** 0
- **Priority Score:** 204.0
- **Functions:** 1/1 matched (target 2)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tracing/src/field.rs` vs expected `field.rs`
- **Proposed provenance header:** `// port-lint: source field.rs` (current: `// port-lint: source tracing/src/field.rs`)
- **Lint issues:** 1

### 6. level_filters

- **Target:** `tracing.LevelFilters [PROVENANCE-FALLBACK]`
- **Similarity:** 0.41
- **Dependents:** 0
- **Priority Score:** 105.9
- **Functions:** 1/1 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tracing/src/level_filters.rs` vs expected `level_filters.rs`
- **Proposed provenance header:** `// port-lint: source level_filters.rs` (current: `// port-lint: source tracing/src/level_filters.rs`)
- **Lint issues:** 1

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Matched

| Source | Target | Path |
|--------|--------|------|
| `macros` | `tracing.Macros` | `macros` |
| `dispatcher` | `tracing.Dispatcher` | `dispatcher` |

