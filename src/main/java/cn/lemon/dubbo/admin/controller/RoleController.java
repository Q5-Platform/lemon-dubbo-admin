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

import cn.lemon.dubbo.system.api.IRoleService;
import cn.lemon.dubbo.system.dto.RoleDto;
import cn.lemon.framework.core.BasicController;
import cn.lemon.framework.query.Page;
import cn.lemon.framework.query.QueryPage;
import cn.lemon.framework.response.ResultResponse;

import com.alibaba.dubbo.config.annotation.Reference;

/**
 * 角色管理
 * @date 2017年8月23日 下午9:12:41 <br>
 * @author lonyee
 */
@RequestMapping("role")
@Controller
public class RoleController extends BasicController {
	@Reference
	private IRoleService roleService;
	
	@RequestMapping("/index")
	public String Index(Model model) {
		return "role/index";
	}
	
	@RequestMapping("/add")
	public String add(Model model) {
		model.addAttribute("role", new RoleDto());
		return "role/edit";
	}
	
	@RequestMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") Integer id) {
		Long userId = this.getUserId();
		model.addAttribute("role", roleService.getById(userId, id));
		return "role/edit";
	}
	
	@RequestMapping("/view/{id}")
	public String view(Model model, @PathVariable("id") Integer id) {
		Long userId = this.getUserId();
		model.addAttribute("role", roleService.getById(userId, id));
		return "role/edit";
	}
	
	@ApiOperation(value="查询角色分页信息",notes="返回分页数据")
	@ResponseBody
	@RequestMapping(value="/pager", method={RequestMethod.GET})
	public ResultResponse pager(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, String name, QueryPage queryPage) {
		Long userId = this.getUserId();
		queryPage.setCondition("name", name);
		Page page = roleService.getListByPager(userId, queryPage);
		return resultResponse.success(page);
	}
	
	@ApiOperation(value="添加或修改角色信息",notes="返回success")
	@ResponseBody
	@RequestMapping(value="/save", method={RequestMethod.POST})
	public ResultResponse save(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, RoleDto roleDto) {
		Long userId = this.getUserId();
		if (roleDto.getId()==null || roleDto.getId()==0) {
			roleService.save(userId, roleDto);
		} else {
			roleService.update(userId, roleDto);
		}
		return resultResponse.success();
	}
	
	@ApiOperation(value="删除角色信息",notes="返回success")
	@ResponseBody
	@RequestMapping(value="/delete/{id}", method={RequestMethod.POST})
	public ResultResponse delete(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, @PathVariable("id") Integer id) {
		Long userId = this.getUserId();
		roleService.delete(userId, id);
		return resultResponse.success();
	}
	
}
