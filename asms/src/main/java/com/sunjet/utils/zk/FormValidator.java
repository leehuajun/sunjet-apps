package com.sunjet.utils.zk;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

import java.util.Map;

/**
 * Created by lhj on 16/10/14.
 */
public class FormValidator extends AbstractValidator {

    public void validate(ValidationContext ctx) {
        //all the bean properties
        Map<String, Property> beanProps = ctx.getProperties(ctx.getProperty().getBase());
        for (String name : beanProps.keySet()) {
            validateEmpty(ctx, name, beanProps.get(name).getValue());
        }

//        validateEmpty(ctx,"name",(String)beanProps.get("name").getValue());
//        validateEmpty(ctx,"logId",(String)beanProps.get("logId").getValue());
//        validateEmpty(ctx,"phone",(String)beanProps.get("phone").getValue());
//        validateEmpty(ctx,"userType",(String)beanProps.get("userType").getValue());
//        validateEmpty(ctx,"userType",(String)beanProps.get("userType").getValue());


        //first let's check the passwords match
//        validatePasswords(ctx, (String)beanProps.get("password").getValue(), (String)ctx.getValidatorArg("retypedPassword"));
    }

    private void validateEmpty(ValidationContext ctx, String name, Object value) {
        if (value == null || StringUtils.isBlank(value.toString())) {
            this.addInvalidMessage(ctx, name, "不能为空");
        }
    }

//    private void validateEmpty(ValidationContext ctx,String name,String value){
//        if(value==null || value.trim().equals("")){
//            this.addInvalidMessage(ctx, name, "不能为空");
//        }
//    }

//    private void validatePasswords(ValidationContext ctx, String password, String retype) {
//        if(password == null || retype == null || (!password.equals(retype))) {
//            this.addInvalidMessage(ctx, "password", "密码不匹配！");
//        }
//    }
}