package com.group13.mapper;

import com.group13.entity.Chat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.group13.entity.User;
import com.group13.entity.dto.ChatUserList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author group13
 * @since 2022-04-21
 */
@Repository
public interface ChatMapper extends BaseMapper<Chat> {

    /**
     * get user list by staff nickname
     * @param staffId
     * @return
     */
    List<ChatUserList> getUserList(@Param("staffId") String staffId);

    /**
     * get chat info between user and staff
     * @param staffId
     * @return
     */
    List<Chat> list7Day(@Param("staffId") String staffId,@Param("userId") String userId);
}
