package com.witsoft.gongli.model;

import com.witsoft.gongli.device.enums.MachineStatusEnum;
import lombok.Data;

import java.util.Date;

@Data
public class DeviceStatus {

    private String deviceName;
    //设备：0：故障 1：运行
    private String running;
    //良品数 --每次都返回总的数目，只需要更新到库中即可，不需要进行累加
    private Integer goodQuantity = 0;
    //差品数 --同上
    //给一个初始化值，防止数据计算的时候出现空指针
    private Integer badQuantity = 0;
    //事件产生时间
    private Date createDate;
    //机器是否在线：0：停机 1：开机
    private String machinesOnline;


    public Integer getGoodQuantity() {
        if(goodQuantity == null){
            return 0;
        }
        return goodQuantity;
    }

    public Integer getBadQuantity() {
        if(badQuantity == null){
            return 0;
        }
        return badQuantity;
    }

    /**
     * @desc 统一状态码
     */
    public MachineStatusEnum getStatus(){
        //!!设备开停机状态优先级高于运行状态，如果是关机状态，不处理running状态的数据
        if(null != machinesOnline){
            if(machinesOnline.equals(MachineStatusEnum.TURNING.getValue())){
                return MachineStatusEnum.TURNING;
            }
            return MachineStatusEnum.STOPPING;
        }

        if(null != running){
            if(MachineStatusEnum.RUNNING.getValue().equals(running)){
                return MachineStatusEnum.RUNNING;
            }
            return MachineStatusEnum.WAITING;
        }

        return null;
    }


    public MachineStatusEnum getRunningStatus(){
        //!!设备开停机状态优先级高于运行状态，如果是关机状态，不处理running状态的数据
        if(null != running){
            if(MachineStatusEnum.RUNNING.getValue().equals(running)){
                return MachineStatusEnum.RUNNING;
            }
            return MachineStatusEnum.WAITING;
        }

        //fixed(2021.11.12)：没用待机，运行事件。默认走开机，关机判断
        return getStatus();
    }
}
