package com.group13.controller;


import com.group13.common.R;
import com.group13.entity.Address;
import com.group13.entity.Orders;
import com.group13.service.AddressService;
import com.group13.service.OrdersService;
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
 * @since 2022-04-20
 */
@RestController
@RequestMapping("/api/address")
@Api(tags = "address Controller")
@CrossOrigin
public class AddressController {

    private AddressService addressService;
    private OrdersService ordersService;

    @Autowired
    public AddressController(AddressService addressService, OrdersService ordersService) {
        this.addressService = addressService;
        this.ordersService = ordersService;
    }

    /**
     * 根据id查找address
     * @param id
     * @return
     */
    @ApiOperation("select address by id")
    @PostMapping("selectById/{id}")
    public R selectById(@PathVariable String id){
        Orders order = ordersService.getById(id);
        String addressId = order.getAddressId();
        Address address = addressService.getById(addressId);
        return R.ok().data("address", address);
    }


    /**
     * new Address
     * @param address
     * @return
     */
    @ApiOperation("new address")
    @PostMapping("newAddress/{id}")
    public R addAddress(@RequestBody Address address, @PathVariable("id") String id){
        addressService.updateAndChange(address, id);
        return R.ok();
    }
}

