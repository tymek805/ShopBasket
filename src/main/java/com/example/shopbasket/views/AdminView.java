package com.example.shopbasket.views;

import com.example.shopbasket.models.Category;
import com.example.shopbasket.models.Product;
import com.example.shopbasket.repositories.CategoryRepository;
import com.example.shopbasket.repositories.ProductRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.RolesAllowed;

import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Route(value = "admin-view", layout = MainView.class)
@PageTitle("Admin View")
@RolesAllowed("ADMIN")
@SpringComponent
public class AdminView extends VerticalLayout {
    Grid<Product> gridProducts;
    Grid<Category> gridCategories;

    ProductForm productForm;
    ProductRepository productRepository;
    CategoryRepository categoryRepository;

    public AdminView(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;

        gridProducts = new Grid<>(Product.class);
        gridCategories = new Grid<>(Category.class);

        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), new H3("Products"), getContent());
        updateList();
        closeEditor();
        gridProducts.addComponentColumn(item -> new Button("Delete", click -> {}));
    }

    private Component getToolbar() {
        Button refreshButton = new Button("Refresh");
        refreshButton.addClickListener(e -> {
            updateList();
            productForm.updateCategories(categoryRepository.findAll());
        });

        Button addProductButton = new Button("Add product");
        addProductButton.addClickListener(e -> addProduct());
        return new HorizontalLayout(refreshButton, addProductButton);
    }

    private void configureGrid() {
        gridProducts.setSizeFull();
        gridProducts.setColumns("id", "name", "weight", "price");
        gridProducts.addColumn(product -> {
            if (product.getCategory() != null)
                return product.getCategory().getName();
            return null;
        }).setHeader("Category");

        gridProducts.getColumns().forEach(col -> col.setAutoWidth(true));
        gridProducts.asSingleSelect().addValueChangeListener(event -> editProduct(event.getValue()));
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(gridProducts, productForm);
        content.setFlexGrow(2, gridProducts);
        content.setFlexGrow(1, productForm);
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        productForm = new ProductForm(categoryRepository.findAll());
        productForm.setWidth("25em");
        productForm.addSaveListener(this::saveProduct);
        productForm.addDeleteListener(this::deleteProduct);
        productForm.addCloseListener(e -> closeEditor());
    }

    private void saveProduct(ProductForm.SaveEvent event) {
        productRepository.save(event.getProduct());
        updateList();
        closeEditor();
    }

    public void editProduct(Product product) {
        if (product == null) {
            closeEditor();
        } else {
            productForm.setProduct(product);
            productForm.setVisible(true);
        }
    }

    private void deleteProduct(ProductForm.DeleteEvent event) {
        productRepository.deleteById(event.getProduct().getId());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        productForm.setProduct(null);
        productForm.setVisible(false);
    }

    private void addProduct() {
        gridProducts.asSingleSelect().clear();
        editProduct(new Product());
    }

    private void updateList() {
        gridProducts.setItems(productRepository.findAll());
    }
}
