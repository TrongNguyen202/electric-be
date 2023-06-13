package usoft.cdm.electronics_market.repository;

import usoft.cdm.electronics_market.model.CategoryChildHomePage;
import usoft.cdm.electronics_market.model.CategoryHomePage;
import usoft.cdm.electronics_market.util.CommonUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
