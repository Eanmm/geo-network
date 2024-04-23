/*
package com.xue;

import com.pi4j.io.gpio.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public enum LightControl {


    */
/**
     * 单例
     *//*

    INSTANCE;

    private static final GpioController GPIO = GpioFactory.getInstance();
    private static final GpioPinDigitalOutput RED = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_23, "red", PinState.LOW);
    private static final GpioPinDigitalOutput YELLOW = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_24, "yellow", PinState.LOW);
    private static final GpioPinDigitalOutput GREEN = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_25, "green", PinState.LOW);

    private ScheduledExecutorService executorService;

    LightControl() {
        executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("light-pool-%d").daemon(true).build());
        executorService.scheduleAtFixedRate(this::lightDown, 0, 1, TimeUnit.SECONDS);
    }

    public void lightClose() {
        if (ObjectUtils.isNotEmpty(executorService)) {
            executorService.shutdownNow();
            lightUp();
            executorService = new ScheduledThreadPoolExecutor(1,
                    new BasicThreadFactory.Builder().namingPattern("light-pool-%d").daemon(true).build());
        }
    }

    public void lightUp() {
        if (RED.isLow()) {
            RED.high();
            YELLOW.high();
            GREEN.high();
        }
    }

    public void lightDown() {
        if (RED.isHigh()) {
            RED.low();
            YELLOW.low();
            GREEN.low();
        }
    }
}
*/
