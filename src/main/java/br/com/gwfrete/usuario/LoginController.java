package br.com.gwfrete.usuario;

import br.com.gwfrete.usuario.UsuarioBO;
import br.com.gwfrete.exception.AutenticacaoException;
import br.com.gwfrete.usuario.Usuario;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final UsuarioBO usuarioBO = new UsuarioBO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("usuarioLogado") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        try {
            Usuario usuario = usuarioBO.autenticar(email, senha);
            HttpSession sessaoAtual = request.getSession(false);

            if (sessaoAtual != null) {
                sessaoAtual.invalidate();
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioLogado", usuario);
            session.setAttribute("permissoesUsuario", usuarioBO.obterPermissoes(usuario.getPerfil()));
            session.setMaxInactiveInterval(30 * 60);

            response.sendRedirect(request.getContextPath() + "/dashboard");
        } catch (AutenticacaoException e) {
            request.setAttribute("erroLogin", e.getMessage());
            request.setAttribute("emailInformado", email);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
