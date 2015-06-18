/**
 * Project             :FaceRecognise project
 * Comments            :管理一个视频人脸识别任务的线程
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.ds.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.ds.utils.ExcelService;

public class VideoProcessTaskThread extends Thread {
	private Map<Integer, VideoProcessor> videoProcessors = new HashMap<Integer, VideoProcessor>();
	private int maxVideoProcessNum = 2;
	private double totalSize = 0;
	private double processedSize = 0;
	private Map<Integer, ProcessListenerImp> processListeners = new HashMap<Integer, ProcessListenerImp>();

	private Map<Integer, VideoProcessor> runningVideoProcessors = new HashMap<Integer, VideoProcessor>();
	private int nextIndex = -1;
	private boolean finish = false;
	private ArrayList<Entry<Integer, VideoProcessor>> delEntry = new ArrayList<Entry<Integer, VideoProcessor>>();
	private long costTime;
	
	private static long startTime = 0;
	private static long time = 0;
	

	protected synchronized void append(double size) {
		processedSize += size;
	}

	public VideoProcessTaskThread(int taskId,List photo, List video, String savePath,
			float magnification, float threshold) {
		
		ExcelService excelService = new ExcelService(savePath, taskId+"");
		excelService.addRow(0, new String[]{"任务号","视频名","结果照片","照片","时间"});

		for (int i = 0; i < video.size(); i++) {
			ProcessListenerImp listener = new ProcessListenerImp() {

				@Override
				public void onProcessSize(double size) {
					append(size);

				}
			};
			processListeners.put(i, listener);
			VideoProcessor videoProcessor = new VideoProcessor(taskId,i,
					magnification, threshold, photo, (String) video.get(i),
					savePath, listener,excelService);
			totalSize += videoProcessor.getCalSize();
			videoProcessors.put(i, videoProcessor);
		}
	}

	@Override
	public void run() {
		
		startTime = System.currentTimeMillis();

		int temp = maxVideoProcessNum > videoProcessors.size() ? videoProcessors
				.size() : maxVideoProcessNum;

		for (int i = 0; i < temp; i++) {

			videoProcessors.get(i).start();

			runningVideoProcessors.put(i, videoProcessors.get(i));
			nextIndex += 1;
		}

		while (!finish) {

			try {
				Thread.sleep(1000);
				//Thread.sleep(3000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (nextIndex < videoProcessors.size() - 1) {
				for (Entry<Integer, VideoProcessor> entry : runningVideoProcessors.entrySet()) {
					
					if (entry.getValue().isAllFinish()) {
						delEntry.add(entry);
					}
				}
				runningVideoProcessors.entrySet().removeAll(delEntry);
				delEntry.clear();
				
					for (int i = 0; i < maxVideoProcessNum
							- runningVideoProcessors.size()
							&& nextIndex < videoProcessors.size() - 1; i++) {
						nextIndex += 1;
						VideoProcessor processor = videoProcessors
								.get(nextIndex);
						processor.start();
						runningVideoProcessors.put(nextIndex, processor);
					}

				
			} else {
				for (Entry<Integer, VideoProcessor> entry : runningVideoProcessors.entrySet()) {
					if (entry.getValue().isAllFinish()) {
						delEntry.add(entry);
					}
				}
				runningVideoProcessors.entrySet().removeAll(delEntry);
				delEntry.clear();
			}
			
           time = System.currentTimeMillis();
            setCostTime(time - startTime);
            
			if (nextIndex >= videoProcessors.size() - 1
					&& runningVideoProcessors.size() == 0) {
				
				long timeFinish = System.currentTimeMillis();
	            setCostTime(timeFinish - startTime);
	            
				finish = true;
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
		this.notify();
	}

	public double getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(double totalSize) {
		this.totalSize = totalSize;
	}

	public synchronized double getProcessedSize() {
		return processedSize;
	}

	public synchronized void setProcessedSize(double processedSize) {
		this.processedSize = processedSize;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	public synchronized long getCostTime() {
		return costTime;
	}

	public synchronized void setCostTime(long costTime) {
		this.costTime = costTime;
	}

}
