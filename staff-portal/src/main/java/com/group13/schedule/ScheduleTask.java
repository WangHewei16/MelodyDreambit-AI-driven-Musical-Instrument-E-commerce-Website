package com.group13.schedule;


import com.group13.service.StatisticsDailyService;
import com.group13.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author xujinfengxu
 */
@Component
public class ScheduleTask {
    private StatisticsDailyService staService;

    @Autowired
    public ScheduleTask(StatisticsDailyService staService) {
        this.staService = staService;
    }

    /**
     * 每天凌晨一点自动执行
     * 添加前一天的数据
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void taskSta(){
        staService.getCount(DateUtil.formatDate(DateUtil.addDays(new Date(), -1)));
    }

}
