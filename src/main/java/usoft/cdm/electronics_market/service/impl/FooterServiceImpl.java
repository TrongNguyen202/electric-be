package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.constant.Message;
import usoft.cdm.electronics_market.entities.FooterPage;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.model.FooterModel;
import usoft.cdm.electronics_market.repository.FooterPageRepository;
import usoft.cdm.electronics_market.service.FooterService;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.MapperUtil;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FooterServiceImpl implements FooterService {
    private final FooterPageRepository footerPageRepository;

    private final UserService userService;

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
        List<FooterModel> list = footerPageRepository.findAllModelByTypeAndWarehouse(3, idWarehouse);
        if (list.isEmpty()) {
            list.add(new FooterModel(null, "facebook", null));
            list.add(new FooterModel(null, "youtube", null));
            list.add(new FooterModel(null, "tiktok", null));
            list.add(new FooterModel(null, "instagram", null));
        }
        return ResponseUtil.ok(list);
    }

    @Override
    public ResponseEntity<?> saveSocialNetwork(List<FooterModel> list, Integer idWarehouse) {
        List<FooterPage> remove = footerPageRepository.findAllByTypeAndIdWarehouse(3, idWarehouse);
        List<FooterPage> s = new ArrayList<>();
        list.forEach(model -> {
            FooterPage f = new FooterPage();
            if (model.getId() != null) {
                f = footerPageRepository.findById(model.getId()).orElse(new FooterPage());
                remove.remove(f);
            }
            f.setName(model.getName());
            f.setLink(model.getLink());
            f.setType(3);
            f.setIdWarehouse(idWarehouse);
            s.add(f);
        });
        footerPageRepository.saveAll(s);
        footerPageRepository.deleteAll(remove);
        return ResponseUtil.ok(footerPageRepository.findAllModelByTypeAndWarehouse(3, idWarehouse));
    }

    @Override
    public ResponseEntity<?> saveSupportMain(FooterModel model) {
        Users userLogin = this.userService.getCurrentUser();
        FooterPage footerPage = FooterPage
                .builder()
                .type(4)
                .name(model.getName())
                .content(model.getContent())
                .icon(model.getIcon())
                .build();
        footerPage.setCreatedBy(userLogin.getUsername());

        this.footerPageRepository.save(footerPage);
        return ResponseUtil.ok(footerPage);
    }

    @Override
    public ResponseEntity<?> updateSupportMain(FooterModel model) {
        Users userLogin = this.userService.getCurrentUser();
        Optional<FooterPage> footerPage = this.footerPageRepository.findAllByIdAndType(model.getId(), 4);
        if (footerPage.isEmpty()) {
            throw new BadRequestException("Không tìm thấy id ");
        }
        FooterPage page = MapperUtil.map(model, FooterPage.class);
        page.setType(4);
        page.setUpdatedBy(userLogin.getUsername());
        this.footerPageRepository.save(page);
        return ResponseUtil.ok(page);
    }

    @Override
    public ResponseEntity<?> getAllSupportMain(Pageable pageable) {
        Page<FooterPage> footerPages = this.footerPageRepository.findAllByType(4, pageable);
        return ResponseUtil.ok(footerPages);
    }

    @Override
    public ResponseEntity<?> getByIdSupportMain(Integer idMain) {
        Optional<FooterPage> page = this.footerPageRepository.findAllByIdAndType(idMain, 4);
        if (page.isEmpty()) {
            throw new BadRequestException("Không tìm thấy id");
        }
        return ResponseUtil.ok(page.get());
    }

    @Override
    public ResponseEntity<?> deleteSupportMain(List<Integer> idMains) {
        List<FooterPage> footerPages = new ArrayList<>();
        if (idMains.size() < 1) {
            throw new BadRequestException("Phải thêm cấu hình cần xóa");
        }
        idMains.forEach(idMain -> {
            Optional<FooterPage> page = this.footerPageRepository.findAllByIdAndType(idMain, 4);
            if (page.isEmpty()) {
                throw new BadRequestException("Không tìm thấy id");
            }
            FooterPage footerPage = page.get();
            footerPages.add(footerPage);
        });
        this.footerPageRepository.deleteAll(footerPages);
        return ResponseUtil.message(Message.REMOVE);
    }

    @Override
    public ResponseEntity<?> getAllSupportMainList() {
        return ResponseUtil.ok(this.footerPageRepository.findAllByType(4));
    }
}
