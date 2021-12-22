package com.qd.cjb.mapper;

import com.qd.cjb.common.utils.MyMapper;
import com.qd.cjb.pojo.Users;

public interface UsersMapper extends MyMapper<Users> {


    void addLikeCounts(String userId);

    void reduceLikeCounts (String userId);

    void addFans(String userId);

    void reduceFans(String userId);

    void addFollow(String userId);

    void reduceFollow(String userId);
}
