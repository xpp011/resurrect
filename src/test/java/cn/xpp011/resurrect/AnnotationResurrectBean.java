package cn.xpp011.resurrect;

import cn.xpp011.resurrect.annotation.Resurrect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author renyu.shen
 **/
@Component
@Slf4j
public class AnnotationResurrectBean {

    @Resurrect
    public String resurrect(String name, Integer id) {
        log.info("name: {}, id: {}", name, id);
        return name;
    }

}
