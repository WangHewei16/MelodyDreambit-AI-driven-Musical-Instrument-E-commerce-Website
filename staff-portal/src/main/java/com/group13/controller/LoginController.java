package com.group13.controller;

import com.group13.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @author xujinfengxu
 */
@RestController
@RequestMapping("/api/admin/user")
@Api(tags = "admin login controller")
@CrossOrigin
public class LoginController {
    /**
     * login
     */
    @ApiOperation("login")
    @PostMapping("login")
    public R login(){
        return R.ok().data("token","admin");
    }

    /**
     * info
     */
    @ApiOperation("getInfo")
    @PostMapping("info")
    public R info(){
        return R.ok().data("roles","admin")
                .data("name","admin")
                .data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }
}
