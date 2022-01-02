package order.domain;

import order.exception.NotChangeOrderStatusException;
import order.exception.OrderErrorCode;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    @Column(nullable = false)
    @Enumerated
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.valueOf(orderStatus);
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    private Order(Long orderTableId, List<OrderLineItem> orderLineItemList) {
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        this.orderLineItems = new OrderLineItems(orderLineItemList);
    }

    public Order(String orderStatus) {
        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    public static Order create(Long orderTableId, List<OrderLineItem> orderLineItemList) {
        Order order = new Order(orderTableId, orderLineItemList);
        order.addOrderLineItems();
        return order;
    }

    private void addOrderLineItems() {
        this.orderLineItems.addOrderLineItems(this);
    }

    public void changeOrderStatus(String request) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new NotChangeOrderStatusException(OrderErrorCode.ORDER_COMPLETE);
        }

        this.orderStatus = OrderStatus.valueOf(request);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    public boolean isProcessing() {
        return orderStatus.equals(OrderStatus.COOKING) || orderStatus.equals(OrderStatus.MEAL);
    }
}
