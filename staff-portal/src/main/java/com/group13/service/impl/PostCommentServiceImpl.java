package com.group13.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.group13.entity.CommodityComment;
import com.group13.entity.CommodityCommentLike;
import com.group13.entity.PostComment;
import com.group13.entity.PostCommentLike;
import com.group13.mapper.PostCommentLikeMapper;
import com.group13.mapper.PostCommentMapper;
import com.group13.service.PostCommentService;
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
 * @since 2022-03-30
 */
@Service
@Transactional
public class PostCommentServiceImpl extends ServiceImpl<PostCommentMapper, PostComment> implements PostCommentService {

    private PostCommentMapper postCommentMapper;
    private PostCommentLikeMapper postCommentLikeMapper;

    @Autowired
    public PostCommentServiceImpl(PostCommentMapper postCommentMapper, PostCommentLikeMapper postCommentLikeMapper) {
        this.postCommentMapper = postCommentMapper;
        this.postCommentLikeMapper = postCommentLikeMapper;
    }

    /**
     * 根据买家秀id删除评论
     * @param id
     */
    @Override
    public void removeCommentByPostId(String id) {
        QueryWrapper<PostComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", id);
        List<PostComment> postCommentList = postCommentMapper.selectList(queryWrapper);
        QueryWrapper<PostCommentLike> wrapper;
        for (PostComment comment : postCommentList) {
            wrapper = new QueryWrapper<>();
            wrapper.eq("post_comment_id", comment.getId());
            postCommentLikeMapper.delete(wrapper);
        }
        postCommentMapper.delete(queryWrapper);
    }

    /**
     * getInfoList by post id
     *
     * @param current
     * @param limit
     * @param postId
     * @return
     */
    @Override
    public Map<String, Object> getInfoList(long current, long limit, String postId) {
        Page<PostComment> page = new Page<>(current, limit);
        // condition
        QueryWrapper<PostComment> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id", postId);
        postCommentMapper.selectPage(page, wrapper);
        long total = page.getTotal();
        List<PostComment> records = page.getRecords();
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
        QueryWrapper<PostCommentLike> wrapper = new QueryWrapper<>();
        wrapper.eq("post_comment_id", id);
        postCommentLikeMapper.delete(wrapper);
        int flag = postCommentMapper.deleteById(id);
        return flag > 0;
    }
}
