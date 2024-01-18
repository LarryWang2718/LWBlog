package com.yjq.programmer.controller.web;

import com.yjq.programmer.dto.ArticleDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.IArticleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController("WebArticleController")
@RequestMapping("/web/article")
public class ArticleController {

    @Resource
    private IArticleService articleService;

    /**
     * @param articleDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<ArticleDTO> saveArticle(@RequestBody ArticleDTO articleDTO){
        return articleService.saveArticle(articleDTO);
    }

    /**
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<ArticleDTO>> getArticleList(@RequestBody PageDTO<ArticleDTO> pageDTO){
        return articleService.getArticleList(pageDTO);
    }

    /**
     * @param articleDTO
     * @return
     */
    @PostMapping("/view")
    public ResponseDTO<ArticleDTO> viewArticle(@RequestBody ArticleDTO articleDTO){
        return articleService.viewArticle(articleDTO);
    }

    /**
     * @param articleDTO
     * @return
     */
    @PostMapping("/get")
    public ResponseDTO<ArticleDTO> getArticleDetail(@RequestBody ArticleDTO articleDTO){
        return articleService.getArticleById(articleDTO);
    }

    /**
     * @param articleDTO
     * @return
     */
    @PostMapping("/hot")
    public ResponseDTO<List<ArticleDTO>> getHotArticleList(@RequestBody ArticleDTO articleDTO){
        return articleService.getHotArticleList(articleDTO);
    }

    /**
     * @param articleDTO
     * @return
     */
    @PostMapping("/author")
    public ResponseDTO<List<ArticleDTO>> getAuthorArticleList(@RequestBody ArticleDTO articleDTO){
        return articleService.getAuthorArticleList(articleDTO);
    }

}
