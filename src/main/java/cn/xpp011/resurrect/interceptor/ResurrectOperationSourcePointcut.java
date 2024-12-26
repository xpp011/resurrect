package cn.xpp011.resurrect.interceptor;

import cn.xpp011.resurrect.annotation.ResurrectOperationSource;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author renyu.shen
 **/
public class ResurrectOperationSourcePointcut extends StaticMethodMatcherPointcut implements Serializable {

    private ResurrectOperationSource operationSource;

    public ResurrectOperationSourcePointcut() {
        setClassFilter(new ResurrectOperationSourceClassFilter());
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return operationSource == null ||
                !CollectionUtils.isEmpty(operationSource.getOperations(method, targetClass));
    }

    private final class ResurrectOperationSourceClassFilter implements ClassFilter {

        @Override
        public boolean matches(Class<?> clazz) {
            return operationSource == null || operationSource.isCandidateClass(clazz);
        }
    }

    public void setOperationSource(ResurrectOperationSource operationSource) {
        this.operationSource = operationSource;
    }
}
