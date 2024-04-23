package com.xue;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author xuejingbao
 * @create 2023-09-25 9:56
 */
public enum GpsPosition {

    INSTANCE;

    private final AtomicReference<Position> positionAtr = new AtomicReference<>();

    public void setPosition(Position position) {
        positionAtr.set(position);
    }

    public Position getPosition() {
        return positionAtr.get();
    }

}
