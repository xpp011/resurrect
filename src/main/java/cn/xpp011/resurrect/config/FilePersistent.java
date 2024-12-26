package cn.xpp011.resurrect.config;

import cn.xpp011.resurrect.interceptor.Nutrient;
import lombok.extern.slf4j.Slf4j;

/**
 * @author renyu.shen
 **/
@Slf4j
public class FilePersistent implements Persistent{
    @Override
    public boolean save(Nutrient nutrient) {
        log.info("保存nutrient: {}", nutrient);
        return true;
    }

    @Override
    public boolean commit(String id) {
        log.info("提交nutrient: {}", id);
        return true;
    }

    @Override
    public boolean flagRetry(String id) {
        log.info("标记重试nutrient: {}", id);
        return true;
    }
}
