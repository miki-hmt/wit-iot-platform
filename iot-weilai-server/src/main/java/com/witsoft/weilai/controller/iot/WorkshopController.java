package com.witsoft.weilai.controller.iot;


import com.witsoft.weilai.domain.iot.Workshop;
import com.witsoft.weilai.service.iot.WorkshopService;
import com.witsoft.weilai.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@Api(value = "设备车间详情接口", tags = "设备车间详情")
@RestController
@RequestMapping("/workshop")
public class WorkshopController {

    @Resource
    private WorkshopService workshopService;


    @ApiOperation(value = "车间列表接口")
    @GetMapping("/list")
    public Result list(){
        List<Workshop> list = workshopService.getWorkshopList();

        return Result.success(list);
    }
}
