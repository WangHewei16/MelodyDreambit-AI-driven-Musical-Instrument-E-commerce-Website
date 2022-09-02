package com.group13.controller;

import com.group13.common.R;
import com.group13.entity.User;
import com.group13.entity.vo.UserQueryVo;
import com.group13.service.UserService;
import com.group13.utils.MD5;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author group13
 * @since 2022-03-10
 */
@RestController
@CrossOrigin
@Api(tags = "users management api")
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取所有讲师列表
     * @return
     */
    @ApiOperation("get all users list")
    @PostMapping("getUsersList")
    public R getUsersList(){
        List<User> list = userService.list(null);
        return R.ok().data("list", list);
    }

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    @ApiOperation("delete user by id")
    @PostMapping("deleteUser/{id}")
    public R deleteUser(@PathVariable String id){
        boolean b = userService.removeById(id);
        if (b){
            return R.ok();
        }
        return R.error();
    }

    /**
     * page user list by condition
     * @param current
     * @param limit
     * @param userQueryVo
     * @return
     */
    @ApiOperation("page user list by condition")
    @PostMapping("pageUserListCondition/{current}/{limit}")
    public R pageListUser(@PathVariable("current") long current,
                          @PathVariable("limit") long limit,
                          @RequestBody(required = false) UserQueryVo userQueryVo) {
        Map<String, Object> map = userService.pageByVo(current, limit, userQueryVo);
        return R.ok().data(map);
    }

    /**
     * add user
     * @param user
     * @return
     */
    @ApiOperation("add user")
    @PostMapping("addUser")
    public R addUser(@RequestBody User user){
        String password = user.getPassword();
        String md5pwd = MD5.encrypt(password);
        user.setPassword(md5pwd);
        boolean save = userService.save(user);
        if (save){
            return R.ok();
        }
        return R.error();
    }

    /**
     * get user by id
     * @param id
     * @return
     */
    @ApiOperation("get user by id")
    @PostMapping("getUser/{id}")
    public R getUser(@PathVariable String id){
        User user = userService.getById(id);
        return R.ok().data("user", user);
    }

    /**
     * update user
     * @param user
     * @return
     */
    @ApiOperation("update user")
    @PostMapping("updateUser")
    public R updateUser(@RequestBody User user){
        User byId = userService.getById(user.getId());
        if (!byId.getPassword().equals(user.getPassword())){
            String password = user.getPassword();
            user.setPassword(MD5.encrypt(password));
        }
        boolean b = userService.updateById(user);
        if (b){
            return R.ok();
        }
        return R.error();
    }

}

