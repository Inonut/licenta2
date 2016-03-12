package licenta.utils

import javafx.application.Platform

import java.util.concurrent.CountDownLatch

/**
 * Created by Dragos on 08.02.2016.
 */
public class Concurrency {

    /*private static final ExecutorService executorService = Executors.newFixedThreadPool(10, {r ->
        return Thread.start { r.run(); };
    });*/

    public Concurrency() {
    }

    public synchronized void setRunLater(Runnable run) {
        if (Platform.isFxApplicationThread()) {
            run.run();
        } else {
            Platform.runLater({ ->
                run.run();
            });
        }
    }


    public synchronized void setRunNow(Runnable run) {
        if (Platform.isFxApplicationThread()) {
            run.run();
        } else {
            def latch = new CountDownLatch(1);
            Platform.runLater({ ->
                run.run();
                latch.countDown();
            });
            latch.await();
        }
    }

    public synchronized void setExecute(Runnable run) {
//        executorService.execute(run);

        Thread.start { run.run(); };
    }
}