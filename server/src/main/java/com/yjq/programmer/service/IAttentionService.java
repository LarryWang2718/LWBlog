package com.yjq.programmer.service;

import com.yjq.programmer.dto.AttentionDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;

import java.util.List;


public interface IAttentionService {

    ResponseDTO<Boolean> addAttention(AttentionDTO attentionDTO);

    ResponseDTO<Boolean> removeAttention(AttentionDTO attentionDTO);

    ResponseDTO<Boolean> judgeAttention(AttentionDTO attentionDTO);

    ResponseDTO<PageDTO<AttentionDTO>> getAttentionList(PageDTO<AttentionDTO> pageDTO);

    ResponseDTO<List<AttentionDTO>> getAllAttentionList(AttentionDTO attentionDTO);
}
