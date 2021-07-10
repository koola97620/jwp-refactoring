package kitchenpos.order.domain;

import kitchenpos.common.exception.UnableChangeEmptyStatusException;
import kitchenpos.common.exception.UnableChangeOrderStatusException;
import kitchenpos.common.exception.UnableUngroupStatusException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.common.exception.UnableOrderCausedByEmptyTableException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "ordered_time", updatable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {
    }

    public Order(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public static Order newOrder(OrderTable orderTable, LocalDateTime orderedTime, List<OrderLineItem> newOrderLineItems) {
        return new Order(orderTable.getId(), OrderStatus.COOKING, orderedTime, newOrderLineItems);
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

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, getOrderStatus())) {
            throw new UnableChangeOrderStatusException("완료상태인 준문은 상태변경이 불가능합니다.");
        }

        this.orderStatus = orderStatus;
    }

    public void ungroupValidation() {
        if (getOrderStatus() == OrderStatus.COOKING || getOrderStatus() == OrderStatus.MEAL) {
            throw new UnableUngroupStatusException("요리중이거나 식사중인 상태는 단체테이블을 해지할수 없습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(orderTableId, order.orderTableId) && orderStatus == order.orderStatus && Objects.equals(orderedTime, order.orderedTime) && Objects.equals(orderLineItems, order.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public void isEnabledChangeEmptyStatus() {
        if (orderStatus == OrderStatus.COOKING || orderStatus == OrderStatus.MEAL) {
            throw new UnableChangeEmptyStatusException("요리중이거나 식사중인 테이블은 빈테이블로 변경이 불가능합니다.");
        }
    }

    public void reception() {
        orderLineItems.reception(this);
    }
}