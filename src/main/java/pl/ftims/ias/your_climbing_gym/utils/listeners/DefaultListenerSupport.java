package pl.ftims.ias.your_climbing_gym.utils.listeners;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;
//class for retrying
public class DefaultListenerSupport extends RetryListenerSupport {
    @Override
    public <T, E extends Throwable> void close(RetryContext context,
                                               RetryCallback<T, E> callback, Throwable throwable) {
        System.out.println("on Close");
        super.close(context, callback, throwable);
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context,
                                                 RetryCallback<T, E> callback, Throwable throwable) {
        System.out.println("on Error");
        super.onError(context, callback, throwable);
    }

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context,
                                                 RetryCallback<T, E> callback) {
        System.out.println("on Open");
        return super.open(context, callback);
    }
}
