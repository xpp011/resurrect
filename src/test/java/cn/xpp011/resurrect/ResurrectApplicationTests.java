package cn.xpp011.resurrect;

import cn.xpp011.resurrect.annotation.EnableResurrect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = ResurrectApplication.class)
class ResurrectApplicationTests {

    @Autowired
    private AnnotationResurrectBean annotationResurrectBean;

    @Test
    void contextLoads() {
        String str = annotationResurrectBean.resurrect("名字", -1);
        log.info("启动成功: {}", str);
    }

}
