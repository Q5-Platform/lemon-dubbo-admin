package cn.lemon.dubbo.admin.controller;

import cn.lemon.dubbo.admin.authc.RequestPermissions;
import cn.lemon.dubbo.system.api.IArticleService;
import cn.lemon.dubbo.system.api.IDictService;
import cn.lemon.dubbo.system.dto.ArticleDto;
import cn.lemon.framework.core.BasicController;
import cn.lemon.framework.query.Page;
import cn.lemon.framework.query.QueryPage;
import cn.lemon.framework.response.ResultResponse;
import cn.lemon.framework.response.ServiceException;
import com.alibaba.dubbo.config.annotation.Reference;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 文章管理接口
 * @author lonyee
 */
@Controller
@RequestMapping("/article")
@RequestPermissions({"admin_article"})
public class ArticleController extends BasicController {
	@Reference
	private IDictService dictService;
	@Reference
	private IArticleService articleService;
	
	@RequestMapping(value="/index", method={RequestMethod.GET})
	public String Index(Model model) {
		Long userId = this.getUserId();
		model.addAttribute("articleTypes", dictService.getList(userId, "articleType"));
		return "article/index";
	}
	
	@RequestMapping(value="/add", method={RequestMethod.GET})
	public String add(Model model) {
		Long userId = this.getUserId();
		model.addAttribute("article", new ArticleDto());
		model.addAttribute("articleTypes", dictService.getList(userId, "articleType"));
		return "article/edit";
	}
	
	@RequestMapping(value="/edit/{id}", method={RequestMethod.GET})
	public String edit(Model model, @PathVariable("id") Integer id) {
		Long userId = this.getUserId();
		model.addAttribute("article", articleService.getById(userId, id));
		model.addAttribute("articleTypes", dictService.getList(userId, "articleType"));
		return "article/edit";
	}
	
	@RequestMapping(value="/view/{id}", method={RequestMethod.GET})
	public String view(Model model, @PathVariable("id") Integer id) {
		Long userId = this.getUserId();
		model.addAttribute("article", articleService.getById(userId, id));
		model.addAttribute("articleTypes", dictService.getList(userId, "articleType"));
		return "article/edit";
	}
	
	@ApiOperation(value="查询文章分页信息",notes="返回分页数据")
	@ResponseBody
	@RequestMapping(value="/pager", method={RequestMethod.GET})
	public ResultResponse pager(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, String articleType, String keyword, QueryPage queryPage) {
		Long userId = this.getUserId();
		queryPage.setCondition("articleType", articleType);
		queryPage.setCondition("keyword", keyword);
		Page page = articleService.getListByPager(userId, queryPage);
		return resultResponse.success(page);
	}
	
	@ApiOperation(value="添加或修改文章信息",notes="返回success")
	@ResponseBody
	@RequestMapping(value="/save", method={RequestMethod.POST})
	public ResultResponse save(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, ArticleDto articleDto) throws ServiceException {
		Long userId = this.getUserId();
		try {
			String content = HtmlUtils.htmlUnescape(articleDto.getContent());
			articleDto.setContent(content);
		} catch (Exception ex) {
			logger.warn("文章内容转码失败", ex);
		}
		if (articleDto.getId()==null || articleDto.getId()==0) {
			articleService.save(userId, articleDto);
		} else {
			articleService.update(userId, articleDto);
		}
		return resultResponse.success();
	}
	
	@ApiOperation(value="删除文章信息",notes="返回success")
	@ResponseBody
	@RequestMapping(value="/delete/{id}", method={RequestMethod.POST})
	public ResultResponse delete(@ApiParam(value="授权凭证") @CookieValue(value=TOKEN, required=true) String token, @PathVariable("id") Integer id) throws ServiceException {
		Long userId = this.getUserId();
		articleService.delete(userId, id);
		return resultResponse.success();
	}
}
