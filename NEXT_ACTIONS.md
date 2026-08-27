# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 8/8 (100.0%)
- **Function parity:** 56/65 matched (target 112) — 86.2%
- **Class/type parity:** 13/15 matched (target 23) — 86.7%
- **Combined symbol parity:** 69/80 matched (target 135) — 86.2%
- **Average inline-code cosine:** 0.40 (function body across 8 matched files)
- **Average documentation cosine:** 0.49 (doc text across 8 matched files)
- **Cheat-zeroed Files:** 2
- **Critical Issues:** 6 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. span

- **Target:** `tracing.Span`
- **Similarity:** 0.41
- **Dependents:** 1
- **Priority Score:** 1074305.9
- **Functions:** 30/36 matched (target 45)
- **Missing functions:** `log`, `eq`, `hash`, `fmt`, `drop`, `deref`
- **Types:** 6/7 matched
- **Missing types:** `PhantomNotSend`
- **Tests:** 1/1 matched

### 2. instrument

- **Target:** `tracing.Instrument`
- **Similarity:** 0.52
- **Dependents:** 1
- **Priority Score:** 1041804.8
- **Functions:** 12/15 matched (target 19)
- **Missing functions:** `span`, `inner`, `dispatcher`
- **Types:** 2/3 matched (target 4)
- **Missing types:** `Output`

### 3. lib

- **Target:** `tracing.Lib`
- **Similarity:** 0.44
- **Dependents:** 0
- **Priority Score:** 1305.6
- **Functions:** 9/9 matched (target 16)
- **Missing functions:** _none_
- **Types:** 4/4 matched (target 6)
- **Missing types:** _none_
- **Lint issues:** 2

### 4. subscriber

- **Target:** `tracing.Subscriber`
- **Similarity:** 0.84
- **Dependents:** 0
- **Priority Score:** 301.6
- **Functions:** 3/3 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 5. field

- **Target:** `tracing.Field`
- **Similarity:** 0.60
- **Dependents:** 0
- **Priority Score:** 204.0
- **Functions:** 1/1 matched (target 2)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 6. level_filters

- **Target:** `tracing.LevelFilters`
- **Similarity:** 0.41
- **Dependents:** 0
- **Priority Score:** 105.9
- **Functions:** 1/1 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 7. macros

- **Target:** `tracing.Macros [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 14)
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 8. dispatcher

- **Target:** `tracing.Dispatcher [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 12)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 5)
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

