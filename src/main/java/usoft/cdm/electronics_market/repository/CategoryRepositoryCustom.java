package usoft.cdm.electronics_market.repository;

import usoft.cdm.electronics_market.model.CategoryChildHomePage;
import usoft.cdm.electronics_market.model.CategoryDTO;
import usoft.cdm.electronics_market.model.CategoryHomePage;

import java.util.List;

public interface CategoryRepositoryCustom {

    List<CategoryHomePage> getParentIdIsNullAndStatus();

    List<CategoryChildHomePage> getParentIdAndStatus(Integer categoryId);

    List<CategoryDTO> getAllCateForSearchProduct(String name);

    List<CategoryDTO> getAllParentByBrand(Integer brandId);

    List<CategoryDTO> getAllChildByBrand(Integer brandId);
}
