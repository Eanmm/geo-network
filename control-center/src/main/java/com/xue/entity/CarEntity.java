package com.xue.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("CAR")
public class CarEntity {
    /**
     * 主键
     */
    @ApiModelProperty("车辆ID")
    @TableId("CAR_ID")
    private int stationId;

    /**
     * 车辆名称
     */
    @ApiModelProperty("车辆名称")
    @TableField("CAR_NAME")
    private String carName;

    /**
     * 车辆长度
     */
    @ApiModelProperty("车辆长度,单位cm")
    @TableField("CAR_LENGTH ")
    private int length;

    /**
     * 车辆宽度
     */
    @ApiModelProperty("车辆宽度,单位cm")
    @TableField("CAR_WIDTH")
    private int width;

    /**
     * 车辆颜色
     */
    @ApiModelProperty("车辆颜色")
    @TableField("CAR_COLOR")
    private String carColor;

    /**
     * 车辆方向
     */
    @ApiModelProperty("车辆方向")
    @TableField("CAR_DIRECTION")
    private int direction;

    /**
     * 车辆位经度
     */
    @ApiModelProperty("车辆位置经度")
    @TableField("CAR_LNG")
    private double longitude;

    /**
     * 车辆位置纬度
     */
    @ApiModelProperty("车辆位置纬度")
    @TableField("CAR_LAT")
    private double latitude;

}