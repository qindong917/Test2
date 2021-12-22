package com.qd.cjb.api.controller;

import com.qd.cjb.common.enums.VideoStatusEnum;
import com.qd.cjb.common.utils.JSONResult;
import com.qd.cjb.common.utils.MergeVideoBgm;
import com.qd.cjb.common.utils.PagedResult;
import com.qd.cjb.pojo.Bgm;
import com.qd.cjb.pojo.UsersReport;
import com.qd.cjb.pojo.Videos;
import com.qd.cjb.service.BgmService;
import com.qd.cjb.service.RecordService;
import com.qd.cjb.service.ReportService;
import com.qd.cjb.service.VideoService;
import io.swagger.annotations.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @author Administrator
 * @version : 1.0
 * @auther : hjx
 * @Date : 2021/2/1
 * @Description : nuc.edu.hjx.controller
 */
@RestController
@RequestMapping("/video")
@Api(value = "视频接口", tags = "视频的测试接口")
public class VideoController extends BatisController{

    @Autowired
    private BgmService bgmService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private RecordService recordService;
    @Autowired
    private ReportService reportService;

    /**
     * 上传视频接口
     * @param userId
     * @param bgmId
     * @param videoSecound
     * @param videoWidth
     * @param videoHeight
     * @param desc
     * @param file
     * @return
     */
    @ApiOperation(value = "上传视频", notes = "上传视频接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户Id",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(name = "bgmId",value = "bgmId",dataType = "String",paramType = "form"),
            @ApiImplicitParam(name = "videoSecound",value = "视频时长",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(name = "videoWidth",value = "视频宽度",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(name = "videoHeight",value = "视频长度",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(name = "desc",value = "视频描述",dataType = "String",paramType = "form"),
    })
    @PostMapping(value = "/uploadvideo", headers ="content-type=multipart/form-data")
    public JSONResult uploadVideo(String userId, String bgmId, double videoSecound, int videoWidth, int videoHeight,
                                String desc, @ApiParam(value = "视频文件",required = true)MultipartFile file) throws Exception {
        //判断userId是否为空
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("账号不能为空！");
        }
        //获取文件的资源路径
        String uploadDb = "/user/" + userId + "/video/";
        String filename = file.getOriginalFilename();

        String videoOutputPath = null;
        String coverPath = null;

        if (StringUtils.isNotBlank(filename)) {
            File video = new File(USER_PATH + uploadDb + filename);
            //对文件的父文件夹进行查询，不存在就创建新文件夹
            if (video.getParentFile() !=null && !video.getParentFile().isDirectory()) {
                video.getParentFile().mkdirs();
            }
            try {
                videoOutputPath = USER_PATH + uploadDb + filename;
                FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(USER_PATH + uploadDb + filename));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (StringUtils.isNotBlank(bgmId)) {
                Bgm bgm = bgmService.queryByBgmId(bgmId);
                String bgmPath = USER_PATH + bgm.getPath();
                MergeVideoBgm tool = new MergeVideoBgm(FFMPEG_EXE);
                String videoInputPath = USER_PATH + uploadDb + filename;
                videoOutputPath = uploadDb + UUID.randomUUID().toString() + ".mp4";
                tool.convert(videoInputPath,bgmPath,USER_PATH + videoOutputPath,videoSecound);
                video.delete();
                coverPath = getCoverPath(userId,USER_PATH + videoOutputPath);

            }

            coverPath = getCoverPath(userId,videoOutputPath);

            Videos videos = new Videos();
            videos.setUserId(userId);
            videos.setAudioId(bgmId);
            videos.setVideoSeconds((float)videoSecound);
            videos.setVideoWidth(videoWidth);
            videos.setVideoHeight(videoHeight);
            videos.setVideoDesc(desc);
            videos.setVideoPath(uploadDb + filename);
            videos.setCoverPath(coverPath);
            videos.setCreateTime(new Date());
            videos.setStatus(VideoStatusEnum.SUCCESS.value);
            videoService.saveVideo(videos);
            return JSONResult.ok();
        }

        return JSONResult.errorMsg("上传视频失败！");
    }

    /**
     * 生成视频前一秒截图当做封面
     * @param userId
     * @param videoIntputPath
     * @return String
     * @throws IOException
     */
    public String getCoverPath(String userId ,String videoIntputPath) throws IOException {
        String uploadDb = "/user/" + userId + "/cover/";
        String covername = UUID.randomUUID().toString() + ".jpg";
        File cover = new File(USER_PATH + uploadDb + covername);
        if (cover.getParentFile() !=null && !cover.getParentFile().isDirectory()) {
            cover.getParentFile().mkdirs();
        }
        MergeVideoBgm tool = new MergeVideoBgm(FFMPEG_EXE);
        tool.getcover(videoIntputPath,USER_PATH + uploadDb + covername);
        return uploadDb + covername;
    }

    /**
     * 获取视频的分页视频信息
     * @param page
     * @return
     */
    @ApiOperation(value = "获取视频的分页数据",notes = "视频分页数据接口")
    @PostMapping("/showallvideo")
    public JSONResult showAllVideos(Integer page) {
        if (page == null) {
            page = 1;
        }
        PagedResult result = videoService.getAllVideos(page,PAGE_SIZE);
        return JSONResult.ok(result);
    }

    /**
     * 获取搜索视频分页
     * @param video
     * @param page
     * @return
     */
    @ApiOperation(value = "搜索视频分页展示",notes = "搜索视频分页展示接口")
    @PostMapping("/queryvideo")
    public JSONResult showQueryVideos(@RequestBody Videos video,Integer page) {
        //保存热搜词
        String record = video.getVideoDesc();
        if (StringUtils.isNotBlank(record)) {
            recordService.saveRecord(record);
        }
        //分页显示
        if (page == null) {
            page = 1;
        }
        //查询带有热搜词的视频
        PagedResult result = videoService.getqueryVideos(page,PAGE_SIZE,record);
        System.out.println(result.getRows());
        return JSONResult.ok(result);
    }

    /**
     * 对视频点赞
     * @param userId
     * @param videoId
     * @return
     */
    @ApiOperation(value = "点赞视频", notes = "点赞接口")
    @PostMapping("/like")
    public JSONResult likeVideo(String userId, String videoId) {
        if(userId == null || videoId == null) {
            return JSONResult.errorMsg("请重新登录");
        }
        videoService.addLikeCounts(userId,videoId);
        return JSONResult.ok();
    }

    /**
     * 取消点赞视频
     * @param userId
     * @param videoId
     * @return
     */
    @ApiOperation(value = "取消点赞", notes = "取消点赞接口")
    @PostMapping("/unlike")
    public JSONResult unlikeVideo(String userId, String videoId) {
        if(userId == null || videoId == null) {
            return JSONResult.errorMsg("请重新登录");
        }
        videoService.reduceLikeCounts(userId,videoId);
        return JSONResult.ok();
    }

    /**
     * 查询我的作品
     * @param userId
     * @param page
     * @return
     */
    @PostMapping("/myvideo")
    public JSONResult queryMyVideo(String userId, Integer page) {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.ok();
        }
        if (page == null) {
            page = 1;
        }
        PagedResult result = videoService.queryMyVideos(page,PAGE_SIZE + 1,userId);
        return JSONResult.ok(result);
    }

    /**
     * 查询我的收藏
     * @param userId
     * @param page
     * @return
     */
    @PostMapping("/mylike")
    public JSONResult queryMyLike(String userId, Integer page) {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.ok();
        }
        if (page == null) {
            page = 1;
        }
        PagedResult result = videoService.queryMyLikes(page,PAGE_SIZE + 1,userId);
        return JSONResult.ok(result);
    }

    /**
     * 查询我的关注
     * @param userId
     * @param page
     * @return
     */
    @ApiOperation(value = "我的关注", notes = "查询关注视频的接口")
    @PostMapping("/myfollow")
    public JSONResult queryMyFollow(String userId, Integer page) {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.ok();
        }
        if (page == null) {
            page = 1;
        }
        PagedResult result = videoService.queryMyFollows(page,PAGE_SIZE + 1,userId);
        return JSONResult.ok(result);
    }

    @PostMapping("/report")
    public JSONResult reportVideo(@RequestBody UsersReport report) {

        if (report == null) {
            return JSONResult.errorMsg("举报失败");
        }
        reportService.addReport(report);
        return JSONResult.ok("举报成功");

    }
}
