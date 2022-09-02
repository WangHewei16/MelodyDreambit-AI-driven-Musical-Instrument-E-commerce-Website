package com.group13.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.group13.entity.CommodityComment;
import com.group13.entity.CommodityCommentLike;
import com.group13.mapper.CommodityCommentLikeMapper;
import com.group13.mapper.CommodityCommentMapper;
import com.group13.service.CommodityCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author group13
 * @since 2022-03-16
 */
@Service
@Transactional
public class CommodityCommentServiceImpl extends ServiceImpl<CommodityCommentMapper, CommodityComment> implements CommodityCommentService {

    private CommodityCommentMapper commentMapper;
    private CommodityCommentLikeMapper commentLikeMapper;

    @Autowired
    public CommodityCommentServiceImpl(CommodityCommentMapper commentMapper, CommodityCommentLikeMapper commentLikeMapper) {
        this.commentMapper = commentMapper;
        this.commentLikeMapper = commentLikeMapper;
    }

    /**
     * getInfoList by commodity id
     *
     * @param current
     * @param limit
     * @param commodityId
     * @return
     */
    @Override
    public Map<String, Object> getInfoList(long current, long limit, String commodityId) {
        Page<CommodityComment> page = new Page<>(current, limit);
        // condition
        QueryWrapper<CommodityComment> wrapper = new QueryWrapper<>();
        wrapper.eq("commodity_id", commodityId);
        commentMapper.selectPage(page, wrapper);
        long total = page.getTotal();
        List<CommodityComment> records = page.getRecords();
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("rows", records);
        return map;
    }

    /**
     * remove comment by id
     * @param id
     * @return
     */
    @Override
    public boolean removeCommentById(String id) {
        QueryWrapper<CommodityCommentLike> wrapper = new QueryWrapper<>();
        wrapper.eq("comment_id", id);
        commentLikeMapper.delete(wrapper);
        int flag = commentMapper.deleteById(id);
        return flag > 0;
    }

    /**
     * 根据商品id删除对应评论和评论对应点赞信息
     * @param id
     * @return
     */
    @Override
    public boolean removeCommentByCommodityId(String id) {
        QueryWrapper<CommodityComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("commodity_id", id);
        List<CommodityComment> commodityComments = commentMapper.selectList(queryWrapper);
        QueryWrapper<CommodityCommentLike> wrapper;
        for (CommodityComment comment : commodityComments) {
            wrapper = new QueryWrapper<>();
            wrapper.eq("comment_id", comment.getId());
            commentLikeMapper.delete(wrapper);
        }
        commentMapper.delete(queryWrapper);
        return true;
    }
}
