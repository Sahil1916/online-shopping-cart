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

public class OrderService {

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

        // 2. Calculate total amount
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Cart cart : cartItems) {

            Product product = productDAO.getProductById(cart.getProductId());

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

            Product product = productDAO.getProductById(cart.getProductId());

            OrderItem item = new OrderItem();

            item.setOrderId(orderId);
            item.setProductId(cart.getProductId());
            item.setQuantity(cart.getQuantity());
            item.setPrice(product.getPrice());

            orderItemDAO.saveOrderItem(item);
        }

        // 6. Clear Cart
        cartDAO.clearCart(userId);

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