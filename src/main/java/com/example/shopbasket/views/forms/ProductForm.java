package com.example.shopbasket.views.forms;

import com.example.shopbasket.models.Category;
import com.example.shopbasket.models.Product;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;

public class ProductForm extends ItemForm<Product> {
    TextField name = new TextField("Name");
    NumberField weight = new NumberField("Weight");
    NumberField price = new NumberField("Price");
    ComboBox<Category> categoryComboBox = new ComboBox<>("Category");

    public ProductForm(List<Category> categories) {
        super(Product.class);
        categoryComboBox.setItems(categories);
        categoryComboBox.setItemLabelGenerator(Category::getName);

        binder.forField(weight)
                .withValidator(value -> value != null && value > 0.0, "Value must be greater than zero")
                .bind(Product::getWeight, Product::setWeight);
        binder.forField(price)
                .withValidator(value -> value != null && value > 0.0, "Value must be greater than zero")
                .bind(Product::getPrice, Product::setPrice);
        binder.forField(categoryComboBox).bind(Product::getCategory, Product::setCategory);

        binder.bindInstanceFields(this);
        add(name, weight, price, categoryComboBox, createButtonsLayout());
    }

    public void updateCategories(List<Category> categories) {
        categoryComboBox.setItems(categories);
        categoryComboBox.setItemLabelGenerator(Category::getName);
        binder.forField(categoryComboBox).bind(Product::getCategory, Product::setCategory);
    }
}
