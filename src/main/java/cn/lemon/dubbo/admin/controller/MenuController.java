/**
 * Organization: lemon-china<br>
 * Date: 2017年8月23日下午9:12:41<br>
 * Copyright (c) 2017, lonyee@live.com All Rights Reserved.
 *
 */
package cn.lemon.dubbo.admin.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lemon.dubbo.admin.authc.RequestPermissions;
import cn.lemon.dubbo.system.api.IMenuService;
import cn.lemon.dubbo.system.api.IProjectService;
import cn.lemon.dubbo.system.dto.MenuDto;
import cn.lemon.framework.core.BasicController;
import cn.lemon.framework.query.Page;
import cn.lemon.framework.query.QueryPage;
import cn.lemon.framework.response.ResultResponse;
import cn.lemon.framework.response.ServiceException;

import com.alibaba.dubbo.config.annotation.Reference;

/**
 * 系统菜单
 * @date 2017年8月23日 下午9:12:41 <br>
 * @author lonyee
 */
@Controller
@RequestMapping("menu")
@RequestPermissions({"admin_menu"})
public class MenuController extends BasicController {
	@Reference
	private IMenuService menuService;
	@Reference
	private IProjectService projectService;
	
	@RequestMapping(value="/index", method={RequestMethod.GET})
	public String Index(Model model) {
		Long userId = this.getUserId();
		model.addAttribute("projects", projectService.getList(userId));
		return "menu/index";
	}

	@RequestMapping(value="/add", method={RequestMethod.GET})
	public String add(Model model) {
		Long userId = this.getUserId();
		model.addAttribute("menu", new MenuDto());
		model.addAttribute("projects", projectService.getList(userId));
		return "menu/edit";
	}
	
	@RequestMapping(value="/edit/{id}", method={RequestMethod.GET})
	public String edit(Model model, @PathVariable("id") Integer id) {
		Long userId = this.getUserId();
		model.addAttribute("menu", menuService.getById(userId, id));
		model.addAttribute("projects", projectService.getList(userId));
		return "menu/edit";
	}
	
	@RequestMapping(value="/view/{id}", method={RequestMethod.GET})
	public String view(Model model, @PathVariable("id") Integer id) {
		Long userId = this.getUserId();
		model.addAttribute("menu", menuService.getById(userId, id));
		model.addAttribute("projects", projectService.getList(userId));
		return "menu/edit";
	}
	
	@ApiOperation(value="查询菜单分页信息",notes="返回分页数据")
	@ResponseBody
	@RequestMapping(value="/pager", method={RequestMethod.GET})
	public ResultResponse pager(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, Integer projectId, String name, QueryPage queryPage) {
		Long userId = this.getUserId();
		queryPage.setCondition("projectId", projectId);
		queryPage.setCondition("name", name);
		queryPage.setCondition("isLeaf", false);
		Page page = menuService.getListByPager(userId, queryPage);
		return resultResponse.success(page);
	}

	@ApiOperation(value="添加或修改菜单信息",notes="返回success")
	@ResponseBody
	@RequestMapping(value="/save", method={RequestMethod.POST})
	public ResultResponse save(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, MenuDto menuDto) throws ServiceException {
		Long userId = this.getUserId();
		menuDto.setIsLeaf(false);
		if (menuDto.getId()==null || menuDto.getId()==0) {
			menuDto.setParentId(0);
			menuService.save(userId, menuDto);
		} else {
			menuService.update(userId, menuDto);
		}
		return resultResponse.success();
	}
	
	@ApiOperation(value="删除菜单信息",notes="返回success")
	@ResponseBody
	@RequestMapping(value="/delete/{id}", method={RequestMethod.POST})
	public ResultResponse delete(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, @PathVariable("id") Integer id) throws ServiceException {
		Long userId = this.getUserId();
		menuService.delete(userId, id);
		return resultResponse.success();
	}
}
