package kitchenpos.order.domain;

import kitchenpos.order.exception.NotCreateOrderException;
import kitchenpos.order.exception.NotChangeOrderStatusException;
import kitchenpos.order.exception.OrderErrorCode;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"), nullable = false)
    private OrderTable orderTable;

    @Column(nullable = false)
    @Enumerated
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(Long id, OrderTable orderTable, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.valueOf(orderStatus);
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    private Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        if (orderTable.isEmpty()) {
            throw new NotCreateOrderException(orderTable.getId() + OrderErrorCode.EMPTY_ORDER_TABLE);
        }

        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderLineItems = orderLineItems;
    }

    public Order(String orderStatus) {
        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    public static Order create(Long id, OrderTable orderTable, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTable, orderStatus, orderedTime, orderLineItems);
    }

    public static Order create(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        Order order = new Order(orderTable, orderLineItems);
        order.addOrder(orderLineItems);
        return order;
    }

    private void addOrder(List<OrderLineItem> orderLineItems) {
        orderLineItems.forEach(orderLineItem -> {
            orderLineItem.addOrder(this);
        });
    }

    public void changeOrderStatus(String request) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new NotChangeOrderStatusException(OrderErrorCode.ORDER_COMPLETE);
        }

        this.orderStatus = OrderStatus.valueOf(request);
    }

    public static Order create(String orderStatus) {
        return new Order(orderStatus);
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void createId(Long id) {
        this.id = id;
    }

}
