package br.com.ufs.sd.whatsupp.rabbitmq;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

import br.com.ufs.sd.whatsupp.chat.ManipuladorDeMensagens;

@SessionScoped
public class RabbitMQController implements Serializable {
	private static final long serialVersionUID = 1L;

	private RabbitMQThread rabbitMQThread;
	private ManipuladorDeMensagens manipuladorDeMensagens;
	
	public void start(ManipuladorDeMensagens manipuladorDeMensagens) {
		rabbitMQThread = new RabbitMQThread(manipuladorDeMensagens);
		rabbitMQThread.start();
		this.manipuladorDeMensagens = manipuladorDeMensagens;
	}
	
	public void stop() {
		rabbitMQThread.interrupt();
		manipuladorDeMensagens.close();
		rabbitMQThread = null;
		manipuladorDeMensagens = null;
	}
	
	public boolean isStarted() {
		return rabbitMQThread != null && rabbitMQThread.isAlive();
	}
	
	public ManipuladorDeMensagens getManipuladorDeMensagens() {
		return manipuladorDeMensagens;
	}
}
