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
    private Integer stationId;

    /**
     * 警告范围长
     */
    @ApiModelProperty("警告范围长,单位cm")
    @TableField("ALERT_LENGTH ")
    private Integer length;

    /**
     * 警告范围宽
     */
    @ApiModelProperty("警告范围宽，单位cm")
    @TableField("ALERT_WIDTH")
    private Integer width;

    /**
     * 警告形状，0，1，2分别表示：圆形，矩形，椭圆形
     */
    @ApiModelProperty("警告形状")
    @TableField("ALERT_TYPE")
    private Integer type;

    /**
     * 警告中心经度信息
     */
    @ApiModelProperty("警告中心经度信息")
    @TableField("ALERT_LNG")
    private Double longitude;

    /**
     * 警告中心纬度信息
     */
    @ApiModelProperty("警告中心纬度信息")
    @TableField("ALERT_LAT")
    private Double latitude;

    /**
     * 是否展示告警
     */
    @ApiModelProperty("是否展示告警")
    @TableField("SHOW_FLAG")
    private Boolean showFlag;
}
