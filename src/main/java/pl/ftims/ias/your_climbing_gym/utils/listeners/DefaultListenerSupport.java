package pl.ftims.ias.your_climbing_gym.utils.listeners;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;
import pl.ftims.ias.your_climbing_gym.utils.loggers.TransactionLogger;

//class for retrying
public class DefaultListenerSupport extends RetryListenerSupport {

    private final TransactionLogger transactionLogger = new TransactionLogger();
    private boolean lastCommitted = true;

    @Override
    public <T, E extends Throwable> void close(RetryContext context,
                                               RetryCallback<T, E> callback, Throwable throwable) {
        transactionLogger.logTransactionEnd(lastCommitted);
        super.close(context, callback, throwable);
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context,
                                                 RetryCallback<T, E> callback, Throwable throwable) {
        lastCommitted = false;
        transactionLogger.logTransactionError();
        super.onError(context, callback, throwable);
    }

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context,
                                                 RetryCallback<T, E> callback) {
        lastCommitted = true;
        transactionLogger.logTransactionBegin();
        return super.open(context, callback);
    }
}
