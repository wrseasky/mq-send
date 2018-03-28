package com.baidu.rabbitTest.headers;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.BasicProperties.Builder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.ExchangeTypes;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

@SuppressWarnings("all")
public class Producer {
    private final static String EXCHANGE_NAME = "header-exchange";

    public static void main(String[] args) throws Exception {
        // 创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        // 指定用户 密码
        factory.setUsername("admin");
        factory.setPassword("admin");
        // 指定端口
        factory.setPort(AMQP.PROTOCOL.PORT);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明转发器和类型headers
        channel.exchangeDeclare(EXCHANGE_NAME, ExchangeTypes.HEADERS, false, true, null);
        String message = new Date().toLocaleString() + " : log something";

        Map<String, Object> headers = new Hashtable<String, Object>();
        headers.put("bbb", "56789");
        Builder properties = new BasicProperties.Builder();
        properties.headers(headers);

        // 指定消息发送到的转发器,绑定键值对headers键值对
        channel.basicPublish(EXCHANGE_NAME, "", properties.build(), message.getBytes());

        System.out.println("Sent message :'" + message + "'");
        channel.close();
        connection.close();
    }
}
