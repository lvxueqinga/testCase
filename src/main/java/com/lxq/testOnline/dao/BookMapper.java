package com.lxq.testOnline.dao;

import com.alibaba.fastjson.JSONObject;

import com.lxq.testOnline.model.bean.Book;

import java.util.List;



public interface BookMapper {
    //查询全部
    List<JSONObject> searchBook(Book req);


}
