package licenta.exception

import licenta.util.Concurrency
import licenta.util.Util

/**
 * Created by Dragos on 20.03.2016.
 */
class ExceptionHandler {

    private ExceptionHandler() {}

    public static synchronized void handle(Throwable e) {
        if (e instanceof BussinesException) {
            def bussinesException = e;

            Concurrency.callAsync {
                def buttonType = Util.errorMessage(bussinesException.message).showAndWait();
                if (buttonType != null) {
                    bussinesException.runAfterOk();
                }
            }.get();
        } else {
            e.printStackTrace();
        }
    }
}
