package com.example.shopbasket;

import com.example.shopbasket.models.Category;
import com.example.shopbasket.models.Product;
import com.example.shopbasket.repositories.CategoryRepository;
import com.example.shopbasket.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataLoader(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        Category pieczywo = new Category();
        pieczywo.setName("Pieczywo");
        pieczywo.setCode("K1");
        categoryRepository.save(pieczywo);

        Category nabial = new Category();
        nabial.setName("Nabiał");
        nabial.setCode("K2");
        categoryRepository.save(nabial);

        Category mieso = new Category();
        mieso.setName("Mięso");
        mieso.setCode("K3");
        categoryRepository.save(mieso);

        Product chleb = new Product();
        chleb.setName("Chleb");
        chleb.setWeight(1.0);
        chleb.setPrice(5.20);
        chleb.setCategory(pieczywo);

        Product maslo = new Product();
        maslo.setName("Masło");
        maslo.setWeight(0.25);
        maslo.setPrice(7.00);
        maslo.setCategory(nabial);

        Product kielbasa = new Product();
        kielbasa.setName("Kiełbasa");
        kielbasa.setWeight(0.5);
        kielbasa.setPrice(20.00);
        kielbasa.setCategory(mieso);

        productRepository.save(chleb);
        productRepository.save(maslo);
        productRepository.save(kielbasa);

        System.out.println("Baza danych została zainicjalizowana.");
    }
}
