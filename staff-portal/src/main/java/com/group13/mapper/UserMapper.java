package com.group13.mapper;

import com.group13.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author group13
 * @since 2022-03-10
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 计算某天注册人数
     * @param day
     * @return
     */
    Integer countRegister(@Param("day") String day);
}
