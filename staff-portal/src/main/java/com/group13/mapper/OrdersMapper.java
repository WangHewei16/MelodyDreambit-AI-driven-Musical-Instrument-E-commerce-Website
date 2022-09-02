package com.group13.mapper;

import com.group13.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.group13.entity.dto.RefundDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author group13
 * @since 2022-04-20
 */
@Repository
public interface OrdersMapper extends BaseMapper<Orders> {


    /**
     * 获取列表
     * @param id
     * @return
     */
    List<RefundDto> getCommodityDetail(@Param("id") String id);

    /**
     * 加余额
     * @param commodityId
     * @param amount
     */
    void changeAmount(@Param("commodityId") String commodityId, @Param("amount") String amount);
}
