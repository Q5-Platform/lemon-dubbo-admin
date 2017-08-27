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

import cn.lemon.dubbo.system.api.IProjectService;
import cn.lemon.dubbo.system.dto.ProjectDto;
import cn.lemon.framework.core.BasicController;
import cn.lemon.framework.query.Page;
import cn.lemon.framework.query.QueryPage;
import cn.lemon.framework.response.ResultResponse;

import com.alibaba.dubbo.config.annotation.Reference;

/**
 * 项目管理接口
 * @author lonyee
 */
@Controller
@RequestMapping("/project")
public class ProjectController extends BasicController {
	@Reference
	private IProjectService projectService;
	
	@RequestMapping("/index")
	public String Index() {
		return "project/index";
	}
	
	@RequestMapping("/add")
	public String add(Model model) {
		model.addAttribute("project", new ProjectDto());
		return "project/edit";
	}
	
	@RequestMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") Integer id) {
		Long userId = this.getUserId();
		model.addAttribute("project", projectService.getById(userId, id));
		return "project/edit";
	}
	
	@RequestMapping("/view/{id}")
	public String view(Model model, @PathVariable("id") Integer id) {
		Long userId = this.getUserId();
		model.addAttribute("project", projectService.getById(userId, id));
		return "project/edit";
	}
	
	@ApiOperation(value="查询项目分页信息",notes="返回分页数据")
	@ResponseBody
	@RequestMapping(value="/pager", method={RequestMethod.GET})
	public ResultResponse pager(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, String code, String name, QueryPage queryPage) {
		Long userId = this.getUserId();
		queryPage.setCondition("code", code);
		queryPage.setCondition("name", name);
		Page page = projectService.getListByPager(userId, queryPage);
		return resultResponse.success(page);
	}
	
	@ApiOperation(value="添加或修改项目信息",notes="返回success")
	@ResponseBody
	@RequestMapping(value="/save", method={RequestMethod.POST})
	public ResultResponse save(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, ProjectDto projectDto) {
		Long userId = this.getUserId();
		if (projectDto.getId()==null || projectDto.getId()==0) {
			projectService.save(userId, projectDto);
		} else {
			projectService.update(userId, projectDto);
		}
		return resultResponse.success();
	}
	
	@ApiOperation(value="删除项目信息",notes="返回success")
	@ResponseBody
	@RequestMapping(value="/delete/{id}", method={RequestMethod.POST})
	public ResultResponse delete(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, @PathVariable("id") Integer id) {
		Long userId = this.getUserId();
		projectService.delete(userId, id);
		return resultResponse.success();
	}
}
