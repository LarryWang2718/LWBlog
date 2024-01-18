package com.yjq.programmer.service;

import com.yjq.programmer.dto.LikeDTO;
import com.yjq.programmer.dto.ResponseDTO;


public interface ILikeService {

    ResponseDTO<Boolean> likeArticle(LikeDTO likeDTO);

    ResponseDTO<Boolean> unlikeArticle(LikeDTO likeDTO);

    ResponseDTO<Boolean> judgeLike(LikeDTO likeDTO);

}
