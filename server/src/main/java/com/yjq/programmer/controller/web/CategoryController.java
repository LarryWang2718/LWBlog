package com.yjq.programmer.controller.web;

import com.yjq.programmer.dto.CategoryDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.ICategoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController("WebCategoryController")
@RequestMapping("/web/category")
public class CategoryController {

    @Resource
    private ICategoryService categoryService;

    /**
     * @return
     */
    @PostMapping("/all")
    public ResponseDTO<List<CategoryDTO>> getAllCategoryList(){
        return categoryService.getAllCategoryList();
    }

}
