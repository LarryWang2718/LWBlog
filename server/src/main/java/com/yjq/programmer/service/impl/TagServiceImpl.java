package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.TagItemMapper;
import com.yjq.programmer.dao.TagMapper;
import com.yjq.programmer.domain.Tag;
import com.yjq.programmer.domain.TagExample;
import com.yjq.programmer.domain.TagItem;
import com.yjq.programmer.domain.TagItemExample;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.TagDTO;
import com.yjq.programmer.dto.TagItemDTO;
import com.yjq.programmer.service.ITagService;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.CopyUtil;
import com.yjq.programmer.utils.UuidUtil;
import com.yjq.programmer.utils.ValidateEntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
@Transactional
public class TagServiceImpl implements ITagService {

    @Resource
    private TagMapper tagMapper;

    @Resource
    private TagItemMapper tagItemMapper;

    /**
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<TagDTO>> getTagList(PageDTO<TagDTO> pageDTO) {
        TagExample tagExample = new TagExample();

        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }

        if(pageDTO.getSize() == null){
            pageDTO.setSize(5);
        }
        TagExample.Criteria c1 = tagExample.createCriteria();
        if(pageDTO.getParam() != null) {
            TagDTO tagDTO = pageDTO.getParam();
            c1.andNameLike("%" + tagDTO.getName() + "%");
        }
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());

        List<Tag> tagList = tagMapper.selectByExample(tagExample);
        PageInfo<Tag> pageInfo = new PageInfo<>(tagList);

        pageDTO.setTotal(pageInfo.getTotal());

        List<TagDTO> tagDTOList = CopyUtil.copyList(tagList, TagDTO.class);
        pageDTO.setList(tagDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * @param tagDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveTag(TagDTO tagDTO) {

        CodeMsg validate = ValidateEntityUtil.validate(tagDTO);
        if (!validate.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            return ResponseDTO.errorByMsg(validate);
        }
        Tag tag = CopyUtil.copy(tagDTO, Tag.class);
        if(CommonUtil.isEmpty(tag.getId())) {

            if(isNameExist(tag, "")){
                return ResponseDTO.errorByMsg(CodeMsg.TAG_NAME_EXIST);
            }
            tag.setId(UuidUtil.getShortUuid());
            if(tagMapper.insertSelective(tag) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.TAG_ADD_ERROR);
            }
        } else {

            if(isNameExist(tag, tag.getId())){
                return ResponseDTO.errorByMsg(CodeMsg.TAG_NAME_EXIST);
            }
            if(tagMapper.updateByPrimaryKeySelective(tag) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.TAG_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "Successfully saved tags.");
    }

    /**
     * @param tagDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteTag(TagDTO tagDTO) {
        if(CommonUtil.isEmpty(tagDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        String[] ids = tagDTO.getId().split(",");
        for(String id : ids) {

            TagItemExample tagItemExample = new TagItemExample();
            tagItemExample.createCriteria().andTagIdEqualTo(id);
            tagItemMapper.deleteByExample(tagItemExample);

            if(tagMapper.deleteByPrimaryKey(id) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.TAG_DELETE_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "Successfully deleted tags.");
    }

    /**
     * @return
     */
    @Override
    public ResponseDTO<List<TagDTO>> getAllTagList() {
        TagExample tagExample = new TagExample();
        List<Tag> tagList = tagMapper.selectByExample(tagExample);
        List<TagDTO> tagDTOList = CopyUtil.copyList(tagList, TagDTO.class);
        return ResponseDTO.success(tagDTOList);
    }

    /**
     * @param tagItemDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveTagItem(TagItemDTO tagItemDTO) {
        if(CommonUtil.isEmpty(tagItemDTO.getArticleId())) {
            return ResponseDTO.errorByMsg(CodeMsg.ARTICLE_NOT_EXIST);
        }
        if(tagItemDTO.getTagIdList() == null || tagItemDTO.getTagIdList().length == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.ARTICLE_TAG_EMPTY);
        }

        TagItemExample tagItemExample = new TagItemExample();
        tagItemExample.createCriteria().andArticleIdEqualTo(tagItemDTO.getArticleId());
        tagItemMapper.deleteByExample(tagItemExample);

        for(String tagId: tagItemDTO.getTagIdList()) {
            TagItem tagItem = new TagItem();
            tagItem.setId(UuidUtil.getShortUuid());
            tagItem.setArticleId(tagItemDTO.getArticleId());
            tagItem.setTagId(tagId);
            tagItemMapper.insertSelective(tagItem);
        }
        return ResponseDTO.successByMsg(true, "Successfully saved tag details.");
    }

    /**
     * @param tag
     * @param id
     * @return
     */
    public Boolean isNameExist(Tag tag, String id) {
        TagExample tagExample = new TagExample();
        tagExample.createCriteria().andNameEqualTo(tag.getName());
        List<Tag> selectedTagList = tagMapper.selectByExample(tagExample);
        if(selectedTagList != null && selectedTagList.size() > 0) {
            if(selectedTagList.size() > 1){
                return true;
            }
            if(!selectedTagList.get(0).getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
