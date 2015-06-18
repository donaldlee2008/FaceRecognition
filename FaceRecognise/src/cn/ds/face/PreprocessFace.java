/**
 * Project             :FaceRecognise project
 * Comments            :处理检测得到的人脸
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-3-20 | 创建 | jxm 
 * 2 | 2013-4-26 |优化代码，增加注释量 |jxm 
 */
package cn.ds.face;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;

import java.util.Vector;

import com.googlecode.javacv.cpp.opencv_core.CvRect;

import cn.ds.face.DetectObject;

public class PreprocessFace {

	private final double DESIRED_LEFT_EYE_X = 0.16;
	private final double DESIRED_LEFT_EYE_Y = 0.14;
	private final double FACE_ELLIPSE_CY = 0.40;
	private final double FACE_ELLIPSE_W = 0.52;//0.5;
	private final double FACE_ELLIPSE_H = 0.8;//0.80;

	private double widthScale = 1.3;
	private double heightScale = 1.1;

	final float EYE_SX = 0.16f;
	final float EYE_SY = 0.26f;
	final float EYE_SW = 0.30f;
	final float EYE_SH = 0.28f;


	private DetectObject detectObject;
	
	private CvMat topLeftOfFace = null;
	private CvMat topRightOfFace = null;
	
	private CvMat leftSide = null;
	private CvMat rightSide = null;
	private CvMat wholeFace = null;
	
	private CvMat rot_mat = null;
	private CvMat warped = null;
	private CvMat filtered = null;
	private CvMat mask = null;
	

	public PreprocessFace() {
		detectObject = new DetectObject();
	}

	/**
	 * Description :检测人脸中的双眼的位置，便于对人脸进行处理，取出头发等多余部分，提高识别精度
	 * 
	 * @param face
	 *            :检测出来的人脸
	 * @param eyeCascade1
	 *            ：级联分类器1，不带眼睛
	 * @param eyeCascade2
	 *            ：级联分类器1，带眼睛
	 * @param leftEye
	 *            ：左眼中心坐标
	 * @param rightEye
	 *            ：右眼中心坐标
	 * @return void
	 */
	public void detectBothEyes(CvMat face, CascadeClassifier eyeCascade1,
			CascadeClassifier eyeCascade2, CvPoint leftEye, CvPoint rightEye) {

		/**
		 * 将人脸分为左右两个部分，检测左右两个眼睛的中心位置
		 */

		int leftX = Math.round(face.cols() * EYE_SX*0.625f);
		int topY = Math.round(face.rows() * EYE_SY);
		int widthX = Math.round(face.cols() * EYE_SW);
		int heightY = Math.round(face.rows() * EYE_SH);
		int rightX = (int) Math.round(face.cols() * (1.0 - EYE_SX - EYE_SW));

		// 创建存放左右脸部区域的空间
			topLeftOfFace = new CvMat();	
		
			topRightOfFace = new CvMat();	

		cvGetSubArr(face, topLeftOfFace, cvRect(leftX, topY, widthX, heightY));
		cvGetSubArr(face, topRightOfFace, cvRect(rightX, topY, widthX, heightY));

		// 创建存放检测出眼睛的空间
		CvRect leftEyeRect = new CvRect();
		CvRect rightEyeRect = new CvRect();

		// eyeCascade1，检测左眼区域
		detectObject.detectLargestObject(topLeftOfFace, eyeCascade1,
				leftEyeRect, topLeftOfFace.cols());

		// eyeCascade1，检测右眼区域
		detectObject.detectLargestObject(topRightOfFace, eyeCascade1,
				rightEyeRect, topRightOfFace.cols());

		// 若eyeCascade1，没有检测出左眼区域，用eyeCascade2再检测
		if (leftEyeRect.width() <= 0 && !eyeCascade2.empty()) {

			detectObject.detectLargestObject(topLeftOfFace, eyeCascade2,
					leftEyeRect, topLeftOfFace.cols());
		}

		// 若eyeCascade1，没有检测出又眼区域，用eyeCascade2再检测
		if (rightEyeRect.width() <= 0 && !eyeCascade2.empty()) {

			detectObject.detectLargestObject(topRightOfFace, eyeCascade2,
					rightEyeRect, topRightOfFace.cols());
		}

		// 检测出了左眼区域，将结果存储到leftEyeRect中，这里存储的是原图的信息，所以需要转化恢复
		if (leftEyeRect.width() > 0) {
			leftEyeRect.x(leftEyeRect.x() + leftX);
			leftEyeRect.y(leftEyeRect.y() + topY);
			leftEye.x(leftEyeRect.x() + leftEyeRect.width() / 2);
			leftEye.y(leftEyeRect.y() + leftEyeRect.height() / 2);
		} else {
			// 没有检测出左眼的区域，结果返回-1
			leftEye.x(-1);
			leftEye.y(-1);
		}

		// 检测出了右眼区域，将结果存储到leftEyeRect中，这里存储的是原图的信息，所以需要转化恢复
		if (rightEyeRect.width() > 0) {
			rightEyeRect.x(rightEyeRect.x() + rightX);
			rightEyeRect.y(rightEyeRect.y() + topY);
			rightEye.x(rightEyeRect.x() + rightEyeRect.width() / 2);
			rightEye.y(rightEyeRect.y() + rightEyeRect.height() / 2);
		} else {
			// 没有检测出右眼的区域，结果返回-1
			rightEye.x(-1);
			rightEye.y(-1);
		}

//		if(topLeftOfFace != null){
//			cvReleaseMat(topLeftOfFace);
//		}
//		if(topRightOfFace != null){
//			cvReleaseMat(topRightOfFace);
//		}
	}

