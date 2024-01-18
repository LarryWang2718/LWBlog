package com.yjq.programmer.dao;

import com.yjq.programmer.domain.TagItem;
import com.yjq.programmer.domain.TagItemExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagItemMapper {
    int countByExample(TagItemExample example);

    int deleteByExample(TagItemExample example);

    int deleteByPrimaryKey(String id);

    int insert(TagItem record);

    int insertSelective(TagItem record);

    List<TagItem> selectByExample(TagItemExample example);

    TagItem selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TagItem record, @Param("example") TagItemExample example);

    int updateByExample(@Param("record") TagItem record, @Param("example") TagItemExample example);

    int updateByPrimaryKeySelective(TagItem record);

    int updateByPrimaryKey(TagItem record);
}
