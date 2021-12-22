package com.qd.cjb.api.controller;

import com.qd.cjb.common.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version : 1.0
 * @auther : hjx
 * @Date : 2021/1/28
 * @Description : nuc.edu.hjx.Controller
 */
@RestController
public class BatisController {

    @Autowired
    public RedisOperator redisOperator;
    //用户redis会话
    public static final String USER_SESSION = "user_session";
    //用户路径
    public static final String USER_PATH = "/Users/qindong/Desktop/Imgs";
    //public static final String USER_PATH = "E:/Imgs";
    //ffmpeg路径
    public static final String FFMPEG_EXE = "/Users/qindong/ffmpeg/bin/ffmpeg";
    //public static final String FFMPEG_EXE = "C:\\\\Users\\\\Administrator\\\\Downloads\\\\ffmpeg_124162\\\\ffmpeg-20200315-c467328-win64-static\\\\bin\\\\ffmpeg.exe";


    public static final Integer PAGE_SIZE = 5;
}
