package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.Voucher;
import usoft.cdm.electronics_market.model.voucher.VoucherModel;

import java.util.Date;
import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    Page<Voucher> findAllByStatus(Pageable pageable, boolean status);
    Voucher findByCode(String code);
    @Query("SELECT new usoft.cdm.electronics_market.model.voucher.VoucherModel" +
            "(v.id, v.code, v.startDate, v.endDate, v.discount, v.discountMoney, v.quantity, v.quantityUsed, v.note, v.brand)" +
            "FROM Voucher v WHERE v.id = :id")
    VoucherModel getModelById(@Param("id") Integer id);

    @Query("SELECT DISTINCT new usoft.cdm.electronics_market.entities.Voucher" +
            "(v.id, v.code, v.discount, v.discountMoney) FROM Voucher v, Products p " +
            " WHERE v.brand LIKE CONCAT('%||',p.brandId,'||%') AND  p.id IN :ids AND v.startDate <= :date AND v.status = true")
    List<Voucher> getVoucher(@Param("ids") List<Integer> ids, @Param("date") Date date);

//    @Query("SELECT v.id FROM Voucher v FROM v.endDate < :date")
    List<Voucher> findAllByEndDateGreaterThan(Date date);
}
