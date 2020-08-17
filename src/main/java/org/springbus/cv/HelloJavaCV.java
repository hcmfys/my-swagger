package org.springbus.cv;


import org.bytedeco.opencv.opencv_core.Mat;

import static org.bytedeco.opencv.global.opencv_highgui.imshow;
import static org.bytedeco.opencv.global.opencv_highgui.waitKey;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;

/**
 */
public class HelloJavaCV {

    public static void main(String[] args) {
        //读取原始图片
        Mat image = imread("E:\\yourimg.jpg");
        if (image.empty()) {
            System.err.println("加载图片出错，请检查图片路径！");
            return;
        }
        //显示图片
        imshow("显示原始图像", image);

        //无限等待按键按下
        waitKey(0);
    }
}
