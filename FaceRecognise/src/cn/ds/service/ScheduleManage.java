/**
 * Project             :FaceRecognise project
 * Comments            :视频人脸识别进度管理线程
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.ds.service;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import javax.swing.table.DefaultTableModel;

import cn.ds.domain.Progress;
import cn.ds.domain.VideoTask;

public class ScheduleManage extends Thread {
	private boolean flag = true;
	private int nextIndex = -1;
	private Map<Integer, VideoTask> videoTasks;
	private Map<Integer, Progress> progressMap = new HashMap<Integer, Progress>();
	private Map<Integer, Progress> runningProgressMap = new HashMap<Integer, Progress>();
	private DefaultTableModel model;
	private ArrayList<Entry<Integer, Progress>> delEntry = new ArrayList<Entry<Integer, Progress>>();

	private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
	
	public ScheduleManage(Map<Integer, VideoTask> videoTasks, DefaultTableModel model) {
		
		this.videoTasks = videoTasks;
		this.model = model;
	}

	@Override
	public void run() {
		format.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
		if (videoTasks.size() == 0) {
			WaitSyn();
		}
		while (flag) {
			
			while (nextIndex < videoTasks.size() - 1) {

				nextIndex += 1;

				VideoTask videotask = videoTasks.get(nextIndex);
				double totalSize = videotask.getTotalSize();
				double processedSize = videotask.getProcessedSize();

				Progress progress = new Progress(totalSize, processedSize);
				progressMap.put(nextIndex, progress);

				refreshVideoTaskTableAdd(model, nextIndex);
			}

			for (Entry<Integer, Progress> entry : runningProgressMap.entrySet()) {
				if (entry.getValue().getTotalSize() <= entry.getValue().getProcessedSize()) {
					delEntry.add(entry);
				}
				Progress progress = progressMap.get(entry.getKey());
				double processedSize = entry.getValue().getProcessedSize();
				progress.setProcessedSize(processedSize);
				refreshVideoTaskTableUpdate(model,entry.getKey());	
			}
			runningProgressMap.entrySet().removeAll(delEntry);
			delEntry.clear();
			
			
			
			if(nextIndex == videoTasks.size() - 1 && runningProgressMap.size() == 0){
				WaitSyn();
			}
			

			try {
				Thread.sleep(1000);
				//Thread.sleep(2500);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void refreshVideoTaskTableAdd(DefaultTableModel model, int index) {
		
		Object[] data = new Object[3];
		data[0] = index;
		Progress progress = progressMap.get(index);
		data[1] = progress.getStandardValue(100);
		data[2] = format.format(videoTasks.get(index).getCostTime());
	
		model.addRow(data);
	}

	public void refreshVideoTaskTableUpdate(DefaultTableModel model, int index) {

		Object[] data = new Object[3];
		data[0] = index;
		
		Progress progress = progressMap.get(index);
		data[1] = progress.getStandardValue(100);

		data[2] = format.format(videoTasks.get(index).getCostTime());
	
		for(int i = 0;i < data.length;i++){
			model.setValueAt(data[i], index, i);	
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
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public synchronized Map<Integer, Progress> getRunningProgressMap() {
		return runningProgressMap;
	}

	public synchronized void setRunningProgressMap(
			Map<Integer, Progress> runningProgressMap) {
		this.runningProgressMap = runningProgressMap;
	}

}
