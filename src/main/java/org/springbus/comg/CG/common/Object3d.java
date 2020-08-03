package org.springbus.comg.CG.common;// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// Object3d.java
// 抽象クラス
// 各メソッドは，このクラスのサブクラスで実体を定義する。
//	プログラム３−４

public abstract class Object3d extends MyObject {

	public abstract void draw(Camera c, ObjectNode node);
	public abstract void getNearerIntersection(Ray ray, ObjectNode node);
	public abstract void setNearestIntersection(Ray ray);

}
