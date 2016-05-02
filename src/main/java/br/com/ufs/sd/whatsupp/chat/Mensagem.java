package br.com.ufs.sd.whatsupp.chat;

import java.io.Serializable;
import java.util.Date;

public class Mensagem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String msg;
	private StatusMensagem status;
	private Date data;
	private String remetente;
	private String destinatario;
	
	public Mensagem(){
		super();
	}
	
	public Mensagem(String msg, StatusMensagem status, Date data, String remetente, String destinatario) {
		super();
		this.msg = msg;
		this.status = status;
		this.data = data;
		this.remetente = remetente;
		this.destinatario = destinatario;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public StatusMensagem getStatus() {
		return status;
	}
	public void setStatus(StatusMensagem status) {
		this.status = status;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getRemetente() {
		return remetente;
	}
	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}
	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	
}
