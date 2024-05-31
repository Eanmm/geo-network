package com.xue.frame;


import java.time.Instant;

/**
 * @author xuejingbao
 * @create 2023-05-23 13:59
 */
public class MessageFactory {

    private MessageFactory() {
    }

    private static class MessageFactoryInstance {
        private static final MessageFactory INSTANCE = new MessageFactory();
    }

    public static MessageFactory getInstance() {
        return MessageFactoryInstance.INSTANCE;
    }

    public SimpleCam getCam(Car car) {

        return new SimpleCam(
                car.getStationId(),
                getGenerationDeltaTime(),
                (byte) 128,
                5,
                (int) (car.getLatitude() * 1e7),
                (int) (car.getLongitude() * 1e7),
                0,
                0,
                0,
                car.getHeight(),
                car.getDirection(),
                1,
                car.getSpeed(),
                1,
                car.getLength(),
                car.getWidth(),
                159,
                1,
                2,
                1,
                0
        );
    }

    public SimpleDenm getDenm(Warning warning) {
        return new SimpleDenm(
                warning.getStationId(),
                getGenerationDeltaTime(),
                (byte) 160,
                (byte) 64,
                1,
                2,
                0,
                (int) (warning.getLatitude() * 1e7),
                (int) (warning.getLongitude() * 1e7),
                warning.getLength(),
                warning.getWidth(),
                2,
                3,
                0,
                0,
                0,
                1,
                5,
                (byte) 128,
                4,
                2,
                2,
                0,
                0,
                (byte) 8,
                0,
                0,
                5
        );
    }


    /**
     * 时间获取
     *
     * @return
     */
    private static int getGenerationDeltaTime() {
        Instant instant = Instant.now();
        long generationDeltaTime = (instant.getEpochSecond() * 1000 +
                instant.getNano() / 1000000) % 65536;
        return (int) generationDeltaTime;
    }


}
