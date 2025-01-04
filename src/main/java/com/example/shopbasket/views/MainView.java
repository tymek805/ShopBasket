package com.example.shopbasket.views;

import com.example.shopbasket.security.SecurityService;
import com.example.shopbasket.views.adminview.AdminCategoriesView;
import com.example.shopbasket.views.adminview.AdminProductsView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainView extends AppLayout {
    private final SecurityService securityService;

    public MainView(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
    }

    private void createHeader() {
        H1 logo = new H1("Shop Basket");
        logo.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.MEDIUM);

        HorizontalLayout navigationLayout = new HorizontalLayout(
                new RouterLink("Products", AdminProductsView.class),
                new RouterLink("Categories", AdminCategoriesView.class)
        );

        String username = securityService.getAuthenticatedUser().getUsername();
        H3 usernameLabel = new H3(username);
        usernameLabel.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.Margin.NONE);

        Button logout = new Button("Logout", event -> securityService.logout());

        var header = new HorizontalLayout(logo, navigationLayout, usernameLabel, usernameLabel, logout);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(navigationLayout);
        header.setWidthFull();
        header.addClassNames(LumoUtility.Padding.Vertical.NONE, LumoUtility.Padding.Horizontal.MEDIUM);
        addToNavbar(header);
    }
}
