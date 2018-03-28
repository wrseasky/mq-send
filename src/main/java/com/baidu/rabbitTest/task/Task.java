package com.baidu.rabbitTest.task;

import com.rabbitmq.client.*;

public class Task {

    //队列名称
    private final static String QUEUE_NAME = "workqueue-durable";

    public static void main(String[] args) throws Exception {
        //创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        //指定用户 密码
        factory.setUsername("admin");
        factory.setPassword("admin");
        //指定端口
        factory.setPort(AMQP.PROTOCOL.PORT);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        boolean durable = true; //设置消息持久化  RabbitMQ不允许使用不同的参数重新定义一个队列，所以已经存在的队列，我们无法修改其属性。
        //声明队列
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

        //发送10条消息，依次在消息后面附加1-10个点
        for (int i = 5; i > 0; i--) {
            String dots = "";
            for (int j = 0; j <= i; j++) {
                dots += ".";
            }
            String message = "helloworld" + dots + dots.length();
            //MessageProperties.PERSISTENT_TEXT_PLAIN 标识我们的信息为持久化的
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println("Sent Message：'" + message + "'");
        }
        //关闭频道和资源
        channel.close();
        connection.close();
    }

}