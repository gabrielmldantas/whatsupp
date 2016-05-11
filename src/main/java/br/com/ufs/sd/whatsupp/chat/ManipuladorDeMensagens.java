package br.com.ufs.sd.whatsupp.chat;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import br.com.ufs.sd.whatsupp.rabbitmq.RabbitMQ;

public class ManipuladorDeMensagens extends DefaultConsumer implements Runnable{
	private final List<Mensagem> mensagensRecebidas;
	private String login;
	
	public ManipuladorDeMensagens(String login) throws IOException, TimeoutException {
		super(RabbitMQ.getChannel(login));
		this.login = login;
		mensagensRecebidas = Collections.synchronizedList(new LinkedList<Mensagem>());
	}
	
	public List<Mensagem> getMensagensRecebidas() {
		return mensagensRecebidas;
	}
	
	public void enviarMensagem(Mensagem mensagem) throws IOException, TimeoutException{
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssX").create();
		String mensagemJSONString = gson.toJson(mensagem);
	    RabbitMQ.send(mensagem.getDestinatario(), mensagemJSONString);
	}

	public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
		String jsonString = new String(body, "UTF-8");
		Gson gson = new Gson();
	    Mensagem msg = gson.fromJson(jsonString, Mensagem.class);
	    getMensagensRecebidas().add(msg);
	    System.out.println("@"+msg.getRemetente() +"> '" + msg.getMsg() + "'");
	}
	
	@Override
	public void run() {
		try {
			this.getChannel().basicConsume(login, true, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			this.getChannel().getConnection().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
