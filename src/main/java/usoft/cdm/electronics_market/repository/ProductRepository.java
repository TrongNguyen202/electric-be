package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.Products;
import usoft.cdm.electronics_market.model.bill.ProductBill;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer>, ProductRepositoryCustom {

    Page<Products> findAllByStatus(Boolean status, Pageable pageable);

    Optional<Products> findByIdAndStatus(Integer id, Boolean status);

//    List<Products> findAllByStatus(Boolean status);

    Optional<Products> findByCodeAndStatus(String code, Boolean status);

    List<Products> findByStatusAndCategoryId(Boolean status, Integer categoryId);

    List<Products> findByStatusAndBrandId(Boolean status, Integer brandId);

    List<Products> findByStatusAndWarehouseId(Boolean status, Integer warehouseId);

    @Query("SELECT new usoft.cdm.electronics_market.model.bill.ProductBill" +
            " (bd.id, bd.billId, p.code, p.name, bd.quantity, bd.priceSell)" +
            " FROM Products p, BillDetail bd" +
            " WHERE p.id = bd.productId AND bd.id IN :ids")
    List<ProductBill> findAllByIdInBill(@Param("ids") List<Integer> ids);

    List<Products> findAllByIdIn(List<Integer> ids);

    List<Products> findAllByStatusAndCategoryIdIn(Boolean status, List<Integer> categoryIds);

    Optional<Products> findByBrandId(Integer brandId);

    @Query("SELECT p FROM Products p where p.status = true AND p.categoryId = :category")
    Page<Products> searchByCategory(Pageable pageable, @Param("category") Integer category);

    @Query("SELECT p FROM Products p where p.status = true AND p.name LIKE %:name%")
    Page<Products> searchByName(Pageable pageable, @Param("name") String name);

    @Query("SELECT count(p.id) FROM Products p where p.status = true AND p.categoryId = :category " +
            "AND (p.priceSell between :from AND :to)")
    Integer sumProduct(@Param("category") Integer category,
                       @Param("from") Double priceFrom, @Param("to") Double priceTo);
}