	/**
	 * Description :使灰度图象直方图均衡化，取出光线的干扰
	 * 
	 * @param face
	 *            :检测出来的人脸
	 * @return void
	 */
	public void equalizeLeftAndRightHalves(CvMat faceImg) {

		int w = faceImg.cols();
		int h = faceImg.rows();

		// 1) 全脸均衡
		wholeFace = cvCreateMat(faceImg.rows(), faceImg.cols(),
					faceImg.type());	
		cvEqualizeHist(faceImg, wholeFace);

		// 2) 均衡左右脸
		int midX = w / 2;
		
			leftSide = new CvMat();	
			rightSide = new CvMat();	


		cvGetSubArr(faceImg, leftSide, cvRect(0, 0, midX, h));
		cvGetSubArr(faceImg, rightSide, cvRect(midX, 0, w - midX, h));
		cvEqualizeHist(leftSide, leftSide);
		cvEqualizeHist(rightSide, rightSide);

		// 3) 左半边和右半边和全脸一起，以便于有一个平滑的过渡。
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int v;
				if (x < w / 4) {
					v = (int) leftSide.get(y, x);
				} else if (x < w * 2 / 4) {
					int lv = (int) leftSide.get(y, x);
					int wv = (int) wholeFace.get(y, x);

					float f = (x - w * 1 / 4) / (float) (w * 0.25f);
					v = Math.round((1.0f - f) * lv + (f) * wv);
				} else if (x < w * 3 / 4) {
					int rv = (int) rightSide.get(y, x - midX);
					int wv = (int) wholeFace.get(y, x);

					float f = (x - w * 2 / 4) / (float) (w * 0.25f);
					v = Math.round((1.0f - f) * wv + (f) * rv);
				} else {
					v = (int) rightSide.get(y, x - midX);
				}
				faceImg.put(y, x, v);
			}
		}

//		if (leftSide != null)
//			cvReleaseMat(leftSide);
//		if (rightSide != null)
//			cvReleaseMat(rightSide);
//		if (wholeFace != null)
//			cvReleaseMat(wholeFace);
	}

	/**
	 * Description :得到处理后的图片（人脸）
	 * 
	 * @param srcImg
	 *            :原图片
	 * @param desiredFaceWidth
	 *            :期望处理后的图片宽度
	 * @param faceCascade
	 *            :人脸分类器
	 * @param eyeCascade1
	 *            :眼睛分类器（没眼镜）
	 * @param eyeCascade2
	 *            :眼睛分类器（戴眼镜）
	 * @param doLeftAndRightSeparately
	 *            :区分左右
	 * @param storeFaceRect
	 *            :存储人脸区域
	 * @param storeLeftEye
	 *            :存储左眼中心位置
	 * @param storeRightEye
	 *            :存储右眼中心位置
	 * @return CvMat :处理后的人脸
	 */
	public CvMat getPreprocessedFace(CvMat srcImg, int desiredFaceWidth,int scaleWidth,
			CascadeClassifier faceCascade, CascadeClassifier eyeCascade1,
			CascadeClassifier eyeCascade2, boolean doLeftAndRightSeparately,
			CvRect storeFaceRect, CvPoint storeLeftEye, CvPoint storeRightEye) {

		int desiredFaceHeight = desiredFaceWidth;

		if (storeFaceRect != null)
			storeFaceRect.width(-1);
		if (storeLeftEye != null)
			storeLeftEye.x(-1);
		if (storeRightEye != null)
			storeRightEye.x(-1);

		// 检测人脸的区域
		CvRect faceRect = new CvRect();
		detectObject.detectLargestObject(srcImg, faceCascade, faceRect,
				scaleWidth);

		if (faceRect.width() > 0) {// 检测到人脸

			// 存储人脸区域
			if (storeFaceRect != null) {
				storeFaceRect.x(faceRect.x());
				storeFaceRect.y(faceRect.y());
				storeFaceRect.width(faceRect.width());
				storeFaceRect.height(faceRect.height());
			}

			// 得到检测到的人脸图片
			CvMat faceImg = new CvMat();
			cvGetSubArr(srcImg, faceImg, faceRect);

			// 转化为灰度图片
			CvMat gray = convertToGreyscale(faceImg);

			// 查找眼睛中心位置
			CvPoint leftEye = new CvPoint();
			CvPoint rightEye = new CvPoint();
			detectBothEyes(gray, eyeCascade1, eyeCascade2, leftEye, rightEye);

			// 存储左眼位置
			if (storeLeftEye != null) {
				storeLeftEye.x(leftEye.x());
				storeLeftEye.y(leftEye.y());
			}

			// 存储左眼位置
			if (storeRightEye != null) {
				storeRightEye.x(rightEye.x());
				storeRightEye.y(rightEye.y());
			}

			if (leftEye.x() >= 0 && rightEye.x() >= 0) {// 两眼全部检测到，可以处理
				CvMat dstImg = process(gray, leftEye, rightEye,
						desiredFaceWidth, desiredFaceHeight,
						doLeftAndRightSeparately);
				
				cvEqualizeHist(dstImg, dstImg);
				return dstImg;

			}

		}
		return null;
	}

	/**
	 * Description :得到多张图片（人脸）
	 * 
	 * @param srcImg
	 *            :原图片
	 * @param desiredFaceWidth
	 *            :期望处理后的图片宽度
	 * @param faceCascade
	 *            :人脸分类器
	 * @param eyeCascade1
	 *            :眼睛分类器（没眼镜）
	 * @param eyeCascade2
	 *            :眼睛分类器（戴眼镜）
	 * @param doLeftAndRightSeparately
	 *            :区分左右
	 * @param storeFaceRect
	 *            :存储人脸区域
	 * @param storeLeftEye
	 *            :存储左眼中心位置
	 * @param storeRightEye
	 *            :存储右眼中心位置
	 * @return Vector<CvMat> :处理后的多张人脸图片
	 */
	public Vector<CvMat> getManyPreprocessedFaces(CvMat srcImg,
			int desiredFaceWidth,int scaleWidth, CascadeClassifier faceCascade,
			CascadeClassifier eyeCascade1, CascadeClassifier eyeCascade2,
			boolean doLeftAndRightSeparately, CvRect storeFaceRect,
			CvPoint storeLeftEye, CvPoint storeRightEye) {
		int desiredFaceHeight = desiredFaceWidth;

		if (storeFaceRect != null)
			storeFaceRect.width(-1);
		if (storeLeftEye != null)
			storeLeftEye.x(-1);
		if (storeRightEye != null)
			storeRightEye.x(-1);

		// 查找多个人脸
		CvRect facesRect = new CvRect();
		detectObject.detectManyObjects(srcImg, faceCascade, facesRect,
				scaleWidth);

		Vector<CvMat> dstFaces = new Vector<CvMat>();

		CvMat dstImg = null;
		for (int i = 0; i < facesRect.capacity(); i++) {

			CvRect faceRect = facesRect.position(i);

			if (faceRect.width() > 0) {// 检测到了人脸

				// 存储人脸区域
				if (storeFaceRect != null) {
					storeFaceRect.position(i).x(facesRect.x());
					storeFaceRect.position(i).y(facesRect.y());
					storeFaceRect.position(i).width(facesRect.width());
					storeFaceRect.position(i).height(facesRect.height());
				}

				// 得到人脸图片
				CvMat faceImg = new CvMat();
				cvGetSubArr(srcImg, faceImg, faceRect);

				// 转化为灰度图片
				CvMat gray = convertToGreyscale(faceImg);

				// 查找两个眼睛
				CvPoint leftEye = new CvPoint();
				CvPoint rightEye = new CvPoint();
				detectBothEyes(gray, eyeCascade1, eyeCascade2, leftEye,
						rightEye);

				// 存储左眼中心坐标
				if (storeLeftEye != null) {
					storeLeftEye.position(i).x(leftEye.x());
					storeLeftEye.position(i).y(leftEye.y());
				}

				// 存储右眼中心坐标
				if (storeRightEye != null) {
					storeRightEye.position(i).x(rightEye.x());
					storeRightEye.position(i).y(rightEye.y());
				}

				// 双眼全部检测到，可以进行处理
				if (leftEye.x() >= 0 && rightEye.x() >= 0) {
					dstImg = process(gray, leftEye, rightEye, desiredFaceWidth,
							desiredFaceHeight, doLeftAndRightSeparately);

					// 将处理后的人脸增加到Vector中
					dstFaces.add(dstImg);
				}
			}

		}
		return dstFaces;
	}

	/**
	 * Description :不用检测双眼来处理图片，该方法中的人眼中心位置是通过计算得到的
	 * 
	 * @param srcImg
	 *            :原图片
	 * @param desiredFaceWidth
	 *            :期望处理后的图片宽度
	 * @param faceCascade
	 *            :人脸分类器
	 * @param doLeftAndRightSeparately
	 *            :区分左右
	 * @return CvMat :处理后的人脸图片
	 */
	public CvMat getPreprocessedFaceWithoutDetectEyes(CvMat srcImg,
			int desiredFaceWidth, int scaleWidth,CascadeClassifier faceCascade,
			boolean doLeftAndRightSeparately) {

		int desiredFaceHeight = desiredFaceWidth;

		// 查找人脸区域
		CvRect faceRect = new CvRect();
		detectObject.detectLargestObject(srcImg, faceCascade, faceRect,
				scaleWidth);

		if (faceRect.width() > 0) {// 找到人脸

			// 由于detectLargestObject方法找得的人脸区域过大，所以需要缩小该区域
			//CvRect smallFaceRect = getSmallFaceRect(faceRect);

			// 得到人脸图片
			//CvMat faceImg = new CvMat();
			//cvGetSubArr(srcImg, faceImg, smallFaceRect);
			CvMat faceImg = new CvMat();
			cvGetSubArr(srcImg, faceImg, faceRect);
			// 灰度化人脸
			//CvMat gray = convertToGreyscale(faceImg);
			
			CvMat gray = convertToGreyscale(faceImg);

			CvPoint leftEye = new CvPoint(), rightEye = new CvPoint();

			// 计算双眼的中心位置
			calculateEyeCenter(gray, leftEye, rightEye);

			// 处理人脸
			CvMat dstImg = process(gray, leftEye, rightEye, desiredFaceWidth,
					desiredFaceHeight, doLeftAndRightSeparately);

			return dstImg;
		}
		return null;
	}

	/**
	 * Description :不用检测双眼来处理图片，该方法中的人眼中心位置是通过计算得到的，得到多个人脸
	 * 
	 * @param srcImg
	 *            :原图片
	 * @param desiredFaceWidth
	 *            :期望处理后的图片宽度
	 * @param faceCascade
	 *            :人脸分类器
	 * @param doLeftAndRightSeparately
	 *            :区分左右
	 * @return CvMat :处理后的人脸图片
	 */
	public Vector<CvMat> getManyPreprocessedFacesWithoutDetectEyes(
			CvMat srcImg, int desiredFaceWidth,int scaleWidth,CascadeClassifier faceCascade,
			boolean doLeftAndRightSeparately) {

		int desiredFaceHeight = desiredFaceWidth;

		// 得到人脸区域
		CvRect facesRect = new CvRect();
		detectObject.detectManyObjects(srcImg, faceCascade, facesRect,
				scaleWidth);

		Vector<CvMat> dstFaces = new Vector<CvMat>();
		CvMat dstImg = null;

		for (int i = 0; i < facesRect.capacity(); i++) {

			if (facesRect.position(i).width() > 0) {

				CvRect faceRect = facesRect.position(i);

				// 由于detectLargestObject方法找得的人脸区域过大，所以需要缩小该区域
				//CvRect smallFaceRect = getSmallFaceRect(faceRect);

				// 得到人脸图片
				CvMat faceImg = new CvMat();
				cvGetSubArr(srcImg, faceImg, faceRect);

				// 灰度化
				CvMat gray = convertToGreyscale(faceImg);

				// 计算眼睛中心位置
				CvPoint leftEye = new CvPoint(), rightEye = new CvPoint();
				calculateEyeCenter(gray, leftEye, rightEye);

				dstImg = process(gray, leftEye, rightEye, desiredFaceWidth,
						desiredFaceHeight, doLeftAndRightSeparately);
				dstFaces.add(i, dstImg);
			}

		}
		return dstFaces;
	}

	/**
	 * Description :得到不需要处理的人脸图片
	 * 
	 * @param srcImg
	 *            :原图片
	 * @param desiredFaceWidth
	 *            :期望处理后的图片宽度
	 * @param faceCascade
	 *            :人脸分类器
	 * @return CvMat :得到人脸图片
	 */
	public CvMat getFaceWithoutProcess(CvMat srcImg, int desiredFaceWidth,int scaleWidth,
			CascadeClassifier faceCascade) {

		int desiredFaceHeight = desiredFaceWidth;
		// 检测人脸区域
		CvRect faceRect = new CvRect();
		detectObject.detectLargestObject(srcImg, faceCascade, faceRect,
				scaleWidth);

		if (faceRect.width() > 0) {// 检测到人脸

			
			// 缩小图片
			CvRect smallFaceRect = getSmallFaceRect(faceRect);
			

			// 得到人脸图片
			CvMat faceImg = new CvMat();
			cvGetSubArr(srcImg, faceImg, smallFaceRect);

			// 灰度化图片
			CvMat gray = convertToGreyscale(faceImg);

			// 改变图片尺寸
			CvMat dstImg = cvCreateMat(desiredFaceHeight, desiredFaceWidth,
					gray.type());
			cvResize(gray, dstImg);

			cvEqualizeHist(dstImg, dstImg);

			return dstImg;
		}
		return null;
	}

	/**
	 * Description :得到不需要处理的人脸图片,多张
	 * 
	 * @param srcImg
	 *            :原图片
	 * @param desiredFaceWidth
	 *            :期望处理后的图片宽度
	 * @param faceCascade
	 *            :人脸分类器
	 * @return Vector<CvMat> :得到人脸图片，多张
	 */
	public Vector<CvMat> getManyFacesWithoutProcess(CvMat srcImg,
			int desiredFaceWidth, int scaleWidth,CascadeClassifier faceCascade) {
		int desiredFaceHeight = desiredFaceWidth;

		// 检测人脸区域
		CvRect facesRect = new CvRect();
		detectObject.detectManyObjects(srcImg, faceCascade, facesRect,
				scaleWidth);

		Vector<CvMat> dstFaces = new Vector<CvMat>();
		for (int i = 0; i < facesRect.capacity(); i++) {

			if (facesRect.position(i).width() > 0) {

				CvRect faceRect = facesRect.position(i);

				CvRect smallFaceRect = getSmallFaceRect(faceRect);

				// 得到人脸图片
				CvMat faceImg = new CvMat();
				cvGetSubArr(srcImg, faceImg, smallFaceRect);

				// 灰度化
				CvMat gray = convertToGreyscale(faceImg);

				// 改变图片尺寸
				CvMat dstImg = cvCreateMat(desiredFaceHeight, desiredFaceWidth,
						gray.type());
				cvResize(gray, dstImg);

				cvEqualizeHist(dstImg, dstImg);

				dstFaces.add(i, dstImg);
			}

		}
		return dstFaces;
	}

	/**
	 * Description :处理图片
	 * 
	 * @param gray
	 *            :灰度人脸图片
	 * @param leftEye
	 *            :左眼中心位置
	 * @param rightEye
	 *            :右眼中心位置
	 * @param desiredFaceWidth
	 *            :期望处理后的图片宽度
	 * @param desiredFaceHeight
	 *            :期望处理后的高度
	 * @param doLeftAndRightSeparately
	 *            :是否区分左右脸
	 * @return CvMat :处理后的人脸图片
	 */
	public CvMat process(CvMat gray, CvPoint leftEye, CvPoint rightEye,
			int desiredFaceWidth, int desiredFaceHeight,
			boolean doLeftAndRightSeparately) {

		CvPoint2D32f eyesCenter = new CvPoint2D32f(
				(leftEye.x() + rightEye.x()) * 0.5f,
				(leftEye.y() + rightEye.y()) * 0.5f);
		// Get the angle between the 2 eyes.
		double dy = (rightEye.y() - leftEye.y());
		double dx = (rightEye.x() - leftEye.x());
		double len = Math.sqrt(dx * dx + dy * dy);
		double angle = Math.atan2(dy, dx) * 180.0 / Math.PI;

		// Hand measurements shown that the left eye center should
		// ideally be at roughly (0.19, 0.14) of a scaled face image.
		double DESIRED_RIGHT_EYE_X = (1.0f - DESIRED_LEFT_EYE_X);
		// Get the amount we need to scale the image to be the desired
		// fixed size we want.
		double desiredLen = (DESIRED_RIGHT_EYE_X - DESIRED_LEFT_EYE_X)
				* desiredFaceWidth;
		double scale = desiredLen / len;
		// Get the transformation matrix for rotating and scaling the
		// face to the desired angle & size.

		rot_mat = cvCreateMat(2, 3, CV_32F);	

		cv2DRotationMatrix(eyesCenter, angle, scale, rot_mat);
		// Shift the center of the eyes to be the desired center between
		// the eyes.
		rot_mat.put(0, 2, rot_mat.get(0, 2) + desiredFaceWidth * 0.5f
				- eyesCenter.x());
		rot_mat.put(1, 2, rot_mat.get(1, 2) + desiredFaceHeight
				* DESIRED_LEFT_EYE_Y - eyesCenter.y());

		warped = cvCreateMat(desiredFaceHeight, desiredFaceWidth, CV_8U);	

		// Mat warped = Mat(desiredFaceHeight, desiredFaceWidth, CV_8U,
		// cvScalar(128)); // Clear the output image to a default grey.
		cvWarpAffine(gray, warped, rot_mat);

		// Give the image a standard brightness and contrast, in case it
		// was too dark or had low contrast.
		if (!doLeftAndRightSeparately) {
			// Do it on the whole face.
			cvEqualizeHist(warped, warped);
		} else {
			// Do it seperately for the left and right sides of the
			// face.
			equalizeLeftAndRightHalves(warped);
		}

			filtered = cvCreateMat(warped.rows(), warped.cols(), CV_8U);	

		bilateralFilter(warped, filtered, 0, 20.0, 2.0, BORDER_DEFAULT);

		mask = cvCreateMat(warped.rows(), warped.cols(), CV_8U);	
		
		cvSet(mask, CV_RGB(0, 0, 0));
		// Mat mask = Mat(warped.size(), CV_8U, Scalar(0)); // Start
		// with an empty mask.
		CvPoint faceCenter = cvPoint(desiredFaceWidth / 2,
				(int) Math.round(desiredFaceHeight * FACE_ELLIPSE_CY));
		CvSize size = cvSize(
				(int) Math.round(desiredFaceWidth * FACE_ELLIPSE_W),
				(int) Math.round(desiredFaceHeight * FACE_ELLIPSE_H));
		cvEllipse(mask, faceCenter, size, 0, 0, 360, CV_RGB(255, 255, 255),
				CV_FILLED, 8, 0);
		// imshow("mask", mask);
		// Use the mask, to remove outside pixels.
		CvMat dstImg = cvCreateMat(warped.rows(), warped.cols(), CV_8U);
		cvSet(dstImg, CV_RGB(128, 128, 128));

		cvCopy(filtered, dstImg, mask);
		
		return dstImg;
	}

	/**
	 * Description :将图片转化为灰度图
	 * 
	 * @param src
	 *            : 带转化图片
	 * @return CvMat :处理后的灰度图片
	 */
	public CvMat convertToGreyscale(CvMat src) {
		CvMat gray = cvCreateMat(src.rows(), src.cols(), CV_8U);
		if (src.channels() == 3) {
			cvCvtColor(src, gray, CV_BGR2GRAY);
		} else if (src.channels() == 4) {
			cvCvtColor(src, gray, CV_BGRA2GRAY);
		} else {
			cvCopy(src, gray);
		}
		return gray;
	}

	/**
	 * Description :将检测到的人脸区域缩小，区区不含人脸的部分
	 * 
	 * @param faceRect
	 *            : 待缩小的人脸区域
	 * @return CvRect :缩小后的人脸区域
	 */
	public CvRect getSmallFaceRect(CvRect faceRect) {
		int w = (int) Math.round(faceRect.width() / widthScale);
		int h = (int) Math.round(faceRect.height() / heightScale);

		int x = faceRect.x() + (int) Math.round((faceRect.width() - w) * 0.5);
		int y = faceRect.y() + (int) Math.round((faceRect.height() - h) * 0.5);
		CvRect smallFaceRect = new CvRect(x, y, w, h);
		return smallFaceRect;
	}

	/**
	 * Description :计算眼睛中心
	 * 
	 * @param face
	 *            : 人脸图片
	 * @param leftEye
	 *            :存储计算后左眼中心位置
	 * @param rightEye
	 *            :存储计算后右眼中心位置
	 * @return void
	 */
	public void calculateEyeCenter(CvMat face, CvPoint leftEye, CvPoint rightEye) {
		int leftX = Math.round(face.cols() * EYE_SX);
		int topY = Math.round(face.rows() * EYE_SY);
		int widthX = Math.round(face.cols() * EYE_SW);
		int heightY = Math.round(face.rows() * EYE_SH);
		int rightX = (int) Math.round(face.cols() * (1.0 - EYE_SX - EYE_SW));

		if (leftEye == null) {
			leftEye = cvPoint(leftX + widthX / 2, topY + heightY / 2);
		} else {
			leftEye.x(leftX + widthX / 2);
			leftEye.y(topY + heightY / 2);
		}

		if (rightEye == null) {
			rightEye = cvPoint(rightX + widthX / 2, topY + heightY / 2);
		} else {
			rightEye.x(rightX + widthX / 2);
			rightEye.y(topY + heightY / 2);
		}

	}

}
