package com.group13.mapper;

import com.group13.entity.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author group13
 * @since 2022-03-30
 */
@Repository
public interface PostMapper extends BaseMapper<Post> {

    /**
     * 计算某天发表post数
     * @param day
     * @return
     */
    Integer countPost(@Param("day") String day);
}
