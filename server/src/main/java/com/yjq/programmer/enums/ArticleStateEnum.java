package com.yjq.programmer.enums;


public enum  ArticleStateEnum {

    WAIT(1,"Pending For Review"),

    NOT_SOLVE(2,"Unsolved"),

    SOLVE(3,"Solved"),

    SUCCESS(4,"Approved"),

    FAIL(5,"Not Approved"),

    DRAFT(6,"Draft"),

    ;

    Integer code;

    String desc;

    ArticleStateEnum(Integer code, String desc) {
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
