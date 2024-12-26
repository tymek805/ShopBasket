package com.example.shopbasket.repositories;

import com.example.shopbasket.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
