package servlet;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import dto.CartItemDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Cart;
import service.CartService;

@WebServlet("/api/cart/*")
public class CartServlet extends HttpServlet {

    private final CartService cartService = new CartService();
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Request JSON -> Cart Object
        Cart cart = objectMapper.readValue(req.getReader(), Cart.class);

        // Logged-in User Session
        HttpSession session = req.getSession(false);
        

        if (session == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            objectMapper.writeValue(resp.getWriter(), "Please login first");
            return;
        }

        Long userId = (Long) session.getAttribute("userId");
        
        // Session मधला userId Cart मध्ये set कर
        cart.setUserId(userId);

        // Business Logic
        boolean saved = cartService.addCart(cart);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (saved) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(resp.getWriter(), "Product added to cart successfully");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), "Failed to add product to cart");
        }
    }
    
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	

        HttpSession session = req.getSession(false);
    	
        if (session == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            objectMapper.writeValue(resp.getWriter(), "Please login first");
            return;
        }
        
        Long userId = (Long) session.getAttribute("userId");
        
        // Session मधला userId Cart मध्ये set कर

        // Business Logic
        List<CartItemDTO> cartitems = cartService.getCartByUserId(userId);
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), cartitems);
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter("id");

        if (id == null || id.isBlank()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        HttpSession session = req.getSession(false);

        if (session == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            objectMapper.writeValue(resp.getWriter(), "Please login first");
            return;
        }

        Long userId = (Long) session.getAttribute("userId");

        boolean deleted =
        		cartService.deleteCartItem(Long.parseLong(id), userId);

        resp.setContentType("application/json");

        if (deleted) {
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        objectMapper.writeValue(resp.getWriter(), deleted);
    }
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // JSON -> Cart Object
        Cart cart = objectMapper.readValue(req.getReader(), Cart.class);

        // Session
        HttpSession session = req.getSession(false);

        if (session == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            objectMapper.writeValue(resp.getWriter(), "Please login first");
            return;
        }

        Long userId = (Long) session.getAttribute("userId");

        // Business Logic
        boolean updated = cartService.updateQuantity(
                cart.getId(),
                userId,
                cart.getQuantity());

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (updated) {
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), "Cart quantity updated successfully");
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            objectMapper.writeValue(resp.getWriter(), "Cart item not found");
        }
    }
}