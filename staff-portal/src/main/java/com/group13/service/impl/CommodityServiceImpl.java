package com.group13.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.group13.entity.*;
import com.group13.entity.dto.CommodityBasicInfoDto;
import com.group13.entity.vo.CommodityQueryVo;
import com.group13.handler.exception.Group13Exception;
import com.group13.mapper.*;
import com.group13.service.CommodityCommentService;
import com.group13.service.CommodityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.group13.service.PostCommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author group13
 * @since 2022-03-16
 */
@Service
@Transactional
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements CommodityService {

    private CommodityMapper commodityMapper;
    private CommodityIntroductionMapper introductionMapper;
    private CommodityCommentService commentService;
    private PostCommentService postCommentService;
    private PostMapper postMapper;
    private CommodityLikeMapper likeMapper;

    @Autowired
    public CommodityServiceImpl(CommodityMapper commodityMapper, CommodityIntroductionMapper introductionMapper, CommodityCommentService commentService, PostCommentService postCommentService, PostMapper postMapper, CommodityLikeMapper likeMapper) {
        this.commodityMapper = commodityMapper;
        this.introductionMapper = introductionMapper;
        this.commentService = commentService;
        this.postCommentService = postCommentService;
        this.postMapper = postMapper;
        this.likeMapper = likeMapper;
    }

    /**
     * get commodities list by condition
     * @param current
     * @param limit
     * @param commodityQueryVo
     * @return
     */
    @Override
    public Map<String, Object> pageByVo(long current, long limit, CommodityQueryVo commodityQueryVo) {
        Page<Commodity> page = new Page<>(current, limit);
        // condition
        QueryWrapper<Commodity> wrapper = new QueryWrapper<>();
        // dynamic sql
        String begin = commodityQueryVo.getBegin();
        String end = commodityQueryVo.getEnd();
        Double minPrice = commodityQueryVo.getMinPrice();
        Double maxPrice = commodityQueryVo.getMaxPrice();
        String name = commodityQueryVo.getName();
        Boolean sortAsc = commodityQueryVo.getSortAsc();
        Integer sortBy = commodityQueryVo.getSortBy();
        if (StringUtils.isEmpty(sortAsc)){
            sortAsc = true;
        }
        if (StringUtils.isEmpty(sortBy)){
            sortBy = 0;
        }

        if (!StringUtils.isEmpty(name)){
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)){
            wrapper.le("gmt_create", end);
        }
        if (!StringUtils.isEmpty(minPrice)){
            wrapper.ge("price", minPrice);
        }
        if (!StringUtils.isEmpty(maxPrice)){
            wrapper.le("price", maxPrice);
        }
        if (sortAsc){
            if (sortBy == 1){
                wrapper.orderByAsc("buy_amount");
            }
            else if(sortBy == 2){
                wrapper.orderByAsc("collect_amount");
            }
            else if(sortBy == 3){
                wrapper.orderByAsc("visit_amount");
            }
            else{
                wrapper.orderByDesc("gmt_create");
            }
        }else{
            if (sortBy == 1){
                wrapper.orderByDesc("buy_amount");
            }
            else if(sortBy == 2){
                wrapper.orderByDesc("collect_amount");
            }
            else if(sortBy == 3){
                wrapper.orderByDesc("visit_amount");
            }
            else{
                wrapper.orderByDesc("gmt_create");
            }
        }
        commodityMapper.selectPage(page, wrapper);
        long total = page.getTotal();
        List<Commodity> records = page.getRecords();
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("rows", records);
        return map;

    }

    /**
     * delete commodity by id
     * @param id
     * @return
     */
    @Override
    public boolean removeCommodity(String id) {
        introductionMapper.deleteById(id);
        commentService.removeCommentByCommodityId(id);
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("commodity_id", id);
        List<Post> posts = postMapper.selectList(queryWrapper);
        for (Post post : posts) {
            postCommentService.removeCommentByPostId(post.getId());
        }
        postMapper.delete(queryWrapper);
        QueryWrapper<CommodityLike> wrapper = new QueryWrapper<>();
        wrapper.eq("commodity_id", id);
        likeMapper.delete(wrapper);
        int flag = commodityMapper.deleteById(id);
        System.out.println(flag);
        return flag > 0;
    }

    /**
     * add commodity basic info
     * @param commodityBasicInfoDto
     * @return
     */
    @Override
    public String saveBasicInfo(CommodityBasicInfoDto commodityBasicInfoDto) {
        Commodity commodity = new Commodity();
        BeanUtils.copyProperties(commodityBasicInfoDto, commodity);
        commodity.setBuyAmount(0);
        commodity.setCollectAmount(0);
        commodity.setVisitAmount(0);
        int insert = commodityMapper.insert(commodity);
        if (insert <= 0){
            throw new Group13Exception(20001, "fail to add");
        }
        String id = commodity.getId();
        CommodityIntroduction introBean = new CommodityIntroduction();
        introBean.setId(id);
        introductionMapper.insert(introBean);
        return id;
    }

    /**
     * get commodity basic info by id
     * @param id
     * @return
     */
    @Override
    public CommodityBasicInfoDto getBasicInfo(String id) {
        Commodity commodity = commodityMapper.selectById(id);
        CommodityBasicInfoDto commodityBasicInfoDto = new CommodityBasicInfoDto();
        BeanUtils.copyProperties(commodity, commodityBasicInfoDto);
        return commodityBasicInfoDto;
    }

    /**
     * update commodity basic info
     * @param commodityBasicInfoDto
     * @param id
     * @return
     */
    @Override
    public void updateBasicInfo(CommodityBasicInfoDto commodityBasicInfoDto, String id) {
        Commodity commodity = commodityMapper.selectById(id);
        BeanUtils.copyProperties(commodityBasicInfoDto, commodity);
        commodityMapper.updateById(commodity);
    }

    /**
     * 计算某天访问量
     * @param day
     * @return
     */
    @Override
    public Integer countVisit(String day) {
        return commodityMapper.countVisit(day);
    }


    /**
     * 计算某天购买量
     * @param day
     * @return
     */
    @Override
    public Integer countBuy(String day) {
        return commodityMapper.countBuy(day);
    }


    /**
     * 通过类型获得截止当天的该种类商品浏览总量
     * @param type
     * @return
     */
    @Override
    public int getVisitedByType(int type) {
        return commodityMapper.countVisitByType(type);
    }

}
