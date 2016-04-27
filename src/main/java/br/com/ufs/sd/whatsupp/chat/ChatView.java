package br.com.ufs.sd.whatsupp.chat;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ufs.sd.whatsupp.infra.WhatsuppException;
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
	
	private List<Usuario> contatos;
	private String loginNovoContato;
	
	public List<Usuario> getContatos() {
		if (contatos == null) {
			contatos = contatoSearch.getContatos(userSessionView.getUsuario().getId());
		}
		return contatos;
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
			contatos = null;
		} catch (WhatsuppException e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
		}
	}
}
