package cn.xpp011.resurrect.annotation;

import cn.xpp011.resurrect.interceptor.ResurrectOperation;
import org.springframework.cache.interceptor.CacheOperation;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author renyu.shen
 **/

public interface ResurrectOperationSource {

    default boolean isCandidateClass(Class<?> targetClass) {
        return true;
    }

    Collection<ResurrectOperation> getOperations(Method method, @Nullable Class<?> targetClass);

    Collection<ResurrectOperation> findResurrectOperations(Class<?> clazz);

    Collection<ResurrectOperation> findResurrectOperations(Method method);

}
