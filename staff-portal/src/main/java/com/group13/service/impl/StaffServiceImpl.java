package com.group13.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.group13.entity.Staff;
import com.group13.mapper.StaffMapper;
import com.group13.service.StaffService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.group13.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sun.security.provider.MD5;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author group13
 * @since 2022-04-20
 */
@Service
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements StaffService {

    private StaffMapper staffMapper;

    @Autowired
    public StaffServiceImpl(StaffMapper staffMapper) {
        this.staffMapper = staffMapper;
    }

    /**
     * 根绝openid查询
     * @param openid
     * @return
     */
    @Override
    public Staff getOpenIdMember(String openid) {
        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        Staff member = baseMapper.selectOne(wrapper);
        return member;
    }
}
