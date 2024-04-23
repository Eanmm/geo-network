package com.xue;


import java.time.Instant;

/**
 * @author xuejingbao
 * @create 2023-05-23 13:59
 */
public class MessageFactory {

    // TODO: 2023/5/29 经纬度长度最长是9位，且最高位要小于9

    public static SimpleCam getCam(Position position) {

        return new SimpleCam(
                Config.INSTANCE.getStationId(),
                getGenerationDeltaTime(),
                (byte) 128,
                5,
                (int) (position.getLatitude() * 1e7),
                (int) (position.getLongitude() * 1e7),
                0,
                0,
                0,
                400,
                0,
                1,
                0,
                1,
                40,
                20,
                159,
                1,
                2,
                1,
                0
        );
    }

    public static SimpleDenm getDenm(Position position) {
        return new SimpleDenm(
                Config.INSTANCE.getStationId(),
                getGenerationDeltaTime(),
                (byte) 160,
                (byte) 64,
                1,
                2,
                0,
                (int) (position.getLatitude() * 1e7),
                (int) (position.getLongitude() * 1e7),
                Config.INSTANCE.getRadius(),
                0,
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

    public static int convertDoubleToInt(double input) {
        int scaleFactor = (int) 1e7;
        double scaledValue = input * scaleFactor;
        long roundedValue = Math.round(scaledValue);
        String resultString = String.valueOf(roundedValue);
        if (resultString.length() <= 9) {
            return (int) roundedValue;
        } else {
            String truncatedResult = resultString.substring(0, 9);
            return Integer.parseInt(truncatedResult);
        }
    }


}
