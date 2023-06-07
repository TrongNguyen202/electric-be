package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import usoft.cdm.electronics_market.model.Price;
import usoft.cdm.electronics_market.model.ProductsDTO;
import usoft.cdm.electronics_market.util.CommonUtil;
import usoft.cdm.electronics_market.util.DataUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ProductsDTO> getAllMadeByProducts(Integer categoryId) {
        StringBuilder sql = new StringBuilder("SELECT p.id,p.made_in as madeIn,COUNT(p.made_in) as sumProducts FROM  cdm_products p \n" +
                "WHERE p.status =1 ");

        Map<String, Object> params = new HashMap<>();

        sql.append(" and p.category_id = :categoryId ");
        params.put("categoryId", categoryId);
        sql.append("GROUP BY p.made_in");

        return CommonUtil.getList(em, sql.toString(), params, "getAllMadeInProducts");

    }

    @Override
    public List<ProductsDTO> getAllMadeByProducts(List<Integer> categoryIds) {
        StringBuilder sql = new StringBuilder("SELECT p.id,p.made_in as madeIn,COUNT(p.made_in) as sumProducts FROM  cdm_products p \n" +
                "WHERE p.status =1 ");

        Map<String, Object> params = new HashMap<>();

        sql.append(" and p.category_id In :categoryIds ");
        params.put("categoryIds", categoryIds);
        sql.append("GROUP BY p.made_in");

        return CommonUtil.getList(em, sql.toString(), params, "getAllMadeInProducts");

    }

    @Override
    public Page<ProductsDTO> findByBrandAndPriceAndMadeIn(List<Integer> categoryIds, ProductsDTO dto, Pageable pageable) {
        StringBuilder sql = new StringBuilder("SELECT p.id,p.name,p.price_sell as priceSell,p.price_after_sale as priceAfterSale,p.slug  FROM cdm_products p \n" +
                "WHERE p.status =1 ");

        Map<String, Object> params = new HashMap<>();

        sql.append(" and p.category_id IN :categoryIds ");
        params.put("categoryIds", categoryIds);

        if (!DataUtil.isNullObject(dto.getBrandIds())) {
            sql.append(" and p.brand_id IN :brand");
            params.put("brand", dto.getBrandIds());
        }

        if (!DataUtil.isNullObject(dto.getMadeIns())) {
            sql.append(" and p.made_in IN :madeIn");
            params.put("madeIn", dto.getMadeIns());
        }
        if (!DataUtil.isNullOrEmpty(dto.getPrice())) {
            boolean check = true;
            int cnt = 1;
            for (Price x : dto.getPrice()) {
                if (check)
                    sql.append(" AND (");
                else
                    sql.append(" OR ");
                String priceFrom = "from" + cnt;
                String priceTo = "to" + cnt;
                if (x.getTo() != null)
                    sql.append(" (p.price_sell BETWEEN :").append(priceFrom).append(" AND :").append(priceTo).append(")");
                else
                    sql.append(" (p.price_sell > 200000000)");
                params.put(priceFrom, x.getFrom());
                params.put(priceTo, x.getTo());
                check = false;
                cnt++;
            }
            if (!check)
                sql.append(" )");
        }
        return CommonUtil.getPageImpl(em, sql.toString(), params, pageable, "getBrandAndPriceAndMadeIn");

    }

    @Override
    public Page<ProductsDTO> findByBrandAndPriceAndMadeIn(Integer categoryId, ProductsDTO dto, Pageable pageable) {
        StringBuilder sql = new StringBuilder("SELECT p.id,p.name,p.price_sell as priceSell,p.price_after_sale as priceAfterSale,p.slug  FROM cdm_products p \n" +
                "WHERE p.status =1 ");

        Map<String, Object> params = new HashMap<>();

        sql.append(" and p.category_id = :categoryIds ");
        params.put("categoryIds", categoryId);

        if (!DataUtil.isNullObject(dto.getBrandIds())) {
            sql.append(" and p.brand_id IN :brand");
            params.put("brand", dto.getBrandIds());
        }


        if (!DataUtil.isNullObject(dto.getMadeIns())) {
            sql.append(" and p.made_in IN :madeIn");
            params.put("madeIn", dto.getMadeIns());
        }
        if (!DataUtil.isNullOrEmpty(dto.getPrice())) {
            boolean check = true;
            int cnt = 1;
            for (Price x : dto.getPrice()) {
                if (check)
                    sql.append(" AND (");
                else
                    sql.append(" OR ");
                String priceFrom = "from" + cnt;
                String priceTo = "to" + cnt;
                if (x.getTo() != null)
                    sql.append(" (p.price_sell BETWEEN :").append(priceFrom).append(" AND :").append(priceTo).append(")");
                else
                    sql.append(" (p.price_sell > 200000000)");
                params.put(priceFrom, x.getFrom());
                params.put(priceTo, x.getTo());
                check = false;
                cnt++;
            }
            if (!check)
                sql.append(" )");
        }
        return CommonUtil.getPageImpl(em, sql.toString(), params, pageable, "getBrandAndPriceAndMadeIn");

    }

    @Override
    public Page<ProductsDTO> searchNameForHomepage(String name, Pageable pageable) {
        StringBuilder sql = new StringBuilder("SELECT p.id,p.name,p.price_sell as priceSell,p.price_after_sale as priceAfterSale,p.slug  " +
                "FROM cdm_products p \n" +
                "WHERE p.status =1 ");

        Map<String, Object> params = new HashMap<>();

        if (!DataUtil.isNullString(name)) {
            String[] keywords = name.split("\\s+");
            String query = Arrays.stream(keywords)
                    .map(data -> "+" + data + "*")
                    .collect(Collectors.joining(" "));
            sql.append(" and MATCH(p.name) AGAINST(:keyword IN BOOLEAN MODE) ");
            params.put("keyword", query);

        }

        return CommonUtil.getPageImpl(em, sql.toString(), params, pageable, "getBrandAndPriceAndMadeIn");

    }

    @Override
    public ProductsDTO getDiscountByCategory(Integer categoryId) {
        StringBuilder sql = new StringBuilder("SELECT p.id, 100-((p.price_after_sale/p.price_sell)*100) as discount\n" +
                "FROM cdm_products p \n" +
                "WHERE p.status = true ");

        Map<String, Object> params = new HashMap<>();
        sql.append(" and p.category_id = :categoryId ");
        params.put("categoryId", categoryId);

        sql.append("ORDER BY 100-((p.price_after_sale/p.price_sell)*100) \n" +
                "DESC limit 1");

        return CommonUtil.getObject(em, sql.toString(), params, "getDiscountByCategory");
    }
}
