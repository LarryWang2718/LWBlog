package com.yjq.programmer.service;

import com.yjq.programmer.dto.ArticleDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;

import java.util.List;


public interface IArticleService {

    ResponseDTO<ArticleDTO> saveArticle(ArticleDTO articleDTO);

    ResponseDTO<PageDTO<ArticleDTO>> getArticleList(PageDTO<ArticleDTO> pageDTO);

    ResponseDTO<ArticleDTO> getArticleById(ArticleDTO articleDTO);

    ResponseDTO<ArticleDTO> viewArticle(ArticleDTO articleDTO);

    ResponseDTO<List<ArticleDTO>> getHotArticleList(ArticleDTO articleDTO);

    ResponseDTO<Boolean> updateArticleInfo(ArticleDTO articleDTO);

    ResponseDTO<Boolean> deleteArticle(ArticleDTO articleDTO);

    ResponseDTO<List<ArticleDTO>> getAuthorArticleList(ArticleDTO articleDTO);

    ResponseDTO<Integer> getArticleTotal(ArticleDTO articleDTO);

    ResponseDTO<List<Integer>> getArticleTotalByDay();
}
