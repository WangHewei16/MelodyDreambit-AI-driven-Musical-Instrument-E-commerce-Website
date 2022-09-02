package com.group13.service.impl;

import com.group13.entity.Cart;
import com.group13.mapper.CartMapper;
import com.group13.service.CartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author group13
 * @since 2022-04-15
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

}
