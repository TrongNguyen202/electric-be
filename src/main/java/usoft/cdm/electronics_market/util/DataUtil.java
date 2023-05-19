package usoft.cdm.electronics_market.util;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

@UtilityClass
public class DataUtil {

    public boolean isNullOrEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    public boolean isNullObject(Object obj) {
        return (obj == null);
    }

    public boolean isNullOrEmpty(List<?> lst) {
        return (lst == null || lst.size() == 0);
    }
}
