package cn.xpp011.resurrect.annotation;

import cn.xpp011.resurrect.interceptor.ResurrectOperation;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author renyu.shen
 **/

public interface ResurrectAnnotationParser {

    default boolean isCandidateClass(Class<?> targetClass) {
        return true;
    }


    @Nullable
    Collection<ResurrectOperation> parseResurrectAnnotations(Class<?> type);

    @Nullable
    Collection<ResurrectOperation> parseResurrectAnnotations(Method method);

}
