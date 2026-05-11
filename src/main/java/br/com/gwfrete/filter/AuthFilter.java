package br.com.gwfrete.filter;

import br.com.gwfrete.usuario.Usuario;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI().substring(request.getContextPath().length());

        if (rotaPublica(path)) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        HttpSession session = request.getSession(false);
        boolean autenticado = usuarioAutenticado(session);

        if (!autenticado) {
            if (session != null) {
                invalidarSessao(session);
            }

            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }

    private boolean rotaPublica(String path) {
        return path.equals("/")
                || path.equals("/login")
                || path.equals("/login.jsp")
                || path.equals("/esqueci-senha")
                || path.equals("/recuperar-senha")
                || path.equals("/redefinir-senha")
                || path.startsWith("/assets/")
                || path.startsWith("/favicon");
    }

    private boolean usuarioAutenticado(HttpSession session) {
        if (session == null) {
            return false;
        }

        try {
            return session.getAttribute("usuarioLogado") instanceof Usuario;
        } catch (RuntimeException e) {
            invalidarSessao(session);
            return false;
        }
    }

    private void invalidarSessao(HttpSession session) {
        try {
            session.invalidate();
        } catch (RuntimeException e) {
            // A sessão pode estar corrompida no Redis após mudanças de pacote.
        }
    }
}
