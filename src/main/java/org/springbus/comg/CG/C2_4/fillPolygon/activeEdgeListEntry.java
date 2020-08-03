package org.springbus.comg.CG.C2_4.fillPolygon;
// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// activeEdgeListEntry.java
// ここにアクティブエッジリストの要素を入れる
//	プログラム２−１０

public class activeEdgeListEntry {

	int name;//インデックス
	int topx;//Y座標の大きい方の頂点のX座標
	int topy;//Y座標の大きい方の頂点のY座標
	int boty;//Y座標の小さい方の頂点のY座標
	double delta; // = -(botx-topx)/(boty-topy)
	double x; //あるスキャンラインYでのX座標の値
	boolean isHorizontal;//水平辺かどうかのフラグ
	activeEdgeListEntry next;//次の要素へのポインタ

}
