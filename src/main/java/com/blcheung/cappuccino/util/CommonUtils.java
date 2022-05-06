package com.blcheung.cappuccino.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    public static Date getFutureDateWithSecond(Long seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, seconds.intValue());
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

    /**
     * 生成订单号
     *
     * @return
     */
    public static String makeOrderNo() {
        StringBuffer sb = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        String millis = String.valueOf(calendar.getTimeInMillis());
        String micro = LocalDateTime.now()
                                    .toString();
        String random = String.valueOf(Math.random() * 1000)
                              .substring(0, 2);
        return sb.append(year)
                 .append(month)
                 .append(day)
                 .append(millis.substring(millis.length() - 5))
                 .append(micro.substring(micro.length() - 3))
                 .append(random)
                 .toString();
    }

    /**
     * BigDecimal转字符串
     *
     * @param bigDecimal
     * @return
     */
    public static String toPlain(BigDecimal bigDecimal) {
        return bigDecimal.stripTrailingZeros()
                         .toPlainString();
    }

    /**
     * 字节数组转成16进制表示格式的字符串
     *
     * @return java.lang.String
     * @author BLCheung
     * @date 2021/11/20 2:35 上午
     */
    public static String toHexString(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) return "";

        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            // 0~F前面补零
            if (( aByte & 0xff ) < 0x10) {
                sb.append("0");
                sb.append(Integer.toHexString(0xff & aByte));
            }
        }

        return sb.toString()
                 .toLowerCase();
    }
}
