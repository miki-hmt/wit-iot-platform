package com.wit.iot.mqtt.controller;


import com.wit.iot.mqtt.config.MqttGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mqtt")
public class MqttController {

    @Autowired
    private MqttGateway mqttGateway;

    @PostMapping("/send")
    public String sendMqtt(String sendData){

        mqttGateway.sendToMqtt(sendData, "wittest");

        return "ok";
    }
}
