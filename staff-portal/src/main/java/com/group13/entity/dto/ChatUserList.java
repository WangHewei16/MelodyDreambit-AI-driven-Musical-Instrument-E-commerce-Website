package com.group13.entity.dto;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xujinfengxu
 */
@Data
@Accessors(chain = true)
public class ChatUserList {

    private String userId;
    private String username;
    private String gmtCreate;

}
