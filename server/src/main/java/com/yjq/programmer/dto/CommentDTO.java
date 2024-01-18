package com.yjq.programmer.dto;

import com.yjq.programmer.annotation.ValidateEntity;

import java.util.Date;
import java.util.List;


public class CommentDTO {

    private String id;

    private String fromId;

    private UserDTO fromUserDTO;

    private String toId;

    private UserDTO toUserDTO;

    private String parentId;

    private String articleId;

    private ArticleDTO articleDTO;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=512,minLength=1,errorRequiredMsg="Comment cannot be empty.",errorMaxLengthMsg="Comment cannot be longer than 512.",errorMinLengthMsg="Comment cannot be empty.")
    private String content;

    private Integer type;

    private Integer pick;

    private Date createTime;

    private Boolean isCollapse;

    private List<CommentDTO> childrenList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPick() {
        return pick;
    }

    public void setPick(Integer pick) {
        this.pick = pick;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<CommentDTO> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<CommentDTO> childrenList) {
        this.childrenList = childrenList;
    }

    public UserDTO getFromUserDTO() {
        return fromUserDTO;
    }

    public void setFromUserDTO(UserDTO fromUserDTO) {
        this.fromUserDTO = fromUserDTO;
    }

    public UserDTO getToUserDTO() {
        return toUserDTO;
    }

    public void setToUserDTO(UserDTO toUserDTO) {
        this.toUserDTO = toUserDTO;
    }

    public Boolean getCollapse() {
        return isCollapse;
    }

    public void setCollapse(Boolean collapse) {
        isCollapse = collapse;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public ArticleDTO getArticleDTO() {
        return articleDTO;
    }

    public void setArticleDTO(ArticleDTO articleDTO) {
        this.articleDTO = articleDTO;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", fromId=").append(fromId);
        sb.append(", toId=").append(toId);
        sb.append(", parentId=").append(parentId);
        sb.append(", content=").append(content);
        sb.append(", type=").append(type);
        sb.append(", pick=").append(pick);
        sb.append(", createTime=").append(createTime);
        sb.append(", childrenList=").append(childrenList);
        sb.append(", fromUserDTO=").append(fromUserDTO);
        sb.append(", toUserDTO=").append(toUserDTO);
        sb.append(", isCollapse=").append(isCollapse);
        sb.append(", articleId=").append(articleId);
        sb.append(", articleDTO=").append(articleDTO);
        sb.append("]");
        return sb.toString();
    }
}
