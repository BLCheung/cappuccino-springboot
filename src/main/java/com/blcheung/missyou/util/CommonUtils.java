package com.blcheung.missyou.util;

import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CommonUtils {
    /**
     * 获取当前日期
     *
     * @return
     */
    public static Date getNowDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * 通过秒获取未来某个日期
     *
     * @param seconds 秒
     * @return
     */
    public static Date getFutureDateWithSecond(Integer seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    /**
     * 通过天获取未来某个日期
     *
     * @param days 天数
     * @return
     */
    public static Date getFutureDateWithDay(Integer days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

    /**
     * 是否未超出指定时间范围
     *
     * @param now
     * @param start
     * @param end
     * @return
     */
    public static Boolean isInRangeDate(Date now, Date start, Date end) {
        long nowTime = now.getTime();
        long startTime = start.getTime();
        long endTime = end.getTime();

        return nowTime > startTime && nowTime < endTime;
    }

    /**
     * 是否都是唯一的id集合
     *
     * @param ids
     * @return
     */
    public static Boolean isDistinctIds(List<Long> ids) {
        if (ids.isEmpty()) return false;

        long size = ids.size();
        long uniqueSize = ids.stream()
                             .distinct()
                             .count();

        return size == uniqueSize;
    }
}
