package cn.xpp011.resurrect.interceptor;

import lombok.Builder;

/**
 * @author renyu.shen
 **/
@Builder
public record ResurrectOperation(String name, String condition, Class<?>[] ignore) {
}
