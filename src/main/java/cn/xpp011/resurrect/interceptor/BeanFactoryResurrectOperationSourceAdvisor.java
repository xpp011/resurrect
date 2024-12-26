package cn.xpp011.resurrect.interceptor;

import cn.xpp011.resurrect.annotation.ResurrectOperationSource;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * @author renyu.shen
 **/

public class BeanFactoryResurrectOperationSourceAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private final ResurrectOperationSourcePointcut pointcut = new ResurrectOperationSourcePointcut();

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    public void setOperationSource(ResurrectOperationSource operationSource) {
        pointcut.setOperationSource(operationSource);
    }
}
