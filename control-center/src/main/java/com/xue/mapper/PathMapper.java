package com.xue.mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xue.entity.Path;

import java.util.List;

/**
 * @author Xue
 * @description 针对表【path】的数据库操作Mapper
 * @createDate 2024-05-20 10:58:53
 * @Entity com.xue.entity.Path
 */
public interface PathMapper extends BaseMapper<Path> {

    List<Integer> selectId();

    Path selectOneByPathIdOrderById(@Param("pathId") Integer pathId);

}




