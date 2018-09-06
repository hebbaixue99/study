package study.tools;

import java.io.Serializable;

import org.apache.http.Header;

import com.alibaba.fastjson.JSONObject;

public class StudyConfig implements Serializable {
	private int unitId = 0;
	private int unitIndex=0;
	private int userID=76615124;
	private boolean isFinished =true;
	private int studyWordCount =10;
	private int studyStars=3;
	private int bookId =10375;
	private int studyCount = 5;
	private String finishedDate="2018-09-06T07:40:00.000";
	public StudyConfig(){
		
	}
	public String toString(){
		JSONObject me = new JSONObject();
		me.put("this", this);
		return me.getJSONObject("this").toJSONString();
	}
	public StudyConfig(int bookId,int unitId){
		this.bookId = bookId;
		this.unitId = unitId;
		this.unitIndex= unitId;
	}
	public StudyConfig(int bookId,int unitId,int studyWordCount,int studyCount){
		this.bookId = bookId;
		this.unitId = unitId;
		this.unitIndex= unitId;
		this.studyCount = studyCount;
		this.studyWordCount = studyWordCount;
	}
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	public int getUnitIndex() {
		return unitIndex;
	}
	public void setUnitIndex(int unitIndex) {
		this.unitIndex = unitIndex;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public boolean isFinished() {
		return isFinished;
	}
	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	public int getStudyWordCount() {
		return studyWordCount;
	}
	public void setStudyWordCount(int studyWordCount) {
		this.studyWordCount = studyWordCount;
	}
	public int getStudyStars() {
		return studyStars;
	}
	public void setStudyStars(int studyStars) {
		this.studyStars = studyStars;
	}
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public int getStudyCount() {
		return studyCount;
	}
	public void setStudyCount(int studyCount) {
		this.studyCount = studyCount;
	}
	public String getFinishedDate() {
		return finishedDate;
	}
	public void setFinishedDate(String finishedDate) {
		this.finishedDate = finishedDate;
	}

	
}
