package usoft.cdm.electronics_market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.BillDetail;

import java.util.List;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, Integer> {
    List<BillDetail> findAllByBillId(Integer billId);

    @Query("SELECT bd.id FROM BillDetail bd WHERE bd.billId = :billId")
    List<Integer> findAllProductIdByBillId(@Param("billId") Integer billId);

    @Query("SELECT SUM(bd.quantity) FROM BillDetail bd WHERE bd.productId = :productId GROUP BY bd.productId")
    Integer sumQuantitySell(@Param("productId") Integer productId);

}
