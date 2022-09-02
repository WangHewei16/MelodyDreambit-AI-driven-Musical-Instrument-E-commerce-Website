package com.group13.service;

import com.group13.entity.Chat;
import com.baomidou.mybatisplus.extension.service.IService;
import com.group13.entity.User;
import com.group13.entity.dto.ChatUserList;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author group13
 * @since 2022-04-21
 */
public interface ChatService extends IService<Chat> {

    /**
     * get user list by staff nickname
     * @param staffId
     * @return
     */
    List<ChatUserList> getUserList(String staffId);

    /**
     * get chat info between user and staff
     * @param staffId
     * @return
     */
    List<Chat> list7Day(String staffId, String userId);
}
