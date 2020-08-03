package org.springbus.comutergraphics.CG.common;// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// Group.java
// 物体のグループ化用のクラス
//	プログラム３−１０

public class Group extends Object3d {

	static int numGroups = 0;
	public Group(){ numGroups++; }// コンストラクタ
	public int getGroupNumber(){
		return numGroups;
	}

	// ダミーメソッド
	public void getNearerIntersection(Ray ray, ObjectNode node){}
	public void setNearestIntersection(Ray ray){}
	public void draw(Camera c, ObjectNode node){} 
}
