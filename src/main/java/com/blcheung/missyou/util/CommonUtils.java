package com.blcheung.missyou.util;

import java.util.Calendar;
import java.util.Date;

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
}
