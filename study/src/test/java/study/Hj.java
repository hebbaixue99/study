package study;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Hj {
	public static void main(String[] args) {
		JSONArray oa = new JSONArray();
		int k = 220;
		for (int i = 0; i < 20; i++) {
			JSONObject oj = new JSONObject();
			// "unitId":111,"userID":76615124,"isFinished":true,"bookId":11990,"unitIndex":111,"studyWordCount":10,"studyStars":3,"finishedDate":"2018-09-04T19:46:00.562"
			k++;
			oj.put("unitId", k);
			oj.put("unitIndex", k);
			oj.put("userID", 76615124);
			oj.put("isFinished", true);
			oj.put("studyWordCount", 10);
			oj.put("studyStars", 3);
			oj.put("bookId", 11990);
			oj.put("finishedDate", "2018-09-04T" + "21:" + (i+10) +":"+(int)(Math.random()*50 + 10)+"."+(int)(Math.random()*900 + 100));
			oa.add(oj);
		}
		System.out.print(oa.toJSONString());
		int i = (int)(Math.random()*900 + 100);
	}
	
}
