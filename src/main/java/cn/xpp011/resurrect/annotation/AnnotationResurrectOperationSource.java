package cn.xpp011.resurrect.annotation;

import cn.xpp011.resurrect.interceptor.ResurrectOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.MethodClassKey;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author renyu.shen
 **/

public class AnnotationResurrectOperationSource implements ResurrectOperationSource {

    private final boolean publicMethodsOnly;

    private static final Collection<ResurrectOperation> NULL_CACHING_MARKER = Collections.emptyList();

    protected final Log logger = LogFactory.getLog(getClass());

    private final Set<ResurrectAnnotationParser> annotationParsers;

    private final Map<Object, Collection<ResurrectOperation>> operationCache = new ConcurrentHashMap<>(1024);

    public AnnotationResurrectOperationSource() {
        this(true);
    }

    public AnnotationResurrectOperationSource(boolean publicMethodsOnly) {
        this.publicMethodsOnly = publicMethodsOnly;
        this.annotationParsers = Collections.singleton(new SpringResurrectAnnotationParser());
    }

    @Override
    public boolean isCandidateClass(Class<?> targetClass) {
        for (ResurrectAnnotationParser annotationParser : annotationParsers) {
            if (annotationParser.isCandidateClass(targetClass)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<ResurrectOperation> getOperations(Method method, Class<?> targetClass) {
        if (method.getDeclaringClass() == Object.class) {
            return null;
        }

        Object cacheKey = getCacheKey(method, targetClass);
        Collection<ResurrectOperation> cached = this.operationCache.get(cacheKey);
        if (cached != null) {
            return (cached != NULL_CACHING_MARKER ? cached : null);
        } else {
            Collection<ResurrectOperation> cacheOps = computeResurrectOperations(method, targetClass);
            if (cacheOps != null) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Adding resurrect method '" + method.getName() + "' with operations: " + cacheOps);
                }
                this.operationCache.put(cacheKey, cacheOps);
            } else {
                this.operationCache.put(cacheKey, NULL_CACHING_MARKER);
            }
            return cacheOps;
        }
    }

    @Nullable
    private Collection<ResurrectOperation> computeResurrectOperations(Method method, @Nullable Class<?> targetClass) {
        // Don't allow non-public methods, as configured.
        if (allowPublicMethodsOnly() && !Modifier.isPublic(method.getModifiers())) {
            return null;
        }

        // The method may be on an interface, but we need metadata from the target class.
        // If the target class is null, the method will be unchanged.
        Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);

        // First try is the method in the target class.
        Collection<ResurrectOperation> opDef = findResurrectOperations(specificMethod);
        if (opDef != null) {
            return opDef;
        }

        // Second try is the caching operation on the target class.
        opDef = findResurrectOperations(specificMethod.getDeclaringClass());
        if (opDef != null && ClassUtils.isUserLevelMethod(method)) {
            return opDef;
        }

        if (specificMethod != method) {
            // Fallback is to look at the original method.
            opDef = findResurrectOperations(method);
            if (opDef != null) {
                return opDef;
            }
            // Last fallback is the class of the original method.
            opDef = findResurrectOperations(method.getDeclaringClass());
            if (opDef != null && ClassUtils.isUserLevelMethod(method)) {
                return opDef;
            }
        }

        return null;
    }

    @Override
    public Collection<ResurrectOperation> findResurrectOperations(Class<?> clazz) {
        return determineResurrectOperations(parser -> parser.parseResurrectAnnotations(clazz));
    }

    @Override
    public Collection<ResurrectOperation> findResurrectOperations(Method method) {
        return determineResurrectOperations(parser -> parser.parseResurrectAnnotations(method));
    }

    protected Object getCacheKey(Method method, @Nullable Class<?> targetClass) {
        return new MethodClassKey(method, targetClass);
    }

    @Nullable
    protected Collection<ResurrectOperation> determineResurrectOperations(Function<ResurrectAnnotationParser, Collection<ResurrectOperation>> provider) {
        Collection<ResurrectOperation> ops = null;
        for (ResurrectAnnotationParser parser : this.annotationParsers) {
            Collection<ResurrectOperation> annOps = provider.apply(parser);
            if (annOps != null) {
                if (ops == null) {
                    ops = annOps;
                } else {
                    Collection<ResurrectOperation> combined = new ArrayList<>(ops.size() + annOps.size());
                    combined.addAll(ops);
                    combined.addAll(annOps);
                    ops = combined;
                }
            }
        }
        return ops;
    }

    protected boolean allowPublicMethodsOnly() {
        return this.publicMethodsOnly;
    }
}
