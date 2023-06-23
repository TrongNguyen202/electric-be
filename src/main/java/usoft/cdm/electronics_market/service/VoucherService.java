package usoft.cdm.electronics_market.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.voucher.VoucherModel;

import java.util.List;

public interface VoucherService {
    ResponseEntity<?> getAll(Pageable pageable);

    ResponseEntity<?> getVoucher(List<Integer> ids);

    ResponseEntity<?> getById(Integer id);

    ResponseEntity<?> save(VoucherModel model);

    ResponseEntity<?> delete(Integer id);
}
