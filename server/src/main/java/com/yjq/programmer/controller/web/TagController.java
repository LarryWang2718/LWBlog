package com.yjq.programmer.controller.web;

import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.TagDTO;
import com.yjq.programmer.service.ITagService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController("WebTagController")
@RequestMapping("/web/tag")
public class TagController {

    @Resource
    private ITagService tagService;

    /**
     * @return
     */
    @PostMapping("/all")
    public ResponseDTO<List<TagDTO>> getAllTagList(){
        return tagService.getAllTagList();
    }

}
