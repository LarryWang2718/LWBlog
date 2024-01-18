package com.yjq.programmer.utils;


import com.yjq.programmer.annotation.ValidateEntity;
import com.yjq.programmer.bean.CodeMsg;

import java.lang.reflect.Field;
import java.math.BigDecimal;



public class ValidateEntityUtil {
    public static CodeMsg validate(Object object){
        Field[] declaredFields = object.getClass().getDeclaredFields();

        for(Field field : declaredFields){
            ValidateEntity annotation = field.getAnnotation(ValidateEntity.class);
            field.setAccessible(true);
            if(annotation != null){
                if(annotation.required()){
                    try {
                        Object o = field.get(object);
                        if(o == null){
                            CodeMsg codeMsg = CodeMsg.VALIDATE_ENTITY_ERROR;
                            codeMsg.setMsg(annotation.errorRequiredMsg());
                            return codeMsg;
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Object o = field.get(object);
                    CodeMsg stringResult = confirmStringLength(o, annotation);
                    if(stringResult.getCode().intValue() != 0) {
                        return stringResult;
                    }
                    CodeMsg numberResult = confirmNumberValue(o, annotation);
                    if(numberResult.getCode().intValue() != 0) {
                        return numberResult;
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return CodeMsg.SUCCESS;
    }

    public static CodeMsg confirmStringLength(Object o, ValidateEntity annotation) {
        if(o instanceof String){
            if(annotation.requiredMinLength()){
                if(o.toString().trim().length() < annotation.minLength()){
                    CodeMsg codeMsg = CodeMsg.VALIDATE_ENTITY_ERROR;
                    codeMsg.setMsg(annotation.errorMinLengthMsg());
                    return codeMsg;
                }
            }
            if(annotation.requiredMaxLength())
            {
                if(o.toString().trim().length() > annotation.maxLength()){
                    CodeMsg codeMsg = CodeMsg.VALIDATE_ENTITY_ERROR;
                    codeMsg.setMsg(annotation.errorMaxLengthMsg());
                    return codeMsg;
                }
            }
        }
        return CodeMsg.SUCCESS;
    }

    public static CodeMsg confirmNumberValue(Object o, ValidateEntity annotation) {
        if(isNumberObject(o)){
            if(annotation.requiredMinValue()){
                if(Double.valueOf(o.toString().trim()) < annotation.minValue()){
                    CodeMsg codeMsg = CodeMsg.VALIDATE_ENTITY_ERROR;
                    codeMsg.setMsg(annotation.errorMinValueMsg());
                    return codeMsg;
                }
            }
            if(annotation.requiredMaxValue()){
                if(Double.valueOf(o.toString().trim()) > annotation.maxValue()){
                    CodeMsg codeMsg = CodeMsg.VALIDATE_ENTITY_ERROR;
                    codeMsg.setMsg(annotation.errorMaxValueMsg());
                    return codeMsg;
                }
            }
        }else if(isBigDecimalObject(o)) {
            BigDecimal inputValue = new BigDecimal(o.toString().trim());
            if(annotation.requiredMinValue()){
                BigDecimal minValue = BigDecimal.valueOf(annotation.minValue());
                if(inputValue.compareTo(minValue) == -1){
                    CodeMsg codeMsg = CodeMsg.VALIDATE_ENTITY_ERROR;
                    codeMsg.setMsg(annotation.errorMinValueMsg());
                    return codeMsg;
                }
            }
            if(annotation.requiredMaxValue()){
                BigDecimal maxValue = BigDecimal.valueOf(annotation.maxValue());
                if(inputValue.compareTo(maxValue) == 1){
                    CodeMsg codeMsg = CodeMsg.VALIDATE_ENTITY_ERROR;
                    codeMsg.setMsg(annotation.errorMaxValueMsg());
                    return codeMsg;
                }
            }
        }

        return CodeMsg.SUCCESS;
    }



    /**
     * @param object
     * @return
     */
    public static boolean isNumberObject(Object object){
        if(object instanceof Integer)return true;
        if(object instanceof Long)return true;
        if(object instanceof Float)return true;
        if(object instanceof Double)return true;
        return false;
    }

    /**
     * @param object
     * @return
     */
    public static boolean isBigDecimalObject(Object object){
        if(object instanceof BigDecimal)return true;
        return false;
    }
}
