package com.yjq.programmer.service;

import com.yjq.programmer.dto.CommentDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;


public interface ICommentService {

    ResponseDTO<Boolean> submitComment(CommentDTO commentDTO);

    ResponseDTO<PageDTO<CommentDTO>> getCommentList(PageDTO<CommentDTO> pageDTO);

    ResponseDTO<PageDTO<CommentDTO>> getCommentListByAdmin(PageDTO<CommentDTO> pageDTO);

    ResponseDTO<Boolean> deleteComment(CommentDTO commentDTO);

    ResponseDTO<Integer> countTotalComment(CommentDTO commentDTO);

    ResponseDTO<Boolean> pickComment(CommentDTO commentDTO);

    ResponseDTO<Integer> getCommentTotal();
}
