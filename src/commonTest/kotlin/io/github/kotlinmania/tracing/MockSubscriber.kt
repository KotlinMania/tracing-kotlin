package io.github.kotlinmania.tracing

public class MockSubscriber : Subscriber {
    public val events: MutableList<Event> = mutableListOf()
    public val spans: MutableList<Attributes> = mutableListOf()
    public val records: MutableList<Pair<Id, Record>> = mutableListOf()
    public val entered: MutableList<Id> = mutableListOf()
    public val exited: MutableList<Id> = mutableListOf()
    public val follows: MutableList<Pair<Id, Id>> = mutableListOf()
    private var nextId: Long = 1
    private var current: Id? = null

    override fun registerCallsite(metadata: Metadata): Interest = Interest.ALWAYS

    override fun enabled(metadata: Metadata): Boolean = true

    override fun newSpan(attributes: Attributes): Id {
        spans.add(attributes)
        return Id(nextId++)
    }

    override fun record(id: Id, values: Record) {
        records.add(Pair(id, values))
    }

    override fun recordFollowsFrom(span: Id, follows: Id) {
        this.follows.add(Pair(span, follows))
    }

    override fun event(event: Event) {
        events.add(event)
    }

    override fun enter(id: Id) {
        current = id
        entered.add(id)
    }

    override fun exit(id: Id) {
        if (current == id) {
            current = null
        }
        exited.add(id)
    }

    override fun currentSpan(): Id? = current

    override fun cloneSpan(id: Id): Id = id

    override fun tryClose(id: Id): Boolean = true

    public fun clear() {
        events.clear()
        spans.clear()
        records.clear()
        entered.clear()
        exited.clear()
        follows.clear()
        current = null
        nextId = 1
    }
}
