package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.exception.DifferentOrderAndMenuPriceException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void addMenu(Menu menu) {
        menuProducts.forEach(menuProduct -> {
            menuProduct.addMenu(menu);
        });
    }

    public void checkOverPrice(BigDecimal menuPrice) {
        BigDecimal sum = menuProducts.stream()
                .map(MenuProduct::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (menuPrice.compareTo(sum) > 0) {
            throw new DifferentOrderAndMenuPriceException();
        }
    }
}
