package com.yjq.programmer.enums;


public enum  CommentTypeEnum {

    SUBMIT(1,"Submit"),

    REPLY(2,"Reply"),

    ;

    Integer code;

    String desc;

    CommentTypeEnum(Integer code, String desc) {
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
