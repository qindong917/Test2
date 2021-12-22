package com.qd.cjb.service.serviceimpl;

import com.qd.cjb.common.idworker.Sid;
import com.qd.cjb.mapper.UsersReportMapper;
import com.qd.cjb.pojo.UsersReport;
import com.qd.cjb.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @version : 1.0
 * @auther : hjx
 * @Date : 2021/6/2
 * @Description : short-video
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private UsersReportMapper usersReportMapper;
    @Autowired
    private Sid sid;

    @Override
    public void addReport(UsersReport report) {
        String id = sid.nextShort();
        report.setId(id);
        report.setCreateDate(new Date());
        usersReportMapper.insert(report);
    }
}
