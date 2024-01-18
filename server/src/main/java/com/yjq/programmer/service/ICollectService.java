package com.yjq.programmer.service;

import com.yjq.programmer.dto.CollectDTO;
import com.yjq.programmer.dto.ResponseDTO;


public interface ICollectService {

    ResponseDTO<Boolean> judgeCollect(CollectDTO collectDTO);

    ResponseDTO<Boolean> addCollect(CollectDTO collectDTO);

    ResponseDTO<Boolean> removeCollect(CollectDTO collectDTO);
}
