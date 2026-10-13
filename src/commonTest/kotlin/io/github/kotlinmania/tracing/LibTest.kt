// port-lint: tests lib.rs
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

    @Test
    fun testTracingLogIntegration() {
        val loggedRecords = mutableListOf<io.github.kotlinmania.log.Record>()
        val mockLogger =
            object : io.github.kotlinmania.log.Log {
                override fun enabled(metadata: io.github.kotlinmania.log.Metadata): Boolean = true

                override fun log(record: io.github.kotlinmania.log.Record) {
                    loggedRecords.add(record)
                }

                override fun flush() {}
            }
        val logMeta =
            io.github.kotlinmania.log.Metadata
                .builder()
                .target("test-target")
                .level(io.github.kotlinmania.log.Level.Info)
                .build()
        val meta =
            Metadata(
                name = "test-log",
                target = "test-target",
                level = Level.INFO,
                file = "LibTest.kt",
                line = 42,
                modulePath = "tracing::test",
            )
        val fieldSet = FieldSet(listOf("message", "status"))
        val values = ValueSet(fieldSet, mapOf("message" to "event happened", "status" to "ok"))

        tracingLog(meta, mockLogger, logMeta, values)

        assertEquals(1, loggedRecords.size)
        val rec = loggedRecords[0]
        assertEquals("LibTest.kt", rec.file())
        assertEquals(42, rec.line())
        assertEquals("tracing::test", rec.modulePath())
        assertEquals("event happened status=ok", rec.args().toString())
    }
}
