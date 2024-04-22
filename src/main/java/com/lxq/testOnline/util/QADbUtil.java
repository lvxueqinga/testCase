package com.lxq.testOnline.util;

import com.alibaba.fastjson.JSONObject;
import com.lxq.testOnline.dao.BookMapper;
import com.lxq.testOnline.model.bean.Book;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class QADbUtil {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SqlSession sqlSessionOnline = null;
    private SqlSessionFactory sessionFactoryOnline = null;

    public QADbUtil() {
        logger.debug("initial QADbUtil");
    }

    public void openOnlineConnection() {
        logger.debug("open online db connection");
        String resource = "configuration-online.xml";
        try {
            sessionFactoryOnline = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(
                    resource));
            sqlSessionOnline = sessionFactoryOnline.openSession();
            logger.debug("sqlSessionOnline: " + sqlSessionOnline);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public List<JSONObject> search(Book req) {
        getSqlSessionOnline();
        BookMapper bookMapper = sqlSessionOnline.getMapper(BookMapper.class);
        return  bookMapper.searchBook(req);
    }

    public void getSqlSessionOnline() {
//        开启多线程，要暂时关闭此方法
        sqlSessionOnline.close();
        sqlSessionOnline = sessionFactoryOnline.openSession();
    }

    public void closeOnlineConnection() {
        logger.debug("close online db connection");
        sqlSessionOnline.close();
    }
}
