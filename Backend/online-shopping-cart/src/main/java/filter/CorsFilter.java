package filter;

import java.io.IOException;
import java.util.Set;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles CORS for requests coming from the separately-hosted frontend
 * (VS Code Live Server). Registered explicitly in web.xml (not via
 * @WebFilter) so it is guaranteed to run BEFORE AuthenticationFilter —
 * otherwise preflight OPTIONS requests get rejected with 401 before
 * CORS headers are ever added, which the browser reports as a CORS error.
 */
public class CorsFilter implements Filter {

    // Add any other origins you test from (different Live Server port, etc.)
    private static final Set<String> ALLOWED_ORIGINS = Set.of(
            "http://127.0.0.1:5500",
            "http://localhost:5500"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String origin = req.getHeader("Origin");

        if (origin != null && ALLOWED_ORIGINS.contains(origin)) {
            resp.setHeader("Access-Control-Allow-Origin", origin);
        }

        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        resp.setHeader("Access-Control-Allow-Credentials", "true");

        // Preflight requests stop here — never forwarded to AuthenticationFilter or the servlet
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(jakarta.servlet.FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}
