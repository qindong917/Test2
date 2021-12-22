package com.qd.cjb.service.serviceimpl;

import com.qd.cjb.common.idworker.Sid;
import com.qd.cjb.mapper.SearchRecordsMapper;
import com.qd.cjb.pojo.SearchRecords;
import com.qd.cjb.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @version : 1.0
 * @auther : hjx
 * @Date : 2021/3/9
 * @Description : short-video
 */
@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private Sid sid;

    @Autowired
    private SearchRecordsMapper searchRecordsMapper;

    /**
     * 保存搜索记录
     * @param record
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveRecord(String record) {
        String recordId = sid.nextShort();
        SearchRecords searchRecords = new SearchRecords();
        searchRecords.setId(recordId);
        searchRecords.setContent(record);
        searchRecordsMapper.insert(searchRecords);
    }

    /**
     * 获取热搜词
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<String> getAllRecord() {
        return searchRecordsMapper.getAllHotWord();
    }
}
