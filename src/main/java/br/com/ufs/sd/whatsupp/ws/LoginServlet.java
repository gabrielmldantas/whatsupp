package br.com.ufs.sd.whatsupp.ws;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import br.com.ufs.sd.whatsupp.infra.WhatsuppException;
import br.com.ufs.sd.whatsupp.usuario.AuthenticationService;
import br.com.ufs.sd.whatsupp.usuario.Usuario;

@WebServlet(urlPatterns = "/ws/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private AuthenticationService authenticationService;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Usuario usuario = authenticationService.authenticate(req.getParameter("login"), req.getParameter("senha"));
			resp.setStatus(200);
			resp.getWriter().println(new Gson().toJson(usuario, Usuario.class));
 		} catch (WhatsuppException e) {
 			resp.sendError(401, e.getMessage());
 		}
	}
}
