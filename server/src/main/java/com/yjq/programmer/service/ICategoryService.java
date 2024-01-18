package com.yjq.programmer.service;

import com.yjq.programmer.dto.CategoryDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;

import java.util.List;


public interface ICategoryService {

    ResponseDTO<PageDTO<CategoryDTO>> getCategoryList(PageDTO<CategoryDTO> pageDTO);

    ResponseDTO<Boolean> saveCategory(CategoryDTO categoryDTO);

    ResponseDTO<Boolean> deleteCategory(CategoryDTO categoryDTO);

    ResponseDTO<List<CategoryDTO>> getAllCategoryList();
}
