package com.group13.controller;


import com.group13.common.R;
import com.group13.entity.Commodity;
import com.group13.entity.Orders;
import com.group13.entity.vo.OrderQueryVo;
import com.group13.service.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.image.Kernel;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author group13
 * @since 2022-04-20
 */
@RestController
@Api(tags = "Orders Controller")
@RequestMapping("/api/order")
@CrossOrigin
public class OrdersController {

    private OrdersService ordersService;

    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    /**
     * page order list by condition
     * @param current
     * @param limit
     * @param orderQueryVo
     * @return
     */
    @ApiOperation("page order list by condition")
    @PostMapping("pageOrderListCondition/{current}/{limit}")
    public R pageListUser(@PathVariable("current") long current,
                          @PathVariable("limit") long limit,
                          @RequestBody(required = false) OrderQueryVo orderQueryVo) {
        Map<String, Object> map = ordersService.pageByVo(current, limit, orderQueryVo);
        return R.ok().data(map);
    }

    /**
     * 根据id删除Order
     * @param id
     * @return
     */
    @ApiOperation("delete order by id")
    @PostMapping("deleteOrder/{id}")
    public R deleteOrder(@PathVariable String id){
        Orders order = ordersService.getById(id);
        order.setStatus(3);
        order.setFlowstatus(4);
        ordersService.updateById(order);
        ordersService.refundAmount(id);
        return R.ok();
    }

    @ApiOperation("nextStep order by id")
    @PostMapping("nextStep/{id}")
    public R nextStep(@PathVariable String id){
        Orders order = ordersService.getById(id);
        Integer flowstatus = order.getFlowstatus();
        flowstatus ++;
        order.setFlowstatus(flowstatus);
        ordersService.updateById(order);
        return R.ok();
    }


    @ApiOperation("get commodity list by id")
    @PostMapping("commodityList/{id}")
    public R commodityList(@PathVariable String id){
        List<Commodity> list = ordersService.getCommodityListById(id);
        return R.ok().data("list", list);
    }

}

