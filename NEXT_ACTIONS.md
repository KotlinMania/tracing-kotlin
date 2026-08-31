# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 8/85 (9.4%)
- **Function parity:** 56/411 matched (target 86) — 13.6%
- **Class/type parity:** 13/175 matched (target 18) — 7.4%
- **Combined symbol parity:** 69/586 matched (target 104) — 11.8%
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

### 1. tracing.subscriber

- **Target:** `tracing.Subscriber`
- **Similarity:** 0.84
- **Dependents:** 2
- **Priority Score:** 2000301.6
- **Functions:** 3/3 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 2. tracing.span

- **Target:** `tracing.Span`
- **Similarity:** 0.41
- **Dependents:** 1
- **Priority Score:** 1074305.9
- **Functions:** 30/36 matched (target 45)
- **Missing functions:** `log`, `eq`, `hash`, `fmt`, `drop`, `deref`
- **Types:** 6/7 matched
- **Missing types:** `PhantomNotSend`
- **Tests:** 1/1 matched

### 3. tracing.instrument

- **Target:** `tracing.Instrument`
- **Similarity:** 0.52
- **Dependents:** 1
- **Priority Score:** 1041804.8
- **Functions:** 12/15 matched (target 19)
- **Missing functions:** `span`, `inner`, `dispatcher`
- **Types:** 2/3 matched (target 4)
- **Missing types:** `Output`

### 4. tracing.lib

- **Target:** `tracing.Lib [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 1310.0
- **Functions:** 9/9 matched (target 16)
- **Missing functions:** _none_
- **Types:** 4/4 matched (target 6)
- **Missing types:** _none_

### 5. tracing.field

- **Target:** `tracing.Field`
- **Similarity:** 0.60
- **Dependents:** 0
- **Priority Score:** 204.0
- **Functions:** 1/1 matched (target 2)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 6. tracing.level_filters

- **Target:** `tracing.LevelFilters`
- **Similarity:** 0.41
- **Dependents:** 0
- **Priority Score:** 105.9
- **Functions:** 1/1 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

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
| `tracing.macros` | `tracing.Macros` | `tracing/src/macros` |
| `tracing.dispatcher` | `tracing.Dispatcher` | `tracing/src/dispatcher` |

