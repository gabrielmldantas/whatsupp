package br.com.ufs.sd.rabbitchat.usuario;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ufs.sd.rabbitchat.infra.RabbitChatException;

@Named
@ViewScoped
public class LoginView implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private AuthenticationService authenticationService;
	@Inject
	private UserSessionView userSessionView;
	
	private String login;
	private String senha;

	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public String login() {
		try {
			Usuario usuario = authenticationService.authenticate(login, senha);
			userSessionView.setUsuario(usuario);
			return "/index.jsf?faces-redirect=true";
		} catch (RabbitChatException e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			return "/login.jsf";
		}
	}
	
	public String logout() {
		authenticationService.unauthenticate();
		return "/login.jsf?faces-redirect=true";
	}
}
