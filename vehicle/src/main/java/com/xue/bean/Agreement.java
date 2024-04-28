package com.xue.bean;

import com.fasterxml.jackson.databind.JsonNode;
import com.xue.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xue
 * @create 2024-04-28 10:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Agreement {

    private Integer type;

    private static final Map<Integer, Class<? extends Agreement>> map = new HashMap<Integer, Class<? extends Agreement>>() {{
        put(1, VehicleStatus.class);
        put(2, VehicleAlerts.class);
    }};

    public static Agreement decode(String message) {
        JsonNode jsonNode = JsonUtil.parseJSONObject(message);
        Class<? extends Agreement> aClass = map.get(Integer.valueOf(jsonNode.get("type").textValue()));
        return JsonUtil.parseObject(message, aClass);
    }

}
