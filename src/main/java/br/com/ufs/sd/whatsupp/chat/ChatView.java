package br.com.ufs.sd.whatsupp.chat;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
	
	private Map<String, Conversa> conversas = new HashMap<>();
	private String loginNovoContato;
	private Conversa conversaAtual;
	private String textoMensagemEnvio;
	private boolean reloadConversas = true;
	private List<Conversa> conversasOrdenadas;
	
	public List<Conversa> getConversas() {
		if (reloadConversas) {
			List<Usuario> contatos = contatoSearch.getContatos(userSessionView.getUsuario().getId());
			for (Usuario contato : contatos) {
				if (!conversas.containsKey(contato.getLogin())) {
					Conversa conversa = new Conversa(contato, new LinkedList<Mensagem>());
					conversas.put(contato.getLogin(), conversa);
				}
			}
			reloadConversas = false;
			conversasOrdenadas = new ArrayList<>(conversas.values());
			Collections.sort(conversasOrdenadas, new Comparator<Conversa>() {
				@Override
				public int compare(Conversa o1, Conversa o2) {
					if (o1.getDataUltimaMensagem() == null && o2.getDataUltimaMensagem() == null) {
						return o1.getDestinatario().getNome().compareTo(o2.getDestinatario().getNome());
					}
					if (o1.getDataUltimaMensagem() != null && o2.getDataUltimaMensagem() != null) {
						return o2.getDataUltimaMensagem().compareTo(o1.getDataUltimaMensagem());
					}
					if (o1.getDataUltimaMensagem() != null) {
						return -1;
					}
					return 1;
					
				}
			});
		}
		return conversasOrdenadas;
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
			reloadConversas = true;
		} catch (WhatsuppException e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
		}
	}
	
	public void send() throws IOException, TimeoutException {
		if (textoMensagemEnvio != null && !textoMensagemEnvio.isEmpty()) {
			Mensagem mensagem = new Mensagem(textoMensagemEnvio, StatusMensagem.ENVIANDO, new Date(), userSessionView.getUsuario().getLogin(), conversaAtual.getDestinatario().getLogin());
			rabbitMQController.getManipuladorDeMensagens().enviarMensagem(mensagem);
			textoMensagemEnvio = null;
			mensagem.setMsg(mensagem.getMsg().replace("\r\n", "<br>").replace("\n", "<br>"));
			conversaAtual.getMensagens().add(mensagem);
		}
	}
	
	public void receive() {
		List<Mensagem> mensagens = new ArrayList<>(rabbitMQController.getManipuladorDeMensagens().getMensagensRecebidas());
		if (!mensagens.isEmpty()) {
			rabbitMQController.getManipuladorDeMensagens().getMensagensRecebidas().clear();
		}
		for (Mensagem mensagem : mensagens) {
			mensagem.setMsg(mensagem.getMsg().replace("\r\n", "<br>").replace("\n", "<br>"));
			Conversa conversa = conversas.get(mensagem.getRemetente());
			conversa.getMensagens().add(mensagem);
			conversa.setDataUltimaMensagem(new Date());
			conversa.setTudoLido(false);
		}
		reloadConversas = true;
	}
	
	public Conversa getConversaAtual() {
		return conversaAtual;
	}
	
	public void setConversaAtual(Conversa conversaAtual) {
		this.conversaAtual = conversaAtual;
		if (this.conversaAtual != null) {
			this.conversaAtual.setTudoLido(true);
		}
		this.textoMensagemEnvio = null;
	}
	
	public String getTextoMensagemEnvio() {
		return textoMensagemEnvio;
	}
	
	public void setTextoMensagemEnvio(String textoMensagemEnvio) {
		this.textoMensagemEnvio = textoMensagemEnvio;
	}
}
