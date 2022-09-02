package com.group13.service;

import com.group13.entity.Commodity;
import com.group13.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.group13.entity.vo.OrderQueryVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author group13
 * @since 2022-04-20
 */
public interface OrdersService extends IService<Orders> {

    /**
     * page order list by condition
     * @param current
     * @param limit
     * @param orderQueryVo
     * @return
     */
    Map<String, Object> pageByVo(long current, long limit, OrderQueryVo orderQueryVo);

    /**
     * 退款后重置商品数量
     * @param id
     */
    void refundAmount(String id);

    /**
     * get Commodity List By Id
     * @param id
     * @return
     */
    List<Commodity> getCommodityListById(String id);
}
