package dto;

import enums.OrderStatus;

public class StatusUpdateDTO {

    private OrderStatus status;

    public StatusUpdateDTO() {
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}