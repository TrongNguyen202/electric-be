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
    public ResponseEntity<?> getHotline() {
        return ResponseUtil.ok(footerPageRepository.findByType(1).orElse(new FooterPage()));
    }

    @Override
    public ResponseEntity<?> saveHotline(String hotline) {
        FooterPage f = footerPageRepository.findByType(1).orElse(new FooterPage());
        f.setType(1);
        f.setContent(hotline);
        f = footerPageRepository.save(f);
        return ResponseUtil.ok(f);
    }

    @Override
    public ResponseEntity<?> getCustomerCareById(Integer id) {
        return ResponseUtil.ok(footerPageRepository.findById(id).orElseThrow());
    }

    @Override
    public ResponseEntity<?> getCustomerCare(Pageable pageable) {
        return ResponseUtil.ok(footerPageRepository.findAllModelByType(pageable, 2));
    }

    @Override
    public ResponseEntity<?> getAllCustomerCare() {
        return ResponseUtil.ok(footerPageRepository.findAllModelByType(2));
    }

    @Override
    public ResponseEntity<?> saveCustomerCare(FooterModel model) {
        FooterPage f = footerPageRepository.findById(model.getId()).orElse(new FooterPage());
        f.setName(model.getName());
        f.setContent(model.getContent());
        f.setType(2);
        return ResponseUtil.ok(footerPageRepository.save(f));
    }

    @Override
    public ResponseEntity<?> getSocialNetwork() {
        return ResponseUtil.ok(footerPageRepository.findAllModelByType(3));
    }

    @Override
    public ResponseEntity<?> saveSocialNetwork(List<FooterModel> list) {
        List<FooterPage> remove = footerPageRepository.findAllByType(3);
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
            s.add(f);
        });
        footerPageRepository.saveAll(s);
        footerPageRepository.deleteAll(remove);
        return ResponseUtil.ok(footerPageRepository.findAllModelByType(3));
    }
}
