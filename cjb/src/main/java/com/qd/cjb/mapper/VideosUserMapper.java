package com.qd.cjb.mapper;

import com.qd.cjb.common.utils.MyMapper;
import com.qd.cjb.pojo.Videos;
import com.qd.cjb.pojo.vo.VideosVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideosUserMapper extends MyMapper<Videos> {

    List<VideosVo> queryAllVideos();

    List<VideosVo> queryVideoByDesc(@Param("videoDesc") String videoDesc);

    List<VideosVo> queryMyVideos(@Param("userId") String userId);

    List<VideosVo> queryMyLikes(@Param("userId") String userId);

    List<VideosVo> queryMyFollows(@Param("userId") String suerId);
}
