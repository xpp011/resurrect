package cn.xpp011.resurrect.annotation;

import cn.xpp011.resurrect.config.ProxyResurrectConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ProxyResurrectConfiguration.class)
public @interface EnableResurrect {

    int order() default Ordered.LOWEST_PRECEDENCE;

}
