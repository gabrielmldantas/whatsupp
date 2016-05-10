package br.com.ufs.sd.whatsupp.ws;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import br.com.ufs.sd.whatsupp.usuario.Usuario;
import br.com.ufs.sd.whatsupp.usuario.UsuarioService;

@WebServlet(urlPatterns = "/ws/contato")
public class ContatoServlet extends HttpServlet {
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
		List<Usuario> contatos = usuarioService.getContatos(login);
		if (contatos == null) {
			resp.sendError(404);
			return;
		}
		String json = new Gson().toJson(contatos);
		resp.setStatus(200);
		resp.getWriter().write(json);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		usuarioService.adicionarContato(Integer.valueOf(req.getParameter("idUsuario")), req.getParameter("loginNovoContato"));
		resp.setStatus(200);
	}
}
