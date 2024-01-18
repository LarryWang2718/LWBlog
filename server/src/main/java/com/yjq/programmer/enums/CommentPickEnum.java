package com.yjq.programmer.enums;


public enum CommentPickEnum {

    NO(1,"No"),

    YES(2,"Yes"),

    ;

    Integer code;

    String desc;

    CommentPickEnum(Integer code, String desc) {
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
