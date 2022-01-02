package order.application;

import common.config.event.Events;
import order.domain.*;
import order.dto.OrderLineItemRequest;
import order.dto.OrderRequest;
import order.dto.OrderResponse;
import order.dto.OrderStatusUpdateRequest;
import order.exception.NotFoundOrderException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderValidator orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItems = createOrderLineItems(orderRequest.getOrderLineItems());
        orderValidator.validate(orderRequest);
        Events.raise(new OrderCreatedEvent(orderRequest.getOrderTableId()));
        return OrderResponse.of(orderRepository.save(Order.create(orderRequest.getOrderTableId(), orderLineItems)));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusUpdateRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(NotFoundOrderException::new);
        savedOrder.changeOrderStatus(request.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }

    private List<OrderLineItem> createOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItemRequest -> OrderLineItem.of(orderLineItemRequest.getMenuId(), orderLineItemRequest.getQuantity()))
                .collect(Collectors.toList());
    }

}
