package com.group13.service;

import com.group13.entity.PostComment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author group13
 * @since 2022-03-30
 */
public interface PostCommentService extends IService<PostComment> {

    /**
     * 根据买家秀id删除评论
     * @param id
     */
    void removeCommentByPostId(String id);

    /**
     * getInfoList by post id
     *
     * @param current
     * @param limit
     * @param postId
     * @return
     */
    Map<String, Object> getInfoList(long current, long limit, String postId);

    /**
     * remove comment by id
     * @param id
     * @return
     */
    boolean removeCommentById(String id);
}
