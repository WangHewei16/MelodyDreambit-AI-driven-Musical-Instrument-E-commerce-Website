package com.group13.service;

import com.group13.entity.Post;
import com.baomidou.mybatisplus.extension.service.IService;
import com.group13.entity.vo.PostQueryVo;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author group13
 * @since 2022-03-30
 */
public interface PostService extends IService<Post> {

    /**
     * get commodities list by condition
     * @param current
     * @param limit
     * @param postQueryVo
     * @return
     */
    Map<String, Object> pageByVo(long current, long limit, PostQueryVo postQueryVo);

    /**
     * remove post by id
     * @param id
     * @return
     */
    boolean removePostById(String id);

    /**
     * 计算某天post数量
     * @param day
     * @return
     */
    Integer countPost(String day);
}
