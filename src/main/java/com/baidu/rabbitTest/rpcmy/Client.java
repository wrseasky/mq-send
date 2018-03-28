package com.baidu.rabbitTest.rpcmy;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Client {
    private Connection connection;
    private String requestQueue = "rpc_queue";
    private String responseQueue;
    private QueueingConsumer consumer;
    private Channel channel;

    public Client() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setPort(AMQP.PROTOCOL.PORT);

        connection = factory.newConnection();
        channel = connection.createChannel();
        responseQueue = channel.queueDeclare().getQueue();

        consumer = new QueueingConsumer(channel);
    }

    public String call(String message) throws Exception {
        String response = null;
        String corrId = System.currentTimeMillis() + "";
        AMQP.BasicProperties build = new AMQP.BasicProperties.Builder().correlationId(corrId).replyTo(responseQueue).build();
        channel.basicPublish("", requestQueue, build, message.getBytes());

        channel.basicConsume(responseQueue, false, consumer);
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            if (corrId.equals(delivery.getProperties().getCorrelationId())) {
                response = new String(delivery.getBody());
                break;
            }
        }
        return response;
    }
}
