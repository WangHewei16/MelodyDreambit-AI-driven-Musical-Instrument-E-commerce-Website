package com.group13.service;

import com.group13.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author group13
 * @since 2022-04-07
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    /**
     * 统计某一天数据
     * @param day
     * @return
     */
    void getCount(String day);

    /**
     * 图表显示
     * 返回两部分数据，日期json数组，数量json数组
     * @param type
     * @param begin
     * @param end
     * @return
     */
    Map<String, Object> getShowData(String type, String begin, String end);

    /**
     * 饼状图显示数据
     * @param day
     * @return
     */
    Map<String, Object> getPieShowData(String day);

    /**
     * 柱状图显示数据
     * @param begin
     * @param end
     * @return
     */
    Map<String, Object> getBarShowData(String begin, String end);
}
