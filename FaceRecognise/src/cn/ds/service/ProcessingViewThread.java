package cn.ds.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


import cn.ds.view.LoadingView;

public class ProcessingViewThread extends Thread {
	private String content;
	private BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();
	private LoadingView loadingView;

	public ProcessingViewThread(String content) {
		this.content = content;
	}

	@Override
	public void run() {
		loadingView = new LoadingView(content);
		try {
			result.take();
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadingView.dispose();
		
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public BlockingQueue<Boolean> getResult() {
		return result;
	}

}
