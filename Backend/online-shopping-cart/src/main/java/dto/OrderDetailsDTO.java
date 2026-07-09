package dto;

import java.util.List;

import model.Order;
import model.OrderItem;

public class OrderDetailsDTO {

    private Order order;
    private List<OrderItem> items;

    public OrderDetailsDTO() {
    }

    public OrderDetailsDTO(Order order, List<OrderItem> items) {
        this.order = order;
        this.items = items;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}