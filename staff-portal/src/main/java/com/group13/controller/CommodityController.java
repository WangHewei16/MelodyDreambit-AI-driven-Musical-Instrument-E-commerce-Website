package com.group13.controller;


import com.group13.common.R;
import com.group13.entity.Commodity;
import com.group13.entity.dto.CommodityBasicInfoDto;
import com.group13.entity.vo.CommodityQueryVo;
import com.group13.entity.vo.UserQueryVo;
import com.group13.service.CommodityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/commodity")
@Api(tags = "Commodity Controller")
@CrossOrigin
public class CommodityController {

    private CommodityService commodityService;

    @Autowired
    public CommodityController(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    /**
     * 获取所有商品列表
     * @return
     */
    @ApiOperation("get all commodities list")
    @PostMapping("getCommodityList")
    public R getCommodityList(){
        List<Commodity> commodityList = commodityService.list(null);
        return R.ok().data("commodityList",commodityList);
    }

    /**
     * get commodities list by condition
     * @param current
     * @param limit
     * @param commodityQueryVo
     * @return
     */
    @ApiOperation("page commodities list by condition")
    @PostMapping("pageCommodityListCondition/{current}/{limit}")
    public R pageCommodityListCondition(@PathVariable("current") long current,
                          @PathVariable("limit") long limit,
                          @RequestBody(required = false) CommodityQueryVo commodityQueryVo) {
        Map<String, Object> map = commodityService.pageByVo(current, limit, commodityQueryVo);
        return R.ok().data(map);
    }

    /**
     * delete commodity by id
     * @param id
     * @return
     */
    @ApiOperation("delete commodity by id")
    @PostMapping("deleteCommodity/{id}")
    public R deleteCommodity(@PathVariable String id){
        boolean b = commodityService.removeCommodity(id);
        if (b){
            return R.ok();
        }
        return R.error();
    }

    /**
     * add commodity basic info
     * @param commodityBasicInfoDto
     * @return
     */
    @ApiOperation(value = "add commodity basic info")
    @PostMapping("addBasicInfo")
    public R addBasicInfo(@RequestBody CommodityBasicInfoDto commodityBasicInfoDto){
        String id = commodityService.saveBasicInfo(commodityBasicInfoDto);
        return R.ok().data("commodityId", id);
    }

    /**
     * update commodity basic info
     * @param commodityBasicInfoDto
     * @return
     */
    @ApiOperation(value = "update commodity basic info")
    @PostMapping("updateBasicInfo/{id}")
    public R updateBasicInfo(@RequestBody CommodityBasicInfoDto commodityBasicInfoDto,
                             @PathVariable String id){
        commodityService.updateBasicInfo(commodityBasicInfoDto, id);
        return R.ok();
    }

    /**
     * get commodity basic info by id
     * @param id
     * @return
     */
    @ApiOperation(value = "get Basic Info")
    @PostMapping("getBasicInfo/{id}")
    public R getBasicInfo(@PathVariable String id){
        CommodityBasicInfoDto commodityBasicInfoDto = commodityService.getBasicInfo(id);
        return R.ok().data("commodityBasicInfoDto",commodityBasicInfoDto);
    }

    /**
     * Change discount
     * @param id
     * @return
     */
    @ApiOperation(value = "Change discount")
    @PostMapping("changeDiscount/{id}")
    public R changeDiscount(@PathVariable String id){
        Commodity commodity = commodityService.getById(id);
        int discount = commodity.getDiscount();
        commodity.setDiscount(discount ^ 1);
        commodityService.updateById(commodity);
        return R.ok();
    }



}

