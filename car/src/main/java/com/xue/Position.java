package com.xue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xuejingbao
 * @create 2023-05-24 13:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Position {

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

}
