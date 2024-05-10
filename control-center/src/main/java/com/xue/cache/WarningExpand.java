package com.xue.cache;

import com.xue.frame.Warning;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Xue
 * @create 2024-04-29 14:46
 */
@Getter
@Setter
@Slf4j
class WarningExpand {

    private Warning warning;
    private ExecutorService pool = Executors.newSingleThreadExecutor();
    private AtomicBoolean down = new AtomicBoolean(true);


    public WarningExpand(Warning warning, Execute execute) {
        this.warning = warning;
        pool.execute(() -> {
            while (true) {
                try {
                    TimeUnit.MINUTES.sleep(1500);
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
