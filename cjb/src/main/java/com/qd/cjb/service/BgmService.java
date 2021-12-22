package com.qd.cjb.service;


import com.qd.cjb.pojo.Bgm;

import java.util.List;

/**
 * @version : 1.0
 * @auther : hjx
 * @Date : 2021/2/1
 * @Description : nuc.edu.hjx.service
 */
public interface BgmService {

    /**
     * 查询所有的bgm
     * @return List
     */
    List<Bgm> queryAllBgm();

    /**
     * 通过bgmid查询对应的bgm
     * @param BgmId
     * @return bgm
     */
    Bgm queryByBgmId(String BgmId);
}
