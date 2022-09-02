package com.group13.service;

import com.group13.entity.Commodity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.group13.entity.dto.CommodityBasicInfoDto;
import com.group13.entity.vo.CommodityQueryVo;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author group13
 * @since 2022-03-16
 */
public interface CommodityService extends IService<Commodity> {

    /**
     * get commodities list by condition
     * @param current
     * @param limit
     * @param commodityQueryVo
     * @return
     */
    Map<String, Object> pageByVo(long current, long limit, CommodityQueryVo commodityQueryVo);

    /**
     * delete commodity by id
     * @param id
     * @return
     */
    boolean removeCommodity(String id);

    /**
     * add commodity basic info
     * @param commodityBasicInfoDto
     * @return
     */
    String saveBasicInfo(CommodityBasicInfoDto commodityBasicInfoDto);

    /**
     * get commodity basic info by id
     * @param id
     * @return
     */
    CommodityBasicInfoDto getBasicInfo(String id);

    /**
     * update commodity basic info
     * @param commodityBasicInfoDto
     * @param id
     * @return
     */
    void updateBasicInfo(CommodityBasicInfoDto commodityBasicInfoDto, String id);

    /**
     * 计算某天访问量
     * @param day
     * @return
     */
    Integer countVisit(String day);

    /**
     * 计算某天购买量
     * @param day
     * @return
     */
    Integer countBuy(String day);

    /**
     * 通过类型获得截止当天的该种类商品浏览总量
     * @param i
     * @return
     */
    int getVisitedByType(int i);

}
