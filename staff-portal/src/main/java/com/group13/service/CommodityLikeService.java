package com.group13.service;

import com.group13.entity.CommodityLike;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author group13
 * @since 2022-04-07
 */
public interface CommodityLikeService extends IService<CommodityLike> {

    /**
     * get Like List by id
     * @param id
     * @param current
     * @param limit
     * @return
     */
    Map<String, Object> getInfoList(long current, long limit, String id);

    /**
     * delete Like By Id
     * @param id
     * @return
     */
    boolean deleteById(String id);

}
