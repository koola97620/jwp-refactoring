package table.domain;

import order.domain.Order;
import order.exception.NotCreateOrderException;
import order.exception.OrderErrorCode;
import table.exception.NotChangeEmptyException;
import table.exception.NotChangeNumberOfGuestException;
import table.exception.NotValidOrderException;
import table.exception.TableErrorCode;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OrderTableValidator {

    public void validateChangeableEmpty(OrderTable orderTable) {
        for (Order order : orderTable.getOrders()) {
            checkOrderStatus(order);
        }

        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new NotChangeEmptyException(TableErrorCode.ALREADY_ASSIGN_GROUP);
        }
    }

    private void checkOrderStatus(Order order) {
        if (order.isProcessing()) {
            throw new NotValidOrderException();
        }
    }

    public void validateChangeableNumberOfGuests(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new NotChangeNumberOfGuestException(TableErrorCode.EMPTY_TABLE);
        }

        if (orderTable.getNumberOfGuests() < 0) {
            throw new NotChangeNumberOfGuestException(TableErrorCode.GUEST_MORE_THAN_ZERO);
        }
    }

    private void checkEmptyOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new NotCreateOrderException(orderTable.getId() + OrderErrorCode.EMPTY_ORDER_TABLE);
        }
    }
}
