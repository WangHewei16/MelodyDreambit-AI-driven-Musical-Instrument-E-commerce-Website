package com.group13.service;

import com.group13.entity.Address;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author group13
 * @since 2022-04-20
 */
public interface AddressService extends IService<Address> {

    /**
     * new Address
     * @param address
     * @return
     */
    void updateAndChange(Address address, String id);
}
