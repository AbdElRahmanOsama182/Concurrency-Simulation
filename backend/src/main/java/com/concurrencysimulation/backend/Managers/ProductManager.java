package com.concurrencysimulation.backend.Managers;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.concurrencysimulation.backend.Models.Product;

public class ProductManager {
    private static ProductManager instance = null;
    private int nextId = 0;
    private Map<Integer, Product> products = new HashMap<Integer, Product>();

    private ProductManager() {
    }

    public static synchronized ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    public void createProduct() {
        Color color = getRandomColor();
        Product product = new Product(color, nextId);
        products.put(nextId, product);
        nextId++;
    }

    public void createProducts(int n) {
        if (n < 1) {
            n = (int) (Math.random() * 10);
        }
        for (int i = 0; i < n; i++) {
            createProduct();
        }
    }

    public Product getProduct(int id) {
        if (products.containsKey(id))
            return products.get(id);
        return null;
    }

    public void removeProduct(int id) {
        if (products.containsKey(id))
            products.remove(id);
    }

    public Map<Integer, Product> getProducts() {
        return products;
    }

    public void setProducts(Map<Integer, Product> products){
        this.products = products;
    }

    public Color getRandomColor() {
        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        Color color = new Color(r, g, b);
        for (Product product : products.values()) {
            if (product.getColor().equals(color)) {
                return getRandomColor();
            }
        }
        return color;
    }

    public void clear() {
        products.clear();
    }
}
