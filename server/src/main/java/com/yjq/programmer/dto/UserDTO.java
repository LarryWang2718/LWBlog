package com.yjq.programmer.dto;

import com.yjq.programmer.annotation.ValidateEntity;

import java.util.Date;


public class UserDTO {

    private String id;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=8,minLength=1,errorRequiredMsg="Username cannot be empty.",errorMaxLengthMsg="Username cannot be larger than 8.",errorMinLengthMsg="Username cannot be empty.")
    private String username;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=16,minLength=6,errorRequiredMsg="User password cannot be empty.",errorMaxLengthMsg="User password cannot be longer than 16.",errorMinLengthMsg="User password cannot be shorter than 6.")
    private String password;

    private Integer roleId;

    private Date registerTime;

    private Integer sex;

    private String headPic;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=11,minLength=11,errorRequiredMsg="Phone number cannot be empty",errorMaxLengthMsg="Please enter 11-digit phone number",errorMinLengthMsg="Please enter 11-digit phone number")
    private String phone;

    @ValidateEntity(requiredMaxLength=true,maxLength=64,errorMaxLengthMsg="Profile cannot be more tha 64 letters.")
    private String info;

    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", roleId=").append(roleId);
        sb.append(", registerTime=").append(registerTime);
        sb.append(", sex=").append(sex);
        sb.append(", headPic=").append(headPic);
        sb.append(", phone=").append(phone);
        sb.append(", info=").append(info);
        sb.append(", token=").append(token);
        sb.append("]");
        return sb.toString();
    }
}
