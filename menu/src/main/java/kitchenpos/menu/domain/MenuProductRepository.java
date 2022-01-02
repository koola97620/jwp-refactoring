package kitchenpos.menu.domain;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface MenuProductRepository extends Repository<MenuProduct, Long> {
    MenuProduct save(MenuProduct entity);

    Optional<MenuProduct> findById(Long id);

    List<MenuProduct> findAll();

    List<MenuProduct> findAllByMenuId(Long menuId);
}