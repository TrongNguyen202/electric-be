package usoft.cdm.electronics_market.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.FooterModel;

import java.util.List;

public interface FooterService {
    ResponseEntity<?> getHotline(Integer idWarehouse);

    ResponseEntity<?> saveHotline(String hotline, Integer idWarehouse);

    ResponseEntity<?> getCustomerCareById(Integer id);

    ResponseEntity<?> getCustomerCare(Pageable pageable, Integer idWarehouse);

    ResponseEntity<?> getAllCustomerCare(Integer idWarehouse);

    ResponseEntity<?> saveCustomerCare(FooterModel model, Integer idWarehouse);

    ResponseEntity<?> deleteCustomerCare(Integer id);

    ResponseEntity<?> getSocialNetwork(Integer idWarehouse);

    ResponseEntity<?> saveSocialNetwork(List<FooterModel> list, Integer idWarehouse);
}
