package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import usoft.cdm.electronics_market.entities.FooterPage;
import usoft.cdm.electronics_market.model.FooterModel;
import usoft.cdm.electronics_market.repository.FooterPageRepository;
import usoft.cdm.electronics_market.service.FooterService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FooterServiceImpl implements FooterService {
    private final FooterPageRepository footerPageRepository;

    @Override
    public ResponseEntity<?> getHotline(Integer idWarehouse) {
        return ResponseUtil.ok(footerPageRepository.findByTypeAndIdWarehouse(1, idWarehouse).orElse(new FooterPage()));
    }

    @Override
    public ResponseEntity<?> saveHotline(String hotline, Integer idWarehouse) {
        FooterPage f = footerPageRepository.findByTypeAndIdWarehouse(1, idWarehouse).orElse(new FooterPage());
        f.setType(1);
        f.setContent(hotline);
        f.setIdWarehouse(idWarehouse);
        f = footerPageRepository.save(f);
        return ResponseUtil.ok(f);
    }

    @Override
    public ResponseEntity<?> getCustomerCareById(Integer id) {
        return ResponseUtil.ok(footerPageRepository.findById(id).orElseThrow());
    }

    @Override
    public ResponseEntity<?> getCustomerCare(Pageable pageable, Integer idWarehouse) {
        return ResponseUtil.ok(footerPageRepository.findAllModelByTypeAndWarehouse(pageable, 2, idWarehouse));
    }

    @Override
    public ResponseEntity<?> getAllCustomerCare(Integer idWarehouse) {
        return ResponseUtil.ok(footerPageRepository.findAllModelByTypeAndWarehouse(2, idWarehouse));
    }

    @Override
    public ResponseEntity<?> saveCustomerCare(FooterModel model, Integer idWarehouse) {
        FooterPage f = new FooterPage();
        if (model.getId() != null)
            f = footerPageRepository.findById(model.getId()).orElse(new FooterPage());
        f.setName(model.getName());
        f.setContent(model.getContent());
        f.setType(2);
        f.setIdWarehouse(idWarehouse);
        return ResponseUtil.ok(footerPageRepository.save(f));
    }

    @Override
    public ResponseEntity<?> deleteCustomerCare(Integer id) {
        footerPageRepository.deleteById(id);
        return ResponseUtil.ok("Xóa thành công!");
    }


        @Override
    public ResponseEntity<?> getSocialNetwork(Integer idWarehouse) {
        return ResponseUtil.ok(footerPageRepository.findAllModelByTypeAndWarehouse(3, idWarehouse));
    }

    @Override
    public ResponseEntity<?> saveSocialNetwork(List<FooterModel> list, Integer idWarehouse) {
        List<FooterPage> remove = footerPageRepository.findAllByTypeAndIdWarehouse(3, idWarehouse);
        List<FooterPage> s = new ArrayList<>();
        list.forEach(model -> {
            FooterPage f = new FooterPage();
            if (model.getId() != null){
                f = footerPageRepository.findById(model.getId()).orElse(new FooterPage());
                remove.remove(f);
            }
            f.setName(model.getName());
            f.setContent(model.getContent());
            f.setType(3);
            f.setIdWarehouse(idWarehouse);
            s.add(f);
        });
        footerPageRepository.saveAll(s);
        footerPageRepository.deleteAll(remove);
        return ResponseUtil.ok(footerPageRepository.findAllModelByTypeAndWarehouse(3, idWarehouse));
    }
}
