package me.jameshunt.inmotiontestapplication

import com.inmotionsoftware.promisekt.Result
import com.inmotionsoftware.promisekt.Thenable
import org.junit.Assert.fail
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

interface AsyncTests {
    fun wait(countDown: CountDownLatch, timeout: Long, timeUnit: TimeUnit = TimeUnit.SECONDS) {
        val executor = Executors.newSingleThreadExecutor()
        executor.submit { countDown.await() }
        try {
            executor.shutdown()
            if (!executor.awaitTermination(timeout, timeUnit)) { fail("wait() timeout") }
        } catch (e: Throwable) {
            fail("wait() timeout")
        }
    }

    fun <T> Thenable<T>.throwCaughtErrors() {
        (this.result as? Result.rejected)?.let {
            throw it.error
        }
    }
}