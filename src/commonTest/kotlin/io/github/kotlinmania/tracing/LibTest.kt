// port-lint: tests tracing/src/lib.rs
package io.github.kotlinmania.tracing

import kotlin.test.Test
import kotlin.test.assertEquals

class LibTest {
    @Test
    fun testTracingVersion() {
        assertEquals("0.1.41", Tracing.VERSION)
    }

    @Test
    fun testFieldName() {
        val field = FieldName.new("r#type")
        assertEquals("type", field.asStr())
        assertEquals(4, field.len())
        assertEquals("FieldName(type)", field.fmt())
        assertEquals("FieldName(type)", field.toString())
    }

    @Test
    fun testLogValueSetFormatting() {
        val fieldSet = FieldSet(listOf("message", "count"))
        val valueSet = ValueSet(fieldSet, mapOf("message" to "processing", "count" to 10))
        val logValueSet = LogValueSet(valueSet)
        val formatted = logValueSet.toString()
        assertEquals("processing count=10", formatted)
    }
}
