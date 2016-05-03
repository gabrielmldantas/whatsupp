package br.com.ufs.sd.whatsupp.chat;

import java.io.Serializable;
import java.util.List;

import br.com.ufs.sd.whatsupp.usuario.Usuario;

public class Conversa implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<Mensagem> mensagens;
	private Usuario destinatario;
	
	public Conversa(Usuario destinatario, List<Mensagem> mensagens) {
		this.destinatario = destinatario;
		this.mensagens = mensagens;
	}
	
	public List<Mensagem> getMensagens() {
		return mensagens;
	}
	
	public Usuario getDestinatario() {
		return destinatario;
	}
}
