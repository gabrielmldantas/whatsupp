package br.com.ufs.sd.rabbitchat.usuario;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.codec.digest.DigestUtils;

import br.com.ufs.sd.rabbitchat.infra.RabbitChatDS;
import br.com.ufs.sd.rabbitchat.infra.RabbitChatException;

public class AuthenticationService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject @RabbitChatDS
	private DataSource dataSource;
	
	public Usuario authenticate(String login, String senha) {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM tb_usuario WHERE ds_login = ? AND ds_senha = ?");
			ps.setString(1, login);
			ps.setString(2, DigestUtils.sha1Hex(senha));
			ResultSet rs = ps.executeQuery();
			Usuario usuario = null;
			if (rs.next()) {
				usuario = new Usuario();
				usuario.setId(rs.getInt("id_usuario"));
				usuario.setLogin(rs.getString("ds_login"));
				usuario.setNome(rs.getString("nm_usuario"));
			}
			rs.close();
			ps.close();
			if (usuario == null) {
				throw new RabbitChatException("Usuário ou senha inválidos");
			}
			return usuario;
		} catch (SQLException e) {
			throw new RabbitChatException(e);
		}
	}
	
	public void unauthenticate() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.invalidate();
	}
}
