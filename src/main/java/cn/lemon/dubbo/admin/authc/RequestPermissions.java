/**
 * Organization: lemon-china<br>
 * Date: 2017年8月25日上午9:16:42<br>
 * Copyright (c) 2017, lonyee@live.com All Rights Reserved.
 *
 */
package cn.lemon.dubbo.admin.authc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限码校验
 * <p>
 * RequestPermissions({"menu_add","menu_mod"})
 * </p>
 * @date 2017年8月25日 上午9:16:42 <br>
 * @author lonyee
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestPermissions {
	/** 权限码 **/
	String[] value();
}
