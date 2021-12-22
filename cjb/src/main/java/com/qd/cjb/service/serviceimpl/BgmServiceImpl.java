package com.qd.cjb.service.serviceimpl;

import com.qd.cjb.mapper.BgmMapper;
import com.qd.cjb.pojo.Bgm;
import com.qd.cjb.service.BgmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @version : 1.0
 * @auther : hjx
 * @Date : 2021/2/1
 * @Description : nuc.edu.hjx.service.serviceImpl
 */
@Service
public class BgmServiceImpl implements BgmService {

    @Autowired
    private BgmMapper bgmMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Bgm> queryAllBgm() {
        return bgmMapper.selectAll();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Bgm queryByBgmId(String BgmId) {
        return bgmMapper.selectByPrimaryKey(BgmId);
    }
}
