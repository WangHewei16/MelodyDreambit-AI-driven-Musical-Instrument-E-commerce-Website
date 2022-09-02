package com.group13.controller;


import com.group13.common.R;
import com.group13.entity.Post;
import com.group13.entity.vo.CommodityQueryVo;
import com.group13.entity.vo.PostQueryVo;
import com.group13.service.PostService;
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
@CrossOrigin
@Api(tags = "post management api")
@RequestMapping("/api/post")
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * get commodities list by condition
     * @param current
     * @param limit
     * @param postQueryVo
     * @return
     */
    @ApiOperation("page posts list by condition")
    @PostMapping("pagePostListCondition/{current}/{limit}")
    public R pagePostListCondition(@PathVariable("current") long current,
                                        @PathVariable("limit") long limit,
                                        @RequestBody(required = false) PostQueryVo postQueryVo) {
        Map<String, Object> map = postService.pageByVo(current, limit, postQueryVo);
        return R.ok().data(map);
    }

    /**
     * delete post by id
     * @param id
     * @return
     */
    @ApiOperation("delete post by id")
    @PostMapping("deletePost/{id}")
    public R deletePost(@PathVariable String id){
        boolean b = postService.removePostById(id);
        if (b){
            return R.ok();
        }
        return R.error();
    }


    /**
     * update post
     * @param post
     * @return
     */
    @ApiOperation("update post")
    @PostMapping("saveInfo")
    public R saveInfo(@RequestBody Post post){
        String id = post.getId();
        Post coPost = postService.getById(id);
        coPost.setContent(post.getContent());
        coPost.setTitle(post.getTitle());
        postService.updateById(coPost);
        return R.ok();
    }

    /**
     * get post by id
     * @param id
     * @return
     */
    @ApiOperation("get post by id")
    @PostMapping("getInfo/{id}")
    public R getInfo(@PathVariable String id){
        Post post = postService.getById(id);
        return R.ok().data("info", post);
    }
}

