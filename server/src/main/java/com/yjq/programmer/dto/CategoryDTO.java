package com.yjq.programmer.dto;

import com.yjq.programmer.annotation.ValidateEntity;


public class CategoryDTO {

    private String id;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=16,minLength=1,errorRequiredMsg="Article category cannot be empty.",errorMaxLengthMsg="Article category length cannot be longer than 16.",errorMinLengthMsg="Article category cannot be empty.")
    private String name;

    @ValidateEntity(required=true,requiredMaxValue=true,requiredMinValue=true,maxValue=10000,minValue=0,errorRequiredMsg="Article category sorting number cannot be empty.",errorMaxValueMsg="Article category sorting number cannot be larger than 10000.",errorMinValueMsg="Article category sorting number cannot be less than 0.")
    private Integer sort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", sort=").append(sort);
        sb.append("]");
        return sb.toString();
    }
}
