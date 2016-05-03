package br.com.ufs.sd.whatsupp.rabbitmq;

import br.com.ufs.sd.whatsupp.chat.ManipuladorDeMensagens;

public class RabbitMQThread extends Thread {
	public RabbitMQThread(ManipuladorDeMensagens manipuladorDeMensagens) {
		super(manipuladorDeMensagens, "RabbitMQThread");
	}
}
