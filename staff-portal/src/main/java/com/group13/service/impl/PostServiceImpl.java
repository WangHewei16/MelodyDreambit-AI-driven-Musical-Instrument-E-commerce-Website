package com.group13.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.group13.entity.Post;
import com.group13.entity.vo.PostQueryVo;
import com.group13.mapper.PostMapper;
import com.group13.service.PostCommentService;
import com.group13.service.PostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    private PostMapper postMapper;
    private PostCommentService postCommentService;

    @Autowired
    public PostServiceImpl(PostMapper postMapper, PostCommentService postCommentService) {
        this.postMapper = postMapper;
        this.postCommentService = postCommentService;
    }

    /**
     * get commodities list by condition
     * @param current
     * @param limit
     * @param postQueryVo
     * @return
     */
    @Override
    public Map<String, Object> pageByVo(long current, long limit, PostQueryVo postQueryVo) {
        Page<Post> page = new Page<>(current, limit);
        QueryWrapper<Post> wrapper = new QueryWrapper<>();
        String begin = postQueryVo.getBegin();
        String end = postQueryVo.getEnd();
        String commodityId = postQueryVo.getCommodityId();
        String userId = postQueryVo.getUserId();
        String title = postQueryVo.getTitle();
        if (!StringUtils.isEmpty(title)){
            wrapper.like("title", title);
        }
        if (!StringUtils.isEmpty(commodityId)){
            wrapper.eq("commodity_id", commodityId);
        }
        if (!StringUtils.isEmpty(userId)){
            wrapper.eq("user_id", userId);
        }
        if (!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)){
            wrapper.le("gmt_create", end);
        }
        wrapper.orderByDesc("gmt_create");
        postMapper.selectPage(page, wrapper);
        long total = page.getTotal();
        List<Post> records = page.getRecords();
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("rows", records);
        return map;
    }

    /**
     * remove post by id
     * @param id
     * @return
     */
    @Override
    public boolean removePostById(String id) {
        postCommentService.removeCommentByPostId(id);
        int flag = postMapper.deleteById(id);
        return flag > 0;
    }

    /**
     * 计算某天post数量
     * @param day
     * @return
     */
    @Override
    public Integer countPost(String day) {
        return postMapper.countPost(day);
    }
}
