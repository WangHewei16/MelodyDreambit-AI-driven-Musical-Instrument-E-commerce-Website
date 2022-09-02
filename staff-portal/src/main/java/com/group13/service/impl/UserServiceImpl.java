package com.group13.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.group13.entity.User;
import com.group13.entity.vo.UserQueryVo;
import com.group13.mapper.UserMapper;
import com.group13.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author group13
 * @since 2022-03-10
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * pageByVo
     * @param current
     * @param limit
     * @param userQueryVo
     * @return
     */
    @Override
    public Map<String, Object> pageByVo(long current, long limit, UserQueryVo userQueryVo) {
        Page<User> page = new Page<>(current, limit);
        // condition
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // dynamic sql
        String username = userQueryVo.getUsername();
        Boolean isActive = userQueryVo.getIsActive();
        String begin = userQueryVo.getBegin();
        String end = userQueryVo.getEnd();

        if (!StringUtils.isEmpty(username)){
            wrapper.like("username", username);
        }
        if (!StringUtils.isEmpty(isActive)){
            wrapper.eq("is_active", isActive);
        }
        if (!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)){
            wrapper.le("gmt_create", end);
        }

        // order by created tie desc
        wrapper.orderByDesc("gmt_create");
        userMapper.selectPage(page, wrapper);
        long total = page.getTotal();
        List<User> records = page.getRecords();
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("rows", records);
        return map;
    }

    /**
     * 计算某天注册人数
     * @param day
     * @return
     */
    @Override
    public Integer countRegister(String day) {
        return userMapper.countRegister(day);
    }

}
