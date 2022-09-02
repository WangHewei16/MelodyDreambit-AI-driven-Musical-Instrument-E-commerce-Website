package com.group13.mapper;

import com.group13.entity.Commodity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author group13
 * @since 2022-03-16
 */
@Repository
public interface CommodityMapper extends BaseMapper<Commodity> {
    /**
     * 计算某天访问量
     * @param day
     * @return
     */
    Integer countVisit(@Param("day") String day);

    /**
     * 计算某天购买量
     * @param day
     * @return
     */
    Integer countBuy(@Param("day") String day);

    /**
     * 通过类型获得截止当天的该种类商品浏览总量
     * @param type
     * @return
     */
    int countVisitByType(@Param("type") int type);

}
