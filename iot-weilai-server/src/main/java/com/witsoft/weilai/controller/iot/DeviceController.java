package com.witsoft.weilai.controller.iot;

import com.witsoft.weilai.common.BaseController;
import com.witsoft.weilai.domain.iot.Device;
import com.witsoft.weilai.service.iot.DeviceService;
import com.witsoft.weilai.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "设备相关信息接口", value = "设备详情相关接口")
@RestController
@RequestMapping("/device")
public class DeviceController extends BaseController {

    @Resource
    private DeviceService deviceService;


    @ApiOperation(value = "设备列表接口")
    @GetMapping("/list")
    public Result deviceList(){

        List<Device> list = deviceService.list();

        return Result.success(list);
    }
}
