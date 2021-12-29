package kitchenpos.dao;

import kitchenpos.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface TableGroupRepository extends Repository<TableGroup, Long> {
    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    List<TableGroup> findAll();
}
