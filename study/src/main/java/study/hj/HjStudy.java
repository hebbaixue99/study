package study.hj;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.InflaterOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import study.tools.DateUtil;

import com.alibaba.fastjson.JSONObject;


public class HjStudy {
	Map<String, String> loginPageCookies = new HashMap();
	Map<String, String> userCookies = new HashMap();
	JSONObject courses = new JSONObject();
   public void login(){
	   try{
		   
		   File file = new File("e:/userCookies1.txt");
		   if (file.exists()){
		   FileInputStream fis=new FileInputStream("e:/userCookies1.txt");  
		   ObjectInputStream ois=new ObjectInputStream(fis);
		   //readObject 方法用于从流读取对象。应该使用 Java 的安全强制转换来获取所需的类型。在 Java 中，字符串和数组都是对象，所以在序列化期间将其视为对象。读取时，需要将其强制转换为期望的类型。
		   //connection.setRequestProperty("Accept-Charset", "utf-8");  //设置编码语言
           //connection.setRequestProperty("X-Auth-Token", "token");  //设置请求的token
           //connection.setRequestProperty("Connection", "keep-alive");  //设置连接的状态
		   userCookies=(HashMap)ois.readObject(); 
		   ois.close(); 
		   }
		   String login="https://pass.hujiang.com/v2/Handler/UCenter.json?action=Login&business_domain=yyy_cichang&captchaVersion=2&imgcode=&isapp=true&loginType=normal&macRawPwd=false&password=e0ead475ec608460c0cdebf48d3db9b6&token=&userName=13671326787&user_domain=hj ";
		   if (userCookies.size()<=0){
	       loginPageCookies = Jsoup.connect(login)
	    		   .header("Content-Type", "text/*")
	    		   .header("Accept-Charset", "utf-8")
	    		   .header("Mimetype", "application/json")
	    		   .header("charset", "utf-8")
	            .method(Connection.Method.GET).execute().cookies();
	       
	     Connection.Response loginResponse = Jsoup.connect(login)
	    		 .header("Content-Type", "application/json")
	    		 .header("Accept-Charset", "utf-8")
	            //.data("action", "login", "username", "hebbaixue", "password", "bx117128", "forward","")
	            .header("Device-Id", "62F56455-CBD8-411D-8279-B7252635E22B")
	            .header("local-Date", DateUtil.getStringDate())
	            .header("hj-appsign", "62F56455-CBD8-411D-8279-B7252635E22B")
	            .header("hj-appkey", "62F56455-CBD8-411D-8279-B7252635E22B")
	            .header("hj-deviceId", "62F56455-CBD8-411D-8279-B7252635E22B")
	            .cookies(loginPageCookies)
	            .method(Connection.Method.GET).execute();
	     userCookies = loginResponse.cookies();
	     if (userCookies==null || userCookies.size()==0){
	    	 userCookies = loginPageCookies; 
	     }
		   }
		   Element indexResponse = Jsoup.connect("https://pass-cdn.hjapi.com/v1.1/access_token/convert")
	            .cookies(userCookies).get().body();
		  //System.out.println(indexResponse);
	  FileOutputStream fos=new FileOutputStream("e:/userCookies1.txt",false);
	  //文件的序列化  
	  ObjectOutputStream oos=new ObjectOutputStream(fos);
	  //writeObject 方法用于将对象写入流中。所有对象（包括 String 和数组）都//可以通过 writeObject 写入。  
	  oos.writeObject(userCookies);  
	  oos.close();  
	    
	   }catch(Exception e){
		   e.printStackTrace();
	   } 
   }
   public JSONObject getCourse(String url){
	  String json = "";
	  try{
	 Connection.Response courseResponse = Jsoup.connect(url).header("Content-Type", "application/json;charset=UTF-8").ignoreContentType(true).execute() ;
	 json=courseResponse.body();
	  }catch(Exception e){
		  e.printStackTrace();
	  }
	  JSONObject jo = JSONObject.parseObject(json);
	  return jo;
   }
   public void getCourseList(String url,String cid){
	   try{
	   Document doc = Jsoup.connect(url).cookies(userCookies).get();
	   
	     // FileOutputStream fos=new FileOutputStream("d:/success/member_coursedetail_"+cid+".html",false);
	      /* 写入Txt文件 */  
         /* File writename = new File("d:/success/member_coursedetail_"+cid+".html"); // 相对路径，如果没有则要建立一个新的output。txt文件  
          writename.createNewFile(); // 创建新文件  
          BufferedWriter out = new BufferedWriter(new FileWriter(writename));  
          out.write(doc.toString()); // \r\n即为换行  
          out.flush(); // 把缓存区内容压入文件  
          out.close(); // 最后记得关闭文件  
         */
	    Elements ems= doc.getElementsByClass("lesson_content1");
	   if (ems!=null&&ems.size()>0){
		   Elements as=ems.get(0).select("a");
		   if(as!=null){
			   for(int i= 0; i<as.size();i++){
				 Element a = as.get(i);
				// System.out.println(a.attr("href"));
				
				String ccid = a.attr("href").replace(".", ",").split(",")[0].split("-")[1];
				String flash =getFlash("./"+a.attr("href"));
				//String flash =getFlash("http://127.0.0.1/"+a.attr("href"));
				 courses.put(ccid, flash); 
				//System.out.println("http://127.0.0.1:8000/playvideo.html?cid="+ccid);
			   }
		   }
	   } 
	   
	   
	   }catch(Exception e){
		   
	   }
   }
   public String getFlash(String url){
	   try{
	   String doc = Jsoup.connect(url).cookies(userCookies).execute().body();
	   String[] docs = doc.split("var flashvars_7ehdplayer = ");
	   if (docs.length>1){
		   String ds = docs[1];
		   String[] fs = ds.split("var params_7ehdplayer");
		   if (fs.length>1){
			   String json= fs[0].trim().replace(";", "");
			   json= json.split("src:")[1].split(".f4m")[0];
			   json=json.trim().replace("\"", "")+".f4m";
			   return json; 
		   }
	   }
	   }catch(Exception e){
		  e.printStackTrace(); 
	   }
	   return "";
   }
   public static void main1(String[] args){
	   study.FileUtil.readFile("d:/flash");
	   
   }
   public static void main(String[] args){
	   HjStudy h = new HjStudy();
	   h.login();
	   
       
   }
   
}

