package com.example.shopbasket;

import com.example.shopbasket.models.Product;
import com.vaadin.flow.server.VaadinService;
import jakarta.servlet.http.Cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartCookie {
    private static final String CART_COOKIE = "CART";
    private static final String ITEM_SEPARATOR = "/";
    private static final String TUPLE_SEPARATOR = ":";

    public boolean addItemToCart(Product product) {
        List<String> cart = readCart();
        int idxToRemove = -1;
        int value = 1;
        for (int i = 0; i < cart.size() && idxToRemove == -1; i++) {
            String[] itemParts = cart.get(i).split(TUPLE_SEPARATOR);
            if (itemParts.length == 2) {
                if (product.getId().toString().equals(itemParts[0])) {
                    idxToRemove = i;
                    try {
                        value = Integer.parseInt(itemParts[1]) + 1;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        if (idxToRemove != -1)
            cart.remove(idxToRemove);
        cart.add(product.getId() + TUPLE_SEPARATOR + value);
        saveCart(cart);
        return true;
    }

    public void changeAmount(Product product, Integer integer) {
        List<String> cart = readCart();
        int idxToRemove = -1;
        for (int i = 0; i < cart.size() && idxToRemove == -1; i++) {
            String[] itemParts = cart.get(i).split(TUPLE_SEPARATOR);
            if (itemParts.length == 2) {
                if (product.getId().toString().equals(itemParts[0])) {
                    idxToRemove = i;
                }
            }
        }
        if (idxToRemove != -1)
            cart.remove(idxToRemove);
        cart.add(product.getId() + TUPLE_SEPARATOR + integer);
        saveCart(cart);
    }

    public Integer getAmount(Product product) {
        return getCartMap().get(product.getId());
    }

    public HashMap<Long, Integer> getCartMap() {
        HashMap<Long, Integer> cartProducts = new HashMap<>();

        List<String> cart = readCart();
        for (String item : cart) {
            String[] itemParts = item.split(TUPLE_SEPARATOR);
            if (itemParts.length == 2) {
                cartProducts.put(
                        Long.valueOf(itemParts[0]),
                        Integer.parseInt(itemParts[1])
                );
            }
        }
        return cartProducts;
    }

    public List<String> readCart() {
        String cookieValue = readCookie();
        return cookieValue != null ? new ArrayList<>(List.of(cookieValue.split(ITEM_SEPARATOR))) : new ArrayList<>();
    }

    private void saveCart(List<String> cart) {
        Cookie cookie = new Cookie(CART_COOKIE, String.join(ITEM_SEPARATOR, cart));
        cookie.setMaxAge(60 * 10);
        cookie.setPath(VaadinService.getCurrentRequest().getContextPath());
        VaadinService.getCurrentResponse().addCookie(cookie);
    }

    private String readCookie() {
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(CART_COOKIE)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
