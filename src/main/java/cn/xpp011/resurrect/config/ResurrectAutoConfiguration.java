package cn.xpp011.resurrect.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Resurrection自动配置类
 *
 * @author renyu.shen
 **/
@AutoConfiguration
@Import(value = {})
public class ResurrectAutoConfiguration {

    static class ResurrectionImportSelector implements ImportSelector {

        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            return new String[0];
        }
    }
}
