/**
 * Project             :FaceRecognise project
 * Comments            :检测图片中物体的类
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-3-20 | 创建 | jxm 
 * 2 | 2013-4-26 |优化代码，增加注释量 |jxm 
 */
package cn.ds.face;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;

public class DetectObject {

	private CvMat gray = null;
	private CvMat inputImg = null;

	/**
	 * Description :自定义的检测方法，通过参数来控制检测
	 * 
	 * @param img
	 *            :图片
	 * @param cascade
	 *            ：级联分类器
	 * @param objects
	 *            ：存储检测结果
	 * @param scaledWidth
	 *            ：期望处理图片的宽度
	 * @param flags
	 *            ：指明检测的模式
	 * @param minFeatureSize
	 *            ：最小检测尺寸
	 * @param searchScaleFactor
	 *            ：在前后两次相继的扫描中，搜索窗口的比例系数
	 * @param minNeighbors
	 *            ：构成检测目标的相邻矩形的最小个
	 * @return void
	 */
	public void detectObjectsCustom(CvMat img, CascadeClassifier cascade,
			CvRect objects, int scaledWidth, int flags, CvSize minFeatureSize,
			float searchScaleFactor, int minNeighbors) {

		gray = cvCreateMat(img.rows(), img.cols(), CV_8U);

		if (img.channels() == 3) {// 如果图片不是gray的将其转化为gray
			cvCvtColor(img, gray, CV_BGR2GRAY);
		} else {// 如果图片是gray的，直接拷贝到gray
			cvCopy(img, gray);
		}

		// 按照图片原来的宽高比压缩图片，便于快速检测
		// 得到宽高比
		float scale = img.cols() / (float) scaledWidth;
		if (img.cols() > scaledWidth) {// 如果待检测图片的宽大于期望宽度，说明需要压缩
			int scaledHeight = Math.round(img.rows() / scale);
			inputImg = cvCreateMat(scaledHeight, scaledWidth, gray.type());
			cvResize(gray, inputImg, CV_INTER_LINEAR);
		} else {// 如果待检测图片的宽小于期望宽度，说明图片已经较小，不需要压缩
			inputImg = cvCreateMat(gray.rows(), gray.cols(), gray.type());
			cvCopy(gray, inputImg);
		}

		// 在图片中检测人脸，结果存储在objects中
		cascade.detectMultiScale(inputImg, objects, searchScaleFactor,
				minNeighbors, flags, minFeatureSize, cvSize(0, 0));

		// 由于之前压缩过图片，检测出来的结果不是原图上的坐标信息，所以需要扩大到原图中
		if (img.cols() > scaledWidth) {
			for (int i = 0; i < (int) objects.capacity(); i++) {
				objects.position(i).x(
						Math.round(objects.position(i).x() * scale));
				objects.position(i).y(
						Math.round(objects.position(i).y() * scale));
				objects.position(i).width(
						Math.round(objects.position(i).width() * scale));
				objects.position(i).height(
						Math.round(objects.position(i).height() * scale));
			}
		}

		// 确保恢复出来的坐标不会超界
		for (int i = 0; i < (int) objects.capacity(); i++) {
			if (objects.position(i).x() < 0)
				objects.position(i).x(0);
			if (objects.position(i).y() < 0)
				objects.position(i).y(0);
			if (objects.position(i).x() + objects.position(i).width() > img
					.cols())
				objects.position(i).x(img.cols() - objects.position(i).width());
			if (objects.position(i).y() + objects.position(i).height() > img
					.rows())
				objects.position(i)
						.y(img.rows() - objects.position(i).height());
		}

		if (gray != null) {
			cvReleaseMat(gray);
		}
		if (inputImg != null) {
			cvReleaseMat(inputImg);
		}
	}

	/**
	 * Description :得到图片中最大的物体区域
	 * 
	 * @param img
	 *            :图片
	 * @param cascade
	 *            ：级联分类器
	 * @param largestObject
	 *            ：存储检测结果
	 * @param scaledWidth
	 *            ：期望处理图片的宽度
	 * @return void
	 */
	public void detectLargestObject(CvMat img, CascadeClassifier cascade,
			CvRect largestObject, int scaledWidth) {

		// flag = 4，检测最大模式
		int flags = CV_HAAR_FIND_BIGGEST_OBJECT;

		// 做小检测区域为20x20
		CvSize minFeatureSize = cvSize(20, 20);

		// 检测增长因子为1.1，指将搜索窗口依次扩大10%
		float searchScaleFactor = 1.1f;

		int minNeighbors = 4;

		// 调用detectObjectsCustom方法检测最大物体区域
		CvRect objects = new CvRect();
		detectObjectsCustom(img, cascade, objects, scaledWidth, flags,
				minFeatureSize, searchScaleFactor, minNeighbors);

		if (objects.capacity() > 0) {// 检测到物体

			// 将最大物体信息存储
			largestObject.x(objects.position(0).x());
			largestObject.y(objects.position(0).y());
			largestObject.width(objects.position(0).width());
			largestObject.height(objects.position(0).height());
		} else {
			// 没检测到
			largestObject.x(-1);
			largestObject.y(-1);
			largestObject.width(-1);
			largestObject.height(-1);
		}
	}

	/**
	 * Description :得到图片中的多个（所有）物体区域
	 * 
	 * @param img
	 *            :图片
	 * @param cascade
	 *            ：级联分类器
	 * @param objects
	 *            ：存储检测结果
	 * @param scaledWidth
	 *            ：期望处理图片的宽度
	 * @return void
	 */
	public void detectManyObjects(CvMat img, CascadeClassifier cascade,
			CvRect objects, int scaledWidth) {

		// 不是缩放分类器来检测，而是缩放图像
		int flags = CV_HAAR_SCALE_IMAGE;

		// 最小尺寸
		CvSize minFeatureSize = cvSize(20, 20);

		// 检测增长因子为1.1，指将搜索窗口依次扩大10%
		float searchScaleFactor = 1.1f;

		int minNeighbors = 4;

		// 调用detectObjectsCustom方法检测
		detectObjectsCustom(img, cascade, objects, scaledWidth, flags,
				minFeatureSize, searchScaleFactor, minNeighbors);
	}
}
