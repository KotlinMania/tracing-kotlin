// port-lint: source level_filters.rs
package io.github.kotlinmania.tracing

/**
 * The statically configured maximum trace level.
 */
public val STATIC_MAX_LEVEL: LevelFilter = getMaxLevelInner()

private fun getMaxLevelInner(): LevelFilter = LevelFilter.TRACE
