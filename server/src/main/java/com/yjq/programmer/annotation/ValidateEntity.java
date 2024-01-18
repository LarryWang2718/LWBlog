package com.yjq.programmer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateEntity {
    public boolean required() default false;

    public boolean requiredMaxLength() default false;

    public boolean requiredMinLength() default false;

    public boolean requiredMaxValue() default false;

    public boolean requiredMinValue() default false;




    public int maxLength() default -1;//最大长度

    public int minLength() default -1;//最小长度

    public double maxValue() default -1;//大值

    public double minValue() default -1;//最小值





    public String errorRequiredMsg() default "";

    public String errorMinLengthMsg() default "";

    public String errorMaxLengthMsg() default "";

    public String errorMinValueMsg() default "";

    public String errorMaxValueMsg() default "";
}
