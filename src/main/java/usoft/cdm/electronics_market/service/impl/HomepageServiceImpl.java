package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.constant.Message;
import usoft.cdm.electronics_market.entities.HomePage;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.model.HomePageDTO;
import usoft.cdm.electronics_market.repository.HomePageRepository;
import usoft.cdm.electronics_market.service.HomePageService;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.MapperUtil;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


@Service
@Transactional
@RequiredArgsConstructor
public class HomepageServiceImpl implements HomePageService {

    private final HomePageRepository homePageRepository;

    private final UserService userService;

    @Override
    public Page<HomePage> findByAll(Pageable pageable) {
        return this.homePageRepository.findAllByTypeGreaterThanEqual(1, pageable);
    }

    @Override
    public ResponseEntity<?> save(HomePageDTO dto) {
        Users userLogin = this.userService.getCurrentUser();
        HomePage homePage = HomePage
                .builder()
                .img(dto.getImg())
                .linkProduct(dto.getLinkProduct())
                .type(dto.getType())
                .build();
        homePage.setCreatedBy(userLogin.getUsername());
        this.homePageRepository.save(homePage);
        return ResponseUtil.ok(homePage);
    }

    @Override
    public ResponseEntity<?> update(HomePageDTO dto) {
        Users userLogin = this.userService.getCurrentUser();
        Optional<HomePage> optionalHomePage = this.homePageRepository.findById(dto.getId());
        if (optionalHomePage.isEmpty()) {
            throw new BadRequestException("Phải có id của banner");
        } else {
            HomePage homePage = MapperUtil.map(dto, HomePage.class);
            homePage.setUpdatedBy(userLogin.getUsername());
            this.homePageRepository.save(homePage);
            return ResponseUtil.ok(homePage);
        }
    }

    @Override
    public ResponseEntity<?> delete(List<Integer> homepageIds) {
        Users userLogin = this.userService.getCurrentUser();
        List<HomePage> homepageList = new ArrayList<>();
        homepageIds.forEach(homepageId -> {
            Optional<HomePage> optionalHomePage = this.homePageRepository.findById(homepageId);
            if (optionalHomePage.isEmpty()) {
                throw new BadRequestException("Không tìm thấy id của kho");
            }
            HomePage homePage = optionalHomePage.get();
            homePage.setUpdatedBy(userLogin.getUsername());
            homePage.setType(0);
            homepageList.add(homePage);
        });
        this.homePageRepository.saveAll(homepageList);
        return ResponseUtil.message(Message.REMOVE);
    }

    @Override
    public HomePageDTO getById(Integer idHomePage) {
        Optional<HomePage> optionalHomePage = this.homePageRepository.findById(idHomePage);
        if (optionalHomePage.isEmpty()) {
            throw new BadRequestException("Không tìm thấy id của kho");
        } else {
            HomePageDTO dto = MapperUtil.map(optionalHomePage.get(), HomePageDTO.class);
            return dto;
        }

    }

    @Override
    public List<HomePageDTO> display6ImgForHomePage() {
        List<HomePage> homePagesMain = new ArrayList<>();
        List<HomePage> homePageType1 = this.homePageRepository.findAllByType(1);
        Random random = new Random();
        int randomIndex1 = random.nextInt(homePageType1.size());
        HomePage homePage1 = homePageType1.get(randomIndex1);
        homePagesMain.add(homePage1);
        List<HomePage> homePageType2 = this.homePageRepository.findAllByType(2);
        for (int i = 0; i < 2; i++) {
            int randomIndex2 = random.nextInt(homePageType2.size());
            HomePage homePage2 = homePageType2.get(randomIndex2);
            homePagesMain.add(homePage2);
            homePageType2.remove(randomIndex2);
        }
        List<HomePage> homePageType3 = this.homePageRepository.findAllByType(3);
        for (int i = 0; i < 3; i++) {
            int randomIndex3 = random.nextInt(homePageType3.size());
            HomePage homePage3 = homePageType2.get(randomIndex3);
            homePagesMain.add(homePage3);
            homePageType2.remove(randomIndex3);
        }
        List<HomePageDTO> homePageDTOS = MapperUtil.mapList(homePagesMain, HomePageDTO.class);
        return homePageDTOS;
    }


}

