package project.spring.skeleton.data.cache.service.component.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Cache<V> {
    Boolean hasKey(String key);

    void set(String k, V v);

    void delete(String k);

    void set(String k, V v, long expiredDays);

    void multiSet(Map<? extends String, ? extends V> map);

    Boolean multiSetIfAbsent(Map<? extends String, ? extends V> map);

    V get(String o);

    Optional<V> getObject(String o, Class<V> typeClass);

    V getAndSet(String k, V v);

    List<V> multiGet(Collection<String> collection);

    Long increment(String k, long l);

    Double increment(String k, double v);
}