package com.qd.cjb.mapper;

import com.qd.cjb.common.utils.MyMapper;
import com.qd.cjb.pojo.Videos;

public interface VideosMapper extends MyMapper<Videos> {

    void addLikeVideo(String videoId);

    void reduceLikeVideo(String videoId);
}
