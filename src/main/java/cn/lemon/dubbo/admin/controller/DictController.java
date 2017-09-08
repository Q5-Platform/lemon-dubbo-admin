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
import cn.lemon.dubbo.system.api.IDictService;
import cn.lemon.dubbo.system.dto.DictDto;
import cn.lemon.framework.core.BasicController;
import cn.lemon.framework.query.Page;
import cn.lemon.framework.query.QueryPage;
import cn.lemon.framework.response.ResultResponse;
import cn.lemon.framework.response.ServiceException;

import com.alibaba.dubbo.config.annotation.Reference;

/**
 * 数据字典接口
 * @author lonyee
 */
@Controller
@RequestMapping("/dict")
@RequestPermissions({"admin_dict"})
public class DictController extends BasicController {
	@Reference
	private IDictService dictService;
	
	@RequestMapping(value="/index", method={RequestMethod.GET})
	public String Index(Model model) {
		Long userId = this.getUserId();
		model.addAttribute("dicts", dictService.getList(userId, 0));
		return "dict/index";
	}
	
	@RequestMapping(value="/add", method={RequestMethod.GET})
	public String add(Model model) {
		Long userId = this.getUserId();
		model.addAttribute("dict", new DictDto());
		model.addAttribute("dicts", dictService.getList(userId, 0));
		return "dict/edit";
	}
	
	@RequestMapping(value="/edit/{id}", method={RequestMethod.GET})
	public String edit(Model model, @PathVariable("id") Integer id) {
		Long userId = this.getUserId();
		model.addAttribute("dict", dictService.getById(userId, id));
		model.addAttribute("dicts", dictService.getList(userId, 0));
		return "dict/edit";
	}
	
	@RequestMapping(value="/view/{id}", method={RequestMethod.GET})
	public String view(Model model, @PathVariable("id") Integer id) {
		Long userId = this.getUserId();
		model.addAttribute("dict", dictService.getById(userId, id));
		model.addAttribute("dicts", dictService.getList(userId, 0));
		return "dict/edit";
	}
	
	@ApiOperation(value="查询词典分页信息",notes="返回分页数据")
	@ResponseBody
	@RequestMapping(value="/pager", method={RequestMethod.GET})
	public ResultResponse pager(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, Integer parentId, String type, String name, QueryPage queryPage) {
		Long userId = this.getUserId();
		queryPage.setCondition("parentId", parentId);
		queryPage.setCondition("type", type);
		queryPage.setCondition("name", name);
		Page page = dictService.getListByPager(userId, queryPage);
		return resultResponse.success(page);
	}
	
	@ApiOperation(value="添加或修改词典信息",notes="返回success")
	@ResponseBody
	@RequestMapping(value="/save", method={RequestMethod.POST})
	public ResultResponse save(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, DictDto dictDto) throws ServiceException {
		Long userId = this.getUserId();
		if (dictDto.getId()==null || dictDto.getId()==0) {
			dictService.save(userId, dictDto);
		} else {
			dictService.update(userId, dictDto);
		}
		return resultResponse.success();
	}
	
	@ApiOperation(value="删除词典信息",notes="返回success")
	@ResponseBody
	@RequestMapping(value="/delete/{id}", method={RequestMethod.POST})
	public ResultResponse delete(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, @PathVariable("id") Integer id) throws ServiceException {
		Long userId = this.getUserId();
		dictService.delete(userId, id);
		return resultResponse.success();
	}
}
