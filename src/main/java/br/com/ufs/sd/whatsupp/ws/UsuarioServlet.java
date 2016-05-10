package br.com.ufs.sd.whatsupp.ws;

import java.io.BufferedReader;
import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import br.com.ufs.sd.whatsupp.usuario.Usuario;
import br.com.ufs.sd.whatsupp.usuario.UsuarioService;

@WebServlet(urlPatterns = "/ws/usuario")
public class UsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioService usuarioService;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String login = req.getParameter("login");
		if (login == null || login.isEmpty()) {
			resp.sendError(400, "O parâmetro login é obrigatório");
			return;
		}
		Usuario usuario = usuarioService.getUsuario(login);
		if (usuario == null) {
			resp.sendError(404);
			return;
		}
		String json = new Gson().toJson(usuario);
		resp.setStatus(200);
		resp.getWriter().write(json);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BufferedReader reader = req.getReader();
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		Usuario usuario = new Gson().fromJson(sb.toString(), Usuario.class);
		usuarioService.cadastrarUsuario(usuario);
		resp.setStatus(200);
	}
}
