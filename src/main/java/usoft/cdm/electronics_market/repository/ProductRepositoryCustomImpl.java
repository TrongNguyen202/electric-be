package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import usoft.cdm.electronics_market.model.Price;
import usoft.cdm.electronics_market.model.ProductForHomePage;
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
    public List<ProductsDTO> getRelatedProducts(Integer categoryId) {
        StringBuilder sql = new StringBuilder("SELECT p.id,p.name,p.price_sell as priceSell,p.price_after_sale as priceAfterSale,p.slug,b.name as brandName,(SELECT i.img from cdm_image i WHERE i.detail_id = p.id AND i.type = 2 LIMIT 0,1) as imgProduct  " +
                "FROM cdm_products p, cdm_brand b WHERE p.brand_id = b.id and p.status =true ");

        Map<String, Object> params = new HashMap<>();

        sql.append(" and p.category_id = :categoryId ");
        params.put("categoryId", categoryId);
        sql.append("Limit 20");

        return CommonUtil.getList(em, sql.toString(), params, "getProductForBrand");
    }

    @Override
    public List<ProductForHomePage> getProductsForHomePage(Integer categoryId) {
        StringBuilder sql = new StringBuilder("WITH cte_image AS (\n" +
                "  SELECT detail_id, img\n" +
                "  FROM cdm_image\n" +
                "  WHERE type = 2\n" +
                "\tGROUP BY detail_id\n" +
                ")\n" +
                "SELECT p.id, p.name, p.price_sell AS priceSell, p.price_after_sale AS priceAfterSale, p.slug, b.name AS brandName, cte_image.img AS imgProduct\n" +
                "FROM cdm_products p\n" +
                "JOIN cdm_brand b ON p.brand_id = b.id\n" +
                "JOIN cte_image ON cte_image.detail_id = p.id\n" +
                "WHERE p.status = true  ");

        Map<String, Object> params = new HashMap<>();

        sql.append(" and p.category_id = :categoryId ");
        params.put("categoryId", categoryId);
        sql.append("Limit 10");

        return CommonUtil.getList(em, sql.toString(), params, "getProductForHomePage");
    }

    @Override
    public List<ProductsDTO> getProductsInSameBrand(Integer brandId) {
        StringBuilder sql = new StringBuilder("SELECT p.id,p.name,p.price_sell as priceSell,p.price_after_sale as priceAfterSale,p.slug,b.name as brandName,(SELECT i.img from cdm_image i WHERE i.detail_id = p.id AND i.type = 2 LIMIT 0,1) as imgProduct  " +
                "FROM cdm_products p, cdm_brand b WHERE p.brand_id = b.id and p.status =true ");

        Map<String, Object> params = new HashMap<>();

        sql.append(" and p.brand_id = :brandId ");
        params.put("brandId", brandId);
        sql.append("Limit 20");

        return CommonUtil.getList(em, sql.toString(), params, "getProductForBrand");
    }

    @Override
    public Page<ProductsDTO> getProductsInSameBrand(Integer brandId, Pageable pageable) {
        StringBuilder sql = new StringBuilder("WITH cte_image AS (\n" +
                "  SELECT detail_id, img\n" +
                "  FROM cdm_image\n" +
                "  WHERE type = 2\n" +
                "\tGROUP BY detail_id\n" +
                ")\n" +
                "SELECT p.id, p.name, p.price_sell AS priceSell, p.price_after_sale AS priceAfterSale, p.slug, b.name AS brandName, cte_image.img AS imgProduct\n" +
                "FROM cdm_products p\n" +
                "JOIN cdm_brand b ON p.brand_id = b.id\n" +
                "JOIN cte_image ON cte_image.detail_id = p.id\n" +
                "WHERE p.status = true  ");

        Map<String, Object> params = new HashMap<>();

        sql.append(" and p.brand_id = :brandId ");
        params.put("brandId", brandId);
        return CommonUtil.getPageImpl(em, sql.toString(), params, pageable, "getProductForBrand");
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
        StringBuilder sql = new StringBuilder("SELECT p.id,p.name,p.price_sell as priceSell,p.price_after_sale as priceAfterSale,p.slug," +
                "(100-(p.price_after_sale/p.price_sell)*100) as discount  " +
                "FROM cdm_products p \n" +
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
        if (!DataUtil.isNullObject(dto.getValueSort())) {
            if (dto.getValueSort() == 2) {
                sql.append("  ORDER BY p.price_after_sale ASC ");
            } else if (dto.getValueSort() == 3) {
                sql.append("  ORDER BY p.price_after_sale DESC ");
            } else if (dto.getValueSort() == 5) {
                sql.append(" ORDER BY p.created_date DESC");
            } else if (dto.getValueSort() == 4) {
                sql.append("  ORDER BY (100-(p.price_after_sale/p.price_sell)*100) DESC ");
            }
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
    public Page<ProductsDTO> findByBrandAndPriceAndMadeInForSearchProduct(String name, ProductsDTO dto, Pageable pageable) {
        StringBuilder sql = new StringBuilder("SELECT p.id,p.name,p.price_sell as priceSell,p.price_after_sale as priceAfterSale,p.slug  FROM cdm_products p \n" +
                "WHERE p.status =1 ");

        Map<String, Object> params = new HashMap<>();

        String[] keywords = name.split("\\s+");
        String query = Arrays.stream(keywords)
                .map(data -> "+" + data + "*")
                .collect(Collectors.joining(" "));
        sql.append(" and (MATCH(p.name) AGAINST(:keyword IN BOOLEAN MODE) ");
        params.put("keyword", query);

        sql.append(" OR lower(p.name) LIKE :name)");
        params.put("name", "%" + name.toLowerCase() + "%");

        if (!DataUtil.isNullObject(dto.getBrandIds())) {
            sql.append(" and p.brand_id IN :brand");
            params.put("brand", dto.getBrandIds());
        }
        if (!DataUtil.isNullObject(dto.getCategoryId())) {
            sql.append(" and p.category_id = :cate");
            params.put("cate", dto.getCategoryId());
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
                "WHERE p.status =true ");

        Map<String, Object> params = new HashMap<>();

        if (!DataUtil.isNullString(name)) {
            String[] keywords = name.split("\\s+");
            String query = Arrays.stream(keywords)
                    .map(data -> "+" + data + "*")
                    .collect(Collectors.joining(" "));
            sql.append(" and (MATCH(p.name) AGAINST(:keyword IN BOOLEAN MODE) ");
            params.put("keyword", query);
        }
        if (!DataUtil.isNullString(name)) {
            sql.append(" OR lower(p.name) LIKE :name)");
            params.put("name", "%" + name.toLowerCase() + "%");
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

    @Override
    public List<ProductsDTO> getMadeInForNameProduct(String name) {
        StringBuilder sql = new StringBuilder("SELECT p.made_in as madeIn,COUNT(p.made_in) as sumProducts FROM  cdm_products p \n" +
                "WHERE p.status =1 ");

        Map<String, Object> params = new HashMap<>();

        String[] keywords = name.split("\\s+");
        String query = Arrays.stream(keywords)
                .map(data -> "+" + data + "*")
                .collect(Collectors.joining(" "));
        sql.append(" and (MATCH(p.name) AGAINST(:keyword IN BOOLEAN MODE) ");
        params.put("keyword", query);

        sql.append(" OR lower(p.name) LIKE :name)");
        params.put("name", "%" + name.toLowerCase() + "%");
        sql.append("GROUP BY p.made_in");

        return CommonUtil.getList(em, sql.toString(), params, "getAllMadeInSearch");

    }

    @Override
    public ProductsDTO getRangePriceForNameProduct(String name, Double priceFrom, Double priceTo) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(p.id) as sumProducts FROM  cdm_products p \n" +
                "WHERE p.status =1 ");

        Map<String, Object> params = new HashMap<>();

        String[] keywords = name.split("\\s+");
        String query = Arrays.stream(keywords)
                .map(data -> "+" + data + "*")
                .collect(Collectors.joining(" "));
        sql.append(" and (MATCH(p.name) AGAINST(:keyword IN BOOLEAN MODE) ");
        params.put("keyword", query);

        sql.append(" OR lower(p.name) LIKE :name)");
        params.put("name", "%" + name.toLowerCase() + "%");

        sql.append(" and p.price_sell between :from AND :to ");
        params.put("from", priceFrom);
        params.put("to", priceTo);
        return CommonUtil.getObject(em, sql.toString(), params, "getRangePrice");
    }
}
