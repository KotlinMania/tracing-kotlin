import Testing
import Tracing

@Suite struct TracingExportTests {
    @Test func testSwiftModuleLoads() {
        #expect(Bool(true), "Tracing swift module imported cleanly")
    }

    @Test func testTracingVersionAndEnums() {
        #expect(Level.INFO.priority == 3)
        #expect(Level.DEBUG.priority == 4)
        #expect(LevelFilter.TRACE.priority == 5)
    }

    @Test func testSpanNoneAndDisabled() {
        let spanNone = Span.Companion.shared.none()
        #expect(spanNone.isNone())
        #expect(spanNone.isDisabled())
    }
}
