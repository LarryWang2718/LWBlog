package com.yjq.programmer.enums;


public enum ArticleQueryTypeEnum {

    ALL(1,"All Articles"),

    BLOG(2,"Blogs"),

    FORUM(3,"Q&As"),

    LIKE(4,"Liked"),

    COLLECT(5,"Favorites"),

    ;

    Integer code;

    String desc;

    ArticleQueryTypeEnum(Integer code, String desc) {
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
