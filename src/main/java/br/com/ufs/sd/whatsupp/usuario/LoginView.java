package br.com.ufs.sd.whatsupp.usuario;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ufs.sd.whatsupp.chat.ManipuladorDeMensagens;
import br.com.ufs.sd.whatsupp.infra.WhatsuppException;
import br.com.ufs.sd.whatsupp.rabbitmq.RabbitMQController;

@Named
@ViewScoped
public class LoginView implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private AuthenticationService authenticationService;
	@Inject
	private UserSessionView userSessionView;
	@Inject
	private RabbitMQController rabbitMQController;
	
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
			rabbitMQController.start(new ManipuladorDeMensagens(usuario.getLogin()));
			return "/index.jsf?faces-redirect=true";
		} catch (WhatsuppException | IOException | TimeoutException e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			userSessionView.setUsuario(null);
			authenticationService.unauthenticate();
			return "/login.jsf";
		}
	}
	
	public String logout() {
		authenticationService.unauthenticate();
		rabbitMQController.stop();
		return "/login.jsf?faces-redirect=true";
	}
}
