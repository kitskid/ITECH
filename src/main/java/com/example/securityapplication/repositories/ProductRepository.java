package com.example.securityapplication.repositories;

import com.example.securityapplication.models.PersonReact;
import com.example.securityapplication.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> getProductById(int id);

    Optional<Product> findByName(String name);
    void deleteById(Integer integer);

    List<Product> findAllByProvider_Id(int id);


    @Query(value = "select * from product where ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') or (lower(title) LIKE '%?1')) and (price >= ?2 and price <= ?3)", nativeQuery = true)
    List<Product> findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(String title, float ot, float Do);

    // Поиск по наименованию, фильтрация по диапазону цены, сортировка по возрастанию цены
    @Query(value = "select * from product where ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') or (lower(title) LIKE '%?1')) and (price >= ?2 and price <= ?3) order by  price", nativeQuery = true)
    List<Product> findByTitleOrderByPriceAsc(String title, float ot, float Do);

    // Поиск по наименованию, фильтрация по диапазону цены, сортировка по убыванию цены
    @Query(value = "select * from product where ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') or (lower(title) LIKE '%?1')) and (price >= ?2 and price <= ?3) order by  price desc ", nativeQuery = true)
    List<Product> findByTitleOrderByPriceDest(String title, float ot, float Do);

    // Поиск по наименованию,по категории,  фильтрация по диапазону цены, сортировка по возрастанию цены
    @Query(value = "select * from product where category_id=?4 and ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') or (lower(title) LIKE '%?1')) and (price >= ?2 and price <= ?3) order by  price", nativeQuery = true)
    List<Product> findByTitleAndCategoryOrderByPriceAsc(String title, float ot, float Do, int category);

    // Поиск по наименованию,по категории,  фильтрация по диапазону цены, сортировка по убыванию цены
    @Query(value = "select * from product where category_id=?4 and ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') or (lower(title) LIKE '%?1')) and (price >= ?2 and price <= ?3) order by  price desc ", nativeQuery = true)
    List<Product> findByTitleAndCategoryOrderByPriceDesc(String title, float ot, float Do, int category);

    @Query(value = "select * from product where category_id=?3 and price >= ?1 and price <= ?2 order by  price desc ", nativeQuery = true)
    List<Product> findAllByCategoryOrderByPriceDesc(float Ot, float Do, int category);

    @Query(value = "select * from product where category_id=?3 and (price >= ?1 and price <= ?2) order by  price ", nativeQuery = true)
    List<Product> findAllByCategoryOrderByPriceAsc(float Ot, float Do, int category);

    @Query(value = "select * from product where price >= ?1 and price <= ?2 order by price desc", nativeQuery = true)
    List<Product> findAllByPriceOrderByPriceDesc(float Ot, float Do);

    @Query(value = "select * from product where (price >= ?1 and price <= ?2) order by price ", nativeQuery = true)
    List<Product> findAllByPriceOrderByPriceAsc(float Ot, float Do);
}
