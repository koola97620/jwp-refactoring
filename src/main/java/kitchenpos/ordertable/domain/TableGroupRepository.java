package kitchenpos.ordertable.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    void delete(TableGroup entity);
}
