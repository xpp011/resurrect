package cn.xpp011.resurrect.interceptor;

import cn.xpp011.resurrect.annotation.ResurrectOperationSource;
import cn.xpp011.resurrect.config.Persistent;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.IntStream;

/**
 * @author renyu.shen
 **/
@RequiredArgsConstructor
public class ResurrectInterceptor extends ResurrectAspectSupport implements MethodInterceptor, Serializable {

    private final Persistent persistent;

    private final ResurrectOperationSource resurrectOperationSource;
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (resurrectOperationSource == null) {
            return invocation.proceed();
        }
        Object target = invocation.getThis();
        Assert.state(target != null, "Target must not be null");
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
        Collection<ResurrectOperation> operations = resurrectOperationSource.getOperations(invocation.getMethod(), targetClass);
        if (CollectionUtils.isEmpty(operations)) {
            return invocation.proceed();
        }
        Nutrient nutrient = new Nutrient.NutrientBuilder()
                .methodInvocation(invocation)
                .build();
        persistent.save(nutrient);
        try {
            Object proceed = invocation.proceed();
            persistent.commit(nutrient.id());
            return proceed;
        } catch (Throwable e) {
            persistent.flagRetry(nutrient.id());
            throw e;
        }
    }


}
