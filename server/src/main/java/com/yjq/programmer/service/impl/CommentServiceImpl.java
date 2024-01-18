package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.ArticleMapper;
import com.yjq.programmer.dao.CommentMapper;
import com.yjq.programmer.dao.UserMapper;
import com.yjq.programmer.domain.Article;
import com.yjq.programmer.domain.Comment;
import com.yjq.programmer.domain.CommentExample;
import com.yjq.programmer.domain.User;
import com.yjq.programmer.dto.*;
import com.yjq.programmer.enums.ArticleStateEnum;
import com.yjq.programmer.enums.CommentPickEnum;
import com.yjq.programmer.service.ICommentService;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.CopyUtil;
import com.yjq.programmer.utils.UuidUtil;
import com.yjq.programmer.utils.ValidateEntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Service
@Transactional
public class CommentServiceImpl implements ICommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ArticleMapper articleMapper;

    /**
     * @param commentDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> submitComment(CommentDTO commentDTO) {

        CodeMsg validate = ValidateEntityUtil.validate(commentDTO);
        if (!validate.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            return ResponseDTO.errorByMsg(validate);
        }
        Article article = articleMapper.selectByPrimaryKey(commentDTO.getArticleId());
        if(article == null) {
            return ResponseDTO.errorByMsg(CodeMsg.ARTICLE_NOT_EXIST);
        }
        Comment comment = CopyUtil.copy(commentDTO, Comment.class);
        comment.setId(UuidUtil.getShortUuid());
        comment.setCreateTime(new Date());
        if(commentMapper.insertSelective(comment) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.COMMENT_SUBMIT_ERROR);
        }

        article.setCommentNum(article.getCommentNum() + 1);
        articleMapper.updateByPrimaryKeySelective(article);

        return ResponseDTO.successByMsg(true, "Successfully posted comment.");
    }

    /**
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<CommentDTO>> getCommentList(PageDTO<CommentDTO> pageDTO) {
        CommentExample commentExample = new CommentExample();

        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }

        if(pageDTO.getSize() == null){
            pageDTO.setSize(5);
        }

        if(pageDTO.getParam() != null) {
            CommentDTO commentDTO = pageDTO.getParam();
            commentExample.createCriteria().andParentIdEqualTo("").andArticleIdEqualTo(commentDTO.getArticleId());
        }
        commentExample.setOrderByClause("pick desc, create_time desc");
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());

        List<Comment> commentList = commentMapper.selectByExample(commentExample);
        PageInfo<Comment> pageInfo = new PageInfo<>(commentList);

        pageDTO.setTotal(pageInfo.getTotal());
        int pickIndex = 0;
        int nowIndex = -1;

        List<CommentDTO> commentDTOList = CopyUtil.copyList(commentList, CommentDTO.class);
        for(CommentDTO commentDTO : commentDTOList) {
            nowIndex++;

            User fromUser = userMapper.selectByPrimaryKey(commentDTO.getFromId());
            commentDTO.setFromUserDTO(CopyUtil.copy(fromUser, UserDTO.class));
            User toUser = userMapper.selectByPrimaryKey(commentDTO.getToId());
            commentDTO.setToUserDTO(CopyUtil.copy(toUser, UserDTO.class));

            commentDTO.setCollapse(true);

            CommentExample childrenCommentExample = new CommentExample();
            childrenCommentExample.createCriteria().andParentIdEqualTo(commentDTO.getId());
            childrenCommentExample.setOrderByClause("pick desc, create_time desc");
            List<Comment> childrenCommentList = commentMapper.selectByExample(childrenCommentExample);
            List<CommentDTO> childrenCommentDTOList = CopyUtil.copyList(childrenCommentList, CommentDTO.class);
            for(CommentDTO childrenCommentDTO : childrenCommentDTOList) {
                User fromChildrenUser = userMapper.selectByPrimaryKey(childrenCommentDTO.getFromId());
                childrenCommentDTO.setFromUserDTO(CopyUtil.copy(fromChildrenUser, UserDTO.class));
                User toChildrenUser = userMapper.selectByPrimaryKey(childrenCommentDTO.getToId());
                childrenCommentDTO.setToUserDTO(CopyUtil.copy(toChildrenUser, UserDTO.class));
                if(CommentPickEnum.YES.getCode().equals(childrenCommentDTO.getPick())) {

                    commentDTO.setCollapse(false);
                    pickIndex = nowIndex;
                }
            }
            commentDTO.setChildrenList(childrenCommentDTOList);
        }
        if(pickIndex != 0) {

            commentDTOList.add(0, commentDTOList.remove(pickIndex));
        }
        pageDTO.setList(commentDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<CommentDTO>> getCommentListByAdmin(PageDTO<CommentDTO> pageDTO) {
        CommentExample commentExample = new CommentExample();

        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }

        if(pageDTO.getSize() == null){
            pageDTO.setSize(5);
        }

        if(pageDTO.getParam() != null) {
            CommentDTO commentDTO = pageDTO.getParam();
            if(!CommonUtil.isEmpty(commentDTO.getContent())) {
                commentExample.createCriteria().andContentLike("%" + commentDTO.getContent() + "%");
            }
        }
        commentExample.setOrderByClause("create_time desc");
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());

        List<Comment> commentList = commentMapper.selectByExample(commentExample);
        PageInfo<Comment> pageInfo = new PageInfo<>(commentList);

        pageDTO.setTotal(pageInfo.getTotal());

        List<CommentDTO> commentDTOList = CopyUtil.copyList(commentList, CommentDTO.class);
        for(CommentDTO commentDTO : commentDTOList) {

            User fromUser = userMapper.selectByPrimaryKey(commentDTO.getFromId());
            commentDTO.setFromUserDTO(CopyUtil.copy(fromUser, UserDTO.class));
            User toUser = userMapper.selectByPrimaryKey(commentDTO.getToId());
            commentDTO.setToUserDTO(CopyUtil.copy(toUser, UserDTO.class));

            Article article = articleMapper.selectByPrimaryKey(commentDTO.getArticleId());
            commentDTO.setArticleDTO(CopyUtil.copy(article, ArticleDTO.class));
        }

        pageDTO.setList(commentDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * @param commentDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteComment(CommentDTO commentDTO) {
        if(CommonUtil.isEmpty(commentDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        String[] ids = commentDTO.getId().split(",");
        for(String id : ids) {
            Comment comment = commentMapper.selectByPrimaryKey(id);

            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria().andParentIdEqualTo(id);
            commentMapper.deleteByExample(commentExample);

            if(commentMapper.deleteByPrimaryKey(id) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.COMMENT_DELETE_ERROR);
            }

            Article article = articleMapper.selectByPrimaryKey(comment.getArticleId());
            CommentExample commentCountExample = new CommentExample();
            commentCountExample.createCriteria().andArticleIdEqualTo(article.getId());
            article.setCommentNum(commentMapper.countByExample(commentCountExample));
            articleMapper.updateByPrimaryKeySelective(article);
        }
        return ResponseDTO.successByMsg(true, "Successfully deleted comment.");
    }

    /**
     * @return
     */
    @Override
    public ResponseDTO<Integer> countTotalComment(CommentDTO commentDTO) {
        if(CommonUtil.isEmpty(commentDTO.getArticleId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andArticleIdEqualTo(commentDTO.getArticleId());
        return ResponseDTO.success(commentMapper.countByExample(commentExample));
    }

    /**
     * @param commentDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> pickComment(CommentDTO commentDTO) {
        if(CommonUtil.isEmpty(commentDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        Comment comment = commentMapper.selectByPrimaryKey(commentDTO.getId());
        if(comment == null) {
            return ResponseDTO.errorByMsg(CodeMsg.COMMENT_NOT_EXIST);
        }
        Article article = articleMapper.selectByPrimaryKey(comment.getArticleId());
        if(article == null) {
            return ResponseDTO.errorByMsg(CodeMsg.ARTICLE_NOT_EXIST);
        }
        comment.setPick(CommentPickEnum.YES.getCode());
        if(commentMapper.updateByPrimaryKeySelective(comment) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.COMMENT_PICK_ERROR);
        }

        article.setState(ArticleStateEnum.SOLVE.getCode());
        articleMapper.updateByPrimaryKeySelective(article);
        return ResponseDTO.successByMsg(true, "Successfully adopted solution.");
    }

    /**
     * @return
     */
    @Override
    public ResponseDTO<Integer> getCommentTotal() {
        return ResponseDTO.success(commentMapper.countByExample(new CommentExample()));
    }
}
