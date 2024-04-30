package com.xue.cache;

import com.xue.frame.Warning;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Xue
 * @create 2024-04-29 14:46
 */
@Getter
@Slf4j
class WarningExpand {

    private ConcurrentHashMap<Warning, AtomicBoolean> warningMark = new ConcurrentHashMap<>();

    private ExecutorService pool = Executors.newSingleThreadExecutor();

    public WarningExpand() {
        pool.execute(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
                warningMark.forEach((k, v) -> {
                    if (v.getAndSet(true)) {
                        warningMark.remove(k);
                    }
                });
            }
        });
    }

    public void addWarning(Warning warning) {
        if (warningMark.containsKey(warning)) {
            warningMark.get(warning).set(false);
        } else {
            warningMark.put(warning, new AtomicBoolean(false));
        }
    }

}
