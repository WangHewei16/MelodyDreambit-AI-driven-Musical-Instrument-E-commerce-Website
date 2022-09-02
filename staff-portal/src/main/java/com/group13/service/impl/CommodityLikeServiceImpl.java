package com.group13.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.group13.entity.Commodity;
import com.group13.entity.CommodityComment;
import com.group13.entity.CommodityCommentLike;
import com.group13.entity.CommodityLike;
import com.group13.mapper.CommodityCommentLikeMapper;
import com.group13.mapper.CommodityCommentMapper;
import com.group13.mapper.CommodityLikeMapper;
import com.group13.mapper.CommodityMapper;
import com.group13.service.CommodityLikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author group13
 * @since 2022-04-07
 */
@Service
@Transactional
public class CommodityLikeServiceImpl extends ServiceImpl<CommodityLikeMapper, CommodityLike> implements CommodityLikeService {

    private CommodityLikeMapper likeMapper;
    private CommodityMapper commodityMapper;

    @Autowired
    public CommodityLikeServiceImpl(CommodityLikeMapper likeMapper, CommodityMapper commodityMapper) {
        this.likeMapper = likeMapper;
        this.commodityMapper = commodityMapper;
    }

    /**
     * get Like List by id
     * @param id
     * @param current
     * @param limit
     * @return
     */
    @Override
    public Map<String, Object> getInfoList(long current, long limit, String id) {
        Page<CommodityLike> page = new Page<>(current, limit);
        // condition
        QueryWrapper<CommodityLike> wrapper = new QueryWrapper<>();
        wrapper.eq("commodity_id", id);
        likeMapper.selectPage(page, wrapper);
        long total = page.getTotal();
        List<CommodityLike> records = page.getRecords();
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("rows", records);
        return map;
    }


    /**
     * delete Like By Id
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(String id) {
        CommodityLike like = likeMapper.selectById(id);
        String commodityId = like.getCommodityId();
        Commodity commodity = commodityMapper.selectById(commodityId);
        Integer likeAmount = commodity.getLikeAmount();
        commodity.setLikeAmount(likeAmount - 1);
        commodityMapper.updateById(commodity);
        int i = likeMapper.deleteById(id);
        return i > 0;
    }
}
