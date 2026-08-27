# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 8/85 (9.4%)
- **Function parity:** 12/430 matched (target 112) — 2.8%
- **Class/type parity:** 5/175 matched (target 23) — 2.9%
- **Combined symbol parity:** 17/605 matched (target 135) — 2.8%
- **Average inline-code cosine:** 0.16 (function body across 7 matched files)
- **Average documentation cosine:** 0.21 (doc text across 7 matched files)
- **Cheat-zeroed Files:** 2
- **Critical Issues:** 7 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. tests.macros

- **Target:** `tracing.Macros`
- **Similarity:** 0.07
- **Dependents:** 0
- **Priority Score:** 515109.3
- **Functions:** 0/49 matched (target 14)
- **Missing functions:** `fmt`, `span`, `trace_span`, `debug_span`, `info_span`, `warn_span`, `error_span`, `span_root`, `trace_span_root`, `debug_span_root`, `info_span_root`, `warn_span_root`, `error_span_root`, `span_with_parent`, `trace_span_with_parent`, `debug_span_with_parent`, `info_span_with_parent`, `warn_span_with_parent`, `error_span_with_parent`, `span_with_non_rust_symbol`, `large_span`, `event`, `enabled`, `span_enabled`, `event_enabled`, `locals_with_message`, `locals_no_message`, `trace`, `debug`, `info`, `warn`, `error`, `event_root`, `trace_root`, `debug_root`, `info_root`, `warn_root`, `error_root`, `event_with_parent`, `trace_with_parent`, `debug_with_parent`, `info_with_parent`, `warn_with_parent`, `error_with_parent`, `field_shorthand_only`, `borrow_val_events`, `borrow_val_spans`, `callsite_macro_api`, `format_args_already_defined`
- **Types:** 0/2 matched (target 0)
- **Missing types:** `DisplayDebug`, `Position`
- **Tests:** 0/48 matched

### 2. tests.span

- **Target:** `tracing.Span`
- **Similarity:** 0.01
- **Dependents:** 0
- **Priority Score:** 434309.9
- **Functions:** 0/41 matched (target 45)
- **Missing functions:** `handles_to_the_same_span_are_equal`, `handles_to_different_spans_are_not_equal`, `handles_to_different_spans_with_the_same_metadata_are_not_equal`, `make_span`, `spans_always_go_to_the_subscriber_that_tagged_them`, `spans_always_go_to_the_subscriber_that_tagged_them_even_across_threads`, `dropping_a_span_calls_drop_span`, `span_closes_after_event`, `new_span_after_event`, `event_outside_of_span`, `cloning_a_span_calls_clone_span`, `drop_span_when_exiting_dispatchers_context`, `clone_and_drop_span_always_go_to_the_subscriber_that_tagged_the_span`, `span_closes_when_exited`, `enter`, `entered`, `entered_api`, `moved_field`, `dotted_field_name`, `borrowed_field`, `move_field_out_of_struct`, `float_values`, `record_new_value_for_field`, `record_new_values_for_fields`, `record_all_macro_records_new_values_for_fields`, `record_all_macro_records_all_fields`, `record_all_macro_records_all_fields_different_order`, `record_all_macro_unknown_field`, `new_span_with_target_and_log_level`, `explicit_root_span_is_root`, `explicit_root_span_is_root_regardless_of_ctx`, `explicit_child`, `explicit_child_at_levels`, `explicit_child_regardless_of_ctx`, `contextual_root`, `contextual_child`, `display_shorthand`, `debug_shorthand`, `both_shorthands`, `constant_field_name`, `keyword_ident_in_field_name_span_macro`
- **Types:** 0/2 matched (target 7)
- **Missing types:** `Position`, `Foo`
- **Tests:** 0/39 matched

### 3. tests.subscriber

- **Target:** `tracing.Subscriber`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 121210.0
- **Functions:** 0/11 matched (target 3)
- **Missing functions:** `event_macros_dont_infinite_loop`, `register_callsite`, `enabled`, `new_span`, `record`, `record_follows_from`, `event`, `enter`, `exit`, `boxed_subscriber`, `arced_subscriber`
- **Types:** 0/1 matched (target 0)
- **Missing types:** `TestSubscriber`
- **Tests:** 0/3 matched

### 4. tests.instrument

- **Target:** `tracing.Instrument`
- **Similarity:** 0.04
- **Dependents:** 0
- **Priority Score:** 50609.6
- **Functions:** 1/3 matched (target 19)
- **Missing functions:** `span_on_drop`, `drop`
- **Types:** 0/3 matched (target 4)
- **Missing types:** `AssertSpanOnDrop`, `Fut`, `Output`
- **Tests:** 0/1 matched

### 5. tracing.lib

- **Target:** `tracing.Lib [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 1310.0
- **Functions:** 9/9 matched (target 16)
- **Missing functions:** _none_
- **Types:** 4/4 matched (target 6)
- **Missing types:** _none_

### 6. tracing.field

- **Target:** `tracing.Field`
- **Similarity:** 0.60
- **Dependents:** 0
- **Priority Score:** 204.0
- **Functions:** 1/1 matched (target 2)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 7. tracing.level_filters

- **Target:** `tracing.LevelFilters`
- **Similarity:** 0.41
- **Dependents:** 0
- **Priority Score:** 105.9
- **Functions:** 1/1 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 8. tracing.dispatcher

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

