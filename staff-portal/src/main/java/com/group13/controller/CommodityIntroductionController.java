package com.group13.controller;


import com.group13.common.R;
import com.group13.entity.CommodityIntroduction;
import com.group13.service.CommodityIntroductionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author group13
 * @since 2022-03-16
 */
@RestController
@RequestMapping("/api/commodity-introduction")
@Api(tags = "Commodity Introduction Controller")
@CrossOrigin
public class CommodityIntroductionController {

    private CommodityIntroductionService introductionService;

    @Autowired
    public CommodityIntroductionController(CommodityIntroductionService introductionService) {
        this.introductionService = introductionService;
    }

    /**
     * add introduction
     * @param intro
     * @return
     */
    @ApiOperation("add Introduction")
    @PostMapping("addIntro")
    public R addIntro(@RequestBody CommodityIntroduction intro){
        introductionService.updateById(intro);
        return R.ok();
    }

    /**
     * get introduction
     * @param id
     * @return
     */
    @ApiOperation("get Introduction")
    @PostMapping("getIntro/{id}")
    public R getIntro(@PathVariable String id){
        CommodityIntroduction commodityIntroduction = introductionService.getById(id);
        return R.ok().data("intro", commodityIntroduction);
    }
}

