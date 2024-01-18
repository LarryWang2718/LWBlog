package com.yjq.programmer.service.impl;

import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.ArticleMapper;
import com.yjq.programmer.dao.CollectMapper;
import com.yjq.programmer.domain.Article;
import com.yjq.programmer.domain.Collect;
import com.yjq.programmer.domain.CollectExample;
import com.yjq.programmer.dto.CollectDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.ICollectService;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.CopyUtil;
import com.yjq.programmer.utils.UuidUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Service
@Transactional
public class CollectServiceImpl implements ICollectService {

    @Resource
    private CollectMapper collectMapper;

    @Resource
    private ArticleMapper articleMapper;

    /**
     * @param collectDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> judgeCollect(CollectDTO collectDTO) {
        if(CommonUtil.isEmpty(collectDTO.getArticleId()) || CommonUtil.isEmpty(collectDTO.getUserId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        Article article = articleMapper.selectByPrimaryKey(collectDTO.getArticleId());
        if(article == null) {
            return ResponseDTO.errorByMsg(CodeMsg.ARTICLE_NOT_EXIST);
        }
        CollectExample collectExample = new CollectExample();
        collectExample.createCriteria().andArticleIdEqualTo(collectDTO.getArticleId()).andUserIdEqualTo(collectDTO.getUserId());
        List<Collect> collectList = collectMapper.selectByExample(collectExample);
        if(collectList.size() > 0) {
            return ResponseDTO.success(true);
        } else {
            return ResponseDTO.success(false);
        }
    }

    /**
     * @param collectDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> addCollect(CollectDTO collectDTO) {
        if(CommonUtil.isEmpty(collectDTO.getArticleId()) || CommonUtil.isEmpty(collectDTO.getUserId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        Article article = articleMapper.selectByPrimaryKey(collectDTO.getArticleId());
        if(article == null) {
            return ResponseDTO.errorByMsg(CodeMsg.ARTICLE_NOT_EXIST);
        }
        Collect collect = CopyUtil.copy(collectDTO, Collect.class);
        collect.setId(UuidUtil.getShortUuid());
        collect.setCreateTime(new Date());
        if(collectMapper.insertSelective(collect) == 0 ) {
            return ResponseDTO.errorByMsg(CodeMsg.COLLECT_ADD_ERROR);
        }
        // 更新文章的收藏数
        article.setCollectNum(article.getCollectNum() + 1);
        articleMapper.updateByPrimaryKeySelective(article);

        return ResponseDTO.successByMsg(true, "Successfully added to favorite.");
    }

    /**
     * 取消收藏文章
     * @param collectDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> removeCollect(CollectDTO collectDTO) {
        if(CommonUtil.isEmpty(collectDTO.getArticleId()) || CommonUtil.isEmpty(collectDTO.getUserId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        Article article = articleMapper.selectByPrimaryKey(collectDTO.getArticleId());
        if(article == null) {
            return ResponseDTO.errorByMsg(CodeMsg.ARTICLE_NOT_EXIST);
        }
        CollectExample collectExample = new CollectExample();
        collectExample.createCriteria().andArticleIdEqualTo(collectDTO.getArticleId()).andUserIdEqualTo(collectDTO.getUserId());
        if(collectMapper.deleteByExample(collectExample) == 0 ) {
            return ResponseDTO.errorByMsg(CodeMsg.COLLECT_REMOVE_ERROR);
        }
        // 更新文章的收藏数
        article.setCollectNum(article.getCollectNum() - 1);
        articleMapper.updateByPrimaryKeySelective(article);

        return ResponseDTO.successByMsg(true, "Successfully removed from favorites.");
    }
}
