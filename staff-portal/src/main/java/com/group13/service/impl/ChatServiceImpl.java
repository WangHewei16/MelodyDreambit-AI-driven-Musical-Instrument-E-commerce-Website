package com.group13.service.impl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.group13.entity.Chat;
import com.group13.entity.User;
import com.group13.entity.dto.ChatUserList;
import com.group13.mapper.ChatMapper;
import com.group13.service.ChatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author group13
 * @since 2022-04-21
 */
@Service
@Transactional
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements ChatService {

    private ChatMapper chatMapper;

    @Autowired
    public ChatServiceImpl(ChatMapper chatMapper) {
        this.chatMapper = chatMapper;
    }

    /**
     * get user list by staff nickname
     * @param staffId
     * @return
     */
    @Override
    public List<ChatUserList> getUserList(String staffId) {
        return chatMapper.getUserList(staffId);
    }

    /**
     * get chat info between user and staff
     * @param staffId
     * @return
     */
    @Override
    public List<Chat> list7Day(String staffId, String userId) {
        List<Chat> chats = chatMapper.list7Day(staffId, userId);
        int right = chats.size() - 1, left = 0;
        while (left < right){
            Chat tmp = chats.get(left);
            chats.set(left++, chats.get(right));
            chats.set(right--, tmp);
        }
        return chats;
    }
}
