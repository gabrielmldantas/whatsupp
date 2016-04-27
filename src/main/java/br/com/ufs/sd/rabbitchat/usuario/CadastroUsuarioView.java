package br.com.ufs.sd.rabbitchat.usuario;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ufs.sd.rabbitchat.infra.RabbitChatException;

@Named
@ViewScoped
public class CadastroUsuarioView implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioService usuarioService;
	
	private Usuario usuario = new Usuario();
	
	public void validarLogin(AjaxBehaviorEvent event) {
		UIInput inputLogin = (UIInput) event.getComponent();
		if (usuarioService.loginExiste((String) inputLogin.getValue())) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Login existente", ""));
			inputLogin.setValid(false);
		}
	}
	
	public String cadastrar() {
		try {
			if (usuarioService.loginExiste(usuario.getLogin())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Login existente", ""));
				return null;
			}
			usuarioService.cadastrarUsuario(usuario);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuário criado com sucesso", ""));
			return "/login.jsf?faces-redirect=true";
		} catch (RabbitChatException e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			return "/cadastro.jsf";
		}
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
}
