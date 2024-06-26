package com.xue.frame;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Xue
 * @create 2024-04-22 15:13
 */
@Slf4j
public class StatsLogger {
    private AtomicInteger txCam = new AtomicInteger();
    private AtomicInteger rxCam = new AtomicInteger();
    private AtomicInteger txDenm = new AtomicInteger();
    private AtomicInteger rxDenm = new AtomicInteger();

    /**
     * StatsLogger constructor.
     *
     * @param executor Executor service the thread writing stats to log will be added to.
     */
    StatsLogger(ExecutorService executor) {
        executor.submit(logStats);
    }

    /**
     * Increment the count of transmitted CAM messages.
     */
    public void incTxCam() {
        this.txCam.incrementAndGet();
    }

    /**
     * Increment the count of received CAM messages.
     */
    public void incRxCam() {
        this.rxCam.incrementAndGet();
    }

    /**
     * Increment the count of transmitted DENM messages.
     */
    public void incTxDenm() {
        this.txDenm.incrementAndGet();
    }

    /**
     * Increment the count of received DENM messages.
     */
    public void incRxDenm() {
        this.rxDenm.incrementAndGet();
    }


    /**
     * Dedicated thread for periodically logging statistics.
     */
    private Runnable logStats =
            new Runnable() {
                @Override
                public void run() {

                    /* Chill out for a bit to let everything else start
                     * before logging anything.
                     */
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        log.warn("Statistics logger interrupted during sleep.");
                    }
                    /* Log statistics every second */
                    while (true) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            log.warn("Statistics logger interrupted during sleep.");
                        }

                        /* Log stats */
                        log.info(
                                "#CAM (Tx/Rx): {}/{} "
                                        + "| #DENM (Tx/Rx): {}/{} ",
                                txCam,
                                rxCam,
                                txDenm,
                                rxDenm);
                    }
                }
            };
}
