package study;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import com.alibaba.fastjson.JSONObject;

public class FileUtil {
	public static JSONObject readFile(String fName) {
		JSONObject o = new JSONObject();
		try {
			File file = new File(fName);
			if (file.exists()) {
				FileInputStream fis = new FileInputStream(fName);
				ObjectInputStream ois = new ObjectInputStream(fis); 
				o = (JSONObject) ois.readObject();
				ois.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("writeFile:"+o);
		return o;
	}
	public static void writeFile(String fName,JSONObject o){
		try{
		  System.out.println("readFile:"+o);
		  FileOutputStream fos=new FileOutputStream(fName,false);
		   
		  ObjectOutputStream oos=new ObjectOutputStream(fos);
		  
		  oos.writeObject(o);  
		  oos.close();  
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void createDir(String dirName){
		File dir = new File(dirName);
		if (!dir.exists()){
			dir.mkdirs();
		}
	}
	public static void saveRemoteFile(String url,byte[] buf){
		try{
		String dir = "e:/flashstream"+ url.split("flashstream")[1].substring(0, url.split("flashstream")[1].lastIndexOf("/"));
		String fn = "e:/flashstream"+ url.split("flashstream")[1];
		  FileOutputStream fos=new FileOutputStream(fn,false); 
		  //ObjectOutputStream oos=new ObjectOutputStream(fos);
		  //oos.write(buf);
		  //oos.close();
		  fos.write(buf);
		  fos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		String url="http://cdn1.7east.com/flashstream/czsbook/flash/bz2018/18hd/ch/ch101/2016111919236727Seg1-Frag1";
		//String dir = "e:/flashstream"+ url.split("flashstream")[1].substring(0, url.split("flashstream")[1].lastIndexOf("/"));
		//String dir = "e:/flashstream/f1/f2/f3/f4/f5";
		//String buf = "文件测试";
		JSONObject o = FileUtil.readFile("d:/flash");
		Iterator<String> set = o.keySet().iterator();
		while(set.hasNext()){  
		    // 获得key  
		    String key = set.next();  
		    // 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可  
		    JSONObject value = o.getJSONObject(key);  
		    //System.out.println("key: "+key+",value"+value);  
		    for(int i=1;i<new Integer(value.getString("total"));i++){
		    	url = value.getString("url")+"Seg1-Frag"+i;
		    	HttpUtil.doGetB(url);
		    	System.out.println(url);
		    }
		}  

		//HttpUtil.doGetB(url);
		//saveRemoteFile(url,buf.getBytes());
	}
}
