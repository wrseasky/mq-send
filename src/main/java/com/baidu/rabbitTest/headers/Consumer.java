package com.baidu.rabbitTest.headers;

import com.rabbitmq.client.*;
import org.springframework.amqp.core.ExchangeTypes;

import java.util.Hashtable;
import java.util.Map;

public class Consumer {
    private final static String EXCHANGE_NAME = "header-exchange";
    private final static String QUEUE_NAME = "header-queue";

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
        channel.queueDeclare(QUEUE_NAME, false, false, true, null);

        Map<String, Object> headers = new Hashtable<String, Object>();
        headers.put("x-match", "any");//all any all代表定义的多个键值对都要满足，而any则代码只要满足一个就可以了。
        headers.put("aaa", "01234");
        headers.put("bbb", "56789");
        // 为转发器指定队列，设置binding 绑定header键值对
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "", headers);
        QueueingConsumer consumer = new QueueingConsumer(channel);
        // 指定接收者，第二个参数为自动应答，无需手动应答
        channel.basicConsume(QUEUE_NAME, true, consumer);
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(message);
        }
    }
}