import kotlin.coroutines.CoroutineContext
import io.micrometer.tracing.Span
import io.micrometer.tracing.Tracer

class SpanContextElement(
    private val span: Span
) : ThreadContextElement<Span>, CoroutineContext.Element {

    companion object Key : CoroutineContext.Key<SpanContextElement>

    override val key: CoroutineContext.Key<SpanContextElement> get() = Key

    override fun updateThreadContext(context: CoroutineContext): Span {
        val previous = SpanHolder.current
        SpanHolder.current = span
        return previous
    }

    override fun restoreThreadContext(context: CoroutineContext, oldState: Span) {
        SpanHolder.current = oldState
    }
}

object SpanHolder {
    private val threadLocal = ThreadLocal<Span>()

    var current: Span?
        get() = threadLocal.get()
        set(value) = threadLocal.set(value)
}

