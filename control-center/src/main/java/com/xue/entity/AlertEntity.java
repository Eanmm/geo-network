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
    private int alertID;

    /**
     * 告警长度
     */
    @ApiModelProperty("告警长度")
    @TableField("ALERT_LENGTH ")
    private String alertLength;

    /**
     * 告警宽度
     */
    @ApiModelProperty("告警宽度")
    @TableField("ALERT_WIDTH")
    private String alertWidth;

    /**
     * 告警角度
     */
    @ApiModelProperty("告警角度")
    @TableField("ALERT_ANGLE")
    private String alertAngle;

    /**
     * 告警形状，0，1，2分别表示：圆形，矩形，椭圆形
     */
    @ApiModelProperty("告警形状")
    @TableField("ALERT_TYPE")
    private int alertTYPE;

    /**
     * 告警位置经度
     */
    @ApiModelProperty("告警位置经度")
    @TableField("ALERT_LNG")
    private String alertLng;

    /**
     * 告警位置纬度
     */
    @ApiModelProperty("告警位置纬度")
    @TableField("ALERT_LAT")
    private String alertLat;


}
