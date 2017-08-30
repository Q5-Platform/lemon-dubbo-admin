/**
 * Organization: lemon-china<br>
 * Date: 2017年8月23日下午9:12:41<br>
 * Copyright (c) 2017, lonyee@live.com All Rights Reserved.
 *
 */
package cn.lemon.dubbo.admin.controller;

import java.util.ArrayList;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lemon.dubbo.account.api.IUserService;
import cn.lemon.dubbo.account.dto.UserAudittedDto;
import cn.lemon.dubbo.account.dto.UserDto;
import cn.lemon.dubbo.admin.authc.RequestPermissions;
import cn.lemon.dubbo.system.api.IAreaService;
import cn.lemon.dubbo.system.api.IRoleService;
import cn.lemon.framework.core.BasicController;
import cn.lemon.framework.query.Page;
import cn.lemon.framework.query.QueryPage;
import cn.lemon.framework.response.ResultResponse;
import cn.lemon.framework.response.ServiceException;

import com.alibaba.dubbo.config.annotation.Reference;

/**
 * 用户管理
 * @date 2017年8月23日 下午9:12:41 <br>
 * @author lonyee
 */
@Controller
@RequestMapping("user")
@RequestPermissions({"admin_user"})
public class UserController extends BasicController {
	@Reference
	private IUserService userService;
	@Reference
	private IRoleService roleService;
	@Reference
	private IAreaService areaService;
	
	@RequestMapping(value="/index", method={RequestMethod.GET})
	public String Index(Model model) {
		return "user/index";
	}
	
	@RequestMapping(value="/add", method={RequestMethod.GET})
	public String add(Model model) {
		Long userId = this.getUserId();
		model.addAttribute("user", new UserDto());
		model.addAttribute("areas", areaService.getList(userId, 0));
		model.addAttribute("roles", roleService.getList(userId));
		model.addAttribute("roleIds", new ArrayList<Integer>());
		return "user/edit";
	}
	
	@RequestMapping(value="/edit/{id}", method={RequestMethod.GET})
	public String edit(Model model, @PathVariable("id") Long id) {
		Long userId = this.getUserId();
		model.addAttribute("user", userService.getById(userId, id));
		model.addAttribute("areas", areaService.getList(userId, 0));
		model.addAttribute("roles", roleService.getList(userId));
		model.addAttribute("roleIds", roleService.getRoleIds(id));
		return "user/edit";
	}
	
	@RequestMapping(value="/view/{id}", method={RequestMethod.GET})
	public String view(Model model, @PathVariable("id") Long id) {
		Long userId = this.getUserId();
		model.addAttribute("user", userService.getById(userId, id));
		model.addAttribute("areas", areaService.getList(userId, 0));
		model.addAttribute("roles", roleService.getList(userId));
		model.addAttribute("roleIds", roleService.getRoleIds(id));
		return "user/edit";
	}
	
	@RequestMapping(value="/audit/{id}", method={RequestMethod.GET})
	public String audit(Model model, @PathVariable("id") Long id) {
		Long userId = this.getUserId();
		model.addAttribute("user", userService.getAudittedById(userId, id));
		return "user/audit";
	}
		
	@ApiOperation(value="查询用户分页信息",notes="返回分页数据")
	@ResponseBody
	@RequestMapping(value="/pager", method={RequestMethod.GET})
	public ResultResponse pager(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, Long id, String mobile, String nickName, Integer auditted, QueryPage queryPage) {
		Long userId = this.getUserId();
		queryPage.setCondition("id", id);
		queryPage.setCondition("mobile", mobile);
		queryPage.setCondition("nickName", nickName);
		queryPage.setCondition("auditted", auditted);
		Page page = userService.getListByPager(userId, queryPage);
		return resultResponse.success(page);
	}
	
	@ApiOperation(value="添加或修改用户信息",notes="返回success")
	@ResponseBody
	@RequestMapping(value="/save", method={RequestMethod.POST})
	public ResultResponse save(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, UserDto userDto, String password, Integer[] roleIds) throws ServiceException {
		Long userId = this.getUserId();
		if (userDto.getId()==null || userDto.getId()==0) {
			userService.save(userId, userDto, password, roleIds);
		} else {
			userService.update(userId, userDto, password, roleIds);
		}
		return resultResponse.success();
	}
	
	@ApiOperation(value="删除用户信息",notes="返回success")
	@ResponseBody
	@RequestMapping(value="/delete/{id}", method={RequestMethod.POST})
	public ResultResponse delete(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, @PathVariable("id") Long id) {
		Long userId = this.getUserId();
		userService.delete(userId, id);
		return resultResponse.success();
	}
	
	@ApiOperation(value="认证用户",notes="返回success")
	@ResponseBody
	@RequestMapping(value="/auditted", method={RequestMethod.POST})
	public ResultResponse auditted(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, UserAudittedDto userAudittedDto) {
		Long userId = this.getUserId();
		userService.auditted(userId, userAudittedDto);
		return resultResponse.success();
	}
	
}
