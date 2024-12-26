package cn.xpp011.resurrect.interceptor;

import cn.xpp011.resurrect.enums.InvertStatus;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author renyu.shen
 **/

public record Nutrient(String id, String className, String methodName, ObjVal[] objValues, InvertStatus status) {

    public Nutrient {

    }

    public static class NutrientBuilder {
        private String className;

        private String methodName;

        private ObjVal[] objValues;

        public NutrientBuilder methodInvocation(MethodInvocation methodInvocation) {
            Assert.notNull(methodInvocation, "MethodInvocation must not be null");
            Method method = methodInvocation.getMethod();
            Object[] arguments = methodInvocation.getArguments();
            Object target = methodInvocation.getThis();
            Assert.state(target != null, "Target must not be null");
            Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
            this.methodName = method.getName();
            this.className = targetClass.getName();
            int n = arguments.length;
            this.objValues = new ObjVal[n];
            for (int i = 0; i < n; i++) {
                Object argument = arguments[i];
                ObjVal objVal = new ObjVal(argument.getClass().getName(), argument, i);
                this.objValues[i] = objVal;
            }
            return this;
        }

        public Nutrient build() {
            // TODO 解决UUID时钟回拨问题
            return new Nutrient(UUID.randomUUID().toString(), className, methodName, objValues, InvertStatus.PREPARE);
        }
    }
}
