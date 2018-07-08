/*


 * Created on 07.Nis.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package iwb.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import iwb.adapter.ui.ViewAdapter;
import iwb.adapter.ui.ViewMobileAdapter2;
import iwb.adapter.ui.extjs.ExtJs3_3;
import iwb.adapter.ui.f7.F7;
import iwb.adapter.ui.react.React16;
import iwb.adapter.ui.vue.Vue2;
import iwb.adapter.ui.webix.Webix3_3;
import iwb.domain.db.Log5UserAction;
import iwb.domain.db.W5BIGraphDashboard;
import iwb.domain.db.W5Customization;
import iwb.domain.db.W5FileAttachment;
import iwb.domain.db.W5LookUpDetay;
import iwb.domain.db.W5Notification;
import iwb.domain.db.W5Query;
import iwb.domain.db.W5SmsValidCode;
import iwb.domain.helper.W5FormCellHelper;
import iwb.domain.helper.W5QueuedDbFuncHelper;
import iwb.domain.helper.W5QueuedPushMessageHelper;
import iwb.domain.helper.W5ReportCellHelper;
import iwb.domain.result.M5ListResult;
import iwb.domain.result.W5DbFuncResult;
import iwb.domain.result.W5FormResult;
import iwb.domain.result.W5QueryResult;
import iwb.domain.result.W5TableRecordInfoResult;
import iwb.domain.result.W5TemplateResult;
import iwb.domain.result.W5TutorialResult;
import iwb.engine.FrameworkEngine;
import iwb.exception.IWBException;
import iwb.report.RptExcelRenderer;
import iwb.report.RptPdfRenderer;
import iwb.util.FrameworkCache;
import iwb.util.FrameworkSetting;
import iwb.util.GenericUtil;
import iwb.util.HttpUtil;
import iwb.util.JasperUtil;
import iwb.util.LocaleMsgCache;
import iwb.util.UserUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;

@Controller
@RequestMapping("/app")
public class AppServlet implements InitializingBean {
	private static Logger logger = Logger.getLogger(AppServlet.class);

	@Autowired
	private FrameworkEngine engine;

	@Autowired
	private TaskExecutor taskExecutor;

	private ViewAdapter ext3_4;
	private	ViewAdapter	webix3_3;
	private	ViewAdapter	react16;
	private	ViewAdapter	vue2;
	private ViewMobileAdapter2 f7;
	private static String manPicPath = null;
	private static String womanPicPath = null;
	private static String brokenPicPath = null;

	@Override
	public void afterPropertiesSet() throws Exception {
		ext3_4 = new ExtJs3_3();
		webix3_3 = new Webix3_3();
		f7 = new F7();
		react16 = new React16();
		vue2 = new Vue2();
	//	FrameworkCache.activeProjectsStr = "067e6162-3b6f-4ae2-a221-2470b63dff00,29a3d378-3c59-4b5c-8f60-5334e3729959";
		engine.reloadCache(-1);
		// if(PromisSetting.checkLicenseFlag)engine.checkLicences();
		// dao.organizeAudit();
		engine.setJVMProperties(0);
		manPicPath = new ClassPathResource("static/ext3.4.1/custom/images/man-64.png").getFile().getPath();
		brokenPicPath = new ClassPathResource("static/ext3.4.1/custom/images/broken-64.png").getFile().getPath();
		womanPicPath = new ClassPathResource("static/images/custom/ppicture/default_woman_mini.png").getFile().getPath();

		//if(FrameworkSetting.mq)UserUtil.activateMQs();
	}
        
	private ViewAdapter getViewAdapter(Map<String, Object> scd, HttpServletRequest request, ViewAdapter defaultRenderer){
		if(GenericUtil.uInt(scd.get("mobile"))!=0)return ext3_4;
		if(request!=null){
			String renderer = request.getParameter("_renderer");
			if(renderer!=null && renderer.equals("ext3_4"))return ext3_4;
			if(renderer!=null && renderer.startsWith("webix"))return webix3_3;
			if(renderer!=null && renderer.equals("react16"))return react16;
			if(renderer!=null && renderer.equals("vue2"))return vue2;
		}
		if(scd!=null){
			String renderer = (String)scd.get("_renderer");
			if(renderer!=null && renderer.equals("ext3_4"))return ext3_4;
			if(renderer!=null && renderer.startsWith("webix"))return webix3_3;			
			if(renderer!=null && renderer.equals("react16"))return react16;
			if(renderer!=null && renderer.equals("vue2"))return vue2;
		}
		return defaultRenderer;
	}
	
	private ViewAdapter getViewAdapter(Map<String, Object> scd, HttpServletRequest request){
		return getViewAdapter(scd, request, ext3_4);
	}
	
	/*
	 * @RequestMapping("/xxx") public void hndXXX(HttpServletRequest request,
	 * HttpServletResponse response) throws ServletException, IOException {
	 * Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true); Map<String,
	 * Object> x = engine.backUpDatabaseDP(scd); if(x != null){ // Komut
	 * çalıştırılıyor final Process process = Runtime.getRuntime().exec(
	 * "cmd /C start /wait "
	 * +x.get("backupPath").toString()+File.separator+"command.bat"); try {
	 * process.waitFor(); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * // Bu işlem de bitti şimdi dosyalar taşınsın for (final File file :
	 * ((File) x.get("tmpFolder")).listFiles()) { if (!file.isDirectory()) {
	 * for(W5BackupSetting s : (List<W5BackupSetting>) x.get("settings")){
	 * if((s.getFilePrefix()+".DMP").compareTo(file.getName()) == 0){
	 * if(s.getFolder() != null){ Files.move(Paths.get(file.getAbsolutePath()),
	 * Paths.get(x.get("backupPath").toString()+File.separator+s.getFolder().
	 * replace("{date}",
	 * x.get("today").toString())+File.separator+file.getName()),
	 * StandardCopyOption.REPLACE_EXISTING); }else{
	 * Files.move(Paths.get(file.getAbsolutePath()),
	 * Paths.get(x.get("backupPath").toString()+File.separator+file.getName()),
	 * StandardCopyOption.REPLACE_EXISTING); } } } } } } }
	 */
	
	@RequestMapping("/ajaxChangeActiveProject")
	public void hndAjaxChangeActiveProject(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxChangeActiveProject"); 
	    Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
	    String uuid= request.getParameter("_uuid");
	    boolean b = engine.changeActiveProject(scd, uuid);
		response.getWriter().write("{\"success\":"+b+"}");
		response.getWriter().close();		
	}
	
	
	@RequestMapping("/ajaxDebugSyncData")
	public void hndAjaxDebugSyncData(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxDebugSyncData");
		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
		response.setContentType("application/json");
		int customizationId = (Integer) scd.get("customizationId");
		Map m = null;
		switch (GenericUtil.uInt(request, "t")) {
		case 0:
			m = UserUtil.getRecordEditMapInfo(customizationId);
			break;
		case 1:
			m = UserUtil.getUserMapInfo(customizationId);
			break;
		case 2:
			m = UserUtil.getGridSyncMapInfo(customizationId);
			break;

		}
		response.getWriter().write(GenericUtil.fromMapToJsonString2Recursive(m));
		response.getWriter().close();
	}

	@RequestMapping("/ajaxForgotPass")
	public void hndAjaxForgotPass(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Map<String, String> requestParams = GenericUtil.getParameterMap(request);
		requestParams.put("_remote_ip", request.getRemoteAddr());
		if (request.getSession(false) != null && request.getSession(false).getAttribute("securityWordId") != null)
			requestParams.put("securityWordId", request.getSession(false).getAttribute("securityWordId").toString());

		if (request.getSession(false) != null) {
			request.getSession(false).removeAttribute("scd-dev");
		}
		;

		Locale blocale = request.getLocale();
		Map m = new HashMap();
		m.put("customizationId", 0);
		String xlocale = GenericUtil.uStrNvl(request.getParameter("locale"),
				getDefaultLanguage(m, blocale.getLanguage()));
		m.put("locale", xlocale);

		response.setContentType("application/json");
		Map<String, String> res = engine.sendMailForgotPassword(m, requestParams);
		StringBuilder b = new StringBuilder();
		b.append("{\"success\": " + (res.get("success").equals("1") == true ? "true" : "false") + ",\n").append(
				"\"msg\":\"" + (res.get("msg") != null ? LocaleMsgCache.get2(0, xlocale, res.get("msg")) : "") + "\"}");
		response.getWriter().write(b.toString());

		response.getWriter().close();
	}

	@RequestMapping("/ajaxFormCellCode")
	public void hndAjaxFormCellCode(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int formCellId = GenericUtil.uInt(request, "_formCellId");
		logger.info("hndAjaxFormCellCode(" + formCellId + ")");
		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
		Map m = engine.getFormCellCode(scd, GenericUtil.getParameterMap(request), formCellId, 1);
		// m.put("success", true);
		response.setContentType("application/json");
		response.getWriter().write(GenericUtil.fromMapToJsonString2(m));
		response.getWriter().close();
	}

	@RequestMapping("/ajaxChangeChatStatus")
	public void hndAjaxChangeChatStatus(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxChangeChatStatus");
		response.setContentType("application/json");
		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
		int chatStatusTip = GenericUtil.uInt(request, "chatStatusTip");
		response.getWriter().write("{\"success\":" + UserUtil.updateChatStatus(scd, chatStatusTip) + "}");
		response.getWriter().close();
	}

	@RequestMapping("/ajaxGetTableRelationData")
	public void hndAjaxGetTableRelationData(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxGetTableRelationData");
		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
		int tableId = GenericUtil.uInt(request, "_tb_id");
		int tablePk = GenericUtil.uInt(request, "_tb_pk");
		int relId = GenericUtil.uInt(request, "_rel_id");

		response.setContentType("application/json");
		response.getWriter()
				.write(getViewAdapter(scd, request).serializeQueryData(engine.getTableRelationData(scd, tableId, tablePk, relId)).toString());
		response.getWriter().close();
	}
	@RequestMapping("/ajaxQueryData4Stat")
	public void hndAjaxQueryData4Stat(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int gridId = GenericUtil.uInt(request, "_gid");
		if(gridId==0)gridId = -GenericUtil.uInt(request, "_qid");
		logger.info("hndAjaxQueryData4Stat(" + gridId + ")");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		response.setContentType("application/json");
		Map m = engine.executeQuery4Stat(scd, gridId, GenericUtil.getParameterMap(request));
		response.getWriter().write(GenericUtil.fromMapToJsonString2Recursive(m));
		response.getWriter().close();
	}
	@RequestMapping("/ajaxQueryData4StatTree")
	public void hndAjaxQueryData4StatTree(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int gridId = GenericUtil.uInt(request, "_gid");
		logger.info("hndAjaxQueryData4StatTree(" + gridId + ")");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		response.setContentType("application/json");
		Map m = engine.executeQuery4StatTree(scd, gridId, GenericUtil.getParameterMap(request));
		response.getWriter().write(GenericUtil.fromMapToJsonString2Recursive(m));
		response.getWriter().close();
	}
	
	@RequestMapping("/ajaxQueryData")
	public void hndAjaxQueryData(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int queryId = GenericUtil.uInt(request, "_qid");
//		JSONObject jo = null;
		Map<String,String> requestMap = GenericUtil.getParameterMap(request);
/*		if(GenericUtil.safeEquals(request.getContentType(),"application/json")){
			JSONObject jo = HttpUtil.getJson(request);
			if(jo.has("_qid"))queryId = jo.getInt("_qid");
			requestMap.putAll(GenericUtil.fromJSONObjectToMap(jo));
		} */
		logger.info("hndAjaxQueryData(" + queryId + ")");
		Map<String, Object> scd = null;
		HttpSession session = request.getSession(false);
		if ((queryId == 1 || queryId == 824) && (session == null || session.getAttribute("scd-dev") == null
				|| ((HashMap<String, String>) session.getAttribute("scd-dev")).size() == 0)) { // select
																							// role
			if (session == null) {
				response.getWriter().write("{\"success\":false,\"error\":\"no_session\"}");
				return;
			}
			scd = new HashMap<String, Object>();
			scd.put("locale", session.getAttribute("locale"));
			scd.put("userId", session.getAttribute("userId"));
			if (GenericUtil.uInt(session.getAttribute("mobile"))!=0)
				scd.put("mobile", session.getAttribute("mobile"));
			scd.put("customizationId", session.getAttribute("customizationId"));
		} else {
			if (queryId == 142) { // online users
				scd = UserUtil.getScd(request, "scd-dev", false);
				W5QueryResult qr = new W5QueryResult(142);
				W5Query q = new W5Query();
				q.setQueryTip((short) 0);
				qr.setQuery(q);
				qr.setScd(scd);
				qr.setErrorMap(new HashMap());
				qr.setNewQueryFields(FrameworkCache.cachedOnlineQueryFields);
				List<Object[]> lou = UserUtil.listOnlineUsers(scd);
				if (FrameworkSetting.chatShowAllUsers) {
					Map<Integer, Object[]> slou = new HashMap();
					slou.put((Integer) scd.get("userId"), new Object[] { scd.get("userId") });
					for (Object[] o : lou)
						slou.put(GenericUtil.uInt(o[0]), o);
					W5QueryResult allUsers = engine.executeQuery(scd, queryId, requestMap);
					for (Object[] o : allUsers.getData()) {
						String msg = (String) o[6];
						if (msg != null && msg.length() > 18) {
							o[3] = msg.substring(0, 19); // last_msg_date_time
							if (msg.length() > 19)
								o[6] = msg.substring(20);// msg
							else
								o[6] = null;
						} else {
							o[6] = null;
							o[3] = null;
						}

						int u = GenericUtil.uInt(o[0]);

						Object[] o2 = slou.get(u);
						if (o2 == null)
							lou.add(o);
						else if (u != (Integer) scd.get("userId")) {
							if (o2.length > 3)
								o2[3] = o[3];
							if (o2.length > 6)
								o2[6] = o[6];
							if (o2.length > 7)
								o2[7] = o[7];
						}
					}
				}
				qr.setData(lou);
				response.setContentType("application/json");
				response.getWriter().write(getViewAdapter(scd, request).serializeQueryData(qr).toString());
				response.getWriter().close();
				return;
			} else
				scd = UserUtil.getScd(request, "scd-dev", true);// TODO not auto
		}

		ViewAdapter va = getViewAdapter(scd, request);
		if(va instanceof Webix3_3){
			for(String s:requestMap.keySet())if(s.startsWith("sort[") && s.endsWith("]")){
				requestMap.put("sort", s.substring(5,  s.length()-1));
				requestMap.put("dir",requestMap.get(s));
				break;
			}
			
		}
		W5QueryResult queryResult = engine.executeQuery(scd, queryId, requestMap);

		response.setContentType("application/json");
		response.getWriter().write(va.serializeQueryData(queryResult).toString());
		response.getWriter().close();

	}


	@RequestMapping("/importUploadedData")
	public void hndImportUploadedData(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndImportUploadedData");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		response.setContentType("application/json");
		W5FormResult formResult = engine.importUploadedData(scd, GenericUtil.uInt(request, "_ui"),
				GenericUtil.getParameterMap(request));
		response.getWriter().write(getViewAdapter(scd, request).serializePostForm(formResult).toString());
		response.getWriter().close();

	}
	
	@RequestMapping("/ajaxApproveRecord")
	public void hndAjaxApproveRecord(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxApproveRecord");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
		if (FrameworkCache.getAppSettingIntValue(scd, "approval_flag") == 0) {
			response.setContentType("application/json");
			response.getWriter().write("{\"success\":false}");
			return;
		}

		String[] app_rec_ids = request.getParameterValues("_arids");
		Map<String, Object> b = null;
		int approvalAction = GenericUtil.uInt(request, "_aa"); // aprovalAction
		Map<String, String> parameterMap = GenericUtil.getParameterMap(request);

		if (app_rec_ids == null) {
			int approvalRecordId = GenericUtil.uInt(request, "_arid");
			b = engine.approveRecord(scd, approvalRecordId, approvalAction, parameterMap);
		} else {
			String[] version_ids = request.getParameterValues("_avnos");
			for (int i = 0; i < app_rec_ids.length; i++) {
				int approvalRecordId = GenericUtil.uInt(app_rec_ids[i]);
				parameterMap.put("_avno", "" + version_ids[i]);
				parameterMap.put("_arid", "" + approvalRecordId); // dbfunc
																	// varsa
																	// parametre
																	// olarak
																	// kullanılıyor
				b = engine.approveRecord(scd, approvalRecordId, approvalAction, parameterMap);
			}
		}

		response.setContentType("application/json");
		response.getWriter().write("{\"success\":\"" + b.get("status") + "\"");
		if (b.get("fileHash") != null)
			response.getWriter()
					.write(",\"fileHash\":\"" + b.get("fileHash") + "\",\"fileId\":\"" + b.get("fileId") + "\"");
		response.getWriter().write("}");
		response.getWriter().close();
	}

	@RequestMapping("/ajaxLiveSync")
	public void hndAjaxLiveSync(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("ajaxLiveSync");
		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", false);
		response.setContentType("application/json");
		response.getWriter().write("{\"success\":" + FrameworkSetting.liveSyncRecord + "}");
		response.getWriter().close();

		UserUtil.liveSyncAction(scd, GenericUtil.getParameterMap(request));
	}

	@RequestMapping("/ajaxGetTabNotifications")
	public void hndAjaxGetTabNotifications(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxGetTabNotifications");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
		String webPageId = request.getParameter(".w");
		String tabId = request.getParameter(".t");
		int userId = (Integer) scd.get("userId");
		int customizationId = (Integer) scd.get("customizationId");
		String s = GenericUtil.fromMapToJsonString2Recursive(UserUtil.syncGetTabNotifications(customizationId, userId,
				(String) scd.get("sessionId"), webPageId, tabId));
		response.setContentType("application/json");
		response.getWriter().write(s);
		response.getWriter().close();

	}

	@RequestMapping("/ajaxSelectUserRole")
	public void hndAjaxSelectUserRole(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxSelectUserRole");
		HttpSession session = request.getSession(false);
		response.setContentType("application/json");
		int deviceType = GenericUtil.uInt(request.getParameter("_mobile")); //0.web, 1.iphone, 2.android, 3. mobile-web
		if (session == null || (session.getAttribute("userId") == null && session.getAttribute("scd-dev") == null)
				|| (session.getAttribute("scd-dev") == null && !"selectRole".equals(session.getAttribute("waitFor")))) { // sorun
																														// var
			response.getWriter().write("{\"success\":false}"); // tekrar ana login  sayfasina gidecek
			if (session != null)
				session.removeAttribute("scd-dev");
		} else {
			int userId = GenericUtil.uInt(session.getAttribute("scd-dev") == null ? session.getAttribute("userId")
					: ((Map) session.getAttribute("scd-dev")).get("userId"));
			int customizationId = GenericUtil.uInt(session.getAttribute("scd-dev") == null ? session.getAttribute("customizationId")
							: ((Map) session.getAttribute("scd-dev")).get("customizationId"));
			Map<String, Object> oldScd = (Map<String, Object>)session.getAttribute("scd-dev"); 
			Map<String, Object> scd = engine.userRoleSelect(userId, GenericUtil.uInt(request, "userRoleId"),
					GenericUtil.uInt(request, "userCustomizationId"), customizationId, request.getParameter("projectId"), deviceType != 0 ? request.getParameter("_mobile_device_id") : null);
			if (scd == null) {
				response.getWriter().write("{\"success\":false}"); // bir hata
																	// var
				session.removeAttribute("scd-dev");
			} else {
				scd.put("locale", oldScd == null ? session.getAttribute("locale"): oldScd.get("locale"));
				UserUtil.removeUserSession((Integer) scd.get("customizationId"), (Integer) scd.get("userId"), session.getId());
				session.removeAttribute("scd-dev");
				if (FrameworkCache.getAppSettingIntValue(0, "interactive_tutorial_flag") != 0) {
					String ws = (String) scd.get("widgetIds");
					if (ws == null)
						scd.put("widgetIds", "10");
					else if (!GenericUtil.hasPartInside(ws, "10"))
						scd.put("widgetIds", ws + ",10");
				}
				session = request.getSession(true);
				scd.put("sessionId", session.getId());
				if(deviceType!=0){
					scd.put("mobile", deviceType);
					scd.put("mobileDeviceId", request.getParameter("_mobile_device_id"));
				}
				
				if(GenericUtil.uInt(scd.get("renderer"))>1)scd.put("_renderer",new Object[]{0,0,"webix3_3","open1_4","webix4_2","react16","vue2"}[GenericUtil.uInt(scd.get("renderer"))]);
				session.setAttribute("scd-dev", scd);
				UserUtil.onlineUserLogin(scd, request.getRemoteAddr(), session.getId(), (short) deviceType, deviceType != 0 ? request.getParameter("_mobile_device_id") : request.getParameter(".w"));
				response.getWriter().write("{\"success\":true"); // hersey duzgun
				if(GenericUtil.uInt(request, "c")!=0){
					response.getWriter().write(",\"newMsgCnt\":"+ GenericUtil.fromMapToJsonString2Recursive(engine.getUserNotReadChatMap(scd)));
				}
				if(GenericUtil.uInt(request, "d")!=0){
					response.getWriter().write(",\"session\":"+ GenericUtil.fromMapToJsonString2Recursive(scd));
				}
				response.getWriter().write("}");
			}
		}
		response.getWriter().close();

	}

	@RequestMapping("/ajaxChangePassword")
	public void hndAjaxChangePassword(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxChangePassword");
		HttpSession session = request.getSession(false);
		Map<String, Object> scd = null;
		response.setContentType("application/json");
		if (session != null && session.getAttribute("scd-dev") == null && session.getAttribute("userId") != null) {
			scd = new HashMap<String, Object>();
			if (!"expirePassword".equals(session.getAttribute("waitFor"))) {
				response.getWriter().write(
						"{\"success\":false,\"errrorMsg\":\"Waiting 4: " + session.getAttribute("waitFor") + "\"}");
				return;
			}
			scd.put("userId", session.getAttribute("userId"));
			scd.put("locale", session.getAttribute("locale"));
			scd.put("customizationId", session.getAttribute("customizationId"));
		} else
			scd = UserUtil.getScd(request, "scd-dev", true);
		Map<String, String> requestParams = GenericUtil.getParameterMap(request);
		requestParams.put("_remote_ip", request.getRemoteAddr());
		W5DbFuncResult result = engine.executeDbFunc(scd, 250, requestParams, (short) 4);
		boolean success = GenericUtil.uInt(result.getResultMap().get("success")) != 0;
		String errorMsg = result.getResultMap().get("errorMsg");
		if (!success)
			errorMsg = LocaleMsgCache.get2(0, GenericUtil.uStrNvl((String) scd.get("locale"),
					FrameworkCache.getAppSettingStringValue(0, "locale")), errorMsg);

		response.setContentType("application/json");
		if (success) { // basarili simdi sira diger islerde
			if (session.getAttribute("scd-dev") == null) {
				session.setAttribute("waitFor", "selectRole");
			}
			response.getWriter().write("{\"success\":true}");
		} else {
			response.getWriter().write("{\"success\":false,\"errrorMsg\":\"" + errorMsg + "\"}");
		}
		response.getWriter().close();
	}

	@RequestMapping("/ajaxSmsCodeValidation")
	public void hndAjaxSmsCodeValidation(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxSmsCodeValidation");
		Map<String, String> requestParams = GenericUtil.getParameterMap(request);
		HttpSession session = request.getSession(true);

		int userId = GenericUtil.uInt(session.getAttribute("userId"));
		int customizationId = GenericUtil.uInt(session.getAttribute("customizationId"));
		int deviceType = GenericUtil.uInt(session.getAttribute("_mobile"));

		requestParams.put("userId", userId + "");
		requestParams.put("_mobile", deviceType + "");
		requestParams.put("customizationId", customizationId + "");

		W5DbFuncResult result = engine.executeDbFunc(new HashMap(), 1077, requestParams, (short) 4); // user
																										// Authenticate
																										// SMS
																										// DbFunc:1077
		boolean success = GenericUtil.uInt(result.getResultMap().get("success")) != 0;
		String errorMsg = result.getResultMap().get("errorMsg");
		boolean expireFlag = GenericUtil.uInt(result.getResultMap().get("expireFlag")) != 0;
		int roleCount = GenericUtil.uInt(result.getResultMap().get("roleCount"));
		String xlocale = session.getAttribute("locale").toString();
		int forceUserRoleId = GenericUtil.uInt(GenericUtil.uInt(session.getAttribute("forceUserRoleId")));
		if (!success)
			errorMsg = LocaleMsgCache.get2(0, xlocale, errorMsg);
		response.setContentType("application/json");
		Map<String, Object> scd = null;
		if (success) {
			if (expireFlag) {
				session.setAttribute("userId", userId);
				session.setAttribute("waitFor", "expirePassword");
				response.getWriter()
						.write("{\"success\":true,\"expireFlag\":true,\"roleCount\":" + roleCount
								+ ",\"defaultUserCustomizationId\":"
								+ GenericUtil.uInt(result.getResultMap().get("defaultUserCustomizationId")));
			} else if (roleCount < 0 || forceUserRoleId != 0) { // simdi rolunu
																// sec ve login
																// ol
				if (forceUserRoleId == 0)
					forceUserRoleId = -roleCount;
				scd = engine.userRoleSelect(userId, forceUserRoleId,
						GenericUtil.uInt(result.getResultMap().get("defaultUserCustomizationId")),
						GenericUtil.uInt(requestParams.get("customizationId")), requestParams.get("projectId"), deviceType != 0 ? request.getParameter("_mobile_device_id") : null);
				if (scd == null) {
					if (FrameworkSetting.debug)
						logger.info("empty scd");
					response.getWriter().write("{\"success\":false"); // bir
																		// hata
																		// var
					session.removeAttribute("scd-dev");
				} else {
					scd.put("locale", session.getAttribute("locale"));
					session.removeAttribute("scd-dev");
					if (FrameworkCache.getAppSettingIntValue(0, "interactive_tutorial_flag") != 0) {
						String ws = (String) scd.get("widgetIds");
						if (ws == null)
							scd.put("widgetIds", "10");
						else if (!GenericUtil.hasPartInside(ws, "10"))
							scd.put("widgetIds", ws + ",10");
					}
					session = request.getSession(true);
					session.setAttribute("scd-dev", scd);
					if (deviceType != 0) {
						session.setMaxInactiveInterval(FrameworkCache.getAppSettingIntValue(0, "mobile_session_timeout", 1 * 60) * 60); // 1  saat default
						scd.put("mobileDeviceId", request.getParameter("_mobile_device_id"));
						scd.put("mobile", deviceType);
					}
					scd.put("sessionId", session.getId());

					UserUtil.onlineUserLogin(scd, request.getRemoteAddr(), session.getId(), (short) deviceType, deviceType != 0 ? request.getParameter("_mobile_device_id") : request.getParameter(".w"));
					response.getWriter()
							.write("{\"success\":true,\"session\":" + GenericUtil.fromMapToJsonString2(scd) + "}"); // hersey
																													// duzgun
				}
			} else {
				// o zaman once role'u sececek
				/*
				 * if
				 * (GenericUtil.userLoginControl(userId,request.getRemoteAddr(),
				 * request.getSession().getId(),GenericUtil.uInt(requestParams.
				 * get("customizationId")))==false){ response.getWriter().write(
				 * "{\"success\":true,\"loginUserUnique\":true}"); } else
				 */ {
					session.setAttribute("userId", userId);
					session.setAttribute("waitFor", "selectRole");
					response.getWriter()
							.write("{\"success\":true,\"roleCount\":" + roleCount + ",\"defaultUserCustomizationId\":"
									+ GenericUtil.uInt(result.getResultMap().get("defaultUserCustomizationId")) + "}");
					// GenericUtil.onlineUserLogin();
					/*
					 * List l=new ArrayList<Object>(); l.add((String)
					 * requestParams.get("userName")); l.add( new Date());
					 * l.add(request.getRemoteAddr());
					 * l.add(request.getSession().getId());
					 * GenericUtil.lastUserAction.put((String)
					 * requestParams.get("userName"),l);
					 */
				}
			}
		} else {
			response.getWriter().write("{\"success\":false,\"errorMsg\":\"" + errorMsg + "\"}");
		}
		response.getWriter().close();
	}

	@RequestMapping("/ajaxAuthenticateUser")
	public void hndAjaxAuthenticateUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxAuthenticateUser(" + request.getParameter("userName") + ")");

		// System.out.println(request.getParameter("userName")+" "+"
		// "+request.getParameter("passWord")+"
		// "+request.getParameter("locale"));

		Map<String, String> requestParams = GenericUtil.getParameterMap(request);
		requestParams.put("_remote_ip", request.getRemoteAddr());
	/*	if (request.getSession(false) != null && request.getSession(false).getAttribute("securityWordId") != null)
			requestParams.put("securityWordId", request.getSession(false).getAttribute("securityWordId").toString());
*/
		if (request.getSession(false) != null) {
			request.getSession(false).removeAttribute("scd-dev");
		}
		;
		W5DbFuncResult result = engine.executeDbFunc(new HashMap(), 1, requestParams, (short) 4); // user Authenticate DbFunc:1

		/*
		 * 4 success 5 errorMsg 6 userId 7 expireFlag 8 smsFlag 9 roleCount
		 */
		boolean success = GenericUtil.uInt(result.getResultMap().get("success")) != 0;
		String errorMsg = result.getResultMap().get("errorMsg");
		int userId = GenericUtil.uInt(result.getResultMap().get("userId"));
		boolean expireFlag = GenericUtil.uInt(result.getResultMap().get("expireFlag")) != 0;
		boolean smsFlag = GenericUtil.uInt(result.getResultMap().get("smsFlag")) != 0;
		int roleCount = GenericUtil.uInt(result.getResultMap().get("roleCount"));
		String xlocale = GenericUtil.uStrNvl(request.getParameter("locale"),
				FrameworkCache.getAppSettingStringValue(0, "locale"));
		int deviceType = GenericUtil.uInt(request.getParameter("_mobile"));
		if (!success)
			errorMsg = LocaleMsgCache.get2(0, xlocale, errorMsg);
		int forceUserRoleId = GenericUtil.uInt(requestParams.get("userRoleId"));
		response.setContentType("application/json");
		boolean genToken = GenericUtil.uInt(request, "generate_token") != 0;
		Map<String, Object> scd = null;
		if (success) { // basarili simdi sira diger islerde
			HttpSession session = request.getSession(true);
			session.setAttribute("locale", xlocale);
			if(deviceType!=0)session.setAttribute("mobile", deviceType);
			session.setAttribute("forceUserRoleId", forceUserRoleId);
			// session.setAttribute("customizationId",
			// GenericUtil.uInt(result.getResultMap().get("customizationId")));
			int customizationId = GenericUtil.uInt(result.getResultMap().get("customizationId"));
			session.setAttribute("customizationId", customizationId);
			if (smsFlag) {
				session.setAttribute("userId", userId);
				session.setAttribute("waitFor", "sms");
				W5SmsValidCode c = new W5SmsValidCode();
				c.setCustomizationId(customizationId);
				c.setUserId(userId);
				c.setSmsCode(GenericUtil.smsCodeGenerator(
						FrameworkCache.getAppSettingIntValue(customizationId,
								"sms_validation_code_type"),
						FrameworkCache.getAppSettingIntValue(customizationId,
								"sms_validation_code_length")));
				engine.saveObject(c);

				// SMS Gönderme İşlemi //
				HashMap<String, Object> user = engine.getUser(customizationId, userId);
				String messageBody = LocaleMsgCache.get2(customizationId, xlocale, "mobil_onay_kodu") + ": " + c.getSmsCode();

				engine.sendSms(customizationId, userId, user.get("gsm") + "",
						messageBody, 1197, c.getSmsValidCodeId());
				/////////////////////////

				response.getWriter()
						.write("{\"success\":true,\"smsFlag\":true,\"smsValidationId\":" + c.getSmsValidCodeId());
			} else if (expireFlag) {
				session.setAttribute("userId", userId);
				session.setAttribute("waitFor", "expirePassword");
				response.getWriter()
						.write("{\"success\":true,\"expireFlag\":true,\"roleCount\":" + roleCount
								+ ",\"defaultUserCustomizationId\":"
								+ GenericUtil.uInt(result.getResultMap().get("defaultUserCustomizationId")));
			} else if (roleCount < 0 || forceUserRoleId != 0) { // simdi rolunu
																// sec ve login
																// ol
				if (forceUserRoleId == 0)
					forceUserRoleId = -roleCount;
				scd = engine.userRoleSelect(userId, forceUserRoleId,
						GenericUtil.uInt(result.getResultMap().get("defaultUserCustomizationId")),
						customizationId, requestParams.get("projectId"), deviceType != 0 ? request.getParameter("_mobile_device_id") : null);
				if (scd == null) {
					if (FrameworkSetting.debug)
						logger.info("empty scd");
					response.getWriter().write("{\"success\":false"); // bir
																		// hata
																		// var
					session.removeAttribute("scd-dev");
				} else {
					scd.put("locale", session.getAttribute("locale"));
					session.removeAttribute("scd-dev");
					session = request.getSession(true);
					if (FrameworkCache.getAppSettingIntValue(0, "interactive_tutorial_flag") != 0) {
						String ws = (String) scd.get("widgetIds");
						if (ws == null)
							scd.put("widgetIds", "10");
						else if (!GenericUtil.hasPartInside(ws, "10"))
							scd.put("widgetIds", ws + ",10");
					}
					if(GenericUtil.uInt(scd.get("renderer"))>1)scd.put("_renderer",new Object[]{0,0,"webix3_3","open1_4","webix4_2","react16","vue2"}[GenericUtil.uInt(scd.get("renderer"))]);
					session.setAttribute("scd-dev", scd);
					if (deviceType != 0) {
						session.setMaxInactiveInterval(FrameworkCache.getAppSettingIntValue(0, "mobile_session_timeout", 1 * 60) * 60); // 1 saat default
						scd.put("mobileDeviceId", request.getParameter("_mobile_device_id"));
						scd.put("mobile", deviceType);
					}
					scd.put("sessionId", session.getId());
					if(request.getParameter("projectId")!=null)scd.put("projectId", request.getParameter("projectId"));

					UserUtil.onlineUserLogin(scd, request.getRemoteAddr(), session.getId(), (short) deviceType, deviceType != 0 ? request.getParameter("_mobile_device_id") : request.getParameter(".w"));
					response.getWriter().write("{\"success\":true,\"session\":" + GenericUtil.fromMapToJsonString2(scd)); // hersey duzgun
				}
			} else {
				// o zaman once role'u sececek
				/*
				 * if
				 * (GenericUtil.userLoginControl(userId,request.getRemoteAddr(),
				 * request.getSession().getId(),GenericUtil.uInt(requestParams.
				 * get("customizationId")))==false){ response.getWriter().write(
				 * "{\"success\":true,\"loginUserUnique\":true}"); } else
				 */ {
					session.setAttribute("userId", userId);
					session.setAttribute("waitFor", "selectRole");
					response.getWriter().write("{\"success\":true,\"roleCount\":" + roleCount + ",\"defaultUserCustomizationId\":"
									+ GenericUtil.uInt(result.getResultMap().get("defaultUserCustomizationId")));
					// GenericUtil.onlineUserLogin();
					/*
					 * List l=new ArrayList<Object>(); l.add((String)
					 * requestParams.get("userName")); l.add( new Date());
					 * l.add(request.getRemoteAddr());
					 * l.add(request.getSession().getId());
					 * GenericUtil.lastUserAction.put((String)
					 * requestParams.get("userName"),l);
					 */
				}
			}

			if(GenericUtil.uInt(request, "c")!=0){
				response.getWriter().write(",\"newMsgCnt\":"+ GenericUtil.fromMapToJsonString2Recursive(engine.getUserNotReadChatMap(scd)));
			}
			if (genToken && scd != null)
				response.getWriter().write(",\"promis_token\":\""
						+ UserUtil.generateTokenFromScd(scd, 0, request.getRemoteAddr(), 24 * 60 * 60 * 1000) + "\"");
			response.getWriter().write("}");
		} else {
			response.getWriter().write("{\"success\":false,\"errorMsg\":\"" + errorMsg + "\"}");
		}
		response.getWriter().close();
	}

	@RequestMapping("/reloadCache")
	public void hndReloadCache(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndReloadCache");
		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
		/*
		 * bus.reloadCache(GenericUtil.uInt(scd.get("customizationId")));
		 * response.getWriter().write("{\"success\":true}");
		 * response.getWriter().close();
		 */
		// bus.daoReloadJobsCache();
		response.setContentType("application/json");
		int roleId = (Integer) scd.get("roleId");
		if (roleId == 0 || roleId == 2 || GenericUtil.uInt(scd.get("administratorFlag")) != 0) {
			engine.reloadCache(GenericUtil.uInt(scd.get("customizationId")));
			response.getWriter().write("{\"success\":true}");
			response.getWriter().close();
/*			if(FrameworkSetting.mq)try{
				String projectUuid = "067e6162-3b6f-4ae2-a221-2470b63dff00";
				FrameworkCache.wProjects.get(projectUuid).get_mqChannel().basicPublish(projectUuid, "", null, ("iwb:69,0"+projectUuid+","+FrameworkSetting.instanceUuid).getBytes());
			}catch (Exception e) {
			}*/
		} else
			response.getWriter().write("{\"success\":false}");

	}

	@RequestMapping("/cnvMailPassEncDec")
	public void hndCnvMailPassEncDec(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndCnvMailPassEncDec");
		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
		response.setContentType("application/json");
		int roleId = (Integer) scd.get("roleId");
		if (roleId == 0 || roleId == 2 || GenericUtil.uInt(scd.get("administratorFlag")) != 0) {
			engine.cnvMailPassEncDec(scd, GenericUtil.uInt(request, "_dec") == 0);
			engine.reloadCache(GenericUtil.uInt(scd.get("customizationId")));
			response.getWriter().write("{\"success\":true}");
		} else
			response.getWriter().write("{\"success\":false}");
		response.getWriter().close();

	}

	@RequestMapping("/ajaxPostChatMsg")
	public void hndAjaxPostChatMsg(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
		logger.info("hndAjaxPostChatMsg");
		response.setContentType("application/json");
		String msg = request.getParameter("msg");
		int userId = GenericUtil.uInt(request, "receiver_user_id");
		if (userId == 0 || GenericUtil.isEmpty(msg)) {
			response.getWriter().write("{\"success\":false}");
			return;
		}
		Map<String, String> m = GenericUtil.getParameterMap(request);
		String s = m.get("msg");
		if (GenericUtil.uInt(scd.get("mobile")) == 2)
			s = GenericUtil.encodeGetParamsToUTF8(s);// hack for android mobile app
		m.put("msg", s.contains("\\") ? s.replace('\\', '/') : s);
		W5FormResult formResult = engine.postForm4Table(scd, 1703, 2, m, "");

		response.setContentType("application/json");
		if (!GenericUtil.isEmpty(formResult.getErrorMap())) {
			response.getWriter().write("{\"success\":false}");
			response.getWriter().close();
			return;
		}

		Object chatId = formResult.getOutputFields().get("chat_id");
		List<W5QueuedPushMessageHelper> l = UserUtil.publishUserChatMsg((Integer) scd.get("customizationId"),
				(Integer) scd.get("userId"), userId, msg, chatId);
		response.getWriter().write("{\"success\":true, \"delivered_cnt\":1, \"chatId\":"+chatId+"}");
		response.getWriter().close();
		
//		if(FrameworkSetting.mq)UserUtil.mqPublishUserChatMsg(scd, userId, msg, chatId);
		/*
		 * if(!GenericUtil.isEmpty(l)){ executeQueuedMobilePushMessage eqf = new
		 * executeQueuedMobilePushMessage(l); taskExecutor.execute(eqf); }
		 */
	}

	@RequestMapping("/ajaxNotifyChatMsgRead")
	public void hndAjaxNotifyChatMsgRead(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
		logger.info("hndAjaxNotifyChatMsgRead");
		int userId = GenericUtil.uInt(request, "u");
		int msgId = GenericUtil.uInt(request, "m");
		if (userId == 0 || msgId == 0) {
			response.getWriter().write("{\"success\":false}");
			return;
		}
		int countLeft = engine.notifyChatMsgRead(scd, userId, msgId);

		response.setContentType("application/json");
		response.getWriter().write("{\"success\":true, \"countLeft\":" + countLeft + "}");
		response.getWriter().close();

		if (countLeft == 0) {
			UserUtil.publishUserChatMsgRead(scd, userId, msgId);
		}
	}

	@RequestMapping("/ajaxPostForm")
	public void hndAjaxPostForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int formId = GenericUtil.uInt(request, "_fid");
		logger.info("hndAjaxPostForm(" + formId + ")");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		int action = GenericUtil.uInt(request, "a");
		Map<String,String> requestMap = GenericUtil.getParameterMap(request);
