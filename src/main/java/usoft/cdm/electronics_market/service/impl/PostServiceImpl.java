package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import usoft.cdm.electronics_market.entities.Poster;
import usoft.cdm.electronics_market.entities.PosterCategory;
import usoft.cdm.electronics_market.model.Poster.PosterCategoryModel;
import usoft.cdm.electronics_market.model.Poster.PosterSaveCategory;
import usoft.cdm.electronics_market.model.Poster.SavePoster;
import usoft.cdm.electronics_market.repository.PosterCategoryRepository;
import usoft.cdm.electronics_market.repository.PosterRepository;
import usoft.cdm.electronics_market.service.PostService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PosterRepository posterRepository;
    private final PosterCategoryRepository posterCategoryRepository;

    @Override
    public ResponseEntity<?> getPosterCategory(Integer parentId) {
        if (parentId != null)
            return ResponseUtil.ok(posterCategoryRepository.findAllByParentIdAndStatus(parentId, true));
        List<PosterCategory> posterCategoryList = posterCategoryRepository.findAllByParentIdAndStatus(null, true);
        List<PosterCategoryModel> list = new ArrayList<>();
        posterCategoryList.forEach(x -> {
            PosterCategoryModel model = new PosterCategoryModel();
            model.setId(x.getId());
            model.setName(x.getName());
            model.setChild(posterCategoryRepository.findAllByParentIdAndStatus(x.getId(), true));
            list.add(model);
        });
        return ResponseUtil.ok(list);
    }

    @Override
    public ResponseEntity<?> getPosterCategory(Pageable pageable) {
        Page<PosterCategory> posterCategoryList = posterCategoryRepository.findAllByParentIdAndStatus(pageable, null, true);
        List<PosterCategoryModel> list = new ArrayList<>();
        posterCategoryList.forEach(x -> {
            PosterCategoryModel model = new PosterCategoryModel();
            model.setId(x.getId());
            model.setName(x.getName());
            model.setChild(posterCategoryRepository.findAllByParentIdAndStatus(x.getId(), true));
            list.add(model);
        });
        return ResponseUtil.ok(list);
    }

    @Override
    public ResponseEntity<?> savePosterCategory(PosterSaveCategory model) {
        PosterCategory posterCategory = new PosterCategory();
        posterCategory.setId(model.getId());
        posterCategory.setName(model.getName());
        posterCategory.setParentId(model.getParentId());
        return ResponseUtil.ok(posterCategoryRepository.save(posterCategory));
    }

    @Override
    public ResponseEntity<?> deletePosterCategory(Integer id) {
        PosterCategory posterCategory = posterCategoryRepository.findById(id).orElse(null);
        if (posterCategory == null)
            return ResponseUtil.badRequest("Id không chính xác!");
        posterCategory.setStatus(false);
        posterCategoryRepository.save(posterCategory);
        return ResponseUtil.message("Xóa thành công");
    }

    @Override
    public ResponseEntity<?> getPoster(Integer type) {
        return ResponseUtil.ok(posterRepository.findAllByTypeAndStatus(type, 0));
    }

    @Override
    public ResponseEntity<?> getPoster(Pageable pageable, Integer type) {
        return ResponseUtil.ok(posterRepository.findAllByTypeAndStatus(pageable, type, 0));
    }

    @Override
    public ResponseEntity<?> getPosterById(Integer id) {
        return ResponseUtil.ok(posterRepository.findById(id).orElse(null));
    }

    @Override
    public ResponseEntity<?> savePoster(SavePoster req) {
        Poster poster = new Poster(
                req.getId(),
                req.getName(),
                req.getTitle(),
                req.getNote(),
                req.getImg(),
                req.getIdProduct(),
                req.getIdPosterRelated(),
                req.getContent(),
                req.getPostDate(),
                req.getType()
        );
        return ResponseUtil.ok(posterRepository.save(poster));
    }

    @Override
    public ResponseEntity<?> approvePoster(Integer id) {
        Poster poster = posterRepository.findById(id).orElse(null);
        if (poster == null)
            return ResponseUtil.badRequest("Id không chính xác!");
        poster.setStatus(1);
        return ResponseUtil.message("Duyệt thành công");
    }

    @Override
    public ResponseEntity<?> deletePoster(Integer id) {
        Poster poster = posterRepository.findById(id).orElse(null);
        if (poster == null)
            return ResponseUtil.badRequest("Id không chính xác!");
        poster.setStatus(2);
        return ResponseUtil.message("Xóa thành công");
    }
}
