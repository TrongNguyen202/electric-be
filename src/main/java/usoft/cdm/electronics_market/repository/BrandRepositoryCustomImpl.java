package usoft.cdm.electronics_market.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import usoft.cdm.electronics_market.model.BrandDTO;
import usoft.cdm.electronics_market.util.CommonUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrandRepositoryCustomImpl implements BrandRepositoryCustom {


    @PersistenceContext
    private EntityManager em;

    @Override
    public List<BrandDTO> getAllBrandByCategoryId(Integer categoryId) {
        StringBuilder sql = new StringBuilder("SELECT b.id,b.name,COUNT(b.id) as sumProducts FROM cdm_brand b \n" +
                "JOIN cdm_products p ON b.id = p.brand_id\n" +
                "WHERE b.status =1 and p.status =1  ");

        Map<String, Object> params = new HashMap<>();

        sql.append(" and p.category_id = :categoryId  ");
        params.put("categoryId", categoryId);
        sql.append("GROUP BY b.id");
        return CommonUtil.getList(em, sql.toString(), params, "getAllBrandByProducts");

    }
}
