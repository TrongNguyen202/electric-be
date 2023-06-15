package usoft.cdm.electronics_market.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.entities.HomePage;
import usoft.cdm.electronics_market.model.HomePageDTO;

import java.util.List;

public interface HomePageService {

    Page<HomePage> findByAll(Pageable pageable);

    ResponseEntity<?> save(HomePageDTO dto);

    ResponseEntity<?> update(HomePageDTO dto);

    ResponseEntity<?> delete(List<Integer> homepageIds);

    HomePageDTO getById(Integer idHomePage);

    List<HomePageDTO> displayBannerForHomePage();


}
