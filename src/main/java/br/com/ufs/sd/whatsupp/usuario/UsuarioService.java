package br.com.ufs.sd.whatsupp.usuario;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.codec.digest.DigestUtils;

import br.com.ufs.sd.whatsupp.infra.WhatsuppException;

public class UsuarioService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
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
			throw new WhatsuppException(e);
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
			throw new WhatsuppException(e);
		}
	}
	
	public void adicionarContato(int idUsuario, String loginNovoContato) {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement ps = connection.prepareStatement("SELECT u.id_usuario, "
					+ "COALESCE((SELECT 1 FROM tb_contato c WHERE c.id_contato = u.id_usuario AND c.id_usuario = ?), 0) AS existe "
					+ "FROM tb_usuario u "
					+ "WHERE u.ds_login = ?");
			ps.setInt(1, idUsuario);
			ps.setString(2, loginNovoContato);
			ResultSet rs = ps.executeQuery();
			Integer idContato = null;
			boolean existe = false;
			if (rs.next()) {
				idContato = rs.getInt("id_usuario");
				existe = rs.getInt("existe") != 0;
			}
			rs.close();
			ps.close();
			if (existe) {
				throw new WhatsuppException("Contato já existe");
			}
			if (idContato == null) {
				throw new WhatsuppException("Contato não existe");
			}
			ps = connection.prepareStatement("INSERT INTO tb_contato (id_usuario, id_contato) VALUES (?, ?)");
			ps.setInt(1, idUsuario);
			ps.setInt(2, idContato);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new WhatsuppException(e);
		}
	}
}
