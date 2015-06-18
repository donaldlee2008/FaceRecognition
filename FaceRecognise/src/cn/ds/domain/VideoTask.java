/**
 * Project             :FaceRecognise project
 * Comments            :封装了视频识别任务的类
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.ds.domain;

import java.util.List;

import cn.ds.service.VideoProcessTaskThread;

public class VideoTask {
	private int taskId;
	private VideoProcessTaskThread videoProcessTaskThread;
	private double totalSize;
	private double processedSize;
	private List photo;
	private List video;
	private String savePath;
	private float magnification;
	private float threshold;
	private long costTime;
	

	public VideoTask(int taskId, List photo, List video, String savePath,
			float magnification, float threshold) {
		this.taskId = taskId;
		this.photo = photo;
		this.video = video;
		this.savePath = savePath;
		this.magnification = magnification;
		this.threshold = threshold;
		this.videoProcessTaskThread = new VideoProcessTaskThread(taskId,photo, video,
				savePath, magnification, threshold);
		
		this.totalSize = videoProcessTaskThread.getTotalSize();
		this.processedSize = videoProcessTaskThread.getProcessedSize();
		this.costTime = videoProcessTaskThread.getCostTime();

	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public VideoProcessTaskThread getVideoProcessTaskThread() {
		return videoProcessTaskThread;
	}

	public void setVideoProcessTaskThread(
			VideoProcessTaskThread videoProcessTaskThread) {
		this.videoProcessTaskThread = videoProcessTaskThread;
	}

	public double getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(double totalSize) {
		this.totalSize = totalSize;
	}

	public synchronized double getProcessedSize() {
		return processedSize = videoProcessTaskThread.getProcessedSize();
	}

	public void setProcessedSize(double processedSize) {
		this.processedSize = processedSize;
	}

	public synchronized long getCostTime() {
		return videoProcessTaskThread.getCostTime();
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
}
