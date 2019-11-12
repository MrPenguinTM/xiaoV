package org.androidtest.robotp.publicutils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by zhangfan on 2018/4/1.
 */
public class ReflectMethodUtil {

	public static <T> T invokeMethodWithoutParam(Object target,
			String methodName, boolean staticMethod, boolean needReturnValue,
			Class[] classes, Object[] objects) {
		LogUtil.MSG.debug("invokeMethodWithoutParam: target=" + target
				+ ",methodName=" + methodName + ",staticMethod=" + staticMethod
				+ ",needReturnValue=" + needReturnValue);
		if (StringUtil.ifNullOrEmpty(methodName)) {
			throw new IllegalArgumentException(
					"methodName must be not null ...");
		}
		try {
			Method declaredMethod;
			if (target instanceof Class) {
				declaredMethod = ((Class<?>) target)
						.getDeclaredMethod(methodName);
			} else {
				declaredMethod = target.getClass()
						.getDeclaredMethod(methodName);
			}
			if (declaredMethod != null) {
				declaredMethod.setAccessible(true);
				Object result = declaredMethod.invoke(staticMethod ? null
						: target);
				return needReturnValue && result != null ? (T) result : null;
			}
		} catch (NoSuchMethodException | InvocationTargetException
				| IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T invokeMethodWithParam(Object target, String methodName,
			boolean staticMethod, boolean needReturnValue, Class<?>[] params,
			Object[] paramsValue) {
		LogUtil.MSG.debug("invokeMethodWithoutParam: target=" + target
				+ ",methodName=" + methodName + ",staticMethod=" + staticMethod
				+ ",needReturnValue=" + needReturnValue);
		if (StringUtil.ifNullOrEmpty(methodName)) {
			throw new IllegalArgumentException(
					"invoke method illegal, methodName must not be null ...");
		}
		if (params == null || params.length <= 0) {
			throw new IllegalArgumentException(
					"invoke method illegal, method param must not be null or Empty ...");
		}
		if (paramsValue == null || paramsValue.length <= 0) {
			throw new IllegalArgumentException(
					"invoke method illegal, method value must not be null or Empty ...");
		}
		if (params.length != paramsValue.length) {
			throw new IllegalArgumentException(
					"invoke method illegal, method param length is not equal value length ...");
		}
		// paramsValue的各项的数据类型对不上params的各项数据类型, 由具体的Class类的native层识别,
		// 并通知Class类报出异常, 所以此处不予处理.

		try {
			Method declaredMethod;
			if (target instanceof Class) {
				declaredMethod = ((Class<?>) target).getDeclaredMethod(
						methodName, params);
			} else {
				declaredMethod = target.getClass().getDeclaredMethod(
						methodName, params);
			}
			if (declaredMethod != null) {
				declaredMethod.setAccessible(true);
				Object result = declaredMethod.invoke(staticMethod ? null
						: target, paramsValue);
				return needReturnValue && result != null ? (T) result : null;
			}
		} catch (NoSuchMethodException | InvocationTargetException
				| IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
