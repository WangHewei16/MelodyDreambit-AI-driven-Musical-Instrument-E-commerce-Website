package com.group13.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.group13.entity.Cart;
import com.group13.entity.Commodity;
import com.group13.entity.Orders;
import com.group13.entity.dto.RefundDto;
import com.group13.entity.vo.OrderQueryVo;
import com.group13.mapper.CartMapper;
import com.group13.mapper.CommodityMapper;
import com.group13.mapper.OrdersMapper;
import com.group13.service.OrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    private OrdersMapper ordersMapper;
    private CartMapper cartMapper;
    private CommodityMapper commodityMapper;

    @Autowired
    public OrdersServiceImpl(OrdersMapper ordersMapper, CartMapper cartMapper, CommodityMapper commodityMapper) {
        this.ordersMapper = ordersMapper;
        this.cartMapper = cartMapper;
        this.commodityMapper = commodityMapper;
    }

    /**
     * page order list by condition
     * @param current
     * @param limit
     * @param orderQueryVo
     * @return
     */
    @Override
    public Map<String, Object> pageByVo(long current, long limit, OrderQueryVo orderQueryVo) {
        Page<Orders> page = new Page<>(current, limit);
        // condition
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        // 条件参数
        // dynamic sql
        String begin = orderQueryVo.getBegin();
        String end = orderQueryVo.getEnd();
        Integer method = orderQueryVo.getMethod();
        Integer status = orderQueryVo.getStatus();
        Integer flowstatus = orderQueryVo.getFlowstatus();
        if (!StringUtils.isEmpty(status)){
            wrapper.eq("status", status);
        }
        String userId = orderQueryVo.getUserId();

        if (!StringUtils.isEmpty(userId)){
            wrapper.eq("user_id", userId);
        }
        if (!StringUtils.isEmpty(method)){
            wrapper.eq("method", method);
        }
        if (!StringUtils.isEmpty(flowstatus)){
            wrapper.eq("flowstatus", flowstatus);
        }

        if (!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)){
            wrapper.le("gmt_create", end);
        }
        wrapper.orderByDesc("priority");
        // order by created tie desc
        wrapper.orderByDesc("gmt_create");

        ordersMapper.selectPage(page, wrapper);
        long total = page.getTotal();
        List<Orders> records = page.getRecords();
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("rows", records);
        return map;
    }

    /**
     * 退款后重置商品数量
     * @param id
     */
    @Override
    public void refundAmount(String id) {
        List<RefundDto> list = ordersMapper.getCommodityDetail(id);
        for (RefundDto tmp : list){
            System.out.println(tmp.getCommodityId() + " : " + tmp.getAmount());
            ordersMapper.changeAmount(tmp.getCommodityId(), tmp.getAmount());
        }
    }

    /**
     * get Commodity List By Id
     * @param id
     * @return
     */
    @Override
    public List<Commodity> getCommodityListById(String id) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", id);
        List<Cart> carts = cartMapper.selectList(queryWrapper);
        List<Commodity> list = new ArrayList<>();
        for (Cart cart : carts) {
            Commodity commodity = commodityMapper.selectById(cart.getCommodityId());
            list.add(commodity);
        }
        return list;
    }
}
