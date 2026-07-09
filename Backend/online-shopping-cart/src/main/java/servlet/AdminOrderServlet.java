package servlet;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import dto.StatusUpdateDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;
import service.OrderService;

@WebServlet("/api/admin/orders/*")
public class AdminOrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    private final ObjectMapper objectMapper =
            new ObjectMapper()
                    .findAndRegisterModules()
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    
    
    
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // Login Check
        if (session == null || session.getAttribute("userId") == null) {

            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            objectMapper.writeValue(resp.getWriter(), "Please login first");
            return;
        }

        // Admin Check
        String role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(role)) {

            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            objectMapper.writeValue(resp.getWriter(), "Access Denied");
            return;
        }

        try {

            // URL : /api/admin/orders/5
            String pathInfo = req.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {

                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                objectMapper.writeValue(resp.getWriter(), "Order Id is required");
                return;
            }

            Long orderId = Long.parseLong(pathInfo.substring(1));

            // Read Request Body
            StatusUpdateDTO dto =
                    objectMapper.readValue(req.getReader(), StatusUpdateDTO.class);

            boolean updated =
                    orderService.updateOrderStatus(orderId, dto.getStatus());

            if (updated) {

                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(resp.getWriter(),
                        "Order status updated successfully");

            } else {

                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                objectMapper.writeValue(resp.getWriter(),
                        "Order not found");
            }

        } catch (NumberFormatException e) {

            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), "Invalid Order Id");

        } catch (Exception e) {

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), "Something went wrong");
        }
    }
    
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {

            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            objectMapper.writeValue(resp.getWriter(), "Please login first");
            return;
        }

        String role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(role)) {

            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            objectMapper.writeValue(resp.getWriter(), "Access Denied");
            return;
        }

        List<Order> orders = orderService.getAllOrders();

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);

        objectMapper.writeValue(resp.getWriter(), orders);
    }
}
