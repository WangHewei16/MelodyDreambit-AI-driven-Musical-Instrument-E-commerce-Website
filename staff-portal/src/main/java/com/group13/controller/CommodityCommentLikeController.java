package com.group13.controller;


import com.group13.common.R;
import com.group13.service.CommodityCommentLikeService;
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
 * @since 2022-03-24
 */
@RestController
@Api(tags = "commodity comment like controller")
@RequestMapping("/api/commodity-comment-like")
@CrossOrigin
public class CommodityCommentLikeController {

    private CommodityCommentLikeService likeService;

    @Autowired
    public CommodityCommentLikeController(CommodityCommentLikeService likeService) {
        this.likeService = likeService;
    }

    /**
     * get Like List by id
     * @param id
     * @param current
     * @param limit
     * @return
     */
    @ApiOperation("get Like List by id")
    @PostMapping("getLikeList/{id}/{current}/{limit}")
    public R getLikeList(@PathVariable("id") String id,
                         @PathVariable("current") long current,
                         @PathVariable("limit") long limit){
        Map<String, Object> map = likeService.getInfoList(current, limit, id);
        return R.ok().data(map);
    }

    /**
     * delete Like By Id
     * @param id
     * @return
     */
    @ApiOperation("delete Like By Id")
    @PostMapping("deleteLikeById/{id}")
    public R deleteLikeById(@PathVariable("id") String id){
        boolean flag = likeService.deleteById(id);
        if (flag){
            return R.ok();
        }
        return R.error();
    }
}

