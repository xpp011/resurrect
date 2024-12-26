package cn.xpp011.resurrect.annotation;

import cn.xpp011.resurrect.interceptor.ResurrectOperation;
import org.springframework.cache.interceptor.CacheOperation;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * @author renyu.shen
 **/

public class SpringResurrectAnnotationParser implements ResurrectAnnotationParser {

    private static final Set<Class<? extends Annotation>> OPERATION_ANNOTATIONS =
            Set.of(Resurrect.class);

    @Override
    public boolean isCandidateClass(Class<?> targetClass) {
        return AnnotationUtils.isCandidateClass(targetClass, OPERATION_ANNOTATIONS);
    }

    @Override
    public Collection<ResurrectOperation> parseResurrectAnnotations(Class<?> type) {
        return parseAnnotations(type);
    }

    @Override
    public Collection<ResurrectOperation> parseResurrectAnnotations(Method method) {
        return parseAnnotations(method);
    }

    private Collection<ResurrectOperation> parseAnnotations(AnnotatedElement ae) {
        Collection<ResurrectOperation> ops = parseResurrectAnnotations(ae, false);
        if (ops != null && !ops.isEmpty()) {
            Collection<ResurrectOperation> localOps = parseResurrectAnnotations(ae, true);
            if (localOps != null) {
                return localOps;
            }
        }
        return ops;
    }

    private Collection<ResurrectOperation> parseResurrectAnnotations(AnnotatedElement ae, boolean localOnly) {
        Collection<? extends Annotation> annotations = (localOnly ?
                AnnotatedElementUtils.getAllMergedAnnotations(ae, OPERATION_ANNOTATIONS) :
                AnnotatedElementUtils.findAllMergedAnnotations(ae, OPERATION_ANNOTATIONS));
        if (annotations.isEmpty()) {
            return null;
        }
        Collection<ResurrectOperation> ops = new ArrayList<>(1);
        annotations.stream().filter(Resurrect.class::isInstance).map(Resurrect.class::cast)
                .forEach(resurrect -> {
                    ops.add(ResurrectOperation.builder()
                            .name(ae.toString())
                            .condition(resurrect.condition())
                            .ignore(resurrect.ignore())
                            .build());
                });

        return ops;
    }


}
