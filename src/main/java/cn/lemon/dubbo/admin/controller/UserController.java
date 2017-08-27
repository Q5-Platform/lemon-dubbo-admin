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

import cn.lemon.dubbo.account.api.IUserService;
import cn.lemon.dubbo.account.dto.UserDto;
import cn.lemon.framework.core.BasicController;
import cn.lemon.framework.query.Page;
import cn.lemon.framework.query.QueryPage;
import cn.lemon.framework.response.ResultResponse;

import com.alibaba.dubbo.config.annotation.Reference;

/**
 * 用户管理
 * @date 2017年8月23日 下午9:12:41 <br>
 * @author lonyee
 */
@RequestMapping("user")
@Controller
public class UserController extends BasicController {
	@Reference
	private IUserService userService;
	
	@RequestMapping("/index")
	public String Index(Model model) {
		return "user/index";
	}
	
	@RequestMapping("/add")
	public String add(Model model) {
		model.addAttribute("user", new UserDto());
		return "user/edit";
	}
	
	@RequestMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") Long id) {
		Long userId = this.getUserId();
		model.addAttribute("user", userService.getById(userId, id));
		return "user/edit";
	}
	
	@RequestMapping("/view/{id}")
	public String view(Model model, @PathVariable("id") Long id) {
		Long userId = this.getUserId();
		model.addAttribute("user", userService.getById(userId, id));
		return "user/edit";
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
	public ResultResponse save(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, UserDto userDto) {
		Long userId = this.getUserId();
		if (userDto.getId()==null || userDto.getId()==0) {
			userService.save(userId, userDto);
		} else {
			userService.update(userId, userDto);
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
	
}
