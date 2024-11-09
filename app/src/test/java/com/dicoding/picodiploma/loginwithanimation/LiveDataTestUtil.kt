import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun <T> Flow<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterCollect: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)

    runBlocking {
        this@getOrAwaitValue.collect { value ->
            data = value
            latch.countDown()
        }
    }

    try {
        afterCollect.invoke()

        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("Flow value was never set.")
        }

    } catch (e: Exception) {
        throw e // Rethrow any exceptions that occur during collection
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}