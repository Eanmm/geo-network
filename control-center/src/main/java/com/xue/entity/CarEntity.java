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
    private Integer stationId;

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
    private Integer length;

    /**
     * 车辆宽度
     */
    @ApiModelProperty("车辆宽度,单位cm")
    @TableField("CAR_WIDTH")
    private Integer width;

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
    private Integer direction;

    /**
     * 车辆位置经度
     */
    @ApiModelProperty("车辆位置经度")
    @TableField("CAR_LNG")
    private Double longitude;

    /**
     * 车辆位置纬度
     */
    @ApiModelProperty("车辆位置纬度")
    @TableField("CAR_LAT")
    private Double latitude;

    /**
     * 是否虚拟车辆
     */
    @ApiModelProperty("是否虚拟车辆")
    @TableField("IS_VIRTUAL_CAR")
    private Boolean virtualCar;

}