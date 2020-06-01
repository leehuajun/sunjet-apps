package com.sunjet.utils.common;

/**
 * Created by lhj on 16/9/16.
 */
public class StringHelper {
    /**
     * 生成固定长度的流水号，不足长度，前面补0
     *
     * @param sn          数字
     * @param fixedLength 长度
     * @return
     */
    public static String genFixedString(Integer sn, Integer fixedLength) {
        // 0 代表前面补充0
        // 10代表长度为10
        // d 代表参数为正数型
        String format = "%0" + fixedLength + "d";
        String fixedString = String.format(format, sn);
//        logger.info(fixedString);
        return fixedString;
    }
}
