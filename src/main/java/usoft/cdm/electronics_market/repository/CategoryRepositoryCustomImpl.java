package usoft.cdm.electronics_market.repository;

import usoft.cdm.electronics_market.model.CategoryChildHomePage;
import usoft.cdm.electronics_market.model.CategoryDTO;
import usoft.cdm.electronics_market.model.CategoryHomePage;
import usoft.cdm.electronics_market.util.CommonUtil;
import usoft.cdm.electronics_market.util.DataUtil;

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
}
