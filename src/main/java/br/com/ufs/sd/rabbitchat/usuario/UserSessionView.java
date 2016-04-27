package br.com.ufs.sd.rabbitchat.usuario;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class UserSessionView implements Serializable {
	private static final long serialVersionUID = 1L;

	private Usuario usuario;
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public boolean isLogado() {
		return usuario != null;
	}
}
