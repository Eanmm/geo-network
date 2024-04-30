package com.xue.cache;

import com.xue.frame.Car;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Xue
 * @create 2024-04-29 11:24
 */
@Slf4j
@Getter
@Setter
class CarExpand {

    private Car car;
    private ExecutorService pool = Executors.newSingleThreadExecutor();
    private AtomicBoolean down = new AtomicBoolean(true);

    public CarExpand(Car car, Execute execute) {
        this.car = car;
        pool.submit(() -> {
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(1500);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
                if (down.getAndSet(true)) {
                    break;
                }
            }
            execute.run();
        });
    }

    public void delayLife() {
        down.set(false);
    }
}
