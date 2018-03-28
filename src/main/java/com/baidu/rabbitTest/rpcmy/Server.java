package com.baidu.rabbitTest.rpcmy;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Server {
    private static final String resquestQueue = "rpc_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setPort(AMQP.PROTOCOL.PORT);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(resquestQueue, false, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            AMQP.BasicProperties properties = delivery.getProperties();
            String correlationId = properties.getCorrelationId();
            String replyTo = properties.getReplyTo();
            String receive = new String(delivery.getBody());
            System.out.println("receive : " + receive);
            receive += receive;
            channel.basicPublish("", replyTo, properties, receive.getBytes());

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        }
    }
}
