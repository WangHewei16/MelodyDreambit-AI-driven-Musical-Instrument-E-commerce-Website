package com.group13.mapper;

import com.group13.entity.StatisticsDaily;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author group13
 * @since 2022-04-07
 */
@Repository
public interface StatisticsDailyMapper extends BaseMapper<StatisticsDaily> {


    /**
     * diff
     * @param day
     * @return
     */
    StatisticsDaily selectDiffDay(@Param("day") String day);
}
