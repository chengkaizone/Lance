package barcode.lance.client.handler.supplement;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

final class KillerCallable implements Callable<Void> {

	private final Future<?> future;
	private final long timeout;
	private final TimeUnit unit;

	KillerCallable(Future<?> future, long timeout, TimeUnit unit) {
		this.future = future;
		this.timeout = timeout;
		this.unit = unit;
	}

	public Void call() throws ExecutionException, InterruptedException {
		try {
			future.get(timeout, unit);
		} catch (TimeoutException te) {
			future.cancel(true);
		}
		return null;
	}

}
