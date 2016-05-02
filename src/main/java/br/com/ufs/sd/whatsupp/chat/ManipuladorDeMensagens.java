package br.com.ufs.sd.whatsupp.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
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
		mensagensRecebidas = Collections.synchronizedList(new LinkedList());
	}
	
	public List<Mensagem> getMensagensRecebidas() {
		return mensagensRecebidas;
	}
	
	public void enviarMensagem(Mensagem mensagem) throws IOException, TimeoutException{
		Gson gson = new Gson();
		String mensagemJSONString = gson.toJson(mensagem);
	    RabbitMQ.send(mensagem.getDestinatario(), mensagemJSONString);
	}

	public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
			byte[] body) throws IOException {
		String jsonString = new String(body, "UTF-8");
		//System.out.println(" JSON RECEBIDA:'" + jsonString + "'");
		
	    Gson gson = new Gson();
	    Mensagem msg = gson.fromJson(jsonString, Mensagem.class);
	    getMensagensRecebidas().add(msg);
	    
	    System.out.println("@"+msg.getRemetente() +"> '" + msg.getMsg() + "'");
	}
	
	@Override
	public void run() {
		try {
			this.getChannel().basicConsume(this.login, true, this);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) throws IOException, TimeoutException {
		String login = "ana";
		ManipuladorDeMensagens mm = new ManipuladorDeMensagens(login);
		Thread t = new Thread(mm);
		t.start();

		
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		
		while(true){
			//System.out.print(login+">");
			String mensagem = r.readLine();
			
			Mensagem msg = new Mensagem();
			msg.setData(new Date());
			msg.setMsg(mensagem);
			msg.setDestinatario("will");
			msg.setRemetente(login);
			msg.setStatus(StatusMensagem.ENVIANDO);
			mm.enviarMensagem(msg);
		}
	}



}
