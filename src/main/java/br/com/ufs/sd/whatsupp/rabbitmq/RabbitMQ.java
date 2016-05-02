package br.com.ufs.sd.whatsupp.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQ {

	public static boolean send(String queue, String msg) throws IOException, TimeoutException{
	    getChannel(queue).basicPublish("", queue, null, msg.getBytes());   
		return true;
	}
	
	public static Channel getChannel(String queue) throws IOException, TimeoutException{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("willamssouza.koding.io");
		factory.setUsername("guest");
		factory.setPassword("guest");
	
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();
	    channel.queueDeclare(queue, false, false, false, null);
	    
	    return channel;
	}
	
}
