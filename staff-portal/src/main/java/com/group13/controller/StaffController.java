package com.group13.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.group13.common.R;
import com.group13.entity.Staff;
import com.group13.service.StaffService;
import com.group13.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author group13
 * @since 2022-04-20
 */
@RestController
@RequestMapping("/api/staff")
@CrossOrigin
@Api(tags = "staff Controller")
public class StaffController {

    private StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }
    /**
     * 根据token获得用户信息
     * @return
     */
    @ApiOperation("根据token获得用户信息")
    @PostMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        // 解析token
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        // 根据用户id获取用户信息
        Staff member = staffService.getById(memberId);
        System.out.println(member);
        if (!ObjectUtils.isEmpty(member)){
            return R.ok().data("userInfo", member);
        }
        return R.error();
    }

    /**
     * 根据用户名登陆
     * @return
     */
    @ApiOperation("根据用户名登陆")
    @PostMapping("loginByUserName/{username}")
    public R loginByUserName(@PathVariable String username){
        QueryWrapper<Staff> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nickname", username);
        Staff staff = staffService.getOne(queryWrapper);
        if (ObjectUtils.isEmpty(staff)){
            return R.error();
        }
        String token = JwtUtils.getJwtToken(staff.getId(), staff.getNickname());
        return R.ok().data("token", token);
    }

}

