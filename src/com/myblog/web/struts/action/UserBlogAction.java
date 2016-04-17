/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.myblog.web.struts.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.myblog.domain.Article;
import com.myblog.domain.Bloginfo;
import com.myblog.domain.User;
import com.myblog.service.inter.ArticleServiceInter;
import com.myblog.service.inter.BlogInfoServiceInter;
import com.myblog.service.inter.UserServiceInter;

public class UserBlogAction extends DispatchAction {
	@Resource
	UserServiceInter userService;
	@Resource
	BlogInfoServiceInter blogInfoService;
	@Resource
	ArticleServiceInter articleService;
	
	public void setUserService(UserServiceInter userService) {
		this.userService = userService;
	}
	
	public void setBlogInfoService(BlogInfoServiceInter blogInfoService) {
		this.blogInfoService = blogInfoService;
	}

	public void setArticleService(ArticleServiceInter articleService) {
		this.articleService = articleService;
	}

	public ActionForward gotoUserUI(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// get blog username from query string
		String userName = request.getParameter("userName");
		User loginUserInfo = (User) request.getSession().getAttribute("loginUserInfo");
		if(prepareShowVisitInfo(request, userName, 1, 50)){
			return mapping.findForward("gotoUserUI");
		} else {
			return mapping.findForward("opererr");
		}
	}
	
	public ActionForward page(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String pageNowString = request.getParameter("pageNow");
		String userName = request.getParameter("userName");
		int pageNow = 1;
		try {
			pageNow = Integer.parseInt(pageNowString);
		} catch (Exception e) {
			if(prepareShowVisitInfo(request, userName, 1, 50)){
				return mapping.findForward("gotoUserUI");
			} else {
				return mapping.findForward("opererr");
			}
		}
		if(pageNow <= 0 || pageNow > articleService.getArticlePageCount(userService.getUserByUserName(userName), 50)) {
			if(prepareShowVisitInfo(request, userName, 1, 50)){
				return mapping.findForward("gotoUserUI");
			} else {
				return mapping.findForward("opererr");
			}
		} else {
			if(prepareShowVisitInfo(request, userName, pageNow, 50)){
				return mapping.findForward("gotoUserUI");
			} else {
				return mapping.findForward("opererr");
			}
		}
		
	}

	private boolean prepareShowVisitInfo(HttpServletRequest request,
			String userName, int pageNow, int pageSize) {
		// prepare user info
		User visitUserInfo = userService.getUserByUserName(userName);
		if(visitUserInfo == null) {
			return false;
		}
		// test get user info (ok)
		// System.out.println(visitUserInfo.getUserName() + " " + visitUserInfo.getPassword());
		request.setAttribute("visitUserInfo", visitUserInfo);
		
		// prepare blog info
		Bloginfo visitBlogInfo = blogInfoService.getBlogInfoByUser(visitUserInfo);
		// test get blog info (ok)
		// System.out.println(visitBlogInfo.getBlogtitle() + " " + visitBlogInfo.getIdiograph());
		request.setAttribute("visitBlogInfo", visitBlogInfo);
			
		// prepare article count
		Integer visitArticleCount = articleService.getArticleCountByUser(visitUserInfo);
		// test get article count  (ok)
		// System.out.println(visitArticleCount);
		request.setAttribute("visitArticleCount", visitArticleCount);
		
		// prepare article click total count
		Integer visitClickCount = articleService.getClickTotalCountByUser(visitUserInfo);
		// test get article click total count  (ok)
		// System.out.println(visitClickCount);
		request.setAttribute("visitClickCount", visitClickCount);
		
		// prepare article critique total count
		Integer visitCritiqueCount = articleService.getCritiqueTotalCountByUser(visitUserInfo);
		// test get article critique total count  (ok)
		// System.out.println(visitCritiqueCount);
		request.setAttribute("visitCritiqueCount", visitCritiqueCount);
		
		// prepare article info
		List<Article> visitArticleInfo = articleService.getArticlesByUserByPageOrderByTime(visitUserInfo, pageNow, pageSize);
		request.setAttribute("visitArticleInfo", visitArticleInfo);
		
		// prepare article page count
		Integer pageCount = articleService.getArticlePageCount(visitUserInfo, pageSize);
		request.setAttribute("pageCount", pageCount);
		
		// prepare article page now
		request.setAttribute("pageNow", pageNow);
		
		return true;
	}
}