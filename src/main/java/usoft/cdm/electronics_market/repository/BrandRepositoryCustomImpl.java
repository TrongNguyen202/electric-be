package usoft.cdm.electronics_market.repository;


import usoft.cdm.electronics_market.model.BrandDTO;
import usoft.cdm.electronics_market.util.CommonUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BrandRepositoryCustomImpl implements BrandRepositoryCustom {


    @PersistenceContext
    private EntityManager em;

    @Override
    public List<BrandDTO> getAllBrandByCategoryId(Integer categoryId) {
        StringBuilder sql = new StringBuilder("SELECT b.id,b.name,COUNT(b.id) as sumProducts,b.img FROM cdm_brand b \n" +
                "JOIN cdm_products p ON b.id = p.brand_id\n" +
                "WHERE b.status =1 and p.status =1  ");

        Map<String, Object> params = new HashMap<>();

        sql.append(" and p.category_id = :categoryId  ");
        params.put("categoryId", categoryId);
        sql.append("GROUP BY b.id");
        return CommonUtil.getList(em, sql.toString(), params, "getAllBrandByProducts");

    }

    @Override
    public List<BrandDTO> getAllBrandByCategoryId(List<Integer> categoryIds) {
        StringBuilder sql = new StringBuilder("SELECT b.id,b.name,COUNT(b.id) as sumProducts,b.img FROM cdm_brand b \n" +
                "JOIN cdm_products p ON b.id = p.brand_id\n" +
                "WHERE b.status =1 and p.status =1  ");

        Map<String, Object> params = new HashMap<>();

        sql.append(" and p.category_id IN :categoryIds  ");
        params.put("categoryIds", categoryIds);
        sql.append("GROUP BY b.id");
        return CommonUtil.getList(em, sql.toString(), params, "getAllBrandByProducts");

    }

    @Override
    public List<BrandDTO> getAllBrandByNameProduct(String name) {
        StringBuilder sql = new StringBuilder("SELECT b.id,b.name,COUNT(b.id) as sumProducts,b.img FROM cdm_brand b \n" +
                "JOIN cdm_products p ON b.id = p.brand_id\n" +
                "WHERE b.status =1 and p.status =1  ");

        Map<String, Object> params = new HashMap<>();
        String[] keywords = name.split("\\s+");
        String query = Arrays.stream(keywords)
                .map(data -> "+" + data + "*")
                .collect(Collectors.joining(" "));
        sql.append(" and (MATCH(p.name) AGAINST(:keyword IN BOOLEAN MODE) ");
        params.put("keyword", query);

        sql.append(" OR lower(p.name) LIKE :name)");
        params.put("name", "%" + name.toLowerCase() + "%");
        sql.append("GROUP BY b.id");
        return CommonUtil.getList(em, sql.toString(), params, "getAllBrandByProducts");
    }
}
