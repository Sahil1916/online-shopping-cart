package service;

import java.math.BigDecimal;
import java.util.List;

import dao.CartDAO;
import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.ProductDAO;
import enums.OrderStatus;
import model.Cart;
import model.Order;
import model.OrderItem;
import model.Product;
import model.User;

public class OrderService {
	
	private UserService userService = new UserService();
	private EmailService emailService = new EmailService();
    private CartDAO cartDAO = new CartDAO();
    private ProductDAO productDAO = new ProductDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private OrderItemDAO orderItemDAO = new OrderItemDAO();

    public boolean placeOrder(Long userId, String shippingAddress, String paymentMethod) {

        // 1. Get all cart items
        List<Cart> cartItems = cartDAO.getCartItemsByUserId(userId);

        if (cartItems.isEmpty()) {
            return false;
        }

        // 2. Calculate total amount (also validates product still exists & has stock)
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Cart cart : cartItems) {

            Product product = productDAO.findById(cart.getProductId());

            if (product == null || product.getQuantity() < cart.getQuantity()) {
                // Product was removed or there isn't enough stock left
                return false;
            }

            BigDecimal itemTotal =
                    product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity()));

            totalAmount = totalAmount.add(itemTotal);
        }

        // 3. Create Order
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymentMethod);

        // 4. Save Order
        Long orderId = orderDAO.saveOrder(order);

        if (orderId == null) {
            return false;
        }

        // 5. Save Order Items
        for (Cart cart : cartItems) {

            Product product = productDAO.findById(cart.getProductId());

            OrderItem item = new OrderItem();

            item.setOrderId(orderId);
            item.setProductId(cart.getProductId());
            item.setQuantity(cart.getQuantity());
            item.setPrice(product.getPrice());

            orderItemDAO.saveOrderItem(item);

            productDAO.decrementStock(cart.getProductId(), cart.getQuantity());
        }

     // 6. Clear Cart
        cartDAO.clearCart(userId);

        // 7. Send Order Confirmation Email
        try {

            User user = userService.getUserById(userId);

            if (user != null) {

                emailService.sendOrderPlacedMail(
                        user.getName(),
                        user.getEmail(),
                        orderId,
                        totalAmount.doubleValue());

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return true;
    }
    
    public List<Order> getOrdersByUserId(Long userId) {
        return orderDAO.getOrdersByUserId(userId);
    }
    
    public Order getOrderById(Long orderId) {
        return orderDAO.getOrderById(orderId);
    }
    
    

    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemDAO.getOrderItemsByOrderId(orderId);
    }
    
    public boolean updateOrderStatus(Long orderId,
            OrderStatus status) {
    	return orderDAO.updateOrderStatus(orderId, status);
    }
    
    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }
}