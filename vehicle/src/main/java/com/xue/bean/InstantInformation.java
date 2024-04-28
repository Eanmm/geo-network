package com.xue.bean;

import com.xue.frame.Car;
import com.xue.frame.Warning;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Xue
 * @create 2024-04-28 10:20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InstantInformation extends Agreement {

    private List<Car> cars;

    private List<Warning> warnings;

    public InstantInformation(List<Car> cars, List<Warning> warnings) {
        super(4);
        this.cars = cars;
        this.warnings = warnings;
    }

}
