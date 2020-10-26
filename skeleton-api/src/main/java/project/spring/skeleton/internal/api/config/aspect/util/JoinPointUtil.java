package project.spring.skeleton.internal.api.config.aspect.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Slf4j
@UtilityClass
public class JoinPointUtil {
    public static Class<?> getReturnType(JoinPoint joinPoint) {
        final Method targetMethod = findMethod(joinPoint);

        assert targetMethod != null;
        return targetMethod.getReturnType();
    }

    public static Method getDeclaredMethod(JoinPoint joinPoint) {
        final Signature signature = joinPoint.getSignature();
        final MethodSignature methodSignature = (MethodSignature) joinPoint
                .getSignature();

        try {
            return joinPoint.getTarget().getClass()
                    .getDeclaredMethod(signature.getName(), methodSignature.getParameterTypes());

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object extractParameter(JoinPoint joinPoint, String name) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

        Class[] parameterTypes = codeSignature.getParameterTypes();
        String[] parameterNames = codeSignature.getParameterNames();

        int parameterLength = parameterTypes.length;
        for (int typeIdx = 0; typeIdx < parameterLength; typeIdx++) {
            if (parameterNames[typeIdx] == null) {
                continue;
            }

            if (parameterNames[typeIdx].equals(name)) {
                return joinPoint.getArgs()[typeIdx];
            }
        }

        return null;
    }

    public static Method findMethod(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        Class<?> clazz = signature.getDeclaringType();
        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            String methodFullName = method.toString();
            if ((!methodFullName.equals(signature.toLongString()))) {
                continue;
            }
            return method;
        }

        return null;
    }
}