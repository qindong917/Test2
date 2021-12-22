package com.qd.cjb.api.controller;

import com.qd.cjb.common.utils.JSONResult;
import com.qd.cjb.service.RecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @version : 1.0
 * @auther : hjx
 * @Date : 2021/3/10
 * @Description : short-video
 */
@RestController
@RequestMapping("/record")
@Api(value = "搜索记录", tags = "搜索记录的接口")
public class recordController {

    @Autowired
    private RecordService recordService;

    /**
     * 获取热搜词
     * @return
     */
    @ApiOperation(value = "获取热搜词" ,notes = "获取热搜词")
    @PostMapping("/allrecord")
    public JSONResult getAllRecord() {
        List<String> list = recordService.getAllRecord();
        return JSONResult.ok(list);
    }
}
