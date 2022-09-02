package com.group13.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xujinfengxu
 */
@Data
@Accessors(chain = true)
public class ChatDto {
    private String userId;
    private String staffId;
    private String content;
}
