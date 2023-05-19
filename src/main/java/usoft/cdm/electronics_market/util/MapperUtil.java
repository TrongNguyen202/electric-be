package usoft.cdm.electronics_market.util;


import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperUtil {

    private static ModelMapper mm = new ModelMapper();

    public static <T, E> T map(E entity, Class<T> clazz) {

        return mm.map(entity, clazz);
    }

    public static <T, E> void mapFromSource(E source, T updatedOn) {

        mm.map(source, updatedOn);
    }

    public static <T, E> List<T> mapList(List<E> entity, Class<T> clazz) {
        return entity.stream().map(e -> mm.map(e, clazz)).collect(Collectors.toList());
    }

    public static <D, T> Page<D> mapEntityPageIntoDtoPage(Page<T> entities, Class<D> dtoClass) {
        return entities.map(objectEntity -> mm.map(objectEntity, dtoClass));
    }

    private static <T, K> void update(T t, K k) throws IllegalAccessException, NoSuchFieldException {
        Field field1 = t.getClass().getDeclaredField("theField");
        field1.setAccessible(true);
        for (Field field : t.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.get(t) != null) {
                for (Field f : k.getClass().getDeclaredFields()) {
                    f.setAccessible(true);
                    if (f.getName().equals(field.getName())) {
                        try {
                            f.set(k, field.get(t));
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }
}
