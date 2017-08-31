/**
 * Organization: lemon-china<br>
 * Date: 2017年8月28日下午1:56:51<br>
 * Copyright (c) 2017, lonyee@live.com All Rights Reserved.
 *
 */
package cn.lemon.dubbo.admin.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lemon.dubbo.system.api.IAreaService;
import cn.lemon.dubbo.system.dto.AreaDto;
import cn.lemon.framework.core.BasicController;
import cn.lemon.framework.response.ResultResponse;

import com.alibaba.dubbo.config.annotation.Reference;

/**
 * 全国区域管理
 * @date 2017年8月28日 下午1:56:51 <br>
 * @author lonyee
 */
@Controller
@RequestMapping("area")
public class AreaController extends BasicController {
	@Reference
	private IAreaService areaService;
	
	@ApiOperation(value="获取省份信息", notes="返回success")
	@ResponseBody
	@RequestMapping(value="/province", method={RequestMethod.GET})
	public ResultResponse province(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token) {
		Long userId = this.getUserId();
		List<AreaDto> list = areaService.getList(userId, 0);
		return resultResponse.success(list);
	}
	
	@ApiOperation(value="获取城市信息", notes="返回success")
	@ResponseBody
	@RequestMapping(value="/city", method={RequestMethod.GET})
	public ResultResponse city(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, Integer provinceId) {
		Long userId = this.getUserId();
		List<AreaDto> list = areaService.getList(userId, provinceId);
		return resultResponse.success(list);
	}
	
	@ApiOperation(value="获取区县信息", notes="返回success")
	@ResponseBody
	@RequestMapping(value="/county", method={RequestMethod.GET})
	public ResultResponse county(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, Integer cityId) {
		Long userId = this.getUserId();
		List<AreaDto> list = areaService.getList(userId, cityId);
		return resultResponse.success(list);
	}
}
