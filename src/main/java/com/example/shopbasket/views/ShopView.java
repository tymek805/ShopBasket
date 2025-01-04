package com.example.shopbasket.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "", layout = MainView.class)
@PageTitle("Shop")
@PermitAll
public class ShopView extends VerticalLayout {

}
