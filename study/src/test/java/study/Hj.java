package study;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import study.tools.DateUtil;
import study.tools.HttpClientUtil;
import study.tools.JsonFormatTool;
import study.tools.MyHttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Hj {
	private static String dir="./data/";
	private static int bookId = 0 ;
	private static int unitid = 0;
	private static boolean force = false;
	private static String update = "";
	private static int studyCount= 10;
	private static int studyWordCount=10;
	
	public static void main(String[] args) {
	  init(10856,0,10,false,"2018-09-07T08:40:00.000");
	  login();	
	  String token= getToken();
	  String books = getBook(force);
	  study(token,books);
	  /*JSONObject hjStudy = new JSONObject();
	  MyHttpResponse login = new MyHttpResponse();
	  MyHttpResponse token = new MyHttpResponse();
	  JSONObject config = new JSONObject();
	  hjStudy.put("login", login);
	  hjStudy.put("token", token);
	  hjStudy.put("config", config);
	  //saveData("hjStudy",hjStudy);
	  JSONObject t = (JSONObject)loadData("hjStudy",null);
	  System.out.println(t);*/
	  
	}
	public static void init(int _bookId,int _unitid, int _studyCount,int _studyWordCount,boolean _force,String _update){
		update=_update ;
		bookId = _bookId;
		unitid=_unitid;
		force=_force;
		studyCount=_studyCount;
		studyWordCount = _studyWordCount;
	}
	public static String getBook(){
		JSONObject oj = new JSONObject();
		// 12817 "unitId":111,"userID":76615124,"isFinished":true,"bookId":11990,"unitIndex":111,"studyWordCount":10,"studyStars":3,"finishedDate":"2018-09-04T19:46:00.562"
		int id = 30;
		oj.put("unitId", unitid);
		oj.put("unitIndex", unitid);
		oj.put("userID", 76615124);
		oj.put("isFinished", true);
		oj.put("studyWordCount",studyWordCount);
		oj.put("studyStars", 3);
		oj.put("bookId", bookId);
		oj.put("studyCount", studyCount);
		//oj.put("finishedDate", "2018-09-04T" + "21:" + (i+10) +":"+(int)(Math.random()*50 + 10)+"."+(int)(Math.random()*900 + 100));
		//String sd = "2018-09-07T08:40:00.000";
		//sd = DateUtil.getPreTime(sd);
		oj.put("finishedDate",update);
		if (force){
		   oj = (JSONObject)loadData("hjConfig",oj);
		}else{
			
		}
		String study = updateBook(oj);
		return study;
		
	}
	public static void study(String token,String books){
		String studyUrl = "https://cichang.hjapi.com/v3/user/me/books/finished_units";
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Device-Id", "62F56455-CBD8-411D-8279-B7252635E22B");
		headers.put("local-Date", DateUtil.getStringDate());
		headers.put("Access-Token", token);
		MyHttpResponse res = HttpClientUtil.httpPutRaw(studyUrl, books, headers, "UTF-8");
		System.out.println(res.getBody());

	}
	public static void login(){
		try{
		  
		  String loginUrl = "https://pass.hujiang.com/v2/Handler/UCenter.json?";
		  String req= "action=Login&business_domain=yyy_cichang&captchaVersion=2&isapp=true&loginType=normal&macRawPwd=false&password=e0ead475ec608460c0cdebf48d3db9b6&userName=13671326787&user_domain=hj";
		  MyHttpResponse res = (MyHttpResponse)loadData("hjLogin",null); 
		  if (res==null){
			  res= HttpClientUtil.httpGet(loginUrl+req, null, "UTF-8");
			  saveData("hjLogin",res);
		  }
		 // System.out.println(res.getBody());
		 // System.out.println(res.getHeaders());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static String getToken(){
		MyHttpResponse res = (MyHttpResponse)loadData("hjLogin",null);
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Device-Id", "62F56455-CBD8-411D-8279-B7252635E22B");
		headers.put("local-Date", DateUtil.getStringDate());
		headers.put("hj_deviceId", "62F56455-CBD8-411D-8279-B7252635E22B");
		headers.put("hj_appsign", "9294608a724f43e348f1950112714d97");
		headers.put("hj_appkey", "45fd17e02003d89bee7f046bb494de13");
		
		JSONObject oj = JSONObject.parseObject(res.getBody());
		JSONObject req = new JSONObject();
		req.put("club_auth_cookie", oj.getJSONObject("Data").getString("Cookie"));
		String tokenUrl="https://pass-cdn.hjapi.com/v1.1/access_token/convert";
		String stringJson = req.toJSONString();
		MyHttpResponse token = (MyHttpResponse)loadData("hjToken",null);
		if (token==null){
		   token= HttpClientUtil.httpPostRaw(tokenUrl,stringJson, headers, "UTF-8");
		   saveData("hjToken",token);
		}
		//System.out.println(token.getBody());
		JSONObject tk = JSONObject.parseObject(token.getBody());
		return tk.getJSONObject("data").getString("access_token");
		
	}
	public static String  updateBook(JSONObject config){
		JSONArray oa = new JSONArray();
		int k = config.getIntValue("unitIndex");
		String sd = config.getString("finishedDate");
		sd = DateUtil.getPreTime(sd);   
		for (int i = 0; i < config.getIntValue("studyCount"); i++) {
			JSONObject oj = new JSONObject();
			// "unitId":111,"userID":76615124,"isFinished":true,"bookId":11990,"unitIndex":111,"studyWordCount":10,"studyStars":3,"finishedDate":"2018-09-04T19:46:00.562"
			k++;
			oj.put("unitId", k);
			oj.put("unitIndex", k);
			oj.put("userID", 76615124);
			oj.put("isFinished", true);
			oj.put("studyWordCount", config.getInteger("studyWordCount"));
			oj.put("studyStars", 3);
			oj.put("bookId", config.getInteger("bookId"));
			//oj.put("finishedDate", "2018-09-04T" + "21:" + (i+10) +":"+(int)(Math.random()*50 + 10)+"."+(int)(Math.random()*900 + 100));
			sd = DateUtil.getPreTime(sd);
			oj.put("finishedDate",sd);
			oa.add(oj);
		}
		config.put("unitIndex", k);
		config.put("finishedDate", sd);
		String jsonString = JSON.toJSONString(oa,
		SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
		//System.out.println(jsonString);
		System.out.println(JsonFormatTool.formatJson(oa.toJSONString()));
		
        saveData("hjConfig",config);
        return JsonFormatTool.formatJson(oa.toJSONString());
	}
	private static Object loadData(String fn, Object config){
		Object ret=  config;
		try{
		   File file = new File(dir+fn);
		   if (file.exists()){
		   FileInputStream fis=new FileInputStream(dir+fn);  
		   ObjectInputStream ois=new ObjectInputStream(fis);
		   ret=ois.readObject(); 
		   ois.close(); 
		   }else{
			   
		   }
		}catch(Exception e){
			
		}
		return ret;
	}
	private static void saveData(String fn, Object config){
		try{
		 FileOutputStream fos=new FileOutputStream(dir+fn,false);
		  //文件的序列化  
		  ObjectOutputStream oos=new ObjectOutputStream(fos);
		  //writeObject 方法用于将对象写入流中。所有对象（包括 String 和数组）都//可以通过 writeObject 写入。  
		  oos.writeObject(config);  
		  oos.close(); 
		}catch(Exception e){}
	}
	
	
}
