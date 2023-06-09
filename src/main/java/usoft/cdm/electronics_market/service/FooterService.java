package usoft.cdm.electronics_market.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.FooterModel;

import java.util.List;

public interface FooterService {
    ResponseEntity<?> getHotline();

    ResponseEntity<?> saveHotline(String hotline);

    ResponseEntity<?> getCustomerCareById(Integer id);

    ResponseEntity<?> getCustomerCare(Pageable pageable);

    ResponseEntity<?> getAllCustomerCare();

    ResponseEntity<?> saveCustomerCare(FooterModel model);

    ResponseEntity<?> getSocialNetwork();

    ResponseEntity<?> saveSocialNetwork(List<FooterModel> list);
}
