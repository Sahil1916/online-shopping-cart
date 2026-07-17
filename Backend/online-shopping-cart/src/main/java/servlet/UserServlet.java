package servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import service.UserService;

@WebServlet("/api/users/*")
public class UserServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    // ---------------- POST ----------------

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (path == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        switch (path) {

        case "/register":
            register(req, resp);
            break;

        case "/login":
            login(req, resp);
            break;

        case "/logout":
            logout(req, resp);
            break;

        default:
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            objectMapper.writeValue(resp.getWriter(), "Invalid API");
        }
    }

    // ---------------- GET ----------------

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if ("/me".equals(path)) {
            getCurrentUser(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            objectMapper.writeValue(resp.getWriter(), "Invalid API");
        }
    }

    // ---------------- Register ----------------

    private void register(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        User user = objectMapper.readValue(req.getReader(), User.class);

        boolean registered = userService.register(user);

        if (registered) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(resp.getWriter(), "User Registered Successfully");
        } else {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            objectMapper.writeValue(resp.getWriter(), "Email Already Exists");
        }
    }

    // ---------------- Login ----------------

    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {

            User requestUser = objectMapper.readValue(req.getReader(), User.class);

            User user = userService.login(
                    requestUser.getEmail(),
                    requestUser.getPassword());

            if (user == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                objectMapper.writeValue(resp.getWriter(), "Invalid Email or Password");
                return;
            }

            HttpSession session = req.getSession(true);

            session.setAttribute("userId", user.getId());
            session.setAttribute("name", user.getName());
            session.setAttribute("role", user.getRole().name());

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            objectMapper.writeValue(resp.getWriter(), user);

        } catch (Exception e) {

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), "Login failed. Please try again.");
        }
    }
    // ---------------- Current User ----------------

    private void getCurrentUser(HttpServletRequest req,
                                HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);

        if (session == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            objectMapper.writeValue(resp.getWriter(), "Please Login First");
            return;
        }

        Map<String, Object> response = new HashMap<>();

        response.put("userId", session.getAttribute("userId"));
        response.put("name", session.getAttribute("name"));
        response.put("role", session.getAttribute("role"));

        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), response);
    }

    // ---------------- Logout ----------------

    private void logout(HttpServletRequest req,
                        HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), "Logout Successful");
    }
}