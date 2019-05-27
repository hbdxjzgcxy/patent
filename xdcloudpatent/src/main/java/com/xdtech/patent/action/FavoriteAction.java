package com.xdtech.patent.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xdtech.patent.entity.UserFavoritor;
import com.xdtech.patent.service.UserService;
import com.xdtech.search.client.LOGIC;
import com.xdtech.search.client.QueryObjectBuilder;
import com.xdtech.search.client.ws.BooleanQuery;

@Controller
@RequestMapping("/favorite")
public class FavoriteAction extends AbstractBaseSeachAction{
	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping("/tofavorites")  
    public ModelAndView toFavorites(@RequestParam(value = "docNo") String docNo,@RequestParam(value="ti") String ti){
		List<String> favorites = userService.favoritesGroup(currentUser.getId());
		return new ModelAndView("/favorites").addObject("docNo", docNo).addObject("favorites",favorites).addObject("ti", ti);
    }
	
	@RequestMapping("/favorites")  
    public @ResponseBody String favorites(@RequestParam(value = "docNo") String docNo,@RequestParam(value = "folder") String folder){
		if(StringUtils.indexOf(docNo, ",") != -1){
			for(String no:docNo.split(",")){
				UserFavoritor favorites = new UserFavoritor();
				favorites.setCore(CORE_NAME);
				favorites.setUser(currentUser.getId());
				favorites.setDocNo(no);
				favorites.setFolder(folder);
				favorites.setDate(new Date());
				userService.save(favorites);
			}
		}else{
			UserFavoritor favorites = new UserFavoritor();
			favorites.setCore(CORE_NAME);
			favorites.setUser(currentUser.getId());
			favorites.setDocNo(docNo);
			favorites.setFolder(folder);
			favorites.setDate(new Date());
			userService.save(favorites);
		}
		return "true";
    }
	
	@RequestMapping("/delfavorites")  
    public @ResponseBody String delFavorites(@RequestParam(value = "id") String id){
		if(!StringUtils.isEmpty(id)){
			String folder = id;
			List<UserFavoritor> tmp = userService.favorites(currentUser.getId(),folder);
			if(!CollectionUtils.isEmpty(tmp)){
				userService.deleteAll(tmp);
			}
		}
		return "true";
    }
	
	/**
	 * 收藏夹
	 * @param req
	 * @return
	 */
	@RequestMapping("/userfavorites")
	public ModelAndView favorites(){
		Map<String, List<UserFavoritor>> favorites = userService.favorites(currentUser.getId());
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("favorites", favorites);
		params.put("sel", "favorites");
		params.put("cur","admin_center" );
		return mv("user/user_favorites",params);
	}
	
	/**
	 * 检索
	 * @param req
	 * @return
	 */
	@RequestMapping("fav_search")
	public ModelAndView favsearch(@ModelAttribute("params") SearchForm model,@RequestParam(value = "folder") String folder,HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		if(!StringUtils.isEmpty(folder)){
			model.setKwd(folder);
			List<UserFavoritor> list = userService.favorites(currentUser.getId(), folder);
			StringBuilder sb = new StringBuilder();
			for(UserFavoritor f:list){
				sb.append(f.getDocNo()).append(",");
			}
			model.setKwd(sb.toString());
			model.setStyle("list");
			mv = doSearch(model);
			model.setKwd("");
			mv.addObject("cur", "admin_center");
			mv.addObject("sel", "favorites");
			mv.setViewName("search/result/favlist");
			return mv;
		}
		return null;
	}
	
	private ModelAndView mv(String url,Map<String,Object> params){
		ModelAndView mv = new ModelAndView(url);
		for(Object obj:params.keySet()){
			mv.addObject(""+obj+"",params.get(obj));
		}
		return mv;
	}

	@Override
	public void setupQuery(SearchForm model, ModelAndView mv, QueryObjectBuilder builder) {
		BooleanQuery bq = new BooleanQuery();
		for(String no:model.getKwd().split(",")){
			bq.getClasuses().add(newQueryClause(newTermQuery("docNo", getDocNo(currentUser.getId(), no)), LOGIC.OR));
		}
		builder.setQuery(bq);
		builder.addCore(CORE_NAME);
		builder.setHightlightWord(model.getHlKwd());
	}
	
	
	@Override
	protected void setupPage(SearchForm model, QueryObjectBuilder builder) {
		builder.setPage(0, 100);
	}
	

	

}
