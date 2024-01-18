package com.yjq.programmer.dao.my;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface MyArticleMapper {

    Integer getArticleTotalByDate(@Param("queryMap") Map<String, Object> queryMap);
}
