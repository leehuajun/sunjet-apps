package com.sunjet.test;

import com.sunjet.model.basic.DocumentNoEntity;
import com.sunjet.repository.helper.DocumentNoHelper;
import com.sunjet.utils.activiti.CustomComment;
import com.sunjet.utils.common.JsonHelper;
import com.sunjet.utils.common.LoggerUtil;
import com.sunjet.utils.common.UUIDHelper;
import com.sunjet.vm.base.DocStatus;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by lhj on 16/9/16.
 */
public class CommonTest {
    private static Logger logger = LoggerFactory.getLogger(CommonTest.class);

    @Test
    public void testUUID() {
        System.out.println(UUIDHelper.newUuid());
    }

    @Test
    public void testJson() {
        try {
            CustomComment customComment = new CustomComment("同意", "这是正确的");
            String json = JsonHelper.bean2Json(customComment);
            LoggerUtil.getLogger().info(json);
            CustomComment comment = JsonHelper.json2Bean(json, CustomComment.class);
            LoggerUtil.getLogger().info(comment.getResult());
            LoggerUtil.getLogger().info(comment.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void convertStringToDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse("");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date.toString());
    }

    @Test
    public void testDouble() {
        double f = 111231.5546;
        BigDecimal b = new BigDecimal(f);
        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(f1);
    }

    @Test
    public void testEnum() {
        List<DocStatus> statusList = Arrays.asList(DocStatus.values());
        for (DocStatus status : statusList) {
            System.out.println(status.toString());
        }
    }

    @Test
    public void testListContains2() {
        String str = "10,11,12,1,2,3";
        List<String> months = Arrays.asList(str.split(","));
        List<Integer> frigid_months = new ArrayList<>();
        for (String sss : months) {
            frigid_months.add(Integer.parseInt(sss));
        }

        if (frigid_months.contains(LocalDate.now().getMonth().getValue())) {
            System.out.println("包含");
        } else {
            System.out.println("不包含");
        }
    }

    @Test
    public void testListContains() {
        List<Integer> months = Arrays.asList(10, 12, 1, 2, 3);

        if (months.contains(LocalDate.now().getMonth().getValue())) {
            System.out.println("包含");
        } else {
            System.out.println("不包含");
        }
    }

    @Test
    public void testLocalDateMonth() {
        System.out.println(LocalDate.now().getMonth().getValue());
    }

    @Test
    public void testSubstring() {
        String code = "0112001";
        String result = code.substring(3, 4);
        System.out.println(result);
    }

    @Test
    public void testDoubleToInt() {
        double d = 12.0;
        int i = (new Double(d)).intValue();
        System.out.println(i);
    }

    @Test
    public void testLocalDate() {
        LocalDate now = LocalDate.now();
        System.out.println(now.toString());
        System.out.println(now.toString().replace("-", ""));
    }

    @Test
    public void testGenFixedLengthString() {
        logger.info(DocumentNoHelper.genFixedStringBySn(1, 5));
        logger.info(DocumentNoHelper.genFixedStringBySn(12, 5));
        logger.info(DocumentNoHelper.genFixedStringBySn(123, 5));
        logger.info(DocumentNoHelper.genFixedStringBySn(1234, 5));
        logger.info(DocumentNoHelper.genFixedStringBySn(12345, 5));
    }

    @Test
    public void testGetLastNo() {
        logger.info(DocumentNoHelper.getLastNo(new DocumentNoEntity("", "ZBCD")));
        logger.info(DocumentNoHelper.getLastNo(new DocumentNoEntity("", "ZBCD", "20160916")));
        logger.info(DocumentNoHelper.getLastNo(new DocumentNoEntity("", "ZBCD", "20160916", "6")));
    }
}
