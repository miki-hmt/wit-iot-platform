package com.witsoft.weilai.controller.iot;


import com.witsoft.weilai.common.BaseController;
import com.witsoft.weilai.common.dto.DeviceWarningDTO;
import com.witsoft.weilai.service.iot.DeviceWarningService;
import com.witsoft.weilai.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@Api(value = "设备告警信息相关接口", tags = "设备告警详情")
@RequestMapping("/warning")
@RestController
public class deviceWarningController extends BaseController {

    @Resource
    private DeviceWarningService warningService;

    @ApiOperation(value = "获取车间告警列表")
    @GetMapping("/list")
    public Result allList(){
        List<DeviceWarningDTO> allList = warningService.getAllList();

        return Result.success(allList);
    }
}
