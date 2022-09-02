package com.group13.controller;


import com.group13.common.R;
import com.group13.entity.CommodityComment;
import com.group13.entity.User;
import com.group13.service.CommodityCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author group13
 * @since 2022-03-16
 */
@RestController
@RequestMapping("/api/commodity-comment")
@Api(tags = "commodity-comment controller")
@CrossOrigin
public class CommodityCommentController {

    private CommodityCommentService commentService;

    @Autowired
    public CommodityCommentController(CommodityCommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * getInfoList by commodity id
     * @param commodityId
     * @return
     */
    @ApiOperation("getInfoList by commodity id")
    @PostMapping("getInfoList/{commodityId}/{current}/{limit}")
    public R getInfoList(@PathVariable("commodityId") String commodityId,
                         @PathVariable("current") long current,
                         @PathVariable("limit") long limit){
        Map<String, Object> map = commentService.getInfoList(current, limit, commodityId);
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
        CommodityComment commodityComment = commentService.getById(id);
        return R.ok().data("detail", commodityComment);
    }

    /**
     * edit comment content
     * @param detail
     * @return
     */
    @ApiOperation("edit comment content")
    @PostMapping("editComment")
    public R editComment(@RequestBody CommodityComment detail){
        String id = detail.getId();
        CommodityComment comment = commentService.getById(id);
        comment.setContent(detail.getContent());
        commentService.updateById(comment);
        return R.ok();
    }
}

