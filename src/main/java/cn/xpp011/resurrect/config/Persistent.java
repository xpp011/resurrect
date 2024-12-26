package cn.xpp011.resurrect.config;

import cn.xpp011.resurrect.interceptor.Nutrient;

/**
 * @author renyu.shen
 **/

public interface Persistent {

    boolean save(Nutrient nutrient);

    boolean commit(String id);

    boolean flagRetry(String id);

}
