package com.example.shopbasket.views.adminview;

import com.example.shopbasket.models.Identifiable;
import com.example.shopbasket.views.forms.ItemForm;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.InvocationTargetException;

abstract class AdminView<T extends Identifiable> extends VerticalLayout implements BeforeEnterObserver {
    private final Class<T> entityClass;

    Grid<T> grid;
    JpaRepository<T, Long> repository;
    ItemForm<T> form;

    public AdminView(Class<T> entityClass, JpaRepository<T, Long> repository) {
        this.entityClass = entityClass;
        this.grid = new Grid<>(entityClass);
        this.repository = repository;

        setSizeFull();
    }

    protected void configureGrid(String... columns) {
        grid.setSizeFull();
        grid.setColumns(columns);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editItem(event.getValue()));
    }

    protected void configureComponentColumns() {
        grid.addComponentColumn(item -> {
            Button editButton = new Button("Edit", e -> editItem(item));
            Button deleteButton = new Button("Delete", e -> deleteItemById(item.getId()));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            return new HorizontalLayout(editButton, deleteButton);
        });
    }

    protected void configureForm() {
        form.setWidth("25em");
        form.addSaveListener(this::saveItem);
        form.addDeleteListener(this::deleteItem);
        form.addCloseListener(e -> closeEditor());
    }

    protected Component getToolbar(String tableNameText, String addButtonText, ComponentEventListener<ClickEvent<Button>> refreshEvent) {
        H2 tableName = new H2(tableNameText);

        Button addButton = new Button(addButtonText);
        addButton.addClickListener(e -> addItem());

        Button refreshButton = new Button("Refresh");
        refreshButton.addClickListener(refreshEvent);

        HorizontalLayout toolbar = new HorizontalLayout(tableName, addButton, refreshButton);
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        toolbar.expand(tableName);
        toolbar.setWidthFull();
        toolbar.addClassNames(LumoUtility.Padding.Vertical.NONE, LumoUtility.Padding.Horizontal.MEDIUM);
        return toolbar;
    }

    protected HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.setSizeFull();
        return content;
    }

    // ITEM
    private void addItem() {
        grid.asSingleSelect().clear();
        try {
            editItem(entityClass.getDeclaredConstructor().newInstance());
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void editItem(T item) {
        if (item == null) {
            closeEditor();
        } else {
            form.setItem(item);
            form.setVisible(true);
        }
    }

    protected void saveItem(ItemForm.SaveEvent<T> event) {
        repository.save(event.getItem());
        updateList();
        closeEditor();
    }

    protected void deleteItem(ItemForm.DeleteEvent<T> event) {
        repository.deleteById(event.getItem().getId());
        updateList();
        closeEditor();
    }

    private void deleteItemById(Long id) {
        repository.deleteById(id);
        updateList();
    }

    // UTILS
    protected void closeEditor() {
        form.setItem(null);
        form.setVisible(false);
    }

    protected void updateList() {
        grid.setItems(repository.findAll());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        updateList();
    }
}
