package com.qd.cjb.mapper;

import com.qd.cjb.common.utils.MyMapper;
import com.qd.cjb.pojo.SearchRecords;

import java.util.List;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {

    List<String> getAllHotWord();
}
