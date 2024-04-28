package com.xue;

import com.xue.frame.Car;
import com.xue.frame.Warning;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Xue
 * @create 2024-04-28 15:43
 */
public class Region {

    private Region() {
    }

    private static class RegionInstance {
        private static final Region INSTANCE = new Region();
    }

    public static Region getInstance() {
        return RegionInstance.INSTANCE;
    }

    private final CopyOnWriteArrayList<Car> cars = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Warning> warnings = new CopyOnWriteArrayList<>();


}
