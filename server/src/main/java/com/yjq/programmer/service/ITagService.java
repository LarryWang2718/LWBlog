package com.yjq.programmer.service;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.TagDTO;
import com.yjq.programmer.dto.TagItemDTO;

import java.util.List;


public interface ITagService {

    ResponseDTO<PageDTO<TagDTO>> getTagList(PageDTO<TagDTO> pageDTO);

    ResponseDTO<Boolean> saveTag(TagDTO tagDTO);

    ResponseDTO<Boolean> deleteTag(TagDTO tagDTO);

    ResponseDTO<List<TagDTO>> getAllTagList();

    ResponseDTO<Boolean> saveTagItem(TagItemDTO tagItemDTO);
}
