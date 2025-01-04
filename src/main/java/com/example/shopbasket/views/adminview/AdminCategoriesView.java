package com.example.shopbasket.views.adminview;

import com.example.shopbasket.models.Category;
import com.example.shopbasket.repositories.CategoryRepository;
import com.example.shopbasket.views.MainView;
import com.example.shopbasket.views.forms.CategoryForm;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "admin-categories", layout = MainView.class)
@PageTitle("Admin | Categories")
@RolesAllowed("ADMIN")
@SpringComponent
public class AdminCategoriesView extends AdminView<Category> {
    public AdminCategoriesView(CategoryRepository repository) {
        super(Category.class, repository);
        this.form = new CategoryForm();

        configureGrid("id", "name", "code");
        configureComponentColumns();
        configureForm();

        add(getToolbar("Categories", "Add category", e -> updateList()), getContent());
        closeEditor();
    }
}
