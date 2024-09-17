package io.netty.example.echo2;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalDemo {

    private static final ThreadLocal<String> SOME_LOCAL = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return "Hello, " + Thread.currentThread().getName();
            // initialValue:8, ThreadLocalDemo$1 (io.netty.example.echo2)
            // initialValue:5, ThreadLocalDemo$1 (io.netty.example.echo2)
            // setInitialValue:180, ThreadLocal (java.lang)
            // get:170, ThreadLocal (java.lang)
            // main:13, ThreadLocalDemo (io.netty.example.echo2)
        }
    };

    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 10; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(SOME_LOCAL.get());
                }
            });
        }

        System.in.read();
    }

}
