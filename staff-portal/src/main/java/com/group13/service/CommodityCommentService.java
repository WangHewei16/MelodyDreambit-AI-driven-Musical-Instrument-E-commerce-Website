package com.group13.service;

import com.group13.entity.CommodityComment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author group13
 * @since 2022-03-16
 */
public interface CommodityCommentService extends IService<CommodityComment> {

    /**
     * getInfoList by commodity id
     *
     * @param current
     * @param limit
     * @param commodityId
     * @return
     */
    Map<String, Object> getInfoList(long current, long limit, String commodityId);

    /**
     * remove comment by id
     * @param id
     * @return
     */
    boolean removeCommentById(String id);

    /**
     * remove comment by Commodity id
     * @param id
     * @return
     */
    boolean removeCommentByCommodityId(String id);
}
