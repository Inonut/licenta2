package licenta.exception;

/**
 * Created by Dragos on 10.02.2016.
 */
public class BussinesException extends Exception {

    private Runnable run;

    public BussinesException() {
    }

    public BussinesException(String message) {
        super(message);
    }

    public BussinesException(String message, Throwable cause) {
        super(message, cause);
    }

    public BussinesException(Throwable cause) {
        super(cause);
    }

    public BussinesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BussinesException setAftarOk(Runnable run) {
        this.run = run;
        return this;
    }

    public void runAfterOk() throws Throwable {
        if (run != null) {
            run.run();
        }
    }
}
