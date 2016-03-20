package licenta.util

import java.util.concurrent.*

/**
 * Created by Dragos on 13.03.2016.
 */
class Concurrency {

    private ExecutorService pool

    private static synchronized INSTANCE = new Concurrency()

    private Concurrency() {
        this.pool = Executors.newFixedThreadPool(10, { Runnable runnable ->
            final Thread thread = new Thread(runnable)
            thread.daemon = true
            thread
        } as ThreadFactory)

    }

    synchronized static Future callAsync(Closure closure, def ... args) {
        return INSTANCE.pool.submit({
            try {
                closure(*args)
            } catch (Exception e) {
                e.printStackTrace()
            }
        } as Callable)


    }

}
