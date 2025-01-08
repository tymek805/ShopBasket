package com.example.shopbasket.views;

import com.example.shopbasket.CartCookie;
import com.example.shopbasket.models.Product;
import com.example.shopbasket.repositories.ProductRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Route(value = "cart", layout = MainView.class)
@PageTitle("Cart")
@PermitAll
public class CartView extends VerticalLayout {
    private final Grid<Product> grid;
    private final CartCookie cartCookie;
    private NumberField amountField;
    private final ProductRepository productRepository;
    private final HashMap<Product, Integer> amountMap;

    public CartView(ProductRepository productRepository) {
        this.productRepository = productRepository;
        grid = new Grid<>(Product.class);
        cartCookie = new CartCookie();
        amountMap = new HashMap<>();
        configureGrid("id", "name", "weight", "price");

        setSizeFull();
        add(getToolbar(), grid);
        updateList();

        amountField.setValue(calculateTotal());
    }

    private void configureGrid(String... columns) {
        grid.setSizeFull();
        grid.setColumns(columns);
        grid.addColumn(product -> product.getCategory() != null ? product.getCategory().getName() : null).setHeader("Category");
        grid.addComponentColumn(product -> {
            IntegerField amount = new IntegerField();
            amount.setValue(cartCookie.getAmount(product));
            amount.setStepButtonsVisible(true);
            amount.setMin(1);
            amount.addValueChangeListener(event -> {
                cartCookie.changeAmount(product, event.getValue());
                amountMap.put(product, event.getValue());
                amountField.setValue(calculateTotal());
            });

            Button removeButton = new Button(new Icon(VaadinIcon.TRASH));
            removeButton.addClickListener(event -> {
                amountMap.remove(product);
                cartCookie.removeProduct(product);
                updateList();
                amountField.setValue(calculateTotal());
            });
            removeButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

            return new HorizontalLayout(amount, removeButton);
        });
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private Component getToolbar() {
        H2 tableName = new H2("Cart");

        amountField = new NumberField();
        amountField.setLabel("Total");

        Button backButton = new Button("Back", new Icon(VaadinIcon.ARROW_LEFT));
        backButton.addClickListener(event -> {
            UI.getCurrent().navigate(ShopView.class);
        });

        HorizontalLayout toolbar = new HorizontalLayout(tableName, amountField, backButton);
        toolbar.setDefaultVerticalComponentAlignment(Alignment.END);
        toolbar.expand(tableName);
        toolbar.setWidthFull();
        toolbar.addClassNames(LumoUtility.Padding.Vertical.NONE, LumoUtility.Padding.Horizontal.MEDIUM);
        return toolbar;
    }

    private double calculateTotal() {
        return grid.getDataProvider().fetch(new Query<>())
                .map(product -> product.getPrice() * amountMap.getOrDefault(product, 1))
                .reduce(0.0, Double::sum);
    }


    private void updateList() {
        List<Product> products = new ArrayList<>();
        for (Long key : cartCookie.getCartMap().keySet()) {
            productRepository.findById(key).ifPresent(products::add);
        }

        grid.setItems(products);
        for (Product product : products) {
            amountMap.put(product, cartCookie.getAmount(product));
        }
    }
}
