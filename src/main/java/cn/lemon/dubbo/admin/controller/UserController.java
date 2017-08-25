package cn.lemon.dubbo.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.lemon.dubbo.account.api.IAuthenticationService;
import cn.lemon.dubbo.account.api.IUserService;
import cn.lemon.dubbo.account.dto.LoginUserDto;
import cn.lemon.dubbo.account.dto.UserDto;
import cn.lemon.dubbo.admin.vo.UserVo;
import cn.lemon.framework.core.BasicController;
import cn.lemon.framework.response.ResultResponse;
import cn.lemon.framework.response.ServiceException;
import cn.lemon.framework.utils.BeanUtil;
import cn.lemon.framework.utils.CookieUtil;

import com.alibaba.dubbo.config.annotation.Reference;

@Api(description="会员账号登录管理接口")
@RestController
@RequestMapping("/user")
public class UserController extends BasicController {
	@Reference
	private IUserService userService;
	@Reference
	private IAuthenticationService authenticationService;
	
    @ApiOperation(value="登出系统",notes="返回success")
	@RequestMapping(value="/logout", method={RequestMethod.POST})
	public ResultResponse logout(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=false) String token) {
    	authenticationService.logout(token);
		return resultResponse.success();
	}
    
    @ApiOperation(value="用户注册",notes="返回success")
	@RequestMapping(value="/register", method={RequestMethod.POST})
	public ResultResponse register(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=false) String token, 
			@ApiParam(value="手机号", required=true) @RequestParam(required=true) String mobile, 
			@ApiParam(value="密码", required=true) @RequestParam(required=true) String password, 
			@ApiParam(value="昵称") @RequestParam(required=false) String nickName) throws ServiceException {
		authenticationService.register(token, mobile, password, nickName, "123456");
		return resultResponse.success();
	}
	
    @ApiOperation(value="用户登录系统",notes="返回TOKEN和用户基本信息")
	@RequestMapping(value="/login", method={RequestMethod.POST})
	public ResultResponse login(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=false) String token, 
			@ApiParam(value="手机号", required=true) @RequestParam(required=true) String mobile, 
			@ApiParam(value="密码", required=true) @RequestParam(required=true) String password) throws ServiceException {
		LoginUserDto user = authenticationService.login(token, mobile, password, "123456");
		CookieUtil.setCookie(request, response, TOKEN, user.getToken(), 2);
		return resultResponse.success(user);
	}
    
    @ApiOperation(value="获取我的详细信息",notes="返回用户详细信息")
    @RequestMapping(value="/profile", method=RequestMethod.GET)
	public ResultResponse profile(@ApiParam(value="授权凭证", required=true) @CookieValue(value=TOKEN, required=true) String token) {
    	Long userId = this.getUserId();
    	UserDto user = userService.getById(userId, userId);
		return resultResponse.success(user);
	}
    
    @ApiOperation(value="更新我的详细信息",notes="返回success")
    @RequestMapping(value="/profile", method=RequestMethod.POST)
	public ResultResponse updateProfile(@ApiParam(value="授权凭证", required=true) @CookieValue(value=TOKEN, required=true) String token,
			@RequestBody UserVo user) {
    	Long userId = this.getUserId();
    	userService.updateProfile(userId, BeanUtil.toBeanValues(user, UserDto.class));
		return resultResponse.success();
	}
}
