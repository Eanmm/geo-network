package com.xue.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("ALERT")
public class AlertEntity {
    /**
     * 主键
     */
    @ApiModelProperty("告警ID")
    @TableId("ALERT_ID")
    private int stationId;

    /**
     * 警告范围长
     */
    @ApiModelProperty("警告范围长,单位cm")
    @TableField("ALERT_LENGTH ")
    private int length;

    /**
     * 警告范围宽
     */
    @ApiModelProperty("警告范围宽，单位cm")
    @TableField("ALERT_WIDTH")
    private int width;

    /**
     * 警告形状，0，1，2分别表示：圆形，矩形，椭圆形
     */
    @ApiModelProperty("警告形状")
    @TableField("ALERT_TYPE")
    private int type;

    /**
     * 警告中心经度信息
     */
    @ApiModelProperty("警告中心经度信息")
    @TableField("ALERT_LNG")
    private double longitude;

    /**
     * 警告中心纬度信息
     */
    @ApiModelProperty("警告中心纬度信息")
    @TableField("ALERT_LAT")
    private double latitude;


}
