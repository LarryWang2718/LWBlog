package com.yjq.programmer.enums;


public enum ArticleTypeEnum {

    BLOG(1,"Blog"),

    FORUM(2,"Q&A"),

    ;

    Integer code;

    String desc;

    ArticleTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
