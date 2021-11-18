package com.witsoft.weilai.controller.iot;


import com.witsoft.weilai.common.BaseController;
import com.witsoft.weilai.common.dto.StationRateDTO;
import com.witsoft.weilai.service.iot.StationRateService;
import com.witsoft.weilai.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Api(value = "工位详情统计", tags = "工位信息统计接口")
@RestController
@RequestMapping("/station")
public class StationRateController extends BaseController {

    @Resource
    private StationRateService stationRateService;


    @ApiOperation("获取工位统计详情")
    @GetMapping("/info")
    public Result getStationInfo(@RequestParam Integer stationId){

        StationRateDTO stationInfo = stationRateService.getStationInfo(stationId);

        return Result.success(stationInfo);
    }
}
