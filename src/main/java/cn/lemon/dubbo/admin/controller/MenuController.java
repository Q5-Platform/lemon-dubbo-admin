/**
 * Organization: lemon-china<br>
 * Date: 2017年8月23日下午9:12:41<br>
 * Copyright (c) 2017, lonyee@live.com All Rights Reserved.
 *
 */
package cn.lemon.dubbo.admin.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.lemon.dubbo.admin.authc.RequestPermissions;
import cn.lemon.dubbo.system.api.IMenuService;
import cn.lemon.dubbo.system.dto.MenuDto;
import cn.lemon.framework.core.BasicController;
import cn.lemon.framework.response.ResultResponse;

import com.alibaba.dubbo.config.annotation.Reference;

/**
 * 系统菜单
 * @date 2017年8月23日 下午9:12:41 <br>
 * @author lonyee
 */
@RequestMapping("menu")
@RestController
public class MenuController extends BasicController {
	@Reference
	private IMenuService menuService;
	
	/**
	 * 获取菜单数据
	 */
	@RequestPermissions("")
	@RequestMapping(value="/getMenuTree", method = RequestMethod.GET)
	public ResultResponse getMenuTree(@CookieValue(value=TOKEN) String token) {
		Long userId = this.getUserId();
		List<MenuDto> list = menuService.getMenuTree(userId);
		return resultResponse.success(list);
	}
}
