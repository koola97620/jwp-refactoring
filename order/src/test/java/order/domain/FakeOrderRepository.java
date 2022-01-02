package order.domain;

import java.util.*;
import java.util.stream.Collectors;

public class FakeOrderRepository implements OrderRepository {
    private Map<Long, Order> map = new HashMap<>();
    private Long orderKey = 1L;
    private Long orderLineItemKey = 1L;

    @Override
    public Order save(Order inputOrder) {
        if (map.containsKey(inputOrder.getId())) {
            map.put(inputOrder.getId(), inputOrder);
            return inputOrder;
        }
        Order order = new Order(orderKey, inputOrder.getOrderTableId(), inputOrder.getOrderStatus().name(), inputOrder.getOrderedTime(), createOrderLineItems(inputOrder));
        map.put(orderKey, order);
        orderKey++;
        return order;
    }

    private List<OrderLineItem> createOrderLineItems(Order inputOrder) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        Order order = new Order(orderKey, inputOrder.getOrderTableId(), inputOrder.getOrderStatus().name(), inputOrder.getOrderedTime(), inputOrder.getOrderLineItems());

        for (OrderLineItem inputOrderLineItem : inputOrder.getOrderLineItems()) {
            OrderLineItem orderLineItem = new OrderLineItem(orderLineItemKey, order, inputOrderLineItem.getMenuId(), inputOrderLineItem.getQuantity());
            orderLineItems.add(orderLineItem);
            orderLineItemKey++;
        }

        return orderLineItems;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses) {
        return map.values().stream()
                .filter(order -> order.getOrderTableId().equals(orderTableId))
                .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses) {
        return map.values().stream()
                .filter(order -> orderTableIds.contains(order.getOrderTableId()))
                .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }

    public List<Order> findByOrderTableId(Long orderTableId) {
        return map.values().stream()
                .filter(order -> order.getOrderTableId().equals(orderTableId))
                .collect(Collectors.toList());
    }
}