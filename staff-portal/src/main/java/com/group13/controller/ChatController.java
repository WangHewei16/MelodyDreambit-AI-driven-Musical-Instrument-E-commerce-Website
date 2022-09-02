package com.group13.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.group13.common.R;
import com.group13.entity.Chat;
import com.group13.entity.User;
import com.group13.entity.dto.ChatDto;
import com.group13.entity.dto.ChatUserList;
import com.group13.service.ChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author group13
 * @since 2022-04-21
 */
@RestController
@RequestMapping("/api/chat")
@CrossOrigin
@Api(tags = "Char Controller")
public class ChatController {

    private ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * get chat user list by staff nickname
     * @param staffId
     * @return
     */
    @ApiOperation("get chat user list by staff nickname")
    @PostMapping("getChatList/{staffId}")
    public R getChatList(@PathVariable String staffId){
        List<ChatUserList> userList = chatService.getUserList(staffId);
        return R.ok().data("userList", userList);
    }

    /**
     * get chat info between user and staff
     * @param staffId
     * @return
     */
    @ApiOperation("get chat info between user and staff")
    @PostMapping("getChatInfo/{staffId}/{userId}")
    public R getChatInfo(@PathVariable String staffId, @PathVariable String userId){
        List<Chat> list = chatService.list7Day(staffId, userId);
        return R.ok().data("list", list);
    }

    /**
     * send Chat between user and staff
     * @param
     * @return
     */
    @ApiOperation("send Chat between user and staff")
    @PostMapping("sendChat")
    public R sendChat(@RequestBody ChatDto chatDto){
        Chat chat = new Chat();
        System.out.println(chatDto);
        chat.setContent(chatDto.getContent());
        chat.setStaffId(chatDto.getStaffId());
        chat.setUserId(chatDto.getUserId());
        chat.setStatus("1");
        chatService.save(chat);
        return R.ok();
    }
}

