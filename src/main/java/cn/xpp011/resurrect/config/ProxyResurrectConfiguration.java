package cn.xpp011.resurrect.config;

import cn.xpp011.resurrect.annotation.AnnotationResurrectOperationSource;
import cn.xpp011.resurrect.interceptor.BeanFactoryResurrectOperationSourceAdvisor;
import cn.xpp011.resurrect.interceptor.ResurrectInterceptor;
import cn.xpp011.resurrect.annotation.ResurrectOperationSource;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * @author renyu.shen
 **/
@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class ProxyResurrectConfiguration {

    @Bean
    public BeanFactoryResurrectOperationSourceAdvisor resurrectAdvisor(ResurrectOperationSource operationSource,
                                                                       ResurrectInterceptor interceptor) {
        BeanFactoryResurrectOperationSourceAdvisor advisor = new BeanFactoryResurrectOperationSourceAdvisor();
        advisor.setOperationSource(operationSource);
        advisor.setAdvice(interceptor);
        return advisor;
    }

    @Bean
    public ResurrectOperationSource resurrectOperationSource() {
        return new AnnotationResurrectOperationSource(false);
    }

    @Bean
    public ResurrectInterceptor resurrectInterceptor(ResurrectOperationSource source, Persistent persistent) {
        return new ResurrectInterceptor(persistent, source);
    }

    @Bean
    public FilePersistent persistent() {
        return new FilePersistent();
    }

}
