package com.lxq.testOnline.dao;

import com.alibaba.fastjson.JSONObject;

import com.lxq.testOnline.model.bean.Book;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookMapper {
    //查询全部
    List<JSONObject> searchBook(Book req);


}
