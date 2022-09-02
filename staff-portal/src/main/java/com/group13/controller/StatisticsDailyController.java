package com.group13.controller;


import com.group13.common.R;
import com.group13.service.StatisticsDailyService;
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
 * @since 2022-04-07
 */
@RestController
@RequestMapping("/api/statistics-daily")
@Api("statistical control")
@CrossOrigin
public class StatisticsDailyController {

    private StatisticsDailyService staService;

    @Autowired
    public StatisticsDailyController(StatisticsDailyService staService) {
        this.staService = staService;
    }

    /**
     * 统计某一天数据
     * @param day
     * @return
     */
    @ApiOperation("Statistics for a day")
    @PostMapping("getCount/{day}")
    public R getCount(@PathVariable("day") String day){
        staService.getCount(day);
        return R.ok();
    }

    /**
     * 图表显示
     * 返回两部分数据，日期json数组，数量json数组
     * @param type
     * @param begin
     * @param end
     * @return
     */
    @ApiOperation("Chart shows")
    @PostMapping("showData/{type}/{begin}/{end}")
    public R showData(@PathVariable String type,
                      @PathVariable String begin,
                      @PathVariable String end){
        Map<String, Object> map = staService.getShowData(type,begin,end);
        return R.ok().data(map);
    }

    @ApiOperation("Pie Chart shows")
    @PostMapping("showPieData/{day}")
    public R showPieData(@PathVariable String day){
        Map<String, Object> map = staService.getPieShowData(day);
        return R.ok().data(map);
    }

    @ApiOperation("Bar Chart shows")
    @PostMapping("showBarData/{begin}/{end}")
    public R showBarData(@PathVariable String begin,
                         @PathVariable String end){
        Map<String, Object> map = staService.getBarShowData(begin,end);
        return R.ok().data(map);
    }
}

