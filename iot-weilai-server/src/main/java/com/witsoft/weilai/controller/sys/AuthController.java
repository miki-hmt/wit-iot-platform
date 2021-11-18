package com.witsoft.weilai.controller.sys;

import com.alibaba.fastjson.JSON;
import com.witsoft.weilai.common.BaseController;
import com.witsoft.weilai.domain.SysUser;
import com.witsoft.weilai.service.sys.SysUserService;
import com.witsoft.weilai.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author: wit
 * @create 2020/9/15
 * @Description: 登录控制器
 **/
@Slf4j
@Api(tags = "用户认证登录信息接口", value = "用户认证xxx相关接口")
@RestController
public class AuthController extends BaseController {

    @Resource
    private SysUserService sysUserService;

    /**
     * 用户登录
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value="用户登录", notes="用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user", value="登录表单", required=true, paramType="form", dataType="SysUser"),
    })
    public ResponseResult getLogin(@Valid SysUser user){
        try {
            SysUser sysUser = sysUserService.getLogin(user.getLoginName(), user.getPassword());
            if(sysUser != null){
                return ResponseResult.ok(sysUser.getToken());
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseResult.build(-1,"登录失败");
    }

    /**
     * 退出登录
     * @param token
     * @return
     */
    @PostMapping("/outLogin")
    @ApiOperation(value="退出登录", notes="退出登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token", value="token", required=true, paramType="query", dataType="String", dataTypeClass = String.class)
    })
    public ResponseResult outLogin(String token){
        try {
            this.redisService.del(token);
            return ResponseResult.ok();
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseResult.error();
    }

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/getUserInfo")
    @ApiOperation(value="获取用户信息", notes="获取用户信息")
    public String getUserInfo1(){
        SysUser userInfo = getUserInfo();
        return JSON.toJSONString(userInfo);
    }

}
