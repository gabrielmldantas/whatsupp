package br.com.ufs.sd.whatsupp.chat;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ufs.sd.whatsupp.infra.WhatsuppException;
import br.com.ufs.sd.whatsupp.rabbitmq.RabbitMQController;
import br.com.ufs.sd.whatsupp.usuario.UserSessionView;
import br.com.ufs.sd.whatsupp.usuario.Usuario;
import br.com.ufs.sd.whatsupp.usuario.UsuarioService;

@Named
@ViewScoped
public class ChatView implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private ContatoSearch contatoSearch;
	@Inject
	private UserSessionView userSessionView;
	@Inject
	private UsuarioService usuarioService;
	@Inject
	private RabbitMQController rabbitMQController;
	
	private Map<String, Conversa> conversas;
	private String loginNovoContato;
	private Conversa conversaAtual;
	private String textoMensagemEnvio;
	
	public List<Conversa> getConversas() {
		if (conversas == null) {
			conversas = new HashMap<>();
			List<Usuario> contatos = contatoSearch.getContatos(userSessionView.getUsuario().getId());
			for (Usuario contato : contatos) {
				Conversa conversa = new Conversa(contato, new LinkedList<Mensagem>());
				conversas.put(contato.getLogin(), conversa);
			}
		}
		return new ArrayList<>(conversas.values()); //TODO melhorar isso aqui
	}
	
	public String getLoginNovoContato() {
		return loginNovoContato;
	}
	
	public void setLoginNovoContato(String loginNovoContato) {
		this.loginNovoContato = loginNovoContato;
	}
	
	public void adicionarContato() {
		try {
			usuarioService.adicionarContato(userSessionView.getUsuario().getId(), loginNovoContato);
			loginNovoContato = null;
			conversas = null;
		} catch (WhatsuppException e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
		}
	}
	
	public void send() throws IOException, TimeoutException {
		if (textoMensagemEnvio != null && !textoMensagemEnvio.isEmpty()) {
			Mensagem mensagem = new Mensagem(textoMensagemEnvio, StatusMensagem.ENVIANDO, new Date(), userSessionView.getUsuario().getLogin(), conversaAtual.getDestinatario().getLogin());
			rabbitMQController.getManipuladorDeMensagens().enviarMensagem(mensagem);
			textoMensagemEnvio = "";
			conversaAtual.getMensagens().add(mensagem);
		}
	}
	
	public void receive() {
		List<Mensagem> mensagens = new ArrayList<>(rabbitMQController.getManipuladorDeMensagens().getMensagensRecebidas());
		if (!mensagens.isEmpty()) {
			rabbitMQController.getManipuladorDeMensagens().getMensagensRecebidas().clear();
		}
		for (Mensagem mensagem : mensagens) {
			conversas.get(mensagem.getRemetente()).getMensagens().add(mensagem);
		}
	}
	
	public Conversa getConversaAtual() {
		return conversaAtual;
	}
	
	public void setConversaAtual(Conversa conversaAtual) {
		this.conversaAtual = conversaAtual;
	}
	
	public String getTextoMensagemEnvio() {
		return textoMensagemEnvio;
	}
	
	public void setTextoMensagemEnvio(String textoMensagemEnvio) {
		this.textoMensagemEnvio = textoMensagemEnvio;
	}
}
