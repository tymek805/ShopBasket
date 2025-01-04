package com.example.shopbasket.views.adminview;

import com.example.shopbasket.models.Product;
import com.example.shopbasket.repositories.CategoryRepository;
import com.example.shopbasket.repositories.ProductRepository;
import com.example.shopbasket.security.Roles;
import com.example.shopbasket.views.MainView;
import com.example.shopbasket.views.forms.ProductForm;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "admin-products", layout = MainView.class)
@PageTitle("Admin | Products")
@RolesAllowed(Roles.ADMIN)
@SpringComponent
public class AdminProductsView extends AdminView<Product> {
    CategoryRepository categoryRepository;

    public AdminProductsView(ProductRepository productRepository, CategoryRepository categoryRepository) {
        super(Product.class, productRepository);
        this.categoryRepository = categoryRepository;
        this.form = new ProductForm(categoryRepository.findAll());

        configureGrid("id", "name", "weight", "price");
        configureForm();

        add(getToolbar("Products", "Add product", e -> {
            updateList();
            ((ProductForm) form).updateCategories(categoryRepository.findAll());
        }), getContent());

        updateList();
        closeEditor();
    }

    protected void configureGrid(String... columns) {
        super.configureGrid(columns);
        grid.addColumn(product -> product.getCategory() != null ? product.getCategory().getName() : null).setHeader("Category");
        configureComponentColumns();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event){
        updateList();
        if (form instanceof ProductForm) {
            ((ProductForm) form).updateCategories(categoryRepository.findAll());
        }
    }
}
