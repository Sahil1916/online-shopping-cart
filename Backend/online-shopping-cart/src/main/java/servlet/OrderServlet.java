package servlet;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import dto.OrderDetailsDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;
import model.OrderItem;
import service.OrderService;
@WebServlet("/api/orders/*")
public class OrderServlet extends HttpServlet{
	
	
	 private final OrderService orderService  = new OrderService();
	 
	 private final ObjectMapper objectMapper = new ObjectMapper()
		        .findAndRegisterModules()
		        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);	 
	 
	 @Override
	 protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	         throws ServletException, IOException {

	     HttpSession session = req.getSession(false);

	     if (session == null || session.getAttribute("userId") == null) {

	         resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	         objectMapper.writeValue(resp.getWriter(), "Please login first");
	         return;
	     }

	     Long userId = (Long) session.getAttribute("userId");

	     // Body is optional: {"shippingAddress": "...", "paymentMethod": "CARD"|"UPI"|"COD"}
	     String shippingAddress = null;
	     String paymentMethod = null;
	     try {
	         java.util.Map<?, ?> body = objectMapper.readValue(req.getReader(), java.util.Map.class);
	         if (body != null) {
	             Object addr = body.get("shippingAddress");
	             Object pay = body.get("paymentMethod");
	             shippingAddress = addr != null ? addr.toString() : null;
	             paymentMethod = pay != null ? pay.toString() : null;
	         }
	     } catch (Exception ignored) {
	         // No body sent — proceed without shipping/payment info
	     }

	     boolean success = orderService.placeOrder(userId, shippingAddress, paymentMethod);

	     resp.setContentType("application/json");

	     if (success) {

	         resp.setStatus(HttpServletResponse.SC_OK);
	         objectMapper.writeValue(resp.getWriter(), "Order placed successfully");

	     } else {

	         resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	         objectMapper.writeValue(resp.getWriter(), "Cart is empty");

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

	     Long userId = (Long) session.getAttribute("userId");

	     String pathInfo = req.getPathInfo();

	     resp.setContentType("application/json");

	     // ==========================
	     // GET /api/orders
	     // ==========================
	     if (pathInfo == null || pathInfo.equals("/")) {

	         List<Order> orders = orderService.getOrdersByUserId(userId);

	         resp.setStatus(HttpServletResponse.SC_OK);
	         objectMapper.writeValue(resp.getWriter(), orders);

	         return;
	     }

	     // ==========================
	     // GET /api/orders/{id}
	     // ==========================

	     try {

	         Long orderId = Long.parseLong(pathInfo.substring(1));

	         Order order = orderService.getOrderById(orderId);

	         if (order == null) {

	             resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
	             objectMapper.writeValue(resp.getWriter(), "Order not found");
	             return;
	         }

	         // Security Check
	         if (!order.getUserId().equals(userId)) {

	             resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
	             objectMapper.writeValue(resp.getWriter(), "Access Denied");
	             return;
	         }

	         List<OrderItem> items =
	                 orderService.getOrderItemsByOrderId(orderId);

	         OrderDetailsDTO dto =
	                 new OrderDetailsDTO(order, items);

	         resp.setStatus(HttpServletResponse.SC_OK);
	         objectMapper.writeValue(resp.getWriter(), dto);

	     } catch (NumberFormatException e) {

	         resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	         objectMapper.writeValue(resp.getWriter(), "Invalid Order Id");

	     }
	 }

	
}
