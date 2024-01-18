package com.yjq.programmer.controller.web;

import com.yjq.programmer.dto.CommentDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.ICommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController("WebCommentController")
@RequestMapping("/web/comment")
public class CommentController {

    @Resource
    private ICommentService commentService;

    /**
     * @param commentDTO
     * @return
     */
    @PostMapping("/submit")
    public ResponseDTO<Boolean> submitComment(@RequestBody CommentDTO commentDTO){
        return commentService.submitComment(commentDTO);
    }

    /**
     * @param commentDTO
     * @return
     */
    @PostMapping("/pick")
    public ResponseDTO<Boolean> pickComment(@RequestBody CommentDTO commentDTO){
        return commentService.pickComment(commentDTO);
    }

    /**
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<CommentDTO>> getCommentList(@RequestBody PageDTO<CommentDTO> pageDTO){
        return commentService.getCommentList(pageDTO);
    }

    /**
     * @param commentDTO
     * @return
     */
    @PostMapping("/delete")
    public ResponseDTO<Boolean> deleteComment(@RequestBody CommentDTO commentDTO){
        return commentService.deleteComment(commentDTO);
    }

    /**
     * @param commentDTO
     * @return
     */
    @PostMapping("/total")
    public ResponseDTO<Integer> countTotalComment(@RequestBody CommentDTO commentDTO){
        return commentService.countTotalComment(commentDTO);
    }


}
