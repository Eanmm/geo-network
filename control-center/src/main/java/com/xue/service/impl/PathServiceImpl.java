package com.xue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xue.entity.Path;
import com.xue.service.PathService;
import com.xue.mapper.PathMapper;
import org.springframework.stereotype.Service;

/**
* @author Xue
* @description 针对表【path】的数据库操作Service实现
* @createDate 2024-05-20 10:58:53
*/
@Service
public class PathServiceImpl extends ServiceImpl<PathMapper, Path>
    implements PathService{

}




