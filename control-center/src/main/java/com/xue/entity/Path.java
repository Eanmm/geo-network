package com.xue.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @TableName path
 */
@TableName(value ="path")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Path implements Serializable {

    private Integer id;

    private Double longitude;

    private Double latitude;

    private Integer pathId;

    private static final long serialVersionUID = 1L;
}