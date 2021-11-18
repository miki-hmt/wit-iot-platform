package com.witsoft.weilai.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.witsoft.weilai.common.annotation.DB;
import com.witsoft.weilai.dao.sys.SysUserDao;
import com.witsoft.weilai.domain.SysUser;
import com.witsoft.weilai.manager.redis.RedisService;
import com.witsoft.weilai.service.sys.SysUserService;
import com.witsoft.weilai.utils.Md5Utils;
import com.witsoft.weilai.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author: wit
 * @create 2020/9/15
 * @Description:  用户接口实现类
 **/
@Service
@Slf4j
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private RedisService redisService;

    @Resource
    private SysUserDao sysUserDao;


    /**
     * 用户登录
     * @param loginName   登录账号
     * @param password    密码
     * @return
     */
    @DB   //默认db1   @DB(DataSourceType.DB2)
    @Override
    public SysUser getLogin(String loginName, String password) {
        //用户登录
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getLoginName, loginName).eq(SysUser::getPassword, Md5Utils.string2Md5(password));
        SysUser sysUser = sysUserDao.selectOne(queryWrapper);
        if(ObjectUtils.isEmpty(sysUser)){
            return null;
        }

        //创建token并存入到redis
        Map<String, String> map = TokenUtil.createToken(sysUser);
        redisService.set(map.get("token"),map.get("userInfo"));

        sysUser.setToken(map.get("token"));
        log.info("token：{}",map.get("token"));
        return sysUser;
    }

}