/*		if(GenericUtil.safeEquals(request.getContentType(),"application/json")){
			JSONObject jo = HttpUtil.getJson(request);
			requestMap.putAll(GenericUtil.fromJSONObjectToMap(jo));
		}*/
		W5FormResult formResult = engine.postForm4Table(scd, formId, action, requestMap, "");

		response.setContentType("application/json");
		response.getWriter().write(getViewAdapter(scd, request).serializePostForm(formResult).toString());
		response.getWriter().close();
		
		if (formResult.getQueuedDbFuncList() != null)
			for (W5QueuedDbFuncHelper o : formResult.getQueuedDbFuncList()) {
				executeQueuedDbFunc eqf = new executeQueuedDbFunc(o);
				taskExecutor.execute(eqf);
			}

		
		/*
		 * if(!GenericUtil.isEmpty(formResult.getQueuedPushMessageList())){
		 * executeQueuedMobilePushMessage eqf = new
		 * executeQueuedMobilePushMessage(formResult.getQueuedPushMessageList())
		 * ; taskExecutor.execute(eqf); }
		 */
		if (formResult.getErrorMap().isEmpty()){
			UserUtil.syncAfterPostFormAll(formResult.getListSyncAfterPostHelper());
//			UserUtil.mqSyncAfterPostFormAll(formResult.getScd(), formResult.getListSyncAfterPostHelper());
		}


	}


	@RequestMapping("/ajaxPostFormBulkUpdate")
	public void hndAjaxPostFormBulkUpdate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxPostFormBulkUpdate");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		response.setContentType("application/json");
		int formId = GenericUtil.uInt(request, "_fid");
		if (formId > 0) {
			W5FormResult formResult = engine.postBulkUpdate4Table(scd, formId, GenericUtil.getParameterMap(request));

			for (W5QueuedDbFuncHelper o : formResult.getQueuedDbFuncList()) {
				executeQueuedDbFunc eqf = new executeQueuedDbFunc(o);
				taskExecutor.execute(eqf);
			}
			response.getWriter().write(getViewAdapter(scd, request).serializePostForm(formResult).toString());
			response.getWriter().close();

			if (formResult.getErrorMap().isEmpty()){
				UserUtil.syncAfterPostFormAll(formResult.getListSyncAfterPostHelper());
//				UserUtil.mqSyncAfterPostFormAll(formResult.getScd(), formResult.getListSyncAfterPostHelper());
			}

		} else {
			int smsMailId = GenericUtil.uInt(request, "_smsMailId");
			W5DbFuncResult dbFuncResult = engine.postBulkSmsMail4Table(scd, smsMailId,
					GenericUtil.getParameterMap(request));
			response.getWriter().write(getViewAdapter(scd, request).serializeDbFunc(dbFuncResult).toString());
		}

	}

	@RequestMapping("/ajaxQueryData4BulkUpdate")
	public void hndAjaxQueryData4BulkUpdate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxQueryData4BulkUpdate");
		int formId = GenericUtil.uInt(request, "_fid");
		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		W5QueryResult queryResult = engine.executeQuery4BulkUpdate(scd, formId, GenericUtil.getParameterMap(request),
				false);

		response.setContentType("application/json");
		response.getWriter().write(getViewAdapter(scd, request).serializeQueryData(queryResult).toString());
		response.getWriter().close();

	}

	@RequestMapping("/ajaxPing")
	public void hndAjaxPing(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxPing");
		HttpSession session = request.getSession(false);
		boolean notSessionFlag = session == null || session.getAttribute("scd-dev") == null
				|| ((HashMap<String, String>) session.getAttribute("scd-dev")).size() == 0;
		response.setContentType("application/json");
		Map cm = null;
		if(FrameworkSetting.chat && !notSessionFlag && GenericUtil.uInt(request, "c")!=0){
			cm = engine.getUserNotReadChatMap((Map)session.getAttribute("scd-dev"));
		}
		if(GenericUtil.uInt(request, "d")==0 || notSessionFlag)
			response.getWriter().write("{\"success\":true,\"session\":" + !notSessionFlag + (cm!=null ? ", \"newMsgCnt\":"+GenericUtil.fromMapToJsonString2Recursive(cm):"") + "}");
		else {
			Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
			response.getWriter().write("{\"success\":true,\"session\":" + GenericUtil.fromMapToJsonString2Recursive(scd) + (cm!=null ? ", \"newMsgCnt\":"+GenericUtil.fromMapToJsonString2Recursive(cm):"") + "}");
		}
		response.getWriter().close();
	}

	@RequestMapping("/ajaxPostConversionGridMulti")
	public void hndAjaxPostConversionGridMulti(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxPostConversionGridMulti");
		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		response.setContentType("application/json");
		int conversionCount = GenericUtil.uInt(request, "_ccnt");
		if (conversionCount > 0) {
			W5FormResult formResult = engine.postBulkConversionMulti(scd, conversionCount,
					GenericUtil.getParameterMap(request));

			response.getWriter().write(getViewAdapter(scd, request).serializePostForm(formResult).toString());
			response.getWriter().close();

			for (W5QueuedDbFuncHelper o : formResult.getQueuedDbFuncList()) {
				executeQueuedDbFunc eqf = new executeQueuedDbFunc(o);
				taskExecutor.execute(eqf);
			}
			
			if (formResult.getErrorMap().isEmpty()){
				UserUtil.syncAfterPostFormAll(formResult.getListSyncAfterPostHelper());
//				UserUtil.mqSyncAfterPostFormAll(formResult.getScd(), formResult.getListSyncAfterPostHelper());
			}
		} else
			response.getWriter().write("{\"success\":false}");
	}

	@RequestMapping("/ajaxPostEditGrid")
	public void hndAjaxPostEditGrid(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxPostEditGrid");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		response.setContentType("application/json");
		int dirtyCount = GenericUtil.uInt(request, "_cnt");
		int formId = GenericUtil.uInt(request, "_fid");
		if (formId > 0) {
			W5FormResult formResult = engine.postEditGrid4Table(scd, formId, dirtyCount,
					GenericUtil.getParameterMap(request), "", new HashSet<String>());
			response.getWriter().write(getViewAdapter(scd, request).serializePostForm(formResult).toString());
			response.getWriter().close();

			for (W5QueuedDbFuncHelper o : formResult.getQueuedDbFuncList()) {
				executeQueuedDbFunc eqf = new executeQueuedDbFunc(o);
				taskExecutor.execute(eqf);
			}

			if (formResult.getErrorMap().isEmpty()){
				UserUtil.syncAfterPostFormAll(formResult.getListSyncAfterPostHelper());
//				UserUtil.mqSyncAfterPostFormAll(formResult.getScd(), formResult.getListSyncAfterPostHelper());
				
			}

		} else if (formId < 0) { // negatifse direk -dbFuncId
			// int dbFuncId= GenericUtil.uInt(request, "_did");
			W5DbFuncResult dbFuncResult = engine.postEditGridDbFunc(scd, -formId, dirtyCount,
					GenericUtil.getParameterMap(request), "");
			response.getWriter().write(getViewAdapter(scd, request).serializeDbFunc(dbFuncResult).toString());
		} else {
			int conversionId = GenericUtil.uInt(request, "_cnvId");
			if (conversionId > 0) {
				W5FormResult formResult = engine.postBulkConversion(scd, conversionId, dirtyCount,
						GenericUtil.getParameterMap(request), "");
				response.getWriter().write(getViewAdapter(scd, request).serializePostForm(formResult).toString());
				response.getWriter().close();

				for (W5QueuedDbFuncHelper o : formResult.getQueuedDbFuncList()) {
					executeQueuedDbFunc eqf = new executeQueuedDbFunc(o);
					taskExecutor.execute(eqf);
				}
				
				if (formResult.getErrorMap().isEmpty()){
					UserUtil.syncAfterPostFormAll(formResult.getListSyncAfterPostHelper());
//					UserUtil.mqSyncAfterPostFormAll(formResult.getScd(), formResult.getListSyncAfterPostHelper());
				}

			} else {
				response.getWriter().write("{\"success\":false}");
			}
		}
	}

	@RequestMapping("/ajaxBookmarkForm")
	public void hndAjaxBookmarkForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxBookmarkForm");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		int formId = GenericUtil.uInt(request, "_fid");
		int action = GenericUtil.uInt(request, "a");
		W5FormResult formResult = engine.bookmarkForm(scd, formId, action, GenericUtil.getParameterMap(request));

		response.setContentType("application/json");
		response.getWriter().write("{\"success\":true,\"id\":" + formResult.getPkFields().get("id") + "}");

	}

	@RequestMapping("/ajaxExecDbFunc")
	public void hndAjaxExecDbFunc(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxExecDbFunc");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		int dbFuncId = GenericUtil.uInt(request, "_did"); // +:dbFuncId,
															// -:formId
		if (dbFuncId == 0) {
			dbFuncId = -GenericUtil.uInt(request, "_fid"); // +:dbFuncId,
															// -:formId
		}
		W5DbFuncResult dbFuncResult = engine.executeDbFunc(scd, dbFuncId, GenericUtil.getParameterMap(request),
				(short) 1);

		response.setContentType("application/json");
		response.getWriter().write(getViewAdapter(scd, request).serializeDbFunc(dbFuncResult).toString());
		response.getWriter().close();

	}

	

	@RequestMapping("/ajaxGetFormSimple")
	public void hndGetFormSimple(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int formId = GenericUtil.uInt(request, "_fid");
		logger.info("hndGetFormSimple(" + formId + ")");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		int action = GenericUtil.uInt(request, "a");
		W5FormResult formResult = engine.getFormResult(scd, formId, action, GenericUtil.getParameterMap(request));

		response.setContentType("application/json");
		response.getWriter().write(getViewAdapter(scd, request).serializeGetFormSimple(formResult).toString());
		response.getWriter().close();

	}

	@RequestMapping("/ajaxReloadFormCell")
	public void hndAjaxReloadFormCell(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxReloadFormCell");
		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
		int fcId = GenericUtil.uInt(request, "_fcid");
		String webPageId = request.getParameter(".w");
		String tabId = request.getParameter(".t");
		W5FormCellHelper rc = engine.reloadFormCell(scd, fcId, webPageId, tabId);
		response.setContentType("application/json");
		response.getWriter()
				.write(ext3_4
						.serializeFormCellStore(rc, (Integer) scd.get("customizationId"), (String) scd.get("locale"))
						.toString());
		response.getWriter().close();
	}

	@RequestMapping("/ajaxGetFormCellCodeDetail")
	public void hndAjaxGetFormCellCodeDetail(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxGetFormCellCodeDetail");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
		int fccdId = GenericUtil.uInt(request, "_fccdid");
		String result = engine.getFormCellCodeDetail(scd, GenericUtil.getParameterMap(request), fccdId);
		response.setContentType("application/json");
		response.getWriter().write("{\"success\":true,\"result\":\"" + result + "\"}");
		response.getWriter().close();

	}

	@RequestMapping("/ajaxFeed")
	public void hndAjaxFeed(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxFeed");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		response.setContentType("application/json");

		int platestFeedIndex = request.getParameter("_lfi") == null ? -1 : GenericUtil.uInt(request, "_lfi");
		int pfeedTip = request.getParameter("_ft") == null ? -1 : GenericUtil.uInt(request, "_ft");
		int proleId = request.getParameter("_ri") == null ? -1 : GenericUtil.uInt(request, "_ri");
		int puserId = request.getParameter("_ui") == null ? -1 : GenericUtil.uInt(request, "_ui");
		int pmoduleId = request.getParameter("_mi") == null ? -1 : GenericUtil.uInt(request, "_mi");
		// response.setContentType("application/json");
		response.getWriter()
				.write(getViewAdapter(scd, request).serializeFeeds(scd, platestFeedIndex, pfeedTip, proleId, puserId, pmoduleId).toString());
		response.getWriter().close();
		if (FrameworkSetting.liveSyncRecord) {
			UserUtil.getTableGridFormCellCachedKeys((Integer) scd.get("customizationId"),
					/* mainTable.getTableId() */ 671, (Integer) scd.get("userId"), (String) scd.get("sessionId"),
					request.getParameter(".w"), request.getParameter(".t"), /* grdOrFcId */ 919, null, true);
		}
	}
	

	@RequestMapping("/ajaxTsPortletData")
	public void hndAjaxTsPortletData(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxTsPortletData");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		response.setContentType("application/json");

		int porletId = GenericUtil.uInt(request, "_pid");
		String s = engine.getTsDashResult(scd, GenericUtil.getParameterMap(request), porletId);
		response.getWriter().write(s);
		response.getWriter().close();
	}


	@RequestMapping("/showForm")
	public void hndShowForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int formId = GenericUtil.uInt(request, "_fid");
		logger.info("hndShowForm(" + formId + ")");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		int action = GenericUtil.uInt(request, "a");
		W5FormResult formResult = engine.getFormResult(scd, formId, action, GenericUtil.getParameterMap(request));

		response.setContentType("application/json");
		response.getWriter().write(getViewAdapter(scd, request).serializeShowForm(formResult).toString());
		response.getWriter().close();

	}
	
	@RequestMapping("/showMForm")
	public void hndShowMForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int formId = GenericUtil.uInt(request, "_fid");
		logger.info("hndShowMForm(" + formId + ")");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		int action = GenericUtil.uInt(request, "a");
		W5FormResult formResult = engine.getFormResult(scd, formId, action, GenericUtil.getParameterMap(request));

		response.getWriter().write(f7.serializeGetForm(formResult).toString());
		response.getWriter().close();

	}

	@RequestMapping("/showTutorial")
	public void hndShowTutorial(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int tutorialId = GenericUtil.uInt(request, "_ttid");
		logger.info("showTutorial(" + tutorialId + ")");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		W5TutorialResult tutorialResult = engine.getTutorialResult(scd, tutorialId,
				GenericUtil.getParameterMap(request));

		response.setContentType("application/json");
		if (GenericUtil.uInt(scd.get("mobile")) != 0) {
			response.getWriter().write("{\"success\":false}");
		} else {
			response.getWriter().write(getViewAdapter(scd, request).serializeShowTutorial(tutorialResult).toString());
		}
		response.getWriter().close();

	}

	@RequestMapping("/ajaxLogoutUser")
	public void hndAjaxLogoutUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxLogoutUser");
		HttpSession session = request.getSession(false);
		response.setContentType("application/json");
		if (session != null) {
			Map<String, Object> scd = (Map) session.getAttribute("scd-dev");
			if (scd != null) {
				UserUtil.onlineUserLogout((Integer) scd.get("customizationId"), (Integer) scd.get("userId"), scd.containsKey("mobile") ? (String)scd.get("mobileDeviceId") : session.getId());
				if(scd.containsKey("mobile")){
					Map parameterMap = new HashMap(); parameterMap.put("pmobile_device_id", scd.get("mobileDeviceId"));parameterMap.put("pactive_flag", 0);
					engine.executeDbFunc(scd, 673, parameterMap, (short)4);
				}
			}
			session.removeAttribute("scd-dev");
		}
		if(GenericUtil.uInt(request, "d")!=0)throw new IWBException("session","No Session",0,null, "No valid session", null);
		else response.getWriter().write("{\"success\":true}");
	}

	private String getDefaultLanguage(Map<String, Object> scd, String locale) {
		String res = FrameworkCache.getAppSettingStringValue(0, "locale", "en");
		String active_locales = FrameworkCache.getAppSettingStringValue(scd.get("customizationId"), "active_locales");
		if (GenericUtil.isEmpty(active_locales))
			return res;
		if (active_locales.length() == 2)
			return active_locales;
		for (W5LookUpDetay d : FrameworkCache.getLookUp(scd, 2).get_detayList()) {
			if (d.getActiveFlag() == 1 && active_locales.indexOf(d.getVal()) != -1 && d.getVal().equals(locale)) {
				res = d.getVal();
				break;
			}
		}
		return res;
	}

	@RequestMapping("/login.htm")
	public void hndLoginPageOld(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndLoginPage");
		HttpSession session = request.getSession(false);
		if (session != null) {
			if (session.getAttribute("scd-dev") != null) {
				Map<String, Object> scd = (Map<String, Object>) session.getAttribute("scd-dev");
				if (scd != null)
					UserUtil.onlineUserLogout((Integer) scd.get("customizationId"), (Integer) scd.get("userId"),
							(String) scd.get("sessionId"));
			}
			session.removeAttribute("scd-dev");
		}
		int cust_id = FrameworkCache.getAppSettingIntValue(0, "default_customization_id");

		String subDomain = GenericUtil.getSubdomainName(request);
		logger.info("subDomain : " + subDomain);
		if (!subDomain.equals(""))
			cust_id = engine.getSubDomain2CustomizationId(subDomain);

		Map<String, Object> scd = new HashMap();
		scd.put("userId", 1);
		scd.put("customizationId", cust_id);
		Locale blocale = request.getLocale();
		scd.put("locale", getDefaultLanguage(scd, blocale.getLanguage()));

		int templateId = 1; // Login Page Template
		if (FrameworkCache.getAppSettingIntValue(0, "mobile_flag") != 0) {
			String requestHeaderUserAgent = request.getHeader("User-Agent");
			// iphone -> Mozilla/5.0 (iPhone; U; CPU iPhone OS 3_1_3 like Mac OS
			// X; en-us) AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0
			// Mobile/7E18 Safari/528.16
			// android -> Mozilla/5.0 (Linux; U; Android 2.2.2; tr-tr; LG-P970
			// Build/FRG83G) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0
			// Mobile Safari/533.1 MMS/LG-Android-MMS-V1.0/1.2
			if (requestHeaderUserAgent != null) {
				requestHeaderUserAgent = requestHeaderUserAgent.toLowerCase();
				if (requestHeaderUserAgent.contains("symbian") || requestHeaderUserAgent.contains("iphone")
						|| requestHeaderUserAgent.contains("ipad") || request.getParameter("iphone") != null
						|| requestHeaderUserAgent.contains("android") || request.getParameter("android") != null) {
					// templateId = 564; //TODO : sencha ile ilgili kısımda
					// hatalar olduğundan burası geçici olarak kapatıldı.
				}
			}
		}

		W5TemplateResult pageResult = engine.getTemplateResult(scd, templateId, GenericUtil.getParameterMap(request));
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().write(getViewAdapter(scd, request).serializeTemplate(pageResult).toString());
		response.getWriter().close();

	}

	@RequestMapping("/login2.htm")
	public void hndLoginPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndLoginPage");
		HttpSession session = request.getSession(false);
		if (session != null) {
			if (session.getAttribute("scd-dev") != null) {
				Map<String, Object> scd = (Map<String, Object>) session.getAttribute("scd-dev");
				if (scd != null)
					UserUtil.onlineUserLogout((Integer) scd.get("customizationId"), (Integer) scd.get("userId"),
							(String) scd.get("sessionId"));
			}
			session.removeAttribute("scd-dev");
		}
		int cust_id = FrameworkCache.getAppSettingIntValue(0, "default_customization_id");

		String subDomain = GenericUtil.getSubdomainName(request);
		logger.info("subDomain : " + subDomain);
		if (!subDomain.equals(""))
			cust_id = engine.getSubDomain2CustomizationId(subDomain);

		Map<String, Object> scd = new HashMap();
		scd.put("userId", 1);
		scd.put("customizationId", cust_id);
		Locale blocale = request.getLocale();
		scd.put("locale", getDefaultLanguage(scd, blocale.getLanguage()));

		int templateId = 1146; // Login Page Template

		if (FrameworkCache.getAppSettingIntValue(0, "mobile_flag") != 0) {
			String requestHeaderUserAgent = request.getHeader("User-Agent");
			// iphone -> Mozilla/5.0 (iPhone; U; CPU iPhone OS 3_1_3 like Mac OS
			// X; en-us) AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0
			// Mobile/7E18 Safari/528.16
			// android -> Mozilla/5.0 (Linux; U; Android 2.2.2; tr-tr; LG-P970
			// Build/FRG83G) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0
			// Mobile Safari/533.1 MMS/LG-Android-MMS-V1.0/1.2
			if (requestHeaderUserAgent != null) {
				requestHeaderUserAgent = requestHeaderUserAgent.toLowerCase();
				if (requestHeaderUserAgent.contains("symbian") || requestHeaderUserAgent.contains("iphone")
						|| requestHeaderUserAgent.contains("ipad") || request.getParameter("iphone") != null
						|| requestHeaderUserAgent.contains("android") || request.getParameter("android") != null) {
					// templateId = 564; //TODO : sencha ile ilgili kısımda
					// hatalar olduğundan burası geçici olarak kapatıldı.
				}
			}
		}

		W5TemplateResult pageResult = engine.getTemplateResult(scd, templateId, GenericUtil.getParameterMap(request));
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().write(getViewAdapter(scd, request).serializeTemplate(pageResult).toString());
		response.getWriter().close();

	}

	@RequestMapping("/forgotmypass.htm")
	public void hndForgotMyPassPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndForgotMyPassPage");

		int cust_id = FrameworkCache.getAppSettingIntValue(0, "default_customization_id");
		Locale blocale = request.getLocale();

		Map<String, Object> scd = new HashMap();
		scd.put("userId", 1);
		scd.put("customizationId", cust_id);
		scd.put("locale", GenericUtil.getParameterMap(request).get("locale") != null
				? GenericUtil.getParameterMap(request).get("locale") : getDefaultLanguage(scd, blocale.getLanguage()));

		int templateId = 7; // Page Template

		W5TemplateResult pageResult = engine.getTemplateResult(scd, templateId, GenericUtil.getParameterMap(request));
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().write(getViewAdapter(scd, request).serializeTemplate(pageResult).toString());
		response.getWriter().close();

	}

	@RequestMapping("/main.htm")
	public void hndMainPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndMainPage");
		
		HttpSession session = request.getSession(false);
		Map<String, Object> scd = null;
		if(session!=null){
			Object token = session.getAttribute("authToken");
			if(token!=null){
				scd = engine.generateScdFromAuth(1, token.toString());
				if(scd!=null){
					session.removeAttribute("authToken");
					scd.put("locale", "tr");
					session.setAttribute("scd-dev", scd);
				}
				else
					response.sendRedirect("authError.htm");
			} else {
				scd = UserUtil.getScd(request, "scd-dev", true);

			}
		} else { 
			response.sendRedirect("login.htm");
			return;
		}
		

		if (scd.get("mobile") != null)
			scd.remove("mobile");

		int templateId = GenericUtil.uInt(scd.get("mainTemplateId")); // Login
		
	
		
																		// Page
																		// Template
		W5TemplateResult pageResult = engine.getTemplateResult(scd, templateId, GenericUtil.getParameterMap(request));
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().write(getViewAdapter(scd, request).serializeTemplate(pageResult).toString());
		response.getWriter().close();

	}
	
	@RequestMapping("/index.html")
	public void hndLandingPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, Object> scd = new HashMap();
		scd.put("customizationId", 0);scd.put("userId", 0);scd.put("locale", "en");
		W5TemplateResult pageResult = engine.getTemplateResult(scd, 2453, new HashMap());
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().write(getViewAdapter(scd, request).serializeTemplate(pageResult).toString());
		response.getWriter().close();

	}
	
	@RequestMapping("/showPage")
	public void hndShowPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int templateId = GenericUtil.uInt(request, "_tid");
		logger.info("hndShowPage(" + templateId + ")");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		W5TemplateResult pageResult = engine.getTemplateResult(scd, templateId, GenericUtil.getParameterMap(request));
		// if(pageResult.getTemplate().getTemplateTip()!=2 && templateId!=218 &&
		// templateId!=611 && templateId!=551 && templateId!=566){ //TODO:cok
		// amele
		// throw new PromisException("security","Template",0,null, "Wrong
		// Template Tip (must be page)", null);
		// }

		if(pageResult.getTemplate().getTemplateTip()!=0)
			response.setContentType("application/json");

		response.getWriter().write(getViewAdapter(scd, request).serializeTemplate(pageResult).toString());
		response.getWriter().close();

	}
	

	@RequestMapping("/showMList")
	public void hndShowMList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int listId = GenericUtil.uInt(request, "_lid");
		logger.info("hndShowMList(" + listId + ")");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		M5ListResult listResult = engine.getMListResult(scd, listId, GenericUtil.getParameterMap(request));
		// if(pageResult.getTemplate().getTemplateTip()!=2 && templateId!=218 &&
		// templateId!=611 && templateId!=551 && templateId!=566){ //TODO:cok
		// amele
		// throw new PromisException("security","Template",0,null, "Wrong
		// Template Tip (must be page)", null);
		// }

		response.setContentType("application/json");
		response.getWriter().write(f7.serializeList(listResult).toString());
		response.getWriter().close();

	}

	
	@RequestMapping("/grd/*")
	public ModelAndView hndGridReport(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndGridReport");
		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		int gridId = GenericUtil.uInt(request, "_gid");
		String gridColumns = request.getParameter("_columns");

		List<W5ReportCellHelper> list = engine.getGridReportResult(scd, gridId, gridColumns,
				GenericUtil.getParameterMap(request));
		if (list != null) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("report", list);
			m.put("scd-dev", scd);
			ModelAndView result = null;
			if (request.getRequestURI().indexOf(".xls") != -1 || "xls".equals(request.getParameter("_fmt")))
				result = new ModelAndView(new RptExcelRenderer(), m);
			else if (request.getRequestURI().indexOf(".pdf") != -1)
				result = new ModelAndView(new RptPdfRenderer(engine.getCustomizationLogoFilePath(scd)), m);
			else if (request.getRequestURI().indexOf(".csv") != -1) {
				response.setContentType("application/octet-stream");
				response.getWriter().print(GenericUtil.report2csv(list));
			} else if (request.getRequestURI().indexOf(".txt") != -1) {
				response.setContentType("application/octet-stream");
				response.getWriter().print(GenericUtil.report2text(list));
			}
			return result;

		} else {
			response.getWriter().write("Hata");
			response.getWriter().close();

			return null;
		}

	}

	@RequestMapping("/grd2/*") // master detail report
	public ModelAndView hndGrid2Report(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndGrid2Report");
		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		int masterGridId = GenericUtil.uInt(request, "_gid");
		String masterGridColumns = request.getParameter("_columns");
		int detailGridId = GenericUtil.uInt(request, "_gid2");
		String detailGridColumns = request.getParameter("_columns2");
		String params = request.getParameter("_params");
		List<W5ReportCellHelper> list = engine.getGrid2ReportResult(scd, masterGridId, masterGridColumns, detailGridId,
				detailGridColumns, params, GenericUtil.getParameterMap(request));
		if (list != null) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("report", list);
			m.put("scd-dev", scd);

			ModelAndView result = null;
			if (request.getRequestURI().indexOf(".xls") != -1 || "xls".equals(request.getParameter("_fmt")))
				result = new ModelAndView(new RptExcelRenderer(), m);
			else // if(request.getRequestURI().indexOf(".pdf")!=-1)
				result = new ModelAndView(new RptPdfRenderer(engine.getCustomizationLogoFilePath(scd)), m);
			;
			return result;

		} else {
			response.getWriter().write("Hata");
			response.getWriter().close();

			return null;
		}

	}

	/*
	 * 
	 * public ModelAndView hndSaveUserGridSetting( HttpServletRequest request,
	 * HttpServletResponse response) throws ServletException, IOException {
	 * log.info("hndSaveUserGridSetting"); Map<String, Object> scd =
	 * UserUtil.getScd(request, "scd-dev", true);
	 * 
	 * int gridId= GenericUtil.uInt(request, "_gid"); String gridUserDsc =
	 * request.getParameter("_dsc"); String gridColumns =
	 * request.getParameter("_columns"); String gridSFRMCells =
	 * request.getParameter("_sfrm_cells");
	 * response.getWriter().write("{\"success\":"+bus.getGridReportResult(scd,
	 * gridId, gridUserDsc, gridColumns, gridSFRMCells)+"}");
	 * response.getWriter().close(); return null; }
	 */

	@RequestMapping("/dl/*")
	public void hndFileDownload(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndFileDownload");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		int fileAttachmentId = GenericUtil.uInt(request, "_fai");
		String customizationId = String.valueOf((scd.get("customizationId") == null) ? 0 : scd.get("customizationId"));
		String local_path = FrameworkCache.getAppSettingStringValue(scd, "file_local_path");
		String file_path = "";
		if (fileAttachmentId == -1000) { // default company logo
			file_path = local_path + "/0/jasper/iworkbetter.png";
			response.setContentType("image/png");
		} else {
			W5FileAttachment fa = engine.loadFile(scd, fileAttachmentId);
			if (fa == null) {
				throw new IWBException("validation", "File Attachment", fileAttachmentId, null,
						"Invalid Id: " + fileAttachmentId, null);
			}
			ServletOutputStream out = response.getOutputStream();
			file_path = local_path + "/" + customizationId + "/attachment/" + fa.getSystemFileName();

			if (fa.getFileTypeId() == null || fa.getFileTypeId() != -999)
				response.setContentType("application/octet-stream");
			else {
				long expiry = new Date().getTime() + FrameworkSetting.cacheAge * 1000;
				response.setContentType("image/"
						+ fa.getOrijinalFileName().substring(fa.getOrijinalFileName().lastIndexOf(".") + 1));
				response.setDateHeader("Expires", expiry);
				response.setHeader("Cache-Control", "max-age=" + FrameworkSetting.cacheAge);
			}
		}
		ServletOutputStream out = null;
		InputStream stream = null;
		try {
			stream = new FileInputStream(file_path);
			out = response.getOutputStream();
			// write the file to the file specified
			int bytesRead = 0;
			byte[] buffer = new byte[8192];

			while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			if (FrameworkCache.getAppSettingIntValue(scd, "log_download_flag") != 0) {
				Log5UserAction ua = new Log5UserAction(scd);
				ua.setActionTip((short) 1);
				ua.setTableId(44);
				ua.setTablePk(fileAttachmentId);
				ua.setUserIp(request.getRemoteAddr());
				engine.saveObject(ua);

			}
		} catch (Exception e) {
			if (FrameworkSetting.debug)
				e.printStackTrace();
			// bus.logException(e.getMessage(),GenericUtil.uInt(scd.get("customizationId")),GenericUtil.uInt(scd.get("userRoleId")));
			throw new IWBException("generic", "File Attacment", fileAttachmentId, "Unknown Exception",
					e.getMessage(), e.getCause());
		} finally {
			if (out != null)
				out.close();
			if (stream != null)
				stream.close();
		}
	}

	@RequestMapping("/sf/*")
	public void hndShowFile(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int fileAttachmentId = GenericUtil.uInt(request, "_fai");
		logger.info("hndShowFile(" + fileAttachmentId + ")");
		Map<String, Object> scd = null;
		if (fileAttachmentId == 0) {
			scd = UserUtil.getScd(request, "scd-dev", true);
			String spi = request.getRequestURI();
			if (spi.indexOf("/sf/pic")==4 && spi.contains(".")) {
				spi = spi.substring(4+7);
				spi = spi.substring(0, spi.indexOf("."));
				fileAttachmentId = -GenericUtil.uInt(spi);
			}
			if (fileAttachmentId == 0)
				fileAttachmentId = -GenericUtil.uInt(request, "userId");
		}
		InputStream stream = null;
		String filePath = null;
		W5FileAttachment fa = engine.loadFile(scd, fileAttachmentId);
		if (fa == null) { // bulunamamis TODO
			throw new IWBException("validation", "File Attachment", fileAttachmentId, null,
					"Wrong Id: " + fileAttachmentId, null);
		}

		if (fa.getFileAttachmentId() == 1 || fa.getFileAttachmentId() == 2) { // man / woman default picture
//			this.getClass().getClassLoader().getResource("static/ext3.4.1/ext-all.js"); 
//			File folder = new ClassPathResource("static/ext3.4.1/ext-all.js").getFile().getPath();
//			filePath = request.getSession().getServletContext().getRealPath("static/images/custom/ppicture/default_" + (fa.getFileAttachmentId() == 2 ? "wo" : "") + "man_mini.png");
			filePath = fa.getFileAttachmentId() == 2 ? womanPicPath : manPicPath;
		} else {
			if (scd == null)scd = UserUtil.getScd(request, "scd-dev", true);
			String customizationId = String
					.valueOf((scd.get("customizationId") == null) ? 0 : scd.get("customizationId"));
			String file_path = FrameworkCache.getAppSettingStringValue(scd, "file_local_path");
			filePath = file_path + "/" + customizationId + "/attachment/" + fa.getSystemFileName();
		}

		ServletOutputStream out = response.getOutputStream();
		try {
			/*
			 * if(fileAttachmentId<0)try { stream = new
			 * FileInputStream(filePath); } catch(Exception e0){ stream = new
			 * FileInputStream(request.getRealPath("/images/custom/wv.png")); }
			 * else stream = new FileInputStream(filePath);
			 */

			if (stream == null)
				try {
					stream = new FileInputStream(filePath);
				} catch (Exception e0) {
					stream = new FileInputStream(brokenPicPath);
				}

			// write the file to the file specified
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
		} catch (Exception e) {
			if (FrameworkSetting.debug)
				e.printStackTrace();
			// bus.logException(e.getMessage(),GenericUtil.uInt(scd.get("customizationId")),GenericUtil.uInt(scd.get("userRoleId")));
			throw new IWBException("generic", "File Attacment", fileAttachmentId, "Unknown Exception",
					e.getMessage(), e.getCause());
		} finally {
			if(out!=null)out.close();
			if(stream!=null)stream.close();
		}
	}

	@RequestMapping("/jasper/*")
	public ModelAndView hndJasper(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndJasperReport"); 
    	Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
    	int customizationId=(Integer) ((scd.get("customizationId")==null) ? 0 : scd.get("customizationId"));
    	//int jasperId = PromisUtil.uInt(request, "_jid");
    	//int jasperReportId=PromisUtil.uInt(request, "_jrid");
    	//int jasperTypeId=PromisUtil.uInt(request, "_jtid");// jasper raporlara ön yazı eklemek icin   _jtid isminde raporlar yolluyoruz.
    	String locale= (request.getParameter("tlocale")==null) ?  (String)scd.get("locale") : (String) request.getParameter("tlocale") ; //Sisteme login dili değilde farklı bir dil seçeneği kullanılmak isteniyosa
    	Map<String, String> requestParams = GenericUtil.getParameterMap(request);    	
    	int multiJasperFlag=GenericUtil.uInt(request, "_multiple_flag");
    	JasperPrint jasperPrint = new JasperPrint() ;
    	JasperPrint jasperPrintMulti = new JasperPrint();
    	
		// Eğer 20 den fazla sayfa varsa dosya sistemini kullanacak önce //
    	String fileLocalPath = FrameworkCache.getAppSettingStringValue(scd, "file_local_path");
		File tmp = new File(fileLocalPath + "/" + customizationId + "/temp"); 
		if(!tmp.exists()) tmp.mkdirs();	
		int virtualizer_page=FrameworkCache.getAppSettingIntValue(customizationId,"jasper_virtualizer",20);
		JRFileVirtualizer virtualizer = new JRFileVirtualizer(virtualizer_page, tmp.getPath());
		///////////////////////////////////////////////////////////////////
		
    	//W5JasperResult result = null;
    	int jasperId=GenericUtil.uInt(requestParams.get("xjasper_id"));
    	    	
    	try {    		
	    	if(multiJasperFlag!=0){	 	    		
	    		String jasperReportIds=requestParams.get("xjasper_report_ids");	    		
	    		int pageIndex=0;	    		
	    		W5QueryResult queryResult=engine.getJasperMultipleData(scd, requestParams, jasperId);
	    		for(Object[] o: queryResult.getData()){	    				    			
	    			if(jasperReportIds!=null){
	    				//birden fazla raporu birleştirerek yine her birinden istediğimiz kadar sayfa bastırmak için		    				
	    				String[] jr=jasperReportIds.split(",");
	    				for(int i=0;i<jr.length;i++){
	    					requestParams.clear();
	    					//query sonucunun her bir satırını requestParams olarak kullanacağız
		    				for(int j=0;j<o.length;j++){
		    					if (o[j]!=null) requestParams.put(queryResult.getNewQueryFields().get(j).getDsc(),o[j].toString()); //multi query sonucu
		    				}
		    				requestParams.put("_jrid", jr[i]);
	    					jasperPrint=engine.prepareJasperPrint(scd,requestParams,virtualizer);
	    					if(pageIndex==0) jasperPrintMulti=jasperPrint;
							if(pageIndex!=0) for(JRPrintPage jrPage:jasperPrint.getPages())jasperPrintMulti.addPage(jrPage);
							pageIndex++;
	    				}
	    			}else{
	    				//bir raporu çok sayfa bastırmak için, örneğin toplu fatura basımı
		    			requestParams = GenericUtil.getParameterMap(request);
		    			for(int i=0;i<o.length;i++) requestParams.put(queryResult.getNewQueryFields().get(i).getDsc(),o[i].toString()); //multi query sonucu
		    			jasperPrint=engine.prepareJasperPrint(scd,requestParams,virtualizer);
		    			if(pageIndex==0) jasperPrintMulti=jasperPrint;
						if(pageIndex!=0) for(JRPrintPage jrPage:jasperPrint.getPages())jasperPrintMulti.addPage(jrPage);
						pageIndex++;
	    			}
	    		}	    		
	    		jasperPrint=jasperPrintMulti;
	    	}	    	
	    	else {	    	
	    		jasperPrint=engine.prepareJasperPrint(scd,requestParams,virtualizer);
	    	}
			
			/*if(jasperTypeId==2){//Fax Page				
				resultMap.put("total_page_number",jasperPrint.getPages().size());
				JasperPrint faxJasperPrint = JasperFillManager.fillReport(PromisCache.getAppSettingStringValue(scd, "file_local_path") + "/"+customizationId+"/jasper/fax_page.jasper",resultMap, new JRMapCollectionDataSource(result.getResultDetail()));			
				faxJasperPrint=PromisUtil.convertKey2LocaleMsg(faxJasperPrint,locale);
				jasperPrint.addPage(0, (JRPrintPage) faxJasperPrint.getPages().get(0));
			}
			if(jasperTypeId==3){//Cover Page
				JasperPrint coverJasperPrint = JasperFillManager.fillReport(PromisCache.getAppSettingStringValue(scd, "file_local_path")+ "/"+customizationId+"/jasper/cover_page.jasper", (Map)result.getResultMap(), new JRMapCollectionDataSource(result.getResultDetail()));
				coverJasperPrint=PromisUtil.convertKey2LocaleMsg(coverJasperPrint,locale);
				jasperPrint.addPage(0, (JRPrintPage) coverJasperPrint.getPages().get(0));
			}		*/	
			
	    	//if(result.getJasper().getLocaleKeyFlag()==1){//Jasper Raporunda textfild'e Key degerleri verilmisse,Key degerinin karsılıgı alınıyor.
				jasperPrint=JasperUtil.convertKey2LocaleMsg(jasperPrint,locale);
			//}
			
			if(request.getRequestURI().indexOf(".pdf")!=-1 || "pdf".equals(request.getParameter("_fmt"))){ //PDF
				JRExporter exporter =new JRPdfExporter();
				response.setContentType("application/pdf");
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				if(GenericUtil.uInt(request, "_attachFile")==0)exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,response.getOutputStream());
				exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
				
				//engine.jasperAttachFile(jasperPrint,scd,requestParams,attach_file_name);
				
				if(GenericUtil.uInt(request, "_attachFile")!=0){ //pdf +
					
					String []  urlArray=request.getRequestURI().split("/");					
					String attach_file_name=java.net.URLDecoder.decode(urlArray[urlArray.length-1],"UTF-8");
					long fileId = new Date().getTime();
				    String system_file_name=fileId+"."+attach_file_name;		
					String path = GenericUtil.fileAttachPath(customizationId, "jasper");
				    
					
					int table_id=GenericUtil.uInt(request, "_saveTableId");
					String table_pk=request.getParameter("_saveTablePk") != null ? request.getParameter("_saveTablePk") : "0";
					Integer file_type_id=GenericUtil.uInteger(request,"_file_type_id");
					
					File dirPath = new File(path);
				    if (!dirPath.exists()) {
				            dirPath.mkdirs();
				    }
					JasperExportManager.exportReportToPdfFile(jasperPrint, path+File.separator+system_file_name);
					File attachFile=new File(path+File.separator+system_file_name);					
					int totalBytesRead=(int) (attachFile.length());							
					
					engine.jasperFileAttachmentControl(table_id, table_pk, attach_file_name, file_type_id); // daha önce attach dosyaları disable ediyor.
					
					W5FileAttachment fa = new W5FileAttachment();	 		
		  
					try {			  
//						    fa.setFileComment(bean.getFile_comment());
							fa.setCustomizationId(customizationId);
							fa.setFileTypeId(file_type_id);
							fa.setSystemFileName(system_file_name);
							fa.setOrijinalFileName(attach_file_name);
							fa.setTableId(table_id);
							fa.setTablePk(table_pk);
							fa.setTabOrder((short)1);
							fa.setUploadUserId((Integer)scd.get("userId"));
							fa.setFileSize(totalBytesRead);
							fa.setActiveFlag((short)1);
					        engine.saveObject(fa);
					        response.getWriter().printf("{ \"success\": \"%s\" , \"file_attachment_id\": %d}", "true", fa.getFileAttachmentId());
					}
				    catch (Exception e) {
				        	response.getWriter().printf("{ \"success\": \"%s\" }", "false");
					}
				}else{
					JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
				}
			}
			if(request.getRequestURI().indexOf(".xls")!=-1 || "xls".equals(request.getParameter("_fmt"))){ //Excel
				JRExporter exporter = new JRXlsExporter();
				response.setContentType("application/xls") ;
				OutputStream ouputStream = response.getOutputStream();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
				exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, false);  
				exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, true);  
				exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, false);  
				exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, true);  
				exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, true);  
				exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, true);  
				//exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, true);   
				exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
				exporter.exportReport();		
			}	
			
			if(request.getRequestURI().indexOf(".rtf")!=-1 || "rtf".equals(request.getParameter("_fmt"))){ // Word
				JRRtfExporter exporter = new JRRtfExporter();				
				response.setContentType("application/rtf") ;
				OutputStream ouputStream = response.getOutputStream();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
				exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
				exporter.exportReport();		
			}		
			
		
		}catch (JRException e) {
			if(FrameworkSetting.debug)e.printStackTrace();
			//response.getOutputStream().print("Jasper Error: " + e.getMessage());
			//response.getOutputStream().close();
//			bus.logException(e.getMessage(),PromisUtil.uInt(scd.get("customizationId")),PromisUtil.uInt(scd.get("userRoleId")));
			throw new IWBException("Error", "Jasper", jasperId, null, e.getMessage(), e.getCause());
		}finally{
			// Temp klasör içerisi de silinmeli
			virtualizer.cleanup();
		}
		return null;
	
	
	}

	@RequestMapping("/showFormByQuery")
	public void hndShowFormByQuery(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndShowFormByQuery");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		int formId = GenericUtil.uInt(request, "_fid");
		int queryId = GenericUtil.uInt(request, "_qid");
		W5FormResult formResult = engine.getFormResultByQuery(scd, formId, queryId,
				GenericUtil.getParameterMap(request));

		response.setContentType("application/json");
		response.getWriter().write(getViewAdapter(scd, request).serializeShowForm(formResult).toString());
		response.getWriter().close();

	}




	@RequestMapping("/getTableRecordInfo")
	public void hndGetTableRecordInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndGetTableRecordInfo");
		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
		int tableId = GenericUtil.uInt(request, "_tb_id");
		int tablePk = GenericUtil.uInt(request, "_tb_pk");
		W5TableRecordInfoResult r = engine.getTableRecordInfo(scd, tableId, tablePk);
		response.setContentType("application/json");
		response.getWriter().write(r != null ? getViewAdapter(scd, request).serializeTableRecordInfo(r).toString() : "{\"success\":false}");
		response.getWriter().close();
	}
	
	@RequestMapping("/getGraphDashboards")
	public void hndGetGraphDashboards(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndGetGraphDashboards");
		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		List<W5BIGraphDashboard> l = engine.getGraphDashboards(scd);
		if(GenericUtil.isEmpty(l)){
			response.getWriter().write("{\"success\":true,\"data\":[]}");
		} else {
			StringBuilder s = new StringBuilder();
			s.append("{\"success\":true,\"data\":[");
			boolean b = false;
			for(W5BIGraphDashboard gd:l){
				if(b)s.append(","); else b=true;
				s.append(f7.serializeGraphDashboard(gd, scd));
			}
			s.append("]}");

			response.getWriter().write(s.toString());
			
		}
		response.getWriter().close();
	}



	@RequestMapping("/ajaxGetLoginLang")
	public void hndGetLoginLang(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndGetLoginLang");
		Map<String, Object> scd = new HashMap<String, Object>();
		Map<String, String> req = GenericUtil.getParameterMap(request);
		scd.put("userId", 1);
		scd.put("customizationId", FrameworkCache.getAppSettingIntValue(0, "default_customization_id"));
		scd.put("locale", getDefaultLanguage(scd, req.get("language")));
		req.put("xlookup_id", "2");

		W5QueryResult queryResult = engine.executeQuery(scd, 337, req);

		response.setContentType("application/json");
		response.getWriter().write("{\"success\":true,\"q1\":");
		response.getWriter().write(getViewAdapter(scd, request).serializeQueryData(queryResult).toString());
		response.getWriter().write(",\"_localemsg\":");
		response.getWriter().write(GenericUtil.fromMapToJsonString2(LocaleMsgCache.getPublishLocale2(
				FrameworkCache.getAppSettingIntValue(0, "default_customization_id"), req.get("language"))));
		response.getWriter().write("}");
		response.getWriter().close();

	}

	@RequestMapping(value = "/multiupload.form", method = RequestMethod.POST)
	@ResponseBody
	public String multiFileUpload(@RequestParam("files") MultipartFile[] files,
			@RequestParam("customizationId") Integer customizationId, @RequestParam("userId") Integer userId,
			@RequestParam("table_pk") String table_pk, @RequestParam("table_id") Integer table_id,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("multiFileUpload");
		// Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
		String path = FrameworkCache.getAppSettingStringValue(customizationId, "file_local_path") + File.separator
				+ customizationId + File.separator + "attachment";

		File dirPath = new File(path);
		if (!dirPath.exists()) {
			if (!dirPath.mkdirs())
				return "{ \"success\":false, \"msg\":\"wrong file path: " + path + "\"}";
		}
		List<W5FileAttachment> lfa = new ArrayList<W5FileAttachment>();
		if (files.length > 0) {
			for (MultipartFile f : files)
				try {
					long fileId = new Date().getTime();
					W5FileAttachment fa = new W5FileAttachment();
					response.setContentType("application/json; charset=UTF-8");
					fa.setSystemFileName(fileId + "." + GenericUtil.strUTF2En(f.getOriginalFilename()));
					f.transferTo(new File(path + File.separator + fa.getSystemFileName()));
					int totalBytesRead = (int) f.getSize();
					fa.setCustomizationId(customizationId);
					fa.setOrijinalFileName(f.getOriginalFilename());
					fa.setTableId(table_id);
					fa.setTablePk(table_pk);
					fa.setTabOrder(Short.parseShort("1"));
					fa.setUploadUserId(userId);
					fa.setFileSize(totalBytesRead);
					fa.setActiveFlag((short) 1);
					lfa.add(fa);
					engine.saveObject(fa);
					String webPageId = request.getParameter(".w");
					if (!GenericUtil.isEmpty(webPageId))
						try {
							Map m = new HashMap();
							m.put(".w", webPageId);
							m.put(".pk", table_id + "-" + table_pk);
							m.put(".a", "11");
							m.put(".e", "2");
							Map scd = new HashMap();
							scd.put("customizationId", customizationId);
							scd.put("userId", userId);
							scd.put("sessionId", request.getSession(false).getId());
							UserUtil.liveSyncAction(scd, m);// (customizationId,
															// table_id+"-"+table_pk,
															// userId,
															// webPageId,
															// false);
						} catch (Exception e) {
							if (FrameworkSetting.debug)
								e.printStackTrace();
						}
					return "{ \"success\": true, \"fileId\": " + fa.getFileAttachmentId() + ", \"fileName\": '"
							+ GenericUtil.strUTF2En(GenericUtil.stringToJS(f.getOriginalFilename())) + "'}";

					// out.write("{success: true, fileId: "+
					// fa.getFileAttachmentId() +", fileName:
					// '"+f.getOriginalFilename()+"'}");
				} catch (Exception e) {
					if (FrameworkSetting.debug)
						e.printStackTrace();
					return "{ \"success\": false }";
				} finally {
					try {
						if (f.getInputStream() != null)
							f.getInputStream().close();
					} catch (Exception e2) {
						if (FrameworkSetting.debug)
							e2.printStackTrace();
					}
					// out.close();
				}
			// bus.saveAllObjectz(lfa);

		}
		return "{\"success\": false }";
	}

	@RequestMapping(value = "/upload.form", method = RequestMethod.POST)
	@ResponseBody
	public String singleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("table_pk") String table_pk,
			@RequestParam("table_id") Integer table_id, @RequestParam("profilePictureFlag") Integer profilePictureFlag,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("singleFileUpload");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
		Map<String, String> requestParams = GenericUtil.getParameterMap(request);

		String path = FrameworkCache.getAppSettingStringValue(scd.get("customizationId"), "file_local_path")
				+ File.separator + scd.get("customizationId") + File.separator + "attachment";

		File dirPath = new File(path);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		long fileId = new Date().getTime();
		int totalBytesRead = (int) file.getSize();

		W5FileAttachment fa = new W5FileAttachment();
		boolean ppicture = FrameworkSetting.profilePicture
				&& (GenericUtil.uInt(scd.get("customizationId")) == 0 || FrameworkCache
						.getAppSettingIntValue(scd.get("customizationId"), "profile_picture_flag") != 0)
				&& profilePictureFlag != null && profilePictureFlag != 0;
		try {
			// fa.setFileComment(bean.getFile_comment());
			fa.setCustomizationId(GenericUtil.uInt(scd.get("customizationId")));
			// fa.setFileDisciplineId(GenericUtil.uInteger(bean.getFile_discipline_id()));
			// fa.setFileTypeId(GenericUtil.uInteger(bean.getFile_type_id()));
			if (ppicture) {
				int maxFileSize = FrameworkCache.getAppSettingIntValue(0, "profile_picture_max_file_size", 51200);
				if (maxFileSize < totalBytesRead)
					return "{ \"success\": false , \"msg\":\"" + LocaleMsgCache.get2(scd, "max_file_size") + " = "
							+ Math.round(maxFileSize / 1024) + " KB\"}";
				fa.setFileTypeId(-999);// profile picture upload etti
			} else if (table_id == 338) {
				int maxFileSize = FrameworkCache.getAppSettingIntValue(0, "company_picture_max_file_size", 512000);
				if (maxFileSize < totalBytesRead)
					return "{ \"success\": false , \"msg\":\"" + LocaleMsgCache.get2(scd, "max_file_size") + " = "
							+ Math.round(maxFileSize / 1024) + " KB\"}";
				fa.setFileTypeId(-998);// company picture upload etti
			}
			fa.setSystemFileName(fileId + "." + GenericUtil.strUTF2En(file.getOriginalFilename()));
			file.transferTo(new File(path + File.separator + fa.getSystemFileName()));
			fa.setOrijinalFileName(file.getOriginalFilename());
			fa.setTableId(table_id);
			fa.setTablePk(table_pk);
			fa.setTabOrder((short) 1);
			fa.setUploadUserId(GenericUtil.uInt(scd.get("userId")));
			fa.setFileSize(totalBytesRead);
			fa.setActiveFlag((short) 1);
			try {
				if(!ppicture)if (GenericUtil.uStrNvl(requestParams.get("file_type_id"), "") != null) {
					fa.setFileTypeId(Integer.parseInt(GenericUtil.uStrNvl(requestParams.get("file_type_id"), "")));
				}
				if (GenericUtil.uStrNvl(requestParams.get("file_comment"), "") != null) {
					fa.setFileComment(GenericUtil.uStrNvl(requestParams.get("file_comment"), ""));
				}
			} catch (Exception e) {

			}
			engine.saveObject(fa);
			String webPageId = request.getParameter(".w");
			if (!GenericUtil.isEmpty(webPageId)) {
				Map m = new HashMap();
				m.put(".w", webPageId);
				m.put(".pk", table_id + "-" + table_pk);
				m.put(".a", "11");
				m.put(".e", "2");
				UserUtil.liveSyncAction(scd, m);// (customizationId,
												// table_id+"-"+table_pk,
												// userId, webPageId, false);

			}
			return "{ \"success\": true, \"fileId\": " + fa.getFileAttachmentId() + ", \"fileName\": \""
					+ GenericUtil.stringToJS(file.getOriginalFilename()) + "\", \"fileUrl\": \"" + "sf/"
					+ fa.getSystemFileName() + "?_fai=" + fa.getFileAttachmentId() + "\"}";
		} catch (Exception e) {
			if (true || FrameworkSetting.debug)
				e.printStackTrace();
			return "{ \"success\": false }";
		} /*
			 * finally { // transferTo yüzünden zaten hep exceptiona düşüyor.
			 * try {
			 * if(file.getInputStream()!=null)file.getInputStream().close(); }
			 * catch (Exception e2) {
			 * if(PromisSetting.debug)e2.printStackTrace(); } //
			 * response.getWriter().close(); }
			 */

	}

	@RequestMapping(value = "/upload2.form", method = RequestMethod.POST)
	@ResponseBody
	public String singleFileUpload4Webix(@RequestParam("upload") MultipartFile file, @RequestParam("table_pk") String table_pk,
			@RequestParam("table_id") Integer table_id, @RequestParam("profilePictureFlag") Integer profilePictureFlag,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("singleFileUpload4Webix");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
		Map<String, String> requestParams = GenericUtil.getParameterMap(request);

		String path = FrameworkCache.getAppSettingStringValue(scd.get("customizationId"), "file_local_path")
				+ File.separator + scd.get("customizationId") + File.separator + "attachment";

		File dirPath = new File(path);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		long fileId = new Date().getTime();
		int totalBytesRead = (int) file.getSize();

		W5FileAttachment fa = new W5FileAttachment();
		boolean ppicture = FrameworkSetting.profilePicture
				&& (GenericUtil.uInt(scd.get("customizationId")) == 0 || FrameworkCache
						.getAppSettingIntValue(scd.get("customizationId"), "profile_picture_flag") != 0)
				&& profilePictureFlag != null && profilePictureFlag != 0;
		try {
			// fa.setFileComment(bean.getFile_comment());
			fa.setCustomizationId(GenericUtil.uInt(scd.get("customizationId")));
			if (ppicture) {
				int maxFileSize = FrameworkCache.getAppSettingIntValue(0, "profile_picture_max_file_size", 51200);
				if (maxFileSize < totalBytesRead)
					return "{ \"success\": false , \"msg\":\"" + LocaleMsgCache.get2(scd, "max_file_size") + " = "
							+ Math.round(maxFileSize / 1024) + " KB\"}";
				fa.setFileTypeId(-999);// profile picture upload etti
			} else if (table_id == 338) {
				int maxFileSize = FrameworkCache.getAppSettingIntValue(0, "company_picture_max_file_size", 512000);
				if (maxFileSize < totalBytesRead)
					return "{ \"success\": false , \"msg\":\"" + LocaleMsgCache.get2(scd, "max_file_size") + " = "
							+ Math.round(maxFileSize / 1024) + " KB\"}";
				fa.setFileTypeId(-998);// company picture upload etti
			}
			fa.setSystemFileName(fileId + "." + GenericUtil.strUTF2En(file.getOriginalFilename()));
			file.transferTo(new File(path + File.separator + fa.getSystemFileName()));
			fa.setOrijinalFileName(file.getOriginalFilename());
			fa.setTableId(table_id);
			fa.setTablePk(table_pk);
			fa.setTabOrder((short) 1);
			fa.setUploadUserId(GenericUtil.uInt(scd.get("userId")));
			fa.setFileSize(totalBytesRead);
			fa.setActiveFlag((short) 1);
			try {
				if(!ppicture)if (GenericUtil.uStrNvl(requestParams.get("file_type_id"), "") != null) {
					fa.setFileTypeId(Integer.parseInt(GenericUtil.uStrNvl(requestParams.get("file_type_id"), "")));
				}
				if (GenericUtil.uStrNvl(requestParams.get("file_comment"), "") != null) {
					fa.setFileComment(GenericUtil.uStrNvl(requestParams.get("file_comment"), ""));
				}

			} catch (Exception e) {

			}
			engine.saveObject(fa);
			String webPageId = request.getParameter(".w");
			if (!GenericUtil.isEmpty(webPageId)) {
				Map m = new HashMap();
				m.put(".w", webPageId);
				m.put(".pk", table_id + "-" + table_pk);
				m.put(".a", "11");
				m.put(".e", "2");
				UserUtil.liveSyncAction(scd, m);// (customizationId,
												// table_id+"-"+table_pk,
												// userId, webPageId, false);

			}
			return "{ \"success\": true, \"fileId\": " + fa.getFileAttachmentId() + ", \"fileName\": \""
					+ GenericUtil.stringToJS(file.getOriginalFilename()) + "\", \"fileUrl\": \"" + "sf/"
					+ fa.getSystemFileName() + "?_fai=" + fa.getFileAttachmentId() + "\"}";
		} catch (Exception e) {
			if (true || FrameworkSetting.debug)
				e.printStackTrace();
			return "{ \"success\": false }";
		} /*
			 * finally { // transferTo yüzünden zaten hep exceptiona düşüyor.
			 * try {
			 * if(file.getInputStream()!=null)file.getInputStream().close(); }
			 * catch (Exception e2) {
			 * if(PromisSetting.debug)e2.printStackTrace(); } //
			 * response.getWriter().close(); }
			 */

	}
	/*
	 * // CKEDITOR File Browser İçin
	 * 
	 * @RequestMapping("/imageFileBrowser") public ModelAndView hndGetFiles(
	 * HttpServletRequest request, HttpServletResponse response) throws
	 * ServletException, IOException{ Map<String, Object> scd =
	 * UserUtil.getScd(request, "scd-dev", true); Map<String,String> requestParams =
	 * GenericUtil.getParameterMap(request); HashMap<String,String> lookUp =
	 * engine.getFileTypes(scd,1); Map<String, Object> values =
	 * engine.getImage4FBrowser(scd, requestParams); ModelAndView m = new
	 * ModelAndView("imageFileBrowser");
	 * m.addObject("baseUrl",GenericUtil.getBaseURL(request));
	 * m.addObject("images", (List<HashMap>) values.get("images"));
	 * m.addObject("imgCount", values.get("imgCount")); m.addObject("pageNo",
	 * GenericUtil.uInt(requestParams.get("pageno"))); m.addObject("groupByNum",
	 * GenericUtil.uInt(requestParams.get("groupbynum")));
	 * m.addObject("CKEditor",
	 * GenericUtil.uStrNvl(requestParams.get("CKEditor"),""));
	 * m.addObject("CKEditorFuncNum",
	 * GenericUtil.uStrNvl(requestParams.get("CKEditorFuncNum"),""));
	 * m.addObject("langCode",
	 * GenericUtil.uStrNvl(requestParams.get("langCode"),""));
	 * m.addObject("fileName",
	 * GenericUtil.encodeGetParamsToUTF8(GenericUtil.uStrNvl(requestParams.get(
	 * "fileName"),""))); m.addObject("fileType",
	 * GenericUtil.uStrNvl(requestParams.get("fileType"),""));
	 * m.addObject("lookup", lookUp); return m; }
	 * 
	 * @RequestMapping("/imageFileBrowserUpload") public ModelAndView
	 * hndUploadPage( HttpServletRequest request, HttpServletResponse response)
	 * throws ServletException, IOException{ Map<String, Object> scd =
	 * UserUtil.getScd(request, "scd-dev", true); HashMap<String,String> lookUp =
	 * engine.getFileTypes(scd,1); Map<String,String> requestParams =
	 * GenericUtil.getParameterMap(request); ModelAndView m = new
	 * ModelAndView("imageFileBrowserUpload");
	 * m.addObject("baseUrl",GenericUtil.getBaseURL(request));
	 * m.addObject("lookup", lookUp); m.addObject("scd-dev",scd); return m; }
	 * 
	 */
	@RequestMapping("/ajaxCacheInfo")
	public void hndAjaxCacheInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", false);
		int customizationId = (Integer) scd.get("customizationId");
		if (customizationId == 0 && GenericUtil.uInt(request, "cusId") != 0)
			customizationId = GenericUtil.uInt(request, "cusId");
		List<Integer> l = new ArrayList();
		if (customizationId != 0)
			l.add(customizationId);
		else
			for (W5Customization c : FrameworkCache.wCustomization)
				l.add(c.getCustomizationId());
		response.setContentType("application/json");
		StringBuilder sb = new StringBuilder();
		sb.append("{\"success\":true,\"result\":{\n");
		// customizationId:{TableChacheCount, FormCacheCount, GridCacheCount,
		// ObjectCacheCount, }
		boolean b1 = false;
		for (Integer c : l) {
			if (b1)
				sb.append(",\n");
			else
				b1 = true;
			sb.append("\"").append(c).append("\":{");
			sb.append("\"tableCacheCount\":").append(GenericUtil.getSafeSize(FrameworkCache.wTables.get(c)));
			sb.append(",\n \"formCacheCount\":").append(GenericUtil.getSafeSize(FrameworkCache.wForms.get(c)));
			sb.append(",\n \"gridCacheCount\":").append(GenericUtil.getSafeSize(FrameworkCache.wGrids.get(c)));
			sb.append(",\n \"queryCacheCount\":").append(GenericUtil.getSafeSize(FrameworkCache.wQueries));
			sb.append(",\n \"listViewCacheCount\":").append(GenericUtil.getSafeSize(FrameworkCache.wListViews.get(c)));
			sb.append(",\n \"dataViewCacheCount\":").append(GenericUtil.getSafeSize(FrameworkCache.wDataViews.get(c)));
			sb.append(",\n \"widgetCacheCount\":0");
			sb.append(",\n \"localeCacheCount\":{");
			boolean b2 = false;
			for (String loc : LocaleMsgCache.localeMap2.keySet()) {
				if (b2)
					sb.append(",");
				else
					b2 = true;
				sb.append("\n  \"").append(loc).append("\":")
						.append(GenericUtil.getSafeSize(LocaleMsgCache.localeMap2.get(loc)));
			}
			sb.append("}");

			int cnt = 0;
			for (Integer t : FrameworkCache.wTables.get(c).keySet()) {
				cnt += GenericUtil.getSafeSize(FrameworkCache.getTableCacheMap(c, t));
			}
			sb.append(",\n \"objectCacheCount\":").append(cnt);
			sb.append(",\n \"feedCacheCount\":").append(GenericUtil.getSafeSize(FrameworkCache.wFeeds.get(c)));
			sb.append(",\n \"jobCacheCount\":").append(GenericUtil.getSafeSize(FrameworkCache.wJobs));

			sb.append("}");

		}

		sb.append("}");

		sb.append("}");
		response.getWriter().write(sb.toString());
		response.getWriter().close();
	}



	/*
	 * private class executeQueuedMobilePushMessage implements Runnable {
	 * 
	 * private List<W5QueuedPushMessageHelper> listQueuedPushMessage;
	 * 
	 * @Override public void run() { if(listQueuedPushMessage!=null){ String
	 * path = FrameworkCache.getAppSettingStringValue(0,
	 * "mobile_push_key_path"); String passWord =
	 * FrameworkCache.getAppSettingStringValue(0, "mobile_push_password");
	 * if(passWord==null)passWord=""; List<PayloadPerDevice> payloadDevicePairs
	 * = new ArrayList<PayloadPerDevice>(); for(W5QueuedPushMessageHelper
	 * m:listQueuedPushMessage)if(!GenericUtil.isEmpty(m.getDeviceToken()))
	 * switch(m.getDeviceName()){ case 1://ios try{ PushNotificationPayload
	 * payload = PushNotificationPayload.complex();
	 * payload.addAlert(m.getMsg()); payload.addCustomDictionary("tableId",
	 * m.getTableId()); payload.addCustomDictionary("tablePk", m.getTablePk());
	 * if(FrameworkSetting.mobilePushSound &&
	 * FrameworkCache.getAppSettingIntValue(m.getCustomizationId(),
	 * "mobile_push_sound_flag")!=0)payload.addSound("default"); //
	 * payload.setCharacterEncoding("UTF-8"); payloadDevicePairs.add(new
	 * PayloadPerDevice(payload, m.getDeviceToken())); } catch (JSONException e)
	 * { if(FrameworkSetting.debug)e.printStackTrace(); } catch
	 * (InvalidDeviceTokenFormatException e) {
	 * if(FrameworkSetting.debug)e.printStackTrace(); } try {
	 * List<PushedNotification> notifications = Push.payloads(path, passWord,
	 * FrameworkSetting.mobilePushProduction, payloadDevicePairs); for
	 * (PushedNotification notification : notifications) { if
	 * (notification.isSuccessful()) { if(FrameworkSetting.debug)logger.info(
	 * "Push notification sent successfully to: "
	 * +notification.getDevice().getToken()); } else { String invalidToken =
	 * notification.getDevice().getToken(); Exception theProblem =
	 * notification.getException(); theProblem.printStackTrace();
	 * 
	 * ResponsePacket theErrorResponse = notification.getResponse(); if
	 * (theErrorResponse != null) {
	 * if(FrameworkSetting.debug)logger.error(theErrorResponse.getMessage()); }
	 * } } } catch (CommunicationException e) {
	 * if(FrameworkSetting.debug)e.printStackTrace(); } catch (KeystoreException
	 * e) { if(FrameworkSetting.debug)e.printStackTrace(); } break; case
	 * 2://android
	 * 
	 * List<String> androidTargets =new ArrayList<String>();
	 * androidTargets.add(m.getDeviceToken());
	 * 
	 * // Instance of com.android.gcm.server.Sender, that does the //
	 * transmission of a Message to the Google Cloud Messaging service. Sender
	 * sender = new Sender("AIzaSyCTJ4moxk1qqZ47dv2QiJ2cifBy_YyuPTM");//app_id
	 * 
	 * // This Message object will hold the data that is being transmitted // to
	 * the Android client devices. For this demo, it is a simple text // string,
	 * but could certainly be a JSON object. Message message = new
	 * Message.Builder()
	 * 
	 * // If multiple messages are sent using the same .collapseKey() // the
	 * android target device, if it was offline during earlier message //
	 * transmissions, will only receive the latest message for that key when //
	 * it goes back on-line. .collapseKey(m.getTableId()+"-"+m.getTablePk())
	 * .timeToLive(30) .delayWhileIdle(true) .addData("message", m.getMsg())
	 * .addData("header", m.getTableId() == 935 ? "New Chat Message" :
	 * "New Notificaton") .build();
	 * 
	 * try { // use this for multicast messages. The second parameter // of
	 * sender.send() will need to be an array of register ids. MulticastResult
	 * result = sender.send(message, androidTargets, 1);
	 * 
	 * if (result.getResults() != null) { int canonicalRegId =
	 * result.getCanonicalIds(); if (canonicalRegId != 0) {
	 * 
	 * } } else { int error = result.getFailure();
	 * if(FrameworkSetting.debug)logger.error("Broadcast failure: " + error); }
	 * 
	 * } catch (Exception e) { if(FrameworkSetting.debug)e.printStackTrace(); }
	 * 
	 * } } // UserUtil.publishNotification(n, false); }
	 * 
	 * 
	 * public executeQueuedMobilePushMessage(List<W5QueuedPushMessageHelper>
	 * listQueuedPushMessage){ this.listQueuedPushMessage =
	 * listQueuedPushMessage; } }
	 */
	private class executeQueuedDbFunc implements Runnable {// TODO: buralar long
															// polling ile
															// olacak
		private W5QueuedDbFuncHelper queuedDbFunc;
		private long threadId;

		public long getThreadId() {
			return threadId;
		}

		@Override
		public void run() {
			W5Notification n = new W5Notification();// sanal
			n.setUserId((Integer) queuedDbFunc.getScd().get("userId"));
			n.setUserTip((short) GenericUtil.uInt(queuedDbFunc.getScd().get("userTip")));
			try {
				// qt.put(th.getId(), executeDbFunc(scd, dbFuncId, parameterMap,
				// execRestrictTip));//is bitince, result
				W5DbFuncResult result = engine.executeDbFunc(queuedDbFunc.getScd(), queuedDbFunc.getDbFuncId(),
						queuedDbFunc.getParameterMap(), queuedDbFunc.getExecRestrictTip());// is
																							// bitince,
																							// result
				String lbl, sbj;
				switch (queuedDbFunc.getDbFuncId()) {
				case -650:
					lbl = "eMail";
					sbj = queuedDbFunc.getParameterMap().get("pmail_subject");
					break;
				case -631:
					lbl = "SMS";
					sbj = queuedDbFunc.getParameterMap().get("phone") + " - "
							+ queuedDbFunc.getParameterMap().get("body");
					break;
				default:
					lbl = "Other";
					sbj = "???";
				}
				if (result.isSuccess() && !queuedDbFunc.getParameterMap().containsKey("perror_msg")) {// error_msg
																										// yoksa
					n.setNotificationTip((short) 18);// exec-basarili: atiyorum
					n.set_tmpStr("<b>" + lbl + ":</b>" + sbj);
				} else {
					n.setNotificationTip((short) 10);// exec-basarisiz: atiyorum
					String err = queuedDbFunc.getParameterMap().get("perror_msg");
					if (err == null)
						err = GenericUtil.fromMapToHtmlString(result.getErrorMap());
					n.set_tmpStr("<b>" + lbl + " error(s):</b>" + err);
				}
			} catch (Exception e) {
				n.setNotificationTip((short) 10);// exec-basarisiz: atiyorum
				n.setShowUrl(e.getMessage());
			}

			UserUtil.publishNotification(n, false);
		}

		public executeQueuedDbFunc(W5QueuedDbFuncHelper queuedDbFunc) {
			this.queuedDbFunc = queuedDbFunc;
			this.threadId = GenericUtil.getNextThreadId();
		}
	}

	@RequestMapping("/ajaxSendFormSmsMail")
	public void hndAjaxSendFormSmsMail(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxSendFormSmsMail");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		response.setContentType("application/json");
		int smsMailId = GenericUtil.uInt(request, "_fsmid");
		W5DbFuncResult dbFuncResult = engine.sendFormSmsMail(scd, smsMailId, GenericUtil.getParameterMap(request));
		response.getWriter().write(getViewAdapter(scd, request).serializeDbFunc(dbFuncResult).toString());
		response.getWriter().close();

	}

	
	@RequestMapping("/ajaxGlobalNextVal")
	public void hndAjaxGlobalNextVal(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		String id=request.getParameter("id");
		String key=request.getParameter("key");
		
		int nextVal = engine.getGlobalNextval(id, key, request.getRemoteAddr());
		
		response.getWriter().write("{\"success\":true, \"val\":"+nextVal+"}"); //hersey duzgun
		response.getWriter().close();
		
	}
	@RequestMapping("/ajaxOrganizeTable")
	public void hndAjaxOrganizeTable(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		String tableName = request.getParameter("ptable_dsc");
		logger.info("hndAjaxOrganizeTable("+tableName+")"); 
    	Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
    	boolean b = (Integer)scd.get("roleId")!=0 ? false : engine.organizeTable(scd, tableName);
		response.setContentType("application/json");
		response.getWriter().write("{\"success\":"+b+"}");
		response.getWriter().close();
	}

	@RequestMapping("/ajaxCopyTable2Tsdb")
	public void hndAjaxCopyTable2Tsdb(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		int tableId = GenericUtil.uInt(request, "_tid");
		int measurementId = GenericUtil.uInt(request, "_mid");
		logger.info("hndAjaxCopyTable2Tsdb("+tableId+")"); 
    	Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
    	boolean b = (Integer)scd.get("roleId")!=0 ? false : engine.copyTable2Tsdb(scd, tableId, measurementId);
		response.setContentType("application/json");
		response.getWriter().write("{\"success\":"+b+"}");
		response.getWriter().close();
	}
	@RequestMapping("/ajaxOrganizeDbFunc")
	public void hndAjaxOrganizeDbFunc(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		String dbFuncName = request.getParameter("pdb_func_dsc");
		logger.info("hndAjaxOrganizeDbFunc("+dbFuncName+")"); 
    	Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
    	boolean b = (Integer)scd.get("roleId")!=0 ? false : engine.organizeDbFunc(scd, dbFuncName);
		response.setContentType("application/json");
		response.getWriter().write("{\"success\":"+b+"}");
		response.getWriter().close();
	}
	
	
	@RequestMapping("/ajaxBuildForm")
	public void hndAjaxBuildForm(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException, JSONException {
		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
		
		int i = engine.buildForm(scd, request.getParameter("data"));
		response.setContentType("application/json");
		response.getWriter().write("{\"success\":true, \"result\":"+i+"}");
		response.getWriter().close();
	}
	
	@RequestMapping("/ajaxFormBuilderSync")
	public void hndAjaxFormBuilderSync(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, JSONException {
		logger.info("hndAjaxFormBuilderSync");
		JSONObject jo = HttpUtil.getJson(request);
		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
		
		String s = f7.serializeFormFromJSON(jo).toString();
		int cnt = UserUtil.publishFormBuilderData2Mobile((Integer) scd.get("customizationId"), s);
		response.getWriter().write("{\"success\":true, \"delivered_cnt\":"+cnt+"}");
		response.getWriter().close();
	}
		

	
	@RequestMapping("/prj/*/showForm")
	public void hndPrjShowForm(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		String projectUuid = request.getPathInfo().substring(5, 41);
		logger.info("hndPrjShowForm("+projectUuid+")");
		
		Map<String, Object> scd = null;
		HttpSession session = request.getSession(false);
		if(session!=null){
			scd =	(Map)session.getAttribute("scd-dev"); 
		}
		if(scd==null){
			scd = new HashMap();
			scd.put("locale", "tr");
			int userId = 11; //Guest
			if(userId<0)userId=-userId;
			scd.put("userId", userId);
			scd.put("customizationId", 0);
			scd.put("roleId", 0);
			scd.put("userTip", 0);
			
			session = request.getSession(true);
			session.setAttribute("scd-dev", scd);
		}

		ViewAdapter va = getViewAdapter(scd, request, webix3_3); 


		
		int formId= GenericUtil.uInt(request, "_fid");
		int action= GenericUtil.uInt(request, "a");
		W5FormResult formResult = engine.getFormResult(scd, formId, action, GenericUtil.getParameterMap(request));

		W5TemplateResult templateResult = engine.getTemplateResult(scd, va instanceof Webix3_3 ? 11: 12, GenericUtil.getParameterMap(request));
		templateResult.setTemplateObjectList(new ArrayList());
		templateResult.getTemplateObjectList().add(formResult);

		response.getWriter().write(va.serializeTemplate(templateResult).toString());
		response.getWriter().close();
	}
		
	@RequestMapping("/prj/*/ajaxPostForm")
	public void hndPrjAjaxPostForm(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		
		Map<String, Object> scd = null;
		HttpSession session = request.getSession(false);
		if(session!=null){
			scd =	(Map)session.getAttribute("scd-dev"); 
		}
		if(scd==null){
			scd = new HashMap();
			scd.put("locale", "tr");
			int userId = 11;//(int)GenericUtil.getNextLongId();
			if(userId<0)userId=-userId;
			scd.put("userId", userId);
			scd.put("customizationId", 0);
			scd.put("roleId", 0);
			scd.put("userTip", 0);
			
			session = request.getSession(true);
			session.setAttribute("scd-dev", scd);
		}

		int formId= GenericUtil.uInt(request, "_fid");
		int action= GenericUtil.uInt(request, "a");
		W5FormResult formResult = engine.postForm4Table(scd, formId, action, GenericUtil.getParameterMap(request),"");
		response.setContentType("application/json");
		response.getWriter().write(getViewAdapter(scd, request).serializePostForm(formResult).toString());
		response.getWriter().close();
		
		if(formResult.getErrorMap().isEmpty()){
			UserUtil.syncAfterPostFormAll(formResult.getListSyncAfterPostHelper());
//			UserUtil.mqSyncAfterPostFormAll(formResult.getScd(), formResult.getListSyncAfterPostHelper());
		}

	}
	
	@RequestMapping("/ajaxCallWs")
	public void hndAjaxCallWs(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxCallWs"); 
	    Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
	    
		Map m =engine.callWs(scd, request.getParameter("serviceName"), GenericUtil.getParameterMap(request));
		response.getWriter().write(GenericUtil.fromMapToJsonString2Recursive(m));
		response.getWriter().close();		
	}

	/*@RequestMapping("/ajaxDefineWs")
	public void hndAjaxDefineWs(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxDefineWs"); 
	    Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
	    int wsId= GenericUtil.uInt(request, "_wsid");
		response.getWriter().write("{\"success\":"+engine.extractWs(scd, wsId)+"}");
		response.getWriter().close();		
	}*/
	

	@RequestMapping("/ajaxQueryData4Debug")
	public void hndAjaxQueryData4Debug(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxQueryData4Debug"); 
		
    	Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
		int roleId =(Integer)scd.get("roleId");
		if(roleId!=0){
			throw new IWBException("security","Developer",0,null, "You Have to Be Developer TO Run this", null);
		}

		int queryId= GenericUtil.uInt(request, "_qid");

		Object o = engine.executeQuery4Debug(scd, queryId, GenericUtil.getParameterMap(request));
		
		response.setContentType("application/json");
		if(o instanceof W5QueryResult)
			response.getWriter().write(getViewAdapter(scd, request).serializeQueryData((W5QueryResult)o).toString());
		else {
			Map m = (Map)o;//new HashMap();
			m.put("success", true);
//			m.put("data", queryResult.getData());
//			Map m2 = new HashMap();m2.put("startRow", 0);m2.put("fetchCount", queryResult.getData().size());m2.put("totalCount", queryResult.getData().size());
//			m.put("browseInfo", m2);
	//		m.put("sql", queryResult.getExecutedSql());
			response.getWriter().write(GenericUtil.fromMapToJsonString2Recursive(m));
		}
		response.getWriter().close();

	}
	
	@RequestMapping("/ajaxQueryData4Pivot")
	public void hndAjaxQueryData4Pivot(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int tableId = GenericUtil.uInt(request, "_tid");
		logger.info("hndAjaxQueryData4Pivot(" + tableId + ")");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		response.setContentType("application/json");
		response.getWriter().write(GenericUtil.fromListToJsonString2Recursive(engine.executeQuery4Pivot(scd, tableId, GenericUtil.getParameterMap(request))));
		response.getWriter().close();
	}
	
	@RequestMapping("/ajaxQueryData4DataList")
	public void hndAjaxQueryData4DataList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int tableId = GenericUtil.uInt(request, "_tid");
		logger.info("hndAjaxQueryData4DataList(" + tableId + ")");

		Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);

		response.setContentType("application/json");
		response.getWriter().write(GenericUtil.fromListToJsonString2Recursive(engine.executeQuery4DataList(scd, tableId, GenericUtil.getParameterMap(request))));
		response.getWriter().close();
	}
	
	
	@RequestMapping("/ajaxExecDbFunc4Debug")
	public void hndAjaxExecDbFunc4Debug(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("hndAjaxExecDbFunc4Debug"); 
		
    	Map<String, Object> scd = UserUtil.getScd(request, "scd-dev", true);
		int roleId =(Integer)scd.get("roleId");
		if(roleId!=0){
			throw new IWBException("security","Developer",0,null, "You Have to Be Developer TO Run this", null);
		}

		int dbFuncId= GenericUtil.uInt(request, "_did"); // +:dbFuncId, -:formId

		W5DbFuncResult dbFuncResult = engine.executeDbFunc4Debug(scd, dbFuncId, GenericUtil.getParameterMap(request));

		response.setContentType("application/json");
		response.getWriter().write(getViewAdapter(scd, request).serializeDbFunc(dbFuncResult).toString());
		response.getWriter().close();

	}
}