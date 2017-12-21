/**
 * Organization: lemon-china<br>
 * Date: 2017年8月23日下午9:12:41<br>
 * Copyright (c) 2017, lonyee@live.com All Rights Reserved.
 *
 */
package cn.lemon.dubbo.admin.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lemon.dubbo.account.api.IUserService;
import cn.lemon.dubbo.account.dto.UserAuditedDto;
import cn.lemon.dubbo.account.dto.UserDto;
import cn.lemon.dubbo.account.dto.UserOnlineDto;
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
public class UserController extends BasicController {
	@Reference
	private IUserService userService;
	@Reference
	private IRoleService roleService;
	@Reference
	private IAreaService areaService;
	
	@RequestPermissions({"admin_user"})
	@RequestMapping(value="/index", method={RequestMethod.GET})
	public String index(Model model) {
		return "user/index";
	}

	@RequestPermissions({"admin_online"})
	@RequestMapping(value="/online", method={RequestMethod.GET})
	public String online(Model model) {
		return "user/online";
	}
	
	@RequestPermissions({"admin_user"})
	@RequestMapping(value="/add", method={RequestMethod.GET})
	public String add(Model model) {
		Long userId = this.getUserId();
		model.addAttribute("user", new UserDto());
		model.addAttribute("areas", areaService.getList(userId, 0));
		model.addAttribute("roles", roleService.getList(userId));
		model.addAttribute("roleIds", new ArrayList<Integer>());
		return "user/edit";
	}
	
	@RequestPermissions({"admin_user"})
	@RequestMapping(value="/edit/{mId}", method={RequestMethod.GET})
	public String edit(Model model, @PathVariable("mId") Long mId) {
		Long userId = this.getUserId();
		model.addAttribute("user", userService.getByUserId(userId, mId));
		model.addAttribute("areas", areaService.getList(userId, 0));
		model.addAttribute("roles", roleService.getList(userId));
		model.addAttribute("roleIds", roleService.getRoleIds(mId));
		return "user/edit";
	}
	
	@RequestPermissions({"admin_user"})
	@RequestMapping(value="/view/{mId}", method={RequestMethod.GET})
	public String view(Model model, @PathVariable("mId") Long mId) {
		Long userId = this.getUserId();
		model.addAttribute("user", userService.getByUserId(userId, mId));
		model.addAttribute("areas", areaService.getList(userId, 0));
		model.addAttribute("roles", roleService.getList(userId));
		model.addAttribute("roleIds", roleService.getRoleIds(mId));
		return "user/edit";
	}
	
	@RequestPermissions({"admin_user"})
	@RequestMapping(value="/audit/{mId}", method={RequestMethod.GET})
	public String audit(Model model, @PathVariable("mId") Long mId) {
		Long userId = this.getUserId();
		model.addAttribute("user", userService.getAuditedById(userId, mId));
		return "user/audit";
	}
	
	@ApiOperation(value="查询用户信息",notes="返回分页数据")
	@ResponseBody
	@RequestPermissions({"admin_user"})
	@RequestMapping(value="/pager", method={RequestMethod.GET})
	public ResultResponse pager(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, Long mId, String mobile, String nickName, Integer audited, QueryPage queryPage) {
		Long userId = this.getUserId();
		queryPage.setCondition("userId", mId);
		queryPage.setCondition("mobile", mobile);
		queryPage.setCondition("nickName", nickName);
		queryPage.setCondition("audited", audited);
		Page page = userService.getListByPager(userId, queryPage);
		return resultResponse.success(page);
	}
	
	@ApiOperation(value="用户在线信息",notes="返回数据")
	@ResponseBody
	@RequestPermissions({"admin_online"})
	@RequestMapping(value="/online/pager", method={RequestMethod.GET})
	public ResultResponse online(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, String mobile, String nickName, QueryPage queryPage) {
		Long userId = this.getUserId();
		List<UserOnlineDto> list = userService.getOnlineList(userId, mobile, nickName);
		return resultResponse.success(list);
	}
	
	@ApiOperation(value="添加或修改用户信息",notes="返回success")
	@ResponseBody
	@RequestPermissions({"admin_user"})
	@RequestMapping(value="/save", method={RequestMethod.POST})
	public ResultResponse save(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, UserDto userDto, String password, Integer[] roleIds) throws ServiceException {
		Long userId = this.getUserId();
		if (userDto.getUserId()==null || userDto.getUserId()==0) {
			userService.save(userId, userDto, password, roleIds);
		} else {
			userService.update(userId, userDto, password, roleIds);
		}
		return resultResponse.success();
	}
	
	@ApiOperation(value="删除用户信息",notes="返回success")
	@ResponseBody
	@RequestPermissions({"admin_user"})
	@RequestMapping(value="/delete/{mId}", method={RequestMethod.POST})
	public ResultResponse delete(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, @PathVariable("mId") Long mId) {
		Long userId = this.getUserId();
		userService.delete(userId, mId);
		return resultResponse.success();
	}
	
	@ApiOperation(value="认证用户",notes="返回success")
	@ResponseBody
	@RequestPermissions({"admin_user"})
	@RequestMapping(value="/audited", method={RequestMethod.POST})
	public ResultResponse audited(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, UserAuditedDto userAuditedDto) {
		Long userId = this.getUserId();
		userService.audited(userId, userAuditedDto);
		return resultResponse.success();
	}
	
	@ApiOperation(value="冻结账号",notes="返回success")
	@ResponseBody
	@RequestPermissions({"admin_user"})
	@RequestMapping(value="/freezed/{mId}", method={RequestMethod.POST})
	public ResultResponse freezed(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, @PathVariable("mId") Long mId) {
		Long userId = this.getUserId();
		userService.freezed(userId, mId);
		return resultResponse.success();
	}
	
	@ApiOperation(value="解冻账号",notes="返回success")
	@ResponseBody
	@RequestPermissions({"admin_user"})
	@RequestMapping(value="/unfreeze/{mId}", method={RequestMethod.POST})
	public ResultResponse unfreeze(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, @PathVariable("mId") Long mId) {
		Long userId = this.getUserId();
		userService.unfreeze(userId, mId);
		return resultResponse.success();
	}
	
	@ApiOperation(value="在线用户强制踢出",notes="返回success")
	@ResponseBody
	@RequestPermissions({"admin_online"})
	@RequestMapping(value="/online/kill/{mToken}", method={RequestMethod.POST})
	public ResultResponse online(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, @PathVariable("mToken") String mToken) {
		Long userId = this.getUserId();
		userService.kill(userId, mToken);
		return resultResponse.success();
	}
}
