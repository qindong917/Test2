package com.qd.cjb.service;

import java.util.List;

/**
 * @version : 1.0
 * @auther : hjx
 * @Date : 2021/3/9
 * @Description : short-video
 */
public interface RecordService {

    /**
     * 保存热搜词
     * @param record
     */
    void saveRecord(String record);

    /**
     * 获取热搜词
     * @return Lsit<String>
     */
    List<String> getAllRecord();
}
