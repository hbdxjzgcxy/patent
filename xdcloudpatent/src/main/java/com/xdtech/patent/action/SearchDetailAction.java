package com.xdtech.patent.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.xdtech.patent.conf.AppConf;
import com.xdtech.patent.entity.User;
import com.xdtech.patent.service.UserService;
import com.xdtech.search.client.LOGIC;
import com.xdtech.search.client.QueryObjectBuilder;
import com.xdtech.search.client.ws.BooleanQuery;
import com.xdtech.search.client.ws.SearchResult;
import com.xdtech.util.ResourceUtil;

/**
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("detail")
@SessionAttributes("user")
public class SearchDetailAction extends AbstractBaseSeachAction {
	
	@Autowired(required=true)
	UserService service = null;

	/**
	 * 分栏式-著录项目
	 * @param id
	 * @return
	 */
	@RequestMapping("/detail")
	public ModelAndView detail(@ModelAttribute("params") SearchForm model, @RequestParam(value = "docNo") String docNo,
			@RequestParam(value = "hlKwd") String hlKwd) {
		ModelAndView mv = new ModelAndView();
		docNo = getDocNo(currentUser.getId(), docNo);
		model.setKwd(docNo);
		mv = doSearch(model);
		mv.setViewName("search_detail");
		mv.addObject("docNo", docNo).addObject("hlKwd", hlKwd);
		return mv;
	}

	@RequestMapping("/info")
	public ModelAndView detail(@ModelAttribute("params") SearchForm model, @RequestParam(value = "docNo") String docNo,
			@RequestParam(value = "hlKwd") String hlWkd, @RequestParam(value = "type") String type) {
		ModelAndView mv = new ModelAndView();
		model.setKwd(getDocNo(currentUser.getId(), docNo));
		model.setHlKwd(hlWkd);
		mv = doSearch(model);
		mv.setViewName("result_col_" + type);
		return mv;
	}

	@Override
	public void setupQuery(SearchForm model, ModelAndView mv, QueryObjectBuilder builder) {
		BooleanQuery bq = newBooleanQ(newQueryClause(newTermQuery("docNo", model.getKwd()), LOGIC.AND));
		builder.setQuery(bq);
		builder.addCore(CORE_NAME);
		builder.setHightlightWord(model.getHlKwd());
	}

	@Override
	protected void procSearchResult(SearchForm model, ModelAndView mv, SearchResult result) {
		//处理通结果
		if (model.getFacet() == 1)
			proc(result.getFacets(), mv);

		/*
		 * 转换检索结果集
		 */
		List<SearchBean> list = toResultBeanList(result.getDocuments(), SearchBean.class, true);
		if (!CollectionUtils.isEmpty(list)) {
			mv.addObject("doc", list.get(0));
		}
	}

	@RequestMapping("/down")
	public ModelAndView downPdf(@RequestParam String pn, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("/404_pdf");
		String filename = "模板.pdf";
		if (!StringUtils.isEmpty(pn)) {
			filename = pn+".PDF";
			response.reset();
			response.setCharacterEncoding("utf-8");
			response.setContentType("applicaton/pdf");
			InputStream is = null;
			OutputStream os = null;
			String identity = getCurrentUser().getUsername();
			if(getCurrentUser().getPid()!=null) {
				User parent =service.findById(User.class	, getCurrentUser().getPid());
				if(parent!=null) {
					identity = parent.getUsername();
				}
			}
			String path = ResourceUtil.getPatentPdf(identity, pn);
			File file = new File(path);
			if(file.exists()){
				System.out.println(file.getAbsolutePath());
				try {
					
					String userAgent = request.getHeader("User-Agent");
					String name = new String(filename.getBytes(), "ISO8859-1");
					// 针对IE或者以IE为内核的浏览器：
					if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
						name = java.net.URLEncoder.encode(filename, "UTF-8");
					}
					
					response.setHeader("Content-Disposition", "inline;fileName=" + name);
					is = new FileInputStream(file);
					os = response.getOutputStream();
					byte[] b = new byte[2048];
					int length;
					while ((length = is.read(b)) > 0) {
						os.write(b, 0, length);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					mv.addObject("msg", "没有找到与该专利对应的PDF文件!");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
					mv.addObject("msg", "文件名称解析错误:不能识别的文件名!");
				} catch (IOException e) {
					e.printStackTrace();
					mv.addObject("msg", "文件都去错误!");
				} finally {
					try {
						os.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				}
			}else{
				//孟州项目特殊处理，到指定地址下载pdf
				//pn=US20030231277A1   
				//http://pdf.lindenpat.com/LindenpatTempSite/XOS/LindenpatTemp/DownPdfNetworkAction.do?_method=DownPDF&arg0=US-20030231277-A1
				if(pn.contains("/")){
					pn = pn.split("/")[1];
				}
				StringBuilder sb = new StringBuilder();
				sb.append(pn.substring(0,2));
				sb.append("-");
				String lastStr = pn.substring(pn.length()-1, pn.length());
				String reg="^\\d+$";
				boolean result = lastStr.matches(reg);
				if(result){
					sb.append(pn.substring(2,pn.length()-2));
					sb.append("-");
					sb.append(pn.substring(pn.length()-2));
				}else{
					sb.append(pn.substring(2,pn.length()-1));
					sb.append("-");
					sb.append(pn.substring(pn.length()-1));
				}
				String pdfDownloadUrl = AppConf.get().get("pdf.download.url");
				String url = pdfDownloadUrl+"_method=DownPDF&arg0="+sb.toString();
				getPdf(sb.toString(), url, response,os);
				//mv.addObject("msg", "没有找到与该专利对应的PDF文件!");
			}
		}
		return mv;
	}
	/**
	 * 设置检索结果要返回的字段
	 * @param builder
	 */
	protected void setupReturnField(final QueryObjectBuilder builder) {
		builder.addReturnField("TI", true, hl(0)).addReturnField("AB", true, hl(0)).addReturnField("LS", false, hl(0)).addReturnField("PC",false,0)
				.addReturnField("AN", false, hl(0)).addReturnField("PA", true, hl(0)).addReturnField("AD", false, 0).addReturnField("PN", false, 0)
				.addReturnField("PD", false, 0).addReturnField("ADDR", false, 0).addReturnField("IPC", true, 0).addReturnField("AGT", false, 0)
				.addReturnField("AGC", false, 0).addReturnField("AU", false, 0).addReturnField("LS", false, 0).addReturnField("CLM", false, 0).addReturnField("FT", false, 0)
				.addReturnField("PFN", false, 0).addReturnField("LSN", false, 0).addReturnField("LSE", false, 0).addReturnField("PR", true, 0)
				.addReturnField("EXT_DISPLAY_1", false, 0).addReturnField("EXT_DISPLAY_2", false, 0).addReturnField("EXT_DISPLAY_3", false, 0)
				.addReturnField("PFN", false, 0).addReturnField("CDN", false, 0).addReturnField("CTN", false, 0).addReturnField("CPC", false, 0).addReturnField("PRC",false,0)
				.addReturnField("PRD",false,0).addReturnField("PRN",false,0);
		
	}

	private void getPdf(String pn,String filePath,HttpServletResponse response,OutputStream os){
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());  
		String file_path = AppConf.get().get("temp.dir","E:\temp");
		File savePath = new File(file_path+"/"+date);
		if (!savePath.exists()) {
			savePath.mkdir();   
		} 
		String uname =pn+".PDF";
		OutputStream oputstream =null;
		InputStream iputstream =null;
		 InputStream inStream =null;
		try {  
				File file = new File(savePath+"/"+uname);
				if(file!=null && !file.exists()){  
					file.createNewFile();  
				}  
				oputstream = new FileOutputStream(file);  
				URL url = new URL(filePath);  
				HttpURLConnection uc = (HttpURLConnection) url.openConnection();  
				uc.setDoInput(true); 
				uc.connect();  
				iputstream = uc.getInputStream();  
				byte[] buffer = new byte[4*1024];
				int byteRead = -1;     
				while((byteRead=(iputstream.read(buffer)))!= -1){  
					oputstream.write(buffer, 0, byteRead);  
				}  
				oputstream.flush();    
				/*下载到本地了*/
				
				inStream = new FileInputStream(file);
				response.addHeader("Content-Disposition", "attachment;filename=" + new String(uname.getBytes()));
				response.addHeader("Content-Length", "" + file.length());
				os = response.getOutputStream();
				byte[] b = new byte[2048];
				int length;
				while ((length = inStream.read(b)) > 0) {
					os.write(b, 0, length);
				}
				file.delete();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(iputstream !=null)iputstream.close();  
				if(oputstream !=null)oputstream.close();
				if(os !=null)os.close();
				if(inStream !=null)inStream.close();
	            savePath.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}
		}
}
