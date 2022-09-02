package com.group13.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.group13.entity.Cart;
import com.group13.entity.Commodity;
import com.group13.entity.StatisticsDaily;
import com.group13.mapper.CartMapper;
import com.group13.mapper.StatisticsDailyMapper;
import com.group13.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author group13
 * @since 2022-04-07
 */
@Service
@Transactional
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    private UserService userService;
    private CommodityService commodityService;
    private StatisticsDailyMapper staMapper;
    private PostService postService;
    private CartMapper cartMapper;

    @Autowired
    public StatisticsDailyServiceImpl(UserService userService, CommodityService commodityService, StatisticsDailyMapper staMapper, PostService postService, CartMapper cartMapper) {
        this.userService = userService;
        this.commodityService = commodityService;
        this.staMapper = staMapper;
        this.postService = postService;
        this.cartMapper = cartMapper;
    }

    /**
     * 统计某一天数据
     * @param day
     */
    @Override
    public void getCount(String day) {
        //添加记录之前删除表相同日期的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        staMapper.delete(wrapper);

        // 得到截止某一天注册人数
        Integer registerCount = userService.countRegister(day);
        // 得到截止某一天购买人数
        Integer buyCount = commodityService.countBuy(day);
        // 得到截止某一天访问人数
        Integer visitCount = commodityService.countVisit(day);
        // 得到截止某一天买家秀个数
        Integer postCount = postService.countPost(day);

        // 得到购物车type1量
        Integer type1Count = 0;
        // 得到购物车type2量
        Integer type2Count = 0;
        // 得到购物车type3量
        Integer type3Count = 0;
        // 得到购物车type4量
        Integer type4Count = 0;
        // 得到购物车type5量
        Integer type5Count = 0;
        // 得到购物车type6量
        Integer type6Count = 0;
        List<Cart> carts = cartMapper.selectList(null);
        for (Cart cart : carts) {
            String commodityId = cart.getCommodityId();
            Integer amount = cart.getAmount();
            Commodity commodityTmp = commodityService.getById(commodityId);
            Integer type = commodityTmp.getType();
            switch (type){
                case 0:
                    type1Count += amount; break;
                case 1:
                    type2Count += amount; break;
                case 2:
                    type3Count += amount; break;
                case 3:
                    type4Count += amount; break;
                case 4:
                    type5Count += amount; break;
                default:
                    type6Count += amount; break;
            }
        }

        // 商品浏览量
        int visited1 = commodityService.getVisitedByType(0);
        int visited2 = commodityService.getVisitedByType(1);
        int visited3 = commodityService.getVisitedByType(2);
        int visited4 = commodityService.getVisitedByType(3);
        int visited5 = commodityService.getVisitedByType(4);
        int visited6 = commodityService.getVisitedByType(5);

        // 前一天的数据
        StatisticsDaily prevSta = staMapper.selectDiffDay(day);

        System.out.println(prevSta);

        // 添加到统计分析表
        StatisticsDaily sta = new StatisticsDaily();
        // 注册
        sta.setRegisterNum(registerCount);
        sta.setCommodityBuyNum(buyCount);
        sta.setCommodityVisitNum(visitCount);
        sta.setPostNum(postCount);
        // 购物车
        sta.setType1CartNum(type1Count);
        sta.setType2CartNum(type2Count);
        sta.setType3CartNum(type3Count);
        sta.setType4CartNum(type4Count);
        sta.setType5CartNum(type5Count);
        sta.setType6CartNum(type6Count);
        // 商品
        sta.setType1VisitedNum(visited1 - prevSta.getType1VisitedNum());
        sta.setType2VisitedNum(visited2 - prevSta.getType2VisitedNum());
        sta.setType3VisitedNum(visited3 - prevSta.getType3VisitedNum());
        sta.setType4VisitedNum(visited4 - prevSta.getType4VisitedNum());
        sta.setType5VisitedNum(visited5 - prevSta.getType5VisitedNum());
        sta.setType6VisitedNum(visited6 - prevSta.getType6VisitedNum());
        //统计日期
        sta.setDateCalculated(day);
        staMapper.insert(sta);
    }
    /**
     * 图表显示
     * 返回两部分数据，日期json数组，数量json数组
     * @param type
     * @param begin
     * @param end
     * @return
     */
    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated", begin, end);
        List<StatisticsDaily> list = staMapper.selectList(wrapper);
        // 因为if安徽有两部分数据， 日期 和 日期对应数量
        // 前端要求数组json结构，对应后端java代码是list集合
        // 创建两个list集合， 一个日期list，一个日期对应数量list
        List<String> date_calculatedList = new ArrayList<>();
        List<Integer> numDataList = new ArrayList<>();

        // 遍历查询的所有数据list集合，进行封装
        for (StatisticsDaily statisticsDaily : list) {
            // 封装集合
            date_calculatedList.add(statisticsDaily.getDateCalculated());
            switch (type){
                case "register_num":
                    numDataList.add(statisticsDaily.getRegisterNum());
                    break;
                case "commodity_visit_num":
                    numDataList.add(statisticsDaily.getCommodityVisitNum());
                    break;
                case "commodity_buy_num":
                    numDataList.add(statisticsDaily.getCommodityBuyNum());
                    break;
                default:
                    numDataList.add(statisticsDaily.getPostNum());
                    break;
            }
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("date_calculatedList", date_calculatedList);
        map.put("numDataList", numDataList);

        return map;
    }

    /**
     * 饼状图显示数据
     * @param day
     * @return
     */
    @Override
    public Map<String, Object> getPieShowData(String day) {
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated", day);
        StatisticsDaily sta = staMapper.selectOne(wrapper);
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(sta.getType1CartNum());
        list.add(sta.getType2CartNum());
        list.add(sta.getType3CartNum());
        list.add(sta.getType4CartNum());
        list.add(sta.getType5CartNum());
        list.add(sta.getType6CartNum());
        ArrayList<String> typeList = new ArrayList<>();
        typeList.add("Pipes");
        typeList.add("Electronic");
        typeList.add("Percussion");
        typeList.add("Piano");
        typeList.add("Guitar");
        typeList.add("Others");
        map.put("dataList", list);
        map.put("typeList", typeList);
        return map;
    }

    /**
     * 柱状图显示数据
     * @param begin
     * @param end
     * @return
     */
    @Override
    public Map<String, Object> getBarShowData(String begin, String end) {
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated", begin, end);
        List<StatisticsDaily> list = staMapper.selectList(wrapper);
        // 因为if安徽有两部分数据， 日期 和 日期对应数量
        // 前端要求数组json结构，对应后端java代码是list集合
        // 创建两个list集合， 一个日期list，一个日期对应数量list
        List<String> date_calculatedList = new ArrayList<>();
        List<Integer> type1DataList = new ArrayList<>();
        List<Integer> type2DataList = new ArrayList<>();
        List<Integer> type3DataList = new ArrayList<>();
        List<Integer> type4DataList = new ArrayList<>();
        List<Integer> type5DataList = new ArrayList<>();
        List<Integer> type6DataList = new ArrayList<>();

        // 遍历查询的所有数据list集合，进行封装
        for (StatisticsDaily statisticsDaily : list) {
            // 封装集合
            date_calculatedList.add(statisticsDaily.getDateCalculated());
            type1DataList.add(statisticsDaily.getType1VisitedNum());
            type2DataList.add(statisticsDaily.getType2VisitedNum());
            type3DataList.add(statisticsDaily.getType3VisitedNum());
            type4DataList.add(statisticsDaily.getType4VisitedNum());
            type5DataList.add(statisticsDaily.getType5VisitedNum());
            type6DataList.add(statisticsDaily.getType6VisitedNum());
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("date_calculatedList", date_calculatedList);
        map.put("type1DataList", type1DataList);
        map.put("type2DataList", type2DataList);
        map.put("type3DataList", type3DataList);
        map.put("type4DataList", type4DataList);
        map.put("type5DataList", type5DataList);
        map.put("type6DataList", type6DataList);
        return map;
    }
}
