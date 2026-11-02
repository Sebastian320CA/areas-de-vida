package com.usta.ecogrow.util;

import javafx.concurrent.Task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Ejecuta trabajo de base de datos fuera del hilo de JavaFX (con hilos virtuales de Java 21+),
 * así la interfaz nunca se congela. Los callbacks corren en el hilo de JavaFX.
 */
public final class Async {

    private static final ExecutorService EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();

    private Async() {}

    public static <T> void run(Callable<T> work, Consumer<T> onSuccess, Consumer<Throwable> onError) {
        Task<T> task = new Task<>() {
            @Override
            protected T call() throws Exception {
                return work.call();
            }
        };
        task.setOnSucceeded(e -> onSuccess.accept(task.getValue()));
        task.setOnFailed(e -> onError.accept(task.getException()));
        EXECUTOR.execute(task);
    }

    @FunctionalInterface
    public interface ThrowingRunnable {
        void run() throws Exception;
    }

    public static void exec(ThrowingRunnable work, Runnable onSuccess, Consumer<Throwable> onError) {
        run(() -> {
            work.run();
            return Boolean.TRUE;
        }, ok -> onSuccess.run(), onError);
    }
}
