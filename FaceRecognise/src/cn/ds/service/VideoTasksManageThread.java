/**
 * Project             :FaceRecognise project
 * Comments            :人脸识别任务管理线程
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.ds.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import cn.ds.domain.Progress;
import cn.ds.domain.VideoTask;

public class VideoTasksManageThread extends Thread {

	private Map<Integer, VideoTask> videoTasks;
	private Map<Integer, VideoTask> runningVideoTasks;
	private ScheduleManage scheduleManage;
	private int maxVideoTaskNum;
	private int nextIndex = -1;
	private boolean flag = true;
	private ArrayList<Entry<Integer, VideoTask>> delEntry = new ArrayList<Entry<Integer, VideoTask>>();

	public VideoTasksManageThread(Map<Integer, VideoTask> videoTasks,
			Map<Integer, VideoTask> runningVideoTasks,ScheduleManage scheduleManage, int maxVideoTaskNum) {
		this.videoTasks = videoTasks;
		this.runningVideoTasks = runningVideoTasks;
		this.scheduleManage = scheduleManage;
		this.maxVideoTaskNum = maxVideoTaskNum;
	}

	@Override
	public void run() {

		if (nextIndex == videoTasks.size() - 1 || videoTasks.size() == 0) {
			WaitSyn();
		}

		int temp = maxVideoTaskNum > videoTasks.size() ? videoTasks.size()
				: maxVideoTaskNum;
		for (int i = 0; i < temp; i++) {
			
			VideoTask videoTask = videoTasks.get(i);
			VideoProcessTaskThread videoProcessTaskThread = videoTask
					.getVideoProcessTaskThread();
			videoProcessTaskThread.start();

			runningVideoTasks.put(i, videoTask);
			
			Progress progress = new Progress(videoTask.getTotalSize(), videoTask.getProcessedSize());
			scheduleManage.getRunningProgressMap().put(i, progress);
			
			nextIndex += 1;
		}

		while (flag) {
			
			if (nextIndex < videoTasks.size() - 1) {
				
					
					
				for (Entry<Integer, VideoTask> entry : runningVideoTasks
						.entrySet()) {
					Progress progress = scheduleManage.getRunningProgressMap().get(entry.getKey());
					if(progress != null){
					progress.setProcessedSize(entry.getValue().getProcessedSize());
					}
					if (entry.getValue().getVideoProcessTaskThread().isFinish()) {
						delEntry.add(entry);
					}
				}
				runningVideoTasks.entrySet().removeAll(delEntry);
				delEntry.clear();
				
				for (int i = 0; i < maxVideoTaskNum - runningVideoTasks.size() && nextIndex < videoTasks.size() - 1; i++) {
					nextIndex += 1;
					VideoTask videoTask = videoTasks.get(nextIndex);
					VideoProcessTaskThread videoProcessTaskThread = videoTask
							.getVideoProcessTaskThread();
					videoProcessTaskThread.start();
					runningVideoTasks.put(nextIndex, videoTask);
					
					Progress progress = new Progress(videoTask.getTotalSize(), videoTask.getProcessedSize());
					scheduleManage.getRunningProgressMap().put(nextIndex, progress);
					if(scheduleManage.getState() == State.WAITING){
						scheduleManage.Resume();
					}
				}
			} else {
				for (Entry<Integer, VideoTask> entry : runningVideoTasks
						.entrySet()) {
					
					Progress progress = scheduleManage.getRunningProgressMap().get(entry.getKey());
					if(progress != null){
					progress.setProcessedSize(entry.getValue().getProcessedSize());
					}
					
					if (entry.getValue().getVideoProcessTaskThread().isFinish()) {
						delEntry.add(entry);
					}
				}
				runningVideoTasks.entrySet().removeAll(delEntry);
				delEntry.clear();	
			}

			if(nextIndex == videoTasks.size() - 1 && runningVideoTasks.size() == 0){
				WaitSyn();
			}
			
			try {
				Thread.sleep(1000);
				//Thread.sleep(3000);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public synchronized void WaitSyn() {
		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void Resume() {
		synchronized (this) {
			this.notify();
		}
		if(scheduleManage.getState() == State.WAITING){
			scheduleManage.Resume();
		}
	}

	public  synchronized void put(int index, VideoTask videoTask) {
		videoTasks.put(index, videoTask);
	}

	// public void update(Map<Integer, VideoProcessTaskThread> videoTasks) {
	// this.videoTasks = videoTasks;
	// }
}
