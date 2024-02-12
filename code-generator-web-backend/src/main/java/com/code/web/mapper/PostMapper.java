package com.code.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.code.web.model.entity.Post;

import java.util.Date;
import java.util.List;

/**
 * 帖子数据库操作
 *
 * @author <a href="https://github.com/quageral">陈廷墀</a>
 * @from <a href="http://quageral.work">芝士代码共享</a>
 */
public interface PostMapper extends BaseMapper<Post> {

    /**
     * 查询帖子列表（包括已被删除的数据）
     */
    List<Post> listPostWithDelete(Date minUpdateTime);

}




