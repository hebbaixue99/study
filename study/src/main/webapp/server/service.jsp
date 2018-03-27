<%@ page language="java" contentType="text/html; charset=UTF-8"
    import="com.alibaba.fastjson.JSONObject"
    pageEncoding="UTF-8"%>

 <% 
   request.setCharacterEncoding("utf-8");  
   String cid = request.getParameter("cid");  
   String url = request.getParameter("url"); 
   String total = request.getParameter("total"); 
   JSONObject oj = new JSONObject();
    oj.put("cid", cid);
    oj.put("url", url);
    oj.put("total", total);
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