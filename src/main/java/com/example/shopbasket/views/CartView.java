package com.example.shopbasket.views;

import com.example.shopbasket.CartCookie;
import com.example.shopbasket.models.Product;
import com.example.shopbasket.repositories.ProductRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;

@Route(value = "cart", layout = MainView.class)
@PageTitle("Cart")
@PermitAll
public class CartView extends VerticalLayout {
    private final Grid<Product> grid;
    private final CartCookie cartCookie;
    private final ProductRepository productRepository;

    public CartView(ProductRepository productRepository) {
        this.productRepository = productRepository;
        grid = new Grid<>(Product.class);
        cartCookie = new CartCookie();
        configureGrid("id", "name", "weight", "price");

        setSizeFull();
        add(getToolbar(), grid);
        updateList();
    }

    private void configureGrid(String... columns) {
        grid.setSizeFull();
        grid.setColumns(columns);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addColumn(product -> product.getCategory() != null ? product.getCategory().getName() : null).setHeader("Category");
        grid.addComponentColumn(product -> {
            IntegerField amount = new IntegerField();
            amount.setValue(cartCookie.getAmount(product));
            amount.setStepButtonsVisible(true);
            amount.setMin(0);
            amount.addValueChangeListener(event -> cartCookie.changeAmount(product, event.getValue()));
            return amount;
        });
    }

    private Component getToolbar() {
        H2 tableName = new H2("Cart");
        HorizontalLayout toolbar = new HorizontalLayout(tableName);
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        toolbar.expand(tableName);
        toolbar.setWidthFull();
        toolbar.addClassNames(LumoUtility.Padding.Vertical.NONE, LumoUtility.Padding.Horizontal.MEDIUM);
        return toolbar;
    }

    private void updateList() {
        List<Product> products = new ArrayList<>();
        for (Long key : cartCookie.getCartMap().keySet()) {
            productRepository.findById(key).ifPresent(products::add);
        }
        grid.setItems(products);
    }
}
