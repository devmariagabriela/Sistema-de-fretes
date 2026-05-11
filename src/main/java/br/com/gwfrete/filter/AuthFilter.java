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
        boolean autenticado = session != null && session.getAttribute("usuarioLogado") instanceof Usuario;

        if (!autenticado) {
            if (session != null) {
                session.invalidate();
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
}
