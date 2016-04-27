package br.com.ufs.sd.rabbitchat.usuario;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.codec.digest.DigestUtils;

import br.com.ufs.sd.rabbitchat.infra.RabbitChatDS;
import br.com.ufs.sd.rabbitchat.infra.RabbitChatException;

public class UsuarioService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject @RabbitChatDS
	private DataSource dataSource;
	
	public void cadastrarUsuario(Usuario usuario) {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO tb_usuario (ds_login, nm_usuario, ds_senha) VALUES (?, ?, ?)");
			ps.setString(1, usuario.getLogin());
			ps.setString(2, usuario.getNome());
			ps.setString(3, DigestUtils.sha1Hex(usuario.getSenha()));
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RabbitChatException(e);
		}
	}
	
	public boolean loginExiste(String login) {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement ps = connection.prepareStatement("SELECT 1 FROM tb_usuario WHERE ds_login = ?");
			ps.setString(1, login);
			ResultSet rs = ps.executeQuery();
			boolean existe = rs.next();
			rs.close();
			ps.close();
			return existe;
		} catch (SQLException e) {
			throw new RabbitChatException(e);
		}
	}
}
