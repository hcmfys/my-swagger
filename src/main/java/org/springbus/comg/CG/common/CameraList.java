package org.springbus.comg.CG.common;// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// CameraList.java
// カメラデータのリンクつきリスト

public class CameraList extends MyObject {

	static int numCameras=0;//カメラの数
	Camera camera;//カメラオブジェクト保持用
	String name;//カメラの名前
	CameraList next = null;//リストの次の要素へのポインタ

	// コンストラクタ
	public CameraList(Camera camera, String name){
		numCameras++;
		this.camera = camera;
		this.name = name;
		this.next = null;
	}
}
