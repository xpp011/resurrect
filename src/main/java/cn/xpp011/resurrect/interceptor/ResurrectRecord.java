package cn.xpp011.resurrect.interceptor;

import cn.xpp011.resurrect.enums.InvertStatus;

import java.time.LocalDateTime;

/**
 * @author renyu.shen
 **/

public record ResurrectRecord(String id, Nutrient nutrient,
                              String hostname, String ip,
                              int invertCut, InvertStatus status,
                              LocalDateTime time, LocalDateTime interTime) {
}
