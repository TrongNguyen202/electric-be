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

@Service
@RequiredArgsConstructor
public class FooterServiceImpl implements FooterService {
    private final FooterPageRepository footerPageRepository;

    public ResponseEntity<?> getHotline(String hotline){
        return ResponseUtil.ok(footerPageRepository.findByType(1).orElse(new FooterPage()));
    }

    public ResponseEntity<?> saveHotline(String hotline){
        FooterPage f = footerPageRepository.findByType(1).orElse(new FooterPage());
        f.setType(1);
        f.setContent(hotline);
        f = footerPageRepository.save(f);
        return ResponseUtil.ok(f);
    }

    public ResponseEntity<?> getCustomerCareById(Integer id){
        return ResponseUtil.ok(footerPageRepository.findById(id).orElseThrow());
    }

    public ResponseEntity<?> getCustomerCare(Pageable pageable){
        return ResponseUtil.ok(footerPageRepository.findAllByType(pageable, 2));
    }

    public ResponseEntity<?> getAllCustomerCare(){
        return ResponseUtil.ok(footerPageRepository.findAllByType(2));
    }

    public ResponseEntity<?> saveCustomerCare(FooterModel model){
        FooterPage f = footerPageRepository.findById(model.getId()).orElse(new FooterPage());
        f.setName(model.getName());
        f.setContent(model.getContent());
        return ResponseUtil.ok(footerPageRepository.save(f));
    }

    public ResponseEntity<?> save(){
        return null;
    }
}
