package com.bonc.blog.entity;

/**
 * 博客类别实体
 */
public class BlogType {

    private Integer id; //id

    private String typeName; //博客类别名

    private Integer orderNo; //排序

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
}