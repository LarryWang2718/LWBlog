package com.yjq.programmer.controller.web;

import com.yjq.programmer.dto.AttentionDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.IAttentionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController("WebAttentionController")
@RequestMapping("/web/attention")
public class AttentionController {

    @Resource
    private IAttentionService attentionService;

    /**
     * @param attentionDTO
     * @return
     */
    @PostMapping("/add")
    public ResponseDTO<Boolean> addAttention(@RequestBody AttentionDTO attentionDTO){
        return attentionService.addAttention(attentionDTO);
    }

    /**
     * @param attentionDTO
     * @return
     */
    @PostMapping("/remove")
    public ResponseDTO<Boolean> removeAttention(@RequestBody AttentionDTO attentionDTO){
        return attentionService.removeAttention(attentionDTO);
    }

    /**
     * @param attentionDTO
     * @return
     */
    @PostMapping("/judge")
    public ResponseDTO<Boolean> judgeAttention(@RequestBody AttentionDTO attentionDTO){
        return attentionService.judgeAttention(attentionDTO);
    }

    /**
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<AttentionDTO>> getAttentionList(@RequestBody PageDTO<AttentionDTO> pageDTO){
        return attentionService.getAttentionList(pageDTO);
    }

    /**
     * @param attentionDTO
     * @return
     */
    @PostMapping("/all")
    public ResponseDTO<List<AttentionDTO>> getAllAttentionList(@RequestBody AttentionDTO attentionDTO){
        return attentionService.getAllAttentionList(attentionDTO);
    }
}
