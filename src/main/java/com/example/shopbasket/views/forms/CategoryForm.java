package com.example.shopbasket.views.forms;

import com.example.shopbasket.models.Category;
import com.vaadin.flow.component.textfield.TextField;

public class CategoryForm extends ItemForm<Category> {
    TextField name = new TextField("Name");
    TextField code = new TextField("Code");

    public CategoryForm() {
        super(Category.class);
        binder.forField(name).bind(Category::getName, Category::setName);
        binder.forField(code).bind(Category::getCode, Category::setCode);

        binder.bindInstanceFields(this);
        add(name, code, createButtonsLayout());
    }
}
