package com.xue.arrangement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.xue.bean.VehicleAlerts;
import com.xue.frame.Warning;
import com.xue.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import net.gcdc.geonetworking.Position;

import java.io.File;
import java.util.List;

/**
 * @author Xue
 * @create 2024-04-28 16:06
 */
@Slf4j
public class WarningSettings {

    private WarningSettings() {
    }

    private static class WarningSettingsInstance {
        private static final WarningSettings INSTANCE = new WarningSettings();
    }

    public static WarningSettings getInstance() {
        return WarningSettingsInstance.INSTANCE;
    }

    private List<Warning> warnings;

    public void loadFileSettings() {
        String path = System.getProperty("user.dir");
        File file = new File(path, "warning.json");
        if (!file.exists()) {
            throw new RuntimeException("warning.json file not found");
        }
        warnings = JsonUtil.parseArray(file, new TypeReference<List<Warning>>() {
        });
        Integer stationId = Config.getInstance().getStationId();
        for (Warning warning : warnings) {
            warning.setStationId(stationId);
        }
    }

    /**
     * 计算出最近的警告信息
     *
     * @param vehicleAlerts
     */
    public Warning getWarningByNear(VehicleAlerts vehicleAlerts) {
        Position position = new Position(vehicleAlerts.getLatitude(), vehicleAlerts.getLongitude());
        double mix = position.distanceInMetersTo(new Position(warnings.get(0).getLatitude(), warnings.get(0).getLongitude()));
        int index = 0;
        for (int i = 1; i < warnings.size(); i++) {
            Warning warning = warnings.get(i);
            double distance = position.distanceInMetersTo(new Position(warning.getLatitude(), warning.getLongitude()));
            if (distance < mix) {
                mix = distance;
                index = i;
            }
        }
        return warnings.get(index);
    }

}
