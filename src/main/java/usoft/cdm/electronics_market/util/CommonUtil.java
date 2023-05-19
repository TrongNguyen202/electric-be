package usoft.cdm.electronics_market.util;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@UtilityClass
public class CommonUtil {

    public <T> Page<T> getPageImpl(EntityManager em, String sql, Map<String, Object> params, Pageable pageable,
                                   String resultMapping) {
        return getPageImpl(em, sql, "SELECT Count(*) from (" + sql + ") a ", params, pageable, resultMapping);
    }

    public <T> Page<T> getPageImpl(EntityManager em, String sql, String sqlCount, Map<String, Object> params,
                                   Pageable pageable, String resultMapping) {
        Query countQuery = em.createNativeQuery(sqlCount);
        Query query = em.createNativeQuery(sql, resultMapping);
        if (!pageable.isUnpaged()) {
            query.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize());
        }
        if (!DataUtil.isNullOrEmpty(params)) {
            setQueryParams(query, countQuery, params);
        }
        List<T> result = query.getResultList();

        return new PageImpl<>(result, pageable, ((BigInteger) countQuery.getSingleResult()).intValue());
    }

    public void setQueryParams(Query query, Query countQuery, Map<String, Object> params) {
        if (null == params || params.isEmpty()) {
            return;
        }
        params.forEach((k, v) -> {
            query.setParameter(k, v);
            countQuery.setParameter(k, v);
        });
    }

    public <T> List<T> getList(EntityManager em, String sql, Map<String, Object> params, String resultMapping) {
        return getList(em, sql, "SELECT Count(*) from (" + sql + ") a ", params, resultMapping);
    }

    public <T> List<T> getList(EntityManager em, String sql, String sqlCount, Map<String, Object> params,
                               String resultMapping) {
        Query countQuery = em.createNativeQuery(sqlCount);
        Query query = em.createNativeQuery(sql, resultMapping);
        if (!DataUtil.isNullOrEmpty(params)) {
            setQueryParams(query, countQuery, params);
        }
        List<T> result = query.getResultList();

        return result;

    }

    public <T> T getObject(EntityManager em, String sql, Map<String, Object> params, String resultMapping) {
        return getObject(em, sql, "SELECT Count(*) from (" + sql + ") a ", params, resultMapping);
    }


    public <T> T getObject(EntityManager em, String sql, String sqlCount, Map<String, Object> params,
                           String resultMapping) {
        Query countQuery = em.createNativeQuery(sqlCount);
        Query query = em.createNativeQuery(sql, resultMapping);
        if (!DataUtil.isNullOrEmpty(params)) {
            setQueryParams(query, countQuery, params);
        }
        List<T> result = query.getResultList();

        return result.get(0);

    }


    public <T> Optional<T> getObjectOptional(EntityManager em, String sql, Map<String, Object> params, String resultMapping) {
        return getObjectOptional(em, sql, "SELECT Count(*) from (" + sql + ") a ", params, resultMapping);
    }


    public <T> Optional<T> getObjectOptional(EntityManager em, String sql, String sqlCount, Map<String, Object> params,
                                             String resultMapping) {
        Query countQuery = em.createNativeQuery(sqlCount);
        Query query = em.createNativeQuery(sql, resultMapping);
        if (!DataUtil.isNullOrEmpty(params)) {
            setQueryParams(query, countQuery, params);
        }
        List<T> result = query.getResultList();

        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }

    }


}
