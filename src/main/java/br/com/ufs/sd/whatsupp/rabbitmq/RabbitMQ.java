package br.com.ufs.sd.whatsupp.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQ {
	
	private static ConnectionFactory factory;
	
	static {
		factory = new ConnectionFactory();
		factory.setHost("willamssouza.koding.io");
		factory.setPort(5672);
		factory.setUsername("guest");
		factory.setPassword("guest");
	}

	public static boolean send(String queue, String msg) throws IOException, TimeoutException {
	    Channel channel = getChannel(queue);
	    channel.basicPublish("", queue, null, msg.getBytes());
	    channel.getConnection().close();
		return true;
	}
	
	public static Channel getChannel(String queue) throws IOException, TimeoutException {
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();
	    channel.queueDeclare(queue, false, false, false, null);
	    return channel;
	}
	
}
