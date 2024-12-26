package cn.xpp011.resurrect;

import cn.xpp011.resurrect.annotation.EnableResurrect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author renyu.shen
 **/
@SpringBootApplication
@EnableResurrect

public class ResurrectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResurrectApplication.class, args);
    }

}
