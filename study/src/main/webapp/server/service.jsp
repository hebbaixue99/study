<%@ page language="java" contentType="text/html; charset=UTF-8"
    import="com.alibaba.fastjson.JSONObject"
    import="com.alibaba.fastjson.JSONArray"
    import="study.FileUtil"
    pageEncoding="UTF-8"%>

 <% 
   request.setCharacterEncoding("utf-8");  
   String cid = request.getParameter("cid");  
   String url = request.getParameter("url"); 
   String total = request.getParameter("total"); 
   JSONObject ja = new JSONObject();
   ja = study.FileUtil.readFile("d:/flash");
   JSONObject oj = new JSONObject();
    oj.put("cid", cid);
    oj.put("url", url);
    oj.put("total", total); 
   if (ja!=null){
     ja.put(cid, oj);
   }
   
   study.FileUtil.writeFile("d:/flash", ja);
   System.out.println(oj.toJSONString());
   response.setContentType("text/json; charset=UTF-8");  
   //PrintWriter out=response.getWriter();
  // out.println(jsa.toString());
   out.write(oj.toJSONString());
   /*int count = new Integer(total);
   for(int i=1;i<=count;i++){
	   out.write(url+"Seg1-Frag"+i+"<br>");
   }*/
 %>