package com.sinieco.lib_volley.volley.download;

import com.sinieco.lib_volley.volley.Httptask;

/**
 * @author BaiMeng on 2017/11/9.
 */

public class DownLoadItemInfo extends BaseEntity<DownLoadItemInfo> {
    private String mUrl ;

    public String getmFilePath() {
        return mFilePath;
    }

    public void setmFilePath(String mFilePath) {
        this.mFilePath = mFilePath;
    }

    public DownloadStatus getmStatus() {
        return mStatus;
    }

    public void setmStatus(DownloadStatus mStatus) {
        this.mStatus = mStatus;
    }

    public Long getmCurrentLength() {
        return mCurrentLength;
    }

    public void setmCurrentLength(Long mCurrentLength) {
        this.mCurrentLength = mCurrentLength;
    }

    public Long getmTotalLength() {
        return mTotalLength;
    }

    public void setmTotalLength(Long mTotalLength) {
        this.mTotalLength = mTotalLength;
    }

    public Httptask getmHttptask() {
        return mHttptask;
    }

    public void setmHttptask(Httptask mHttptask) {
        this.mHttptask = mHttptask;
    }

    private String mFilePath ;
    private DownloadStatus mStatus ;
    private Long mCurrentLength ;
    private Long mTotalLength ;
    private transient Httptask mHttptask ;
    private Integer id ;
    private String displayName ;
    private String startTime ;
    private String endTime ;
    private String userId ;
    private String httpTaskType ;
    private Integer priority ;
    private Integer stopMode ;

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHttpTaskType() {
        return httpTaskType;
    }

    public void setHttpTaskType(String httpTaskType) {
        this.httpTaskType = httpTaskType;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getStopMode() {
        return stopMode;
    }

    public void setStopMode(Integer stopMode) {
        this.stopMode = stopMode;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    private Integer status ;


    public void setHttptask(Httptask httptask){
        this.mHttptask = httptask ;
    }

    public Httptask getHttptask(){
        return mHttptask ;
    }

    public void setTotalLength (Long totalLength){
        this.mTotalLength = totalLength ;
    }

    public Long getTotalLength(){
        return mTotalLength ;
    }

    public void setCurrentLength (Long currentLength){
        this.mCurrentLength = currentLength ;
    }

    public Long getCurrentLength(){
        return mCurrentLength ;
    }

    public void setStatus(DownloadStatus status){
        this.mStatus = status ;
    }

    public DownloadStatus getStatus (){
        return mStatus ;
    }

    public DownLoadItemInfo(String url, String filePath) {
        this.mUrl = url;
        this.mFilePath = filePath;
    }

    public DownLoadItemInfo() {
    }

    public String getFilePath(){
        return mFilePath ;
    }
}
