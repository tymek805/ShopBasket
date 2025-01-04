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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

@Route(value = "", layout = MainView.class)
@PageTitle("Shop")
@PermitAll
public class ShopView extends VerticalLayout {
    private final ProductRepository productRepository;
    private final Grid<Product> grid;

    private final CartCookie cartCookie;

    public ShopView(ProductRepository productRepository) {
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
        grid.addComponentColumn(product -> new Button("Add to cart", e -> addToCart(product)));
    }

    private Component getToolbar() {
        H2 tableName = new H2("Products");
        Button cartButton = new Button("Cart", new Icon(VaadinIcon.CART));
        cartButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cartButton.addClickListener(event ->
                UI.getCurrent().navigate(CartView.class)
        );

        HorizontalLayout toolbar = new HorizontalLayout(tableName, cartButton);
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        toolbar.expand(tableName);
        toolbar.setWidthFull();
        toolbar.addClassNames(LumoUtility.Padding.Vertical.NONE, LumoUtility.Padding.Horizontal.MEDIUM);
        return toolbar;
    }

    private void addToCart(Product product) {
        boolean response = cartCookie.addItemToCart(product);
        if (response) {
            Notification notification = Notification.show("Successfully added '" + product.getName() + "' to the cart", 2000, Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } else {
            Notification notification = Notification.show("Item could not be added", 2000, Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void updateList() {
        grid.setItems(productRepository.findAll());
    }
}
