package usoft.cdm.electronics_market.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.voucher.VoucherModel;

public interface VoucherService {
    ResponseEntity<?> getAll(Pageable pageable);

    ResponseEntity<?> getById(Integer id);

    ResponseEntity<?> save(VoucherModel model);

    ResponseEntity<?> delete(Integer id);
}
