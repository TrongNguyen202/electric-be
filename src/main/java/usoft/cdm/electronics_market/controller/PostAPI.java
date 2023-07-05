package usoft.cdm.electronics_market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.Poster.PosterSaveCategory;
import usoft.cdm.electronics_market.model.Poster.SavePoster;
import usoft.cdm.electronics_market.service.PostService;
import org.springframework.lang.Nullable;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/poster")
public class PostAPI {
    private final PostService postService;

    @GetMapping("getPosterCategory")
    private ResponseEntity<?> getPosterCategory(@RequestParam @Nullable Integer parentId) {
        return postService.getPosterCategory(parentId);
    }

    @GetMapping("getPosterCategory/page")
    private ResponseEntity<?> getPosterCategory(Pageable pageable) {
        return postService.getPosterCategory(pageable);
    }

    @PostMapping("savePosterCategory")
    private ResponseEntity<?> savePosterCategory(@RequestBody PosterSaveCategory req) {
        return postService.savePosterCategory(req);
    }

    @DeleteMapping("deletePosterCategory")
    private ResponseEntity<?> deletePosterCategory(@RequestParam Integer id) {
        return postService.deletePosterCategory(id);
    }

    @GetMapping
    private ResponseEntity<?> getPoster(@RequestParam Integer type) {
        return postService.getPoster(type);
    }

    @GetMapping("page")
    private ResponseEntity<?> getPoster(Pageable pageable, @RequestParam Integer type) {
        return postService.getPoster(pageable, type);
    }

    @GetMapping("getById")
    private ResponseEntity<?> getPosterById(@RequestParam Integer id) {
        return postService.getPosterById(id);
    }

    @PostMapping
    private ResponseEntity<?> savePoster(@RequestBody SavePoster req) {
        return postService.savePoster(req);
    }

    @DeleteMapping("approve")
    private ResponseEntity<?> approvePoster(@RequestParam Integer id) {
        return postService.approvePoster(id);
    }

    @DeleteMapping
    private ResponseEntity<?> deletePoster(@RequestParam Integer id) {
        return postService.deletePoster(id);
    }

}
