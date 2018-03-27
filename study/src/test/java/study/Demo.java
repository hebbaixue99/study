package study;

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

import com.alibaba.fastjson.JSONObject;


public class Demo {
	Map<String, String> loginPageCookies = new HashMap();
	Map<String, String> userCookies = new HashMap();
	JSONObject courses = new JSONObject();
   public void login(){
	   try{
		   
		   File file = new File("d:/userCookies1.txt");
		   if (file.exists()){
		   FileInputStream fis=new FileInputStream("d:/userCookies1.txt");  
		   ObjectInputStream ois=new ObjectInputStream(fis);
		   //readObject 方法用于从流读取对象。应该使用 Java 的安全强制转换来获取所需的类型。在 Java 中，字符串和数组都是对象，所以在序列化期间将其视为对象。读取时，需要将其强制转换为期望的类型。
		   userCookies=(HashMap)ois.readObject(); 
		   ois.close(); 
		   }
		   if (userCookies.size()<=0){
	       loginPageCookies = Jsoup.connect("./member_login.html")
	            .method(Connection.Method.POST).execute().cookies();
	     Connection.Response loginResponse = Jsoup.connect("./member_login.html")
	            .data("action", "login", "username", "hebbaixue", "password", "bx117128", "forward","")
	            .cookies(loginPageCookies)
	            .method(Connection.Method.POST).execute();
	     userCookies = loginResponse.cookies();
	     if (userCookies==null || userCookies.size()==0){
	    	 userCookies = loginPageCookies; 
	     }
		   }
		   Element indexResponse = Jsoup.connect("./member_index.html")
	            .cookies(userCookies).get().body();
		  //System.out.println(indexResponse);
	  FileOutputStream fos=new FileOutputStream("d:/userCookies1.txt",false);
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
   public static void main(String[] args){
	   Demo d = new Demo();
	   
	   
	   
	   //d.login();
	   d.getFlash("http://127.0.0.1/playvideo.html");
	   
	  /* JSONObject jo = d.getCourse("http://127.0.0.1/courselist.html");
	   JSONArray ja = jo.getJSONArray("rows");
	   if (ja.size()>0){
		   for(int i=0; i<ja.size();i++){
		   JSONObject jso = ja.getJSONObject(i);
		   if (jso.getString("courseurl")!=null){
			   //System.out.println(jso.getString("cid"));
			   String href="./member_coursedetail.html?cid="+jso.getString("cid");
			   d.getCourseList(href,jso.getString("cid"));
			   //System.out.println(href);
		   }
		   }
	   }*/
	   
	   //String href="http://127.0.0.1:8000/member_coursedetail_3_1103.html";
	   //d.getCourseList(href,""); 
	   //System.out.println(d.courses);
	   //text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
	   try{
	   /*Element indexResponse = Jsoup.connect("http://cdn1.7east.com/flashstream/czsbook/flash/bz2018/cczm/ztpx1/20170911143021.f4m")
			   .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apngq=0.8")
			   .get().body();
			   */
	  String doc= "AAABW2Fic3QAAAAAAAAADwAAAAPoAAAAAABBsoAAAAAAAAAAAAAAAAAAAQAAABlhc3J0AAAAAAAAAAABAAAAAQAAACQBAAABFmFmcnQAAAAAAAAD6AAAAAAQAAAAAQAAAAAAAAAAAAIItAAAAAIAAAAAAAII1QABx9wAAAAGAAAAAAAJJ8AAAgi0AAAABwAAAAAACzCVAAHH3AAAAAsAAAAAABJPgAACCLQAAAAMAAAAAAAUWFUAAcfcAAAAEAAAAAAAG3dAAAIItAAAABEAAAAAAB2AFQABx9wAAAAVAAAAAAAknwAAAgi0AAAAFgAAAAAAJqfVAAHH3AAAABoAAAAAAC3GwAACCLQAAAAbAAAAAAAvz5UAAcfcAAAAHwAAAAAANu6AAAIItAAAACAAAAAAADj3VQABx9wAAAAkAAAAAABAFkAAAZwcAAAAAAAAAAAAAAAAAAAAAAA" ; //HttpUtil.doGet("http://cdn1.7east.com/flashstream/czsbook/flash/bz2018/cczm/ztpx1/20170911143021.f4m");
	   System.out.println(doc);
	   System.out.println();
	   final Base64 base64 = new Base64();
	   final String text = "字串文字";
	   final byte[] textByte = text.getBytes("UTF-8");
	   //编码
	   byte[] Encdata = doc.getBytes();
	   int j = 0;
       while(j < Encdata.length)
       {
          Encdata[j] = (byte)(Encdata[j]^5);
          j++;
       }
       
	   final String encodedText = new String(Encdata);  //base64.encodeToString(textByte);
	   System.out.println(encodedText);
	   //解码
	   System.out.println(new String(base64.decode(encodedText), "UTF-8"));
        
	   
		 String b=  "AgAKb25NZXRhRGF0YQgAAAAAAAhkdXJhdGlvbgBAsNGU6BtOggAFd2lkdGgAQIQAAAAAAAAABmhlaWdodABAfgAAAAAAAAAMdmlkZW9jb2RlY2lkAgAEYXZjMQAMYXVkaW9jb2RlY2lkAgAEbXA0YQAKYXZjcHJvZmlsZQBAWQAAAAAAAAAIYXZjbGV2ZWwAQDYAAAAAAAAADnZpZGVvZnJhbWVyYXRlAEAuAAAAAAAAAA9hdWRpb3NhbXBsZXJhdGUAQNWIgAAAAAAADWF1ZGlvY2hhbm5lbHMAP/AAAAAAAAAACXRyYWNraW5mbwoAAAACAwAGbGVuZ3RoAEDviIAAAAAAAAl0aW1lc2NhbGUAQC4AAAAAAAAACGxhbmd1YWdlAgADY2hpAAAJAwAGbGVuZ3RoAEGWoo9wAAAAAAl0aW1lc2NhbGUAQNWIgAAAAAAACGxhbmd1YWdlAgADY2hpAAAJAAAJ";
		//String b= "AAABW2Fic3QAAAAAAAAADwAAAAPoAAAAAABBsoAAAAAAAAAAAAAAAAAAAQAAABlhc3J0AAAAAAAAAAABAAAAAQAAACQBAAABFmFmcnQAAAAAAAAD6AAAAAAQAAAAAQAAAAAAAAAAAAIItAAAAAIAAAAAAAII1QABx9wAAAAGAAAAAAAJJ8AAAgi0AAAABwAAAAAACzCVAAHH3AAAAAsAAAAAABJPgAACCLQAAAAMAAAAAAAUWFUAAcfcAAAAEAAAAAAAG3dAAAIItAAAABEAAAAAAB2AFQABx9wAAAAVAAAAAAAknwAAAgi0AAAAFgAAAAAAJqfVAAHH3AAAABoAAAAAAC3GwAACCLQAAAAbAAAAAAAvz5UAAcfcAAAAHwAAAAAANu6AAAIItAAAACAAAAAAADj3VQABx9wAAAAkAAAAAABAFkAAAZwcAAAAAAAAAAAAAAAAAAAAAAA=";
		 Base64Decoder bd = new Base64Decoder();
		bd.decode(b);
		byte[] bb= bd.drain();
		System.out.println(new String(bb));
		InputStream    bais = new ByteArrayInputStream(bb);
	    ObjectInputStream ois = new ObjectInputStream(bais); 
	    Object o = ois.readObject();
	    //InflaterOutputStream ios = new InflaterOutputStream(bb);
	    
	   
	   }catch(Exception e){
		   e.printStackTrace();
	   }
       
   }
   
}

