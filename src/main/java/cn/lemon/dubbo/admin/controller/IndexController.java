package cn.lemon.dubbo.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lemon.dubbo.account.api.IAuthenticationService;
import cn.lemon.dubbo.account.api.IUserService;
import cn.lemon.dubbo.account.dto.LoginUserDto;
import cn.lemon.dubbo.account.dto.UserDto;
import cn.lemon.dubbo.admin.vo.UserVo;
import cn.lemon.dubbo.system.api.IMenuService;
import cn.lemon.dubbo.system.dto.MenuDto;
import cn.lemon.framework.core.BasicController;
import cn.lemon.framework.response.ResultResponse;
import cn.lemon.framework.response.ServiceException;
import cn.lemon.framework.utils.BeanUtil;
import cn.lemon.framework.utils.CookieUtil;

import com.alibaba.dubbo.config.annotation.Reference;

@Api(description="会员账号登录管理接口")
@Controller
public class IndexController extends BasicController {
	@Reference
	private IUserService userService;
	@Reference
	private IMenuService menuService;
	@Reference
	private IAuthenticationService authenticationService;
	
	@RequestMapping(value="/login", method={RequestMethod.GET})
	public String login() {
		return "login";
	}
	
	@RequestMapping(value="/", method={RequestMethod.GET})
	public String home() {
		return "index";
	}
	
	@RequestMapping(value="/index", method={RequestMethod.GET})
	public String index() {
		return "index";
	}
	
	@RequestMapping(value="/403", method={ RequestMethod.GET })
	public String page403() {
		return "403";
	}
	
	@RequestMapping(value="/404", method={ RequestMethod.GET })
	public String page404() {
		return "404";
	}
	
	@RequestMapping(value="/locked", method={RequestMethod.GET})
	public String locked() {
		CookieUtil.removeAllCookie(request, response);
		return "locked";
	}
	
	@RequestMapping(value="/demo", method={RequestMethod.GET})
	public String demo() {
		return "demo/demo";
	}
	
	@RequestMapping(value="/logout", method={RequestMethod.GET})
	public String logout(@CookieValue(value=TOKEN, required=false) String token) {
		authenticationService.logout(token);
		CookieUtil.removeAllCookie(request, response);
		return "redirect:/login";
	}
    
    @ApiOperation(value="用户注册",notes="返回success")
    @ResponseBody
	@RequestMapping(value="/user/register", method={RequestMethod.POST})
	public ResultResponse register(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=false) String token, 
			@ApiParam(value="手机号", required=true) @RequestParam(required=true) String mobile, 
			@ApiParam(value="密码", required=true) @RequestParam(required=true) String password, 
			@ApiParam(value="昵称") @RequestParam(required=false) String nickName) throws ServiceException {
		authenticationService.register(token, mobile, password, nickName, "lemon-admin");
		return resultResponse.success();
	}
	
    @ApiOperation(value="用户登录系统",notes="返回TOKEN和用户基本信息")
    @ResponseBody
	@RequestMapping(value="/user/login", method={RequestMethod.POST})
	public ResultResponse login(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=false) String token, 
			@ApiParam(value="手机号", required=true) @RequestParam(required=true) String mobile, 
			@ApiParam(value="密码", required=true) @RequestParam(required=true) String password) throws ServiceException {
		LoginUserDto user = authenticationService.login(token, mobile, password, "lemon-admin");
		CookieUtil.setCookie(request, response, TOKEN, user.getToken(), 2);
		return resultResponse.success(user);
	}
    
    @ApiOperation(value="用户解锁",notes="返回TOKEN")
    @ResponseBody
	@RequestMapping(value="/user/unlock", method={RequestMethod.POST})
	public ResultResponse unlock(@ApiParam(value="授权凭证") @RequestParam(required=true) String token, 
			@ApiParam(value="手机号", required=true) @RequestParam(required=true) String mobile, 
			@ApiParam(value="密码", required=true) @RequestParam(required=true) String password) throws ServiceException {
		authenticationService.hasLogined(token, mobile, password, "lemon-admin");
		CookieUtil.setCookie(request, response, TOKEN, token, 2);
		return resultResponse.success();
	}
    
    
    
    @ApiOperation(value="获取我的详细信息",notes="返回用户详细信息")
    @ResponseBody
    @RequestMapping(value="/user/profile", method=RequestMethod.GET)
	public ResultResponse profile(@ApiParam(value="授权凭证", required=true) @CookieValue(value=TOKEN, required=true) String token) {
    	Long userId = this.getUserId();
    	UserDto user = userService.getById(userId, userId);
		return resultResponse.success(user);
	}
    
    @ApiOperation(value="更新我的详细信息",notes="返回success")
    @ResponseBody
    @RequestMapping(value="/user/profile", method=RequestMethod.POST)
	public ResultResponse updateProfile(@ApiParam(value="授权凭证", required=true) @CookieValue(value=TOKEN, required=true) String token,
			@RequestBody UserVo user) {
    	Long userId = this.getUserId();
    	userService.updateProfile(userId, BeanUtil.toBeanValues(user, UserDto.class));
		return resultResponse.success();
	}
	
	/**
	 * 获取菜单数据
	 */
	@ResponseBody
	@RequestMapping(value="/menus", method = RequestMethod.GET)
	public ResultResponse menus(@CookieValue(value=TOKEN, required=true) String token) {
		Long userId = this.getUserId();
		String projectCode = authenticationService.getScope(token);
		List<MenuDto> list = menuService.getMenuTree(projectCode, userId);
		return resultResponse.success(list);
	}
}
