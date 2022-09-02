package com.group13.controller;


import com.group13.common.R;
import com.group13.entity.CommodityComment;
import com.group13.entity.Post;
import com.group13.entity.PostComment;
import com.group13.service.CommodityCommentService;
import com.group13.service.PostCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author group13
 * @since 2022-03-30
 */
@RestController
@RequestMapping("/api/post-comment")
@Api(tags = "post-comment controller")
@CrossOrigin
public class PostCommentController {

    private PostCommentService commentService;

    @Autowired
    public PostCommentController(PostCommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * getInfoList by post id
     * @param postId
     * @return
     */
    @ApiOperation("getInfoList by post id")
    @PostMapping("getInfoList/{postId}/{current}/{limit}")
    public R getInfoList(@PathVariable("postId") String postId,
                         @PathVariable("current") long current,
                         @PathVariable("limit") long limit){
        Map<String, Object> map = commentService.getInfoList(current, limit, postId);
        return R.ok().data(map);
    }

    /**
     * delete by id
     * @param id
     * @return
     */
    @ApiOperation("delete by id")
    @PostMapping("delete/{id}")
    public R delete(@PathVariable("id") String id){
        boolean b = commentService.removeCommentById(id);
        if (b){
            return R.ok();
        }
        return R.error();
    }

    /**
     * getDetail by id
     * @param id
     * @return
     */
    @ApiOperation("getInfo by id")
    @PostMapping("getDetail/{id}")
    public R getInfoList(@PathVariable("id") String id){
        PostComment postComment = commentService.getById(id);
        return R.ok().data("detail", postComment);
    }

    /**
     * edit comment content
     * @param detail
     * @return
     */
    @ApiOperation("edit comment content")
    @PostMapping("editComment")
    public R editComment(@RequestBody PostComment detail){
        String id = detail.getId();
        PostComment comment = commentService.getById(id);
        comment.setContent(detail.getContent());
        commentService.updateById(comment);
        return R.ok();
    }
}

