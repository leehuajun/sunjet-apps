package com.sunjet.repository.helper;

import com.sunjet.model.basic.DocumentNoEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

/**
 * Created by lhj on 16/9/16.
 */
public class DocumentNoHelper {
    private static Logger logger = LoggerFactory.getLogger(DocumentNoHelper.class);

    // 固定的长度
    private final static Integer FIXED_LENGTH = 4;

    public static String getLastNo(DocumentNoEntity entity) {
        String strToday = LocalDate.now().toString().replace("-", "");

        if (strToday.equalsIgnoreCase(entity.getLastNoDate())) {
            Integer currentSn = Integer.parseInt(entity.getLastNoSerialNumber()) + 1;
            return entity.getDocCode().toUpperCase() + strToday + genFixedStringBySn(currentSn, FIXED_LENGTH);
        } else {
            return entity.getDocCode().toUpperCase() + strToday + genFixedStringBySn(1, FIXED_LENGTH);
        }
    }

    /**
     * 生成固定长度的流水号，不足长度，前面补0
     *
     * @param sn          数字
     * @param fixedLength 长度
     * @return
     */
    public static String genFixedStringBySn(Integer sn, Integer fixedLength) {
        // 0 代表前面补充0
        // 10代表长度为10
        // d 代表参数为正数型
        String format = "%0" + fixedLength + "d";
        String fixedString = String.format(format, sn);
//        logger.info(fixedString);
        return fixedString;
    }
}
