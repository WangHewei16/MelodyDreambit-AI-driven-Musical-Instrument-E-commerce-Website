package com.group13.handler.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xujinfengxu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group13Exception extends RuntimeException{

    private Integer code;
    private String msg;

}
