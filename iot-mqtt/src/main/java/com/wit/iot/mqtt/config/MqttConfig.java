package com.wit.iot.mqtt.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import javax.annotation.PostConstruct;
import java.util.Objects;

@Slf4j
@Configuration
@IntegrationComponentScan
@Getter
@Setter
public class MqttConfig {

    public static final String OUTBOUND_CHANNEL = "mqttOutboundChannel";

    public static final String INPUT_CHANNEL = "mqttInputChannel";

    public static final String SUB_TOPICS = "PSimulation,Pressure,PSimulationPump,PSimulationPressure," +
            "PSimulationValve,PSimulationFlow,FSimulation,FSimulationPump,FSimulationPressure," +
            "FSimulationValve,FSimulationFlow,leak,blast,wittest";

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    @Value("${mqtt.serverURIs}")
    private String hostUrl;

    @Value("${mqtt.client.id}")
    private String clientId;

    @Value("${mqtt.topic}")
    private String defaultTopic;

    @Value("${mqtt.completion-timeout}")
    private int completionTimeout;   //连接超时

    @PostConstruct
    public void init() {
        log.debug("username:{} password:{} hostUrl:{} clientId :{}  defaultTopic：{}",
                this.username, this.password, this.hostUrl, this.clientId, this.defaultTopic);
    }

    @Bean
    public MqttPahoClientFactory clientFactory() {

        final MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{hostUrl});
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        final DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
        // 把配置里的 cleanSession 设为false，客户端掉线后 服务器端不会清除session，
        // 当重连后可以接收之前订阅主题的消息。当客户端上线后会接受到它离线的这段时间的消息
        options.setCleanSession(false);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        options.setKeepAliveInterval(20);
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean(value = OUTBOUND_CHANNEL)
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = OUTBOUND_CHANNEL)
    public MessageHandler mqttOutbound() {

        final MqttPahoMessageHandler handler = new MqttPahoMessageHandler(clientId, clientFactory());
        handler.setDefaultQos(1);
        handler.setDefaultRetained(false);
        handler.setDefaultTopic(defaultTopic);
        handler.setAsync(false);
        handler.setAsyncEvents(false);
        return handler;
    }

    /**
     * MQTT消息接收处理
     */
    //接收通道
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    //配置client,监听的配置的所有topics
    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(
                        clientId + "_inbound", clientFactory(), SUB_TOPICS.split(","));
        adapter.setCompletionTimeout(completionTimeout);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    //通过通道获取数据
    @Bean
    @ServiceActivator(inputChannel = INPUT_CHANNEL)
    public MessageHandler handler() {
        return message -> {
            String topic = Objects.requireNonNull(message.getHeaders().get("mqtt_receivedTopic")).toString();
            log.info("topic: {}", topic);
            String[] topics = SUB_TOPICS.split(",");
            for (String t : topics) {
                if (t.equals(topic)) {
                    log.info("payload: {}", message.getPayload().toString());
                }
            }
        };
    }

}
