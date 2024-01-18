package com.yjq.programmer.dto;

import java.util.Date;


public class AttentionDTO {

    private String id;

    private String fromId;

    private UserDTO fromUserDTO;

    private String toId;

    private UserDTO toUserDTO;

    private Date createTime;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", fromId=").append(fromId);
        sb.append(", toId=").append(toId);
        sb.append(", createTime=").append(createTime);
        sb.append(", fromUserDTO=").append(fromUserDTO);
        sb.append(", toUserDTO=").append(toUserDTO);
        sb.append("]");
        return sb.toString();
    }
}
