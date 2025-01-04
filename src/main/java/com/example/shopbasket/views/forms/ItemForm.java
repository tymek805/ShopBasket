package com.example.shopbasket.views.forms;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
public abstract class ItemForm<T> extends FormLayout {
    private final Class<T> itemClass;
    protected final Binder<T> binder;

    public ItemForm(Class<T> itemClass) {
        this.itemClass = itemClass;
        binder = new BeanValidationBinder<>(itemClass);
    }

    protected Component createButtonsLayout() {
        Button save = new Button("Save");
        Button delete = new Button("Delete");
        Button close = new Button("Cancel");

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent<>(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent<>(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent<>(this, binder.getBean()));
        }
    }

    public void setItem(T item) {
        binder.setBean(item);
    }

    public static abstract class ItemFormEvent<T> extends ComponentEvent<ItemForm<T>> {
        private final T item;

        protected ItemFormEvent(ItemForm<T> source, T item) {
            super(source, false);
            this.item = item;
        }

        public T getItem() {
            return item;
        }
    }

    public static class SaveEvent<T> extends ItemFormEvent<T> {
        SaveEvent(ItemForm<T> source, T item) {
            super(source, item);
        }
    }

    public static class DeleteEvent<T> extends ItemFormEvent<T> {
        DeleteEvent(ItemForm<T> source, T item) {
            super(source, item);
        }
    }

    public static class CloseEvent<T> extends ItemFormEvent<T> {
        CloseEvent(ItemForm<T> source) {
            super(source, null);
        }
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent<T>> listener) {
        return addListener((Class<SaveEvent<T>>) (Class<?>) SaveEvent.class, listener);
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent<T>> listener) {
        return addListener((Class<DeleteEvent<T>>) (Class<?>) DeleteEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent<T>> listener) {
        return addListener((Class<CloseEvent<T>>) (Class<?>) CloseEvent.class, listener);
    }
}
