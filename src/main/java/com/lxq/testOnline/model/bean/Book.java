package com.lxq.testOnline.model.bean;

import lombok.Data;


/**
 * book表结构

 */
@Data
public class Book {
    private Integer id;
    private String name;
    private String category;
    private String pic;
}
