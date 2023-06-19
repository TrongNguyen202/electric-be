package usoft.cdm.electronics_market.repository;

import usoft.cdm.electronics_market.model.CategoryChildHomePage;
import usoft.cdm.electronics_market.model.CategoryDTO;
import usoft.cdm.electronics_market.model.CategoryHomePage;
import usoft.cdm.electronics_market.util.CommonUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<CategoryHomePage> getParentIdIsNullAndStatus() {
        StringBuilder sql = new StringBuilder("SELECT c.id ,c.name,c.slug FROM cdm_category c " +
                "WHERE c.parent_id IS NULL AND c.status = TRUE ");

        Map<String, Object> params = new HashMap<>();

        return CommonUtil.getList(em, sql.toString(), params, "getParentIdIsNullAndStatus");
    }

    @Override
    public List<CategoryChildHomePage> getParentIdAndStatus(Integer categoryId) {
        StringBuilder sql = new StringBuilder("SELECT c.id ,c.name,c.slug ,c.icon_img as iconImg ,c.avatar_img as avatarImg FROM cdm_category c " +
                "WHERE c.status = TRUE  ");

        Map<String, Object> params = new HashMap<>();

        sql.append(" and c.parent_id = :categoryId ");
        params.put("categoryId", categoryId);

        return CommonUtil.getList(em, sql.toString(), params, "getParentIdAndStatus");
    }

    @Override
    public List<CategoryDTO> getAllCateForSearchProduct(String name) {
        StringBuilder sql = new StringBuilder("SELECT c.id,c.icon_img as iconImg,c.name,c.slug,COUNT(c.id) as sumProduct " +
                "FROM cdm_category c JOIN cdm_products p ON c.id = p.category_id \n" +
                "WHERE p.status = true ");

        Map<String, Object> params = new HashMap<>();
        String[] keywords = name.split("\\s+");
        String query = Arrays.stream(keywords)
                .map(data -> "+" + data + "*")
                .collect(Collectors.joining(" "));
        sql.append(" and (MATCH(p.name) AGAINST(:keyword IN BOOLEAN MODE) ");
        params.put("keyword", query);

        sql.append(" OR lower(p.name) LIKE :name)");
        params.put("name", "%" + name.toLowerCase() + "%");

        sql.append("GROUP BY c.id");
        return CommonUtil.getList(em, sql.toString(), params, "getCateForSearch");
    }

    @Override
    public List<CategoryDTO> getAllParentByBrand(Integer brandId) {
        StringBuilder sql = new StringBuilder("WITH cateParen as (\n" +
                "SELECT  c.parent_id as id, " +
                "(SELECT c2.icon_img FROM cdm_category c2 WHERE c2.parent_id IS NULL ANd c.parent_id = c2.id  ) as iconImg " +
                " ,(SELECT c2.name FROM cdm_category c2 WHERE c2.parent_id IS NULL ANd c.parent_id = c2.id  ) as name," +
                "(SELECT c2.slug FROM cdm_category c2 WHERE c2.parent_id IS NULL ANd c.parent_id = c2.id  ) as slug," +
                " COUNT(p.id) as sumCount  " +
                "FROM cdm_products p JOIN cdm_category c ON c.id = p.category_id " +
                "WHERE p.status = 1 ");

        Map<String, Object> params = new HashMap<>();
        sql.append(" and p.brand_id = :brandId ");
        params.put("brandId", brandId);
        sql.append(" GROUP BY c.name)");
        sql.append("SELECT id, iconImg,name,slug,SUM(sumCount) as sumProduct FROM cateParen GROUP BY name ");
        return CommonUtil.getList(em, sql.toString(), params, "getCateForSearch");
    }

    @Override
    public List<CategoryDTO> getAllChildByBrand(Integer brandId) {
        StringBuilder sql = new StringBuilder("SELECT c.id,c.name,c.slug,COUNT(p.id) as sumProduct,c.parent_id as parentId " +
                "FROM cdm_products p JOIN cdm_category c ON c.id = p.category_id WHERE  p.status = 1  ");

        Map<String, Object> params = new HashMap<>();

        sql.append(" and p.brand_id = :brandId ");
        params.put("brandId", brandId);
        sql.append(" GROUP BY c.name");
        return CommonUtil.getList(em, sql.toString(), params, "getCateChildForBrand");
    }
}
