package com.group13.service;

import com.group13.entity.Staff;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author group13
 * @since 2022-04-20
 */
public interface StaffService extends IService<Staff> {

    /**
     * 根绝openid查询
     * @param openid
     * @return
     */
    Staff getOpenIdMember(String openid);


}
