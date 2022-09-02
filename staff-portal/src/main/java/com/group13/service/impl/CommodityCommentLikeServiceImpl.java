package com.group13.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.group13.entity.CommodityComment;
import com.group13.entity.CommodityCommentLike;
import com.group13.mapper.CommodityCommentLikeMapper;
import com.group13.mapper.CommodityCommentMapper;
import com.group13.service.CommodityCommentLikeService;
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
 * @since 2022-03-24
 */
@Service
@Transactional
public class CommodityCommentLikeServiceImpl extends ServiceImpl<CommodityCommentLikeMapper, CommodityCommentLike> implements CommodityCommentLikeService {

    private CommodityCommentLikeMapper likeMapper;
    private CommodityCommentMapper commentMapper;

    @Autowired
    public CommodityCommentLikeServiceImpl(CommodityCommentLikeMapper likeMapper, CommodityCommentMapper commentMapper) {
        this.likeMapper = likeMapper;
        this.commentMapper = commentMapper;
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
        Page<CommodityCommentLike> page = new Page<>(current, limit);
        // condition
        QueryWrapper<CommodityCommentLike> wrapper = new QueryWrapper<>();
        wrapper.eq("comment_id", id);
        likeMapper.selectPage(page, wrapper);
        long total = page.getTotal();
        List<CommodityCommentLike> records = page.getRecords();
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
        CommodityCommentLike like = likeMapper.selectById(id);
        String commentId = like.getCommentId();
        CommodityComment commodityComment = commentMapper.selectById(commentId);
        Integer likeAmount = commodityComment.getLikeAmount();
        commodityComment.setLikeAmount(likeAmount - 1);
        commentMapper.updateById(commodityComment);
        int i = likeMapper.deleteById(id);
        return i > 0;
    }
}
