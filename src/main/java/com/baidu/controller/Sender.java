package com.baidu.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import java.util.UUID;

@Component
public class Sender implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);

    private RabbitTemplate rabbitTemplate;

  @Autowired
    public Sender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        this.rabbitTemplate.setConfirmCallback(this);
        this.rabbitTemplate.setReturnCallback(this);
        this.rabbitTemplate.setMandatory(true);
    }

    public void send(Object msg, int i) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
//        LOGGER.info("send: " + correlationData.getId());
        if(i % 2 == 0){
        	this.rabbitTemplate.convertAndSend(AmqpConfig.FOO_EXCHANGE, AmqpConfig.FOO_ROUTINGKEY, msg, correlationData);
        }else{
        	this.rabbitTemplate.convertAndSend(AmqpConfig.FOO_EXCHANGE, "adfsfds", msg, correlationData);
        }
    }
    
    

    /** 回调方法 */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
    	if(ack){
    		System.out.println("ack");
    	}else{
    		System.out.println("no ack");
    	}
//        LOGGER.info("confirm: " + correlationData.getId());
    }

	@Override
	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
		byte[] body = message.getBody();
		Student fromJson = new Gson().fromJson(new String(body), Student.class);
		
		System.out.println("return message  " + fromJson.getAge());
		
		System.out.println(replyCode + " " + replyText + "  " + exchange + "  " + routingKey);
	}

    
	
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}