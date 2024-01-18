package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.*;
import com.yjq.programmer.dao.my.MyArticleMapper;
import com.yjq.programmer.domain.*;
import com.yjq.programmer.dto.*;
import com.yjq.programmer.enums.ArticleQueryTypeEnum;
import com.yjq.programmer.enums.ArticleStateEnum;
import com.yjq.programmer.enums.ArticleTypeEnum;
import com.yjq.programmer.service.IArticleService;
import com.yjq.programmer.service.ITagService;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.CopyUtil;
import com.yjq.programmer.utils.UuidUtil;
import com.yjq.programmer.utils.ValidateEntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@Transactional
@Service
public class   ArticleServiceImpl implements IArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ITagService tagService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private LikeMapper likeMapper;

    @Resource
    private CollectMapper collectMapper;

    @Resource
    private MyArticleMapper myArticleMapper;

    @Resource
    private TagItemMapper tagItemMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private CommentMapper commentMapper;


    @Override
    public ResponseDTO<ArticleDTO> saveArticle(ArticleDTO articleDTO) {

        CodeMsg validate = ValidateEntityUtil.validate(articleDTO);
        if (!validate.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            return ResponseDTO.errorByMsg(validate);
        }

        if(articleDTO.getTagList() == null || articleDTO.getTagList().length() == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.ARTICLE_TAG_EMPTY);
        }
        String[] splitTag = articleDTO.getTagList().split(";");
        if(splitTag.length > 3) {
            return ResponseDTO.errorByMsg(CodeMsg.ARTICLE_TAG_OVER);
        }
        TagItemDTO tagItemDTO = new TagItemDTO();
        tagItemDTO.setTagIdList(splitTag);
        Article article = CopyUtil.copy(articleDTO, Article.class);
        if(CommonUtil.isEmpty(article.getId())) {

            article.setId(UuidUtil.getShortUuid());
            tagItemDTO.setArticleId(article.getId());
            tagService.saveTagItem(tagItemDTO);
            article.setCreateTime(new Date());
            article.setUpdateTime(new Date());
            if(articleMapper.insertSelective(article) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.ARTICLE_ADD_ERROR);
            }
        } else {

            tagItemDTO.setArticleId(article.getId());
            tagService.saveTagItem(tagItemDTO);
            article.setUpdateTime(new Date());
            if(articleMapper.updateByPrimaryKeySelective(article) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.ARTICLE_EDIT_ERROR);
            }
        }
        ResponseDTO<ArticleDTO> responseDTO = getArticleById(CopyUtil.copy(article, ArticleDTO.class));
        if(!CodeMsg.SUCCESS.getCode().equals(responseDTO.getCode())) {
            return responseDTO;
        } else {
            return ResponseDTO.successByMsg(CopyUtil.copy(responseDTO.getData(), ArticleDTO.class), "Successfully saved draft");
        }
    }

    /**
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<ArticleDTO>> getArticleList(PageDTO<ArticleDTO> pageDTO) {
        ArticleExample articleExample = new ArticleExample();

        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }

        if(pageDTO.getSize() == null){
            pageDTO.setSize(5);
        }
        ArticleExample.Criteria c1 = articleExample.createCriteria();
        if(pageDTO.getParam() != null) {
            ArticleDTO articleDTO = pageDTO.getParam();
            if(!CommonUtil.isEmpty(articleDTO.getCategoryId()) && !"0".equals(articleDTO.getCategoryId())) {
                c1.andCategoryIdEqualTo(articleDTO.getCategoryId());
            }
            if(!CommonUtil.isEmpty(articleDTO.getTitle())) {
                c1.andTitleLike("%" + articleDTO.getTitle() + "%");
            }
            if(articleDTO.getType() != null && articleDTO.getType() != 0) {
                c1.andTypeEqualTo(articleDTO.getType());
            }
            if(articleDTO.getState() != null && articleDTO.getState() != 0) {
                c1.andStateEqualTo(articleDTO.getState());
            }
            if(articleDTO.getState() == null) {
                List<Integer> stateList = new ArrayList<>();
                stateList.add(ArticleStateEnum.WAIT.getCode());
                stateList.add(ArticleStateEnum.DRAFT.getCode());
                stateList.add(ArticleStateEnum.FAIL.getCode());
                c1.andStateNotIn(stateList);
            }
            if(!CommonUtil.isEmpty(articleDTO.getUserId())
                    && !ArticleQueryTypeEnum.LIKE.getCode().equals(articleDTO.getQueryType())
                    && !ArticleQueryTypeEnum.COLLECT.getCode().equals(articleDTO.getQueryType())) {
                c1.andUserIdEqualTo(articleDTO.getUserId());
            }
            if(ArticleQueryTypeEnum.BLOG.getCode().equals(articleDTO.getQueryType())) {
                c1.andTypeEqualTo(ArticleTypeEnum.BLOG.getCode());
            }
            if(ArticleQueryTypeEnum.FORUM.getCode().equals(articleDTO.getQueryType())) {
                c1.andTypeEqualTo(ArticleTypeEnum.FORUM.getCode());
            }
            if(ArticleQueryTypeEnum.LIKE.getCode().equals(articleDTO.getQueryType())) {
                LikeExample likeExample = new LikeExample();
                likeExample.createCriteria().andUserIdEqualTo(articleDTO.getUserId());
                List<Like> likeList = likeMapper.selectByExample(likeExample);
                List<String> articleIdList = likeList.stream().map(Like::getArticleId).collect(Collectors.toList());
                if(articleIdList.size() == 0) {
                    articleIdList.add("-1");
                }
                c1.andIdIn(articleIdList);
            }
            if(ArticleQueryTypeEnum.COLLECT.getCode().equals(articleDTO.getQueryType())) {
                CollectExample collectExample = new CollectExample();
                collectExample.createCriteria().andUserIdEqualTo(articleDTO.getUserId());
                List<Collect> collectList = collectMapper.selectByExample(collectExample);
                List<String> articleIdList = collectList.stream().map(Collect::getArticleId).collect(Collectors.toList());
                if(articleIdList.size() == 0) {
                    articleIdList.add("-1");
                }
                c1.andIdIn(articleIdList);
            }
        }
        articleExample.setOrderByClause("top desc, essence desc, official desc, create_time desc");
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());

        List<Article> articleList = articleMapper.selectByExample(articleExample);
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);

        pageDTO.setTotal(pageInfo.getTotal());

        List<ArticleDTO> articleDTOList = CopyUtil.copyList(articleList, ArticleDTO.class);
        for(ArticleDTO articleDTO : articleDTOList) {

            User user = userMapper.selectByPrimaryKey(articleDTO.getUserId());
            articleDTO.setUserDTO(CopyUtil.copy(user, UserDTO.class));

            TagItemExample tagItemExample = new TagItemExample();
            tagItemExample.createCriteria().andArticleIdEqualTo(articleDTO.getId());
            List<TagItem> tagItemList = tagItemMapper.selectByExample(tagItemExample);
            List<String> tagIdList = tagItemList.stream().map(TagItem::getTagId).collect(Collectors.toList());
            List<Tag> tagList;
            if(tagIdList.size() == 0) {
                tagList = new ArrayList<>();
            } else {
                TagExample tagExample = new TagExample();
                tagExample.createCriteria().andIdIn(tagIdList);
                tagList = tagMapper.selectByExample(tagExample);
            }
            articleDTO.setTagDTOList(CopyUtil.copyList(tagList, TagDTO.class));

            Category category = categoryMapper.selectByPrimaryKey(articleDTO.getCategoryId());
            if(category == null) {
                articleDTO.setCategoryDTO(CopyUtil.copy(new Category(), CategoryDTO.class));
            } else {
                articleDTO.setCategoryDTO(CopyUtil.copy(category, CategoryDTO.class));
            }
        }
        pageDTO.setList(articleDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * @param articleDTO
     * @return
     */
    @Override
    public ResponseDTO<ArticleDTO> getArticleById(ArticleDTO articleDTO) {
        if(CommonUtil.isEmpty(articleDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        Article article = articleMapper.selectByPrimaryKey(articleDTO.getId());
        if(article == null) {
            return ResponseDTO.errorByMsg(CodeMsg.ARTICLE_NOT_EXIST);
        }
        ArticleDTO articleDTODB = CopyUtil.copy(article, ArticleDTO.class);

        User user = userMapper.selectByPrimaryKey(articleDTODB.getUserId());
        articleDTODB.setUserDTO(CopyUtil.copy(user, UserDTO.class));

        TagItemExample tagItemExample = new TagItemExample();
        tagItemExample.createCriteria().andArticleIdEqualTo(articleDTODB.getId());
        List<TagItem> tagItemList = tagItemMapper.selectByExample(tagItemExample);
        List<String> tagIdList = tagItemList.stream().map(TagItem::getTagId).collect(Collectors.toList());
        List<Tag> tagList = new ArrayList<>();
        if(tagItemList.size() > 0) {
            TagExample tagExample = new TagExample();
            tagExample.createCriteria().andIdIn(tagIdList);
            tagList = tagMapper.selectByExample(tagExample);
        }
        articleDTODB.setTagDTOList(CopyUtil.copyList(tagList, TagDTO.class));

        Category category = categoryMapper.selectByPrimaryKey(articleDTODB.getCategoryId());
        if(category == null) {
            articleDTODB.setCategoryDTO(CopyUtil.copy(new Category(), CategoryDTO.class));
        } else {
            articleDTODB.setCategoryDTO(CopyUtil.copy(category, CategoryDTO.class));
        }
        return ResponseDTO.success(articleDTODB);
    }

    /**
     * @param articleDTO
     * @return
     */
    @Override
    public ResponseDTO<ArticleDTO> viewArticle(ArticleDTO articleDTO) {

        Article article = articleMapper.selectByPrimaryKey(articleDTO.getId());
        article.setViewNum(article.getViewNum() + 1);
        articleMapper.updateByPrimaryKeySelective(article);
        ResponseDTO<ArticleDTO> responseDTO = getArticleById(articleDTO);
        return responseDTO;
    }

    /**
     * @param articleDTO
     * @return
     */
    @Override
    public ResponseDTO<List<ArticleDTO>> getHotArticleList(ArticleDTO articleDTO) {
        ArticleExample articleExample = new ArticleExample();
        List<Integer> stateList = new ArrayList<>();
        stateList.add(ArticleStateEnum.WAIT.getCode());
        stateList.add(ArticleStateEnum.DRAFT.getCode());
        stateList.add(ArticleStateEnum.FAIL.getCode());
        if(articleDTO.getType() != null) {
            articleExample.createCriteria().andTypeEqualTo(articleDTO.getType()).andStateNotIn(stateList);
        } else {
            articleExample.createCriteria().andStateNotIn(stateList);
        }
        articleExample.setOrderByClause("view_num desc, like_num desc, comment_num desc");
        PageHelper.startPage(1, 5);
        List<Article> articleList = articleMapper.selectByExample(articleExample);
        List<ArticleDTO> articleDTOList = CopyUtil.copyList(articleList, ArticleDTO.class);
        return ResponseDTO.success(articleDTOList);
    }

    /**
     * @param articleDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> updateArticleInfo(ArticleDTO articleDTO) {
        if(CommonUtil.isEmpty(articleDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        Article article = CopyUtil.copy(articleDTO,  Article.class);
        if(articleMapper.updateByPrimaryKeySelective(article) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.ARTICLE_EDIT_ERROR);
        }
        return ResponseDTO.successByMsg(true, "Successfully modified article.");
    }

    /**
     * @param articleDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteArticle(ArticleDTO articleDTO) {
        if(CommonUtil.isEmpty(articleDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        String[] ids = articleDTO.getId().split(",");
        for(String id : ids) {

            TagItemExample tagItemExample = new TagItemExample();
            tagItemExample.createCriteria().andArticleIdEqualTo(articleDTO.getId());
            tagItemMapper.deleteByExample(tagItemExample);

            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria().andArticleIdEqualTo(articleDTO.getId());
            commentMapper.deleteByExample(commentExample);

            LikeExample likeExample = new LikeExample();
            likeExample.createCriteria().andArticleIdEqualTo(articleDTO.getId());
            likeMapper.deleteByExample(likeExample);

            CollectExample collectExample = new CollectExample();
            collectExample.createCriteria().andArticleIdEqualTo(articleDTO.getId());
            collectMapper.deleteByExample(collectExample);

            if(articleMapper.deleteByPrimaryKey(id) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.ARTICLE_DELETE_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "Successfully deleted article.");
    }

    /**

     * @param articleDTO
     * @return
     */
    @Override
    public ResponseDTO<List<ArticleDTO>> getAuthorArticleList(ArticleDTO articleDTO) {
        if(CommonUtil.isEmpty(articleDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        Article article = articleMapper.selectByPrimaryKey(articleDTO.getId());
        ArticleExample articleExample = new ArticleExample();
        List<Integer> stateList = new ArrayList<>();
        stateList.add(ArticleStateEnum.WAIT.getCode());
        stateList.add(ArticleStateEnum.DRAFT.getCode());
        stateList.add(ArticleStateEnum.FAIL.getCode());
        articleExample.createCriteria().andUserIdEqualTo(article.getUserId()).andStateNotIn(stateList);
        PageHelper.startPage(1, 5);
        List<Article> articleList = articleMapper.selectByExample(articleExample);
        List<ArticleDTO> articleDTOList = CopyUtil.copyList(articleList, ArticleDTO.class);
        return ResponseDTO.success(articleDTOList);
    }

    /**
     * @param articleDTO
     * @return
     */
    @Override
    public ResponseDTO<Integer> getArticleTotal(ArticleDTO articleDTO) {
        ArticleExample articleExample = new ArticleExample();
        if(articleDTO.getType() != null) {
            articleExample.createCriteria().andTypeEqualTo(articleDTO.getType());
        }
        return ResponseDTO.success(articleMapper.countByExample(articleExample));
    }

    /**
     * @return
     */
    @Override
    public ResponseDTO<List<Integer>> getArticleTotalByDay() {
        List<Integer> totalList = new ArrayList<>();
        Map<String, Object> queryMap = new HashMap<>();

        queryMap.put("start", 2);
        queryMap.put("end", 1);
        totalList.add(myArticleMapper.getArticleTotalByDate(queryMap));

        queryMap.put("start", 1);
        queryMap.put("end", 0);
        totalList.add(myArticleMapper.getArticleTotalByDate(queryMap));

        queryMap.put("start", 0);
        queryMap.put("end", -1);
        totalList.add(myArticleMapper.getArticleTotalByDate(queryMap));
        return ResponseDTO.success(totalList);
    }

}
