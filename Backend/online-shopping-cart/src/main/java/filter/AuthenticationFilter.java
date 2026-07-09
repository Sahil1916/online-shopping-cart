package filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String method = req.getMethod();

        System.out.println("--------------------------------");
        System.out.println(method + " " + uri);

        // OPTIONS
        if ("OPTIONS".equalsIgnoreCase(method)) {
            chain.doFilter(request, response);
            return;
        }

        // PUBLIC APIs

        // Login
        if ("POST".equalsIgnoreCase(method)
                && uri.endsWith("/api/users/login")) {

            chain.doFilter(request, response);
            return;
        }

        // Register
        if ("POST".equalsIgnoreCase(method)
                && uri.endsWith("/api/users/register")) {

            chain.doFilter(request, response);
            return;
        }

        // Products GET
        if ("GET".equalsIgnoreCase(method)
                && uri.endsWith("/api/products")) {

            chain.doFilter(request, response);
            return;
        }

        // Product Details
        if ("GET".equalsIgnoreCase(method)
                && uri.contains("/api/products")) {

            chain.doFilter(request, response);
            return;
        }

        // Check Login

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {

            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"message\":\"Please login first\"}");
            return;
        }

        System.out.println("SESSION = " + session.getId());
        System.out.println("USER = " + session.getAttribute("userId"));

        chain.doFilter(request, response);
    }
}