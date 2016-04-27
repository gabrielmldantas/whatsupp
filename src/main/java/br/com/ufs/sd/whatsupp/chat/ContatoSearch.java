package br.com.ufs.sd.whatsupp.chat;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import br.com.ufs.sd.whatsupp.infra.RabbitChatDS;
import br.com.ufs.sd.whatsupp.infra.WhatsuppException;
import br.com.ufs.sd.whatsupp.usuario.Usuario;

public class ContatoSearch implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject @RabbitChatDS
	private DataSource dataSource;
	
	public List<Usuario> getContatos(int idUsuario) {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement ps = connection.prepareStatement("SELECT u.* FROM tb_contato c "
					+ "INNER JOIN tb_usuario u ON (u.id_usuario = c.id_contato) WHERE c.id_usuario = ?");
			ps.setInt(1, idUsuario);
			ResultSet rs = ps.executeQuery();
			List<Usuario> contatos = new ArrayList<>();
			while (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setId(rs.getInt("id_usuario"));
				usuario.setLogin(rs.getString("ds_login"));
				usuario.setNome(rs.getString("nm_usuario"));
				contatos.add(usuario);
			}
			rs.close();
			ps.close();
			return contatos;
		} catch (SQLException e) {
			throw new WhatsuppException(e);
		}
	}
}
