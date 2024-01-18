package com.yjq.programmer.controller.web;

import com.yjq.programmer.dto.LikeDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.ILikeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController("WebLikeController")
@RequestMapping("/web/like")
public class LikeController {

    @Resource
    private ILikeService likeService;

    /**
     * @param likeDTO
     * @return
     */
    @PostMapping("/like")
    public ResponseDTO<Boolean> likeArticle(@RequestBody LikeDTO likeDTO){
        return likeService.likeArticle(likeDTO);
    }

    /**
     * @param likeDTO
     * @return
     */
    @PostMapping("/unlike")
    public ResponseDTO<Boolean> unlikeArticle(@RequestBody LikeDTO likeDTO){
        return likeService.unlikeArticle(likeDTO);
    }

    /**
     * @param likeDTO
     * @return
     */
    @PostMapping("/judge")
    public ResponseDTO<Boolean> judgeLike(@RequestBody LikeDTO likeDTO){
        return likeService.judgeLike(likeDTO);
    }

}
