package com.group13.service.impl;

import com.group13.entity.Address;
import com.group13.entity.Orders;
import com.group13.mapper.AddressMapper;
import com.group13.mapper.OrdersMapper;
import com.group13.service.AddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author group13
 * @since 2022-04-20
 */
@Service
@Transactional
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    private AddressMapper addressMapper;
    private OrdersMapper ordersMapper;

    @Autowired
    public AddressServiceImpl(AddressMapper addressMapper, OrdersMapper ordersMapper) {
        this.addressMapper = addressMapper;
        this.ordersMapper = ordersMapper;
    }

    /**
     * new Address
     * @param address
     * @return
     */
    @Override
    public void updateAndChange(Address address, String id) {
        Address address1 = addressMapper.selectById(address);
        address1.setAddress1(address.getAddress1());
        address1.setAddress2(address.getAddress2());
        String uuid = UUID.randomUUID().toString();
        address1.setId(uuid);
        System.out.println(address1);
        addressMapper.insert(address1);
        Orders orders = ordersMapper.selectById(id);
        orders.setAddressId(uuid);
        orders.setStatus(0);
        ordersMapper.updateById(orders);
    }
}
