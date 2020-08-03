package org.springbus.comutergraphics.CG.C2_4.fillPolygon;
// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// activeEdgeList.java
// アクティブエッジリストおよび関連メソッド
//	プログラム２−１１

public class activeEdgeList {

	activeEdgeListEntry header = null;//先頭要素
	activeEdgeListEntry tailer = null;//末尾要素

	// コンストラクタ
	public activeEdgeList(activeEdgeListEntry element){
		header = tailer = element;
	}

	// 挿入ソート
	public void insert(activeEdgeListEntry element){
		activeEdgeListEntry sentinel;//見張り番
		if (element == null || this.header == null) 
			throw new NullPointerException();
		sentinel = this.header;
		int xt = element.topx; int xtold = sentinel.topx;
		double oldDelta = sentinel.delta; double newDelta = element.delta;
		if ((xtold < xt) || ((xtold == xt) && (oldDelta < newDelta))){
			// 新しいデータの方がバケットのデータよりリスト
			// の後ろ側にくる場合
			while (true) {
				if (sentinel.next == null){//末尾に追加して終了
					sentinel.next = element;
					this.tailer = element; break;
				}
				activeEdgeListEntry mp = sentinel.next;
				int xmt = mp.topx;
				double midDelta = mp.delta;
				if ((xmt<xt) ||((xmt == xt)&&(midDelta < newDelta)))								sentinel = mp;//新しい要素はまだ未定
				else { // 間に挿入して終了
					sentinel.next = element;
					element.next = mp;
					break;//tailerは不変
				}
			}
		}
		else {//バケットの最初に挿入
			sentinel = this.header;
			this.header = element; element.next = sentinel;
		}
	}

	// 末尾に要素を追加
	public void append(activeEdgeListEntry element){
		if (element == null) throw new NullPointerException();
		if (tailer == null){ 
			tailer = element; header = element;}//はじめての要素だった場合
		else { tailer.next = element; tailer = element;} //末尾に追加
	}

	// 2つのエッジリストをマージソート
	public activeEdgeList merge(activeEdgeList list, int y){
		if (list == null) return this;
		activeEdgeListEntry a = this.header;//現在のactiveHeader
		activeEdgeListEntry b = list.header;//バケットリスト
		activeEdgeListEntry save = null; // マージするためのポインタ
		activeEdgeList result = null;//返り値をおく変数
		activeEdgeListEntry anext, bnext;//テンポラリに使用
		while (true){
			if (a == null && b == null) return(result);
			else if (a == null){
				if (save != null) {
					save.next = b; result.tailer = list.tailer;
				}
				return(result);
			}
			else if (b == null){
				if (save != null){ 
					save.next = a; result.tailer = this.tailer; }
				return(result);
			}
			int xa = (int)(a.topx + (a.topy - y) * a.delta);
			int xb = b.topx;//バケット側のX値
			if (xa <= xb) {//aの方をresultリストに追加
				anext = a.next; a.next = null;
				if (result == null) {
					result = new activeEdgeList(a);
					save = result.tailer;
				}
				else result.append(a); 
				save = a;
				a = anext;
			}
			else {
				bnext = b.next; b.next = null;
				if (result == null){
					result = new activeEdgeList(b);
					save = result.tailer;
				}
				else result.append(b); 
				save = b;
				b = bnext;
			}
		}
	}

	// アクティブエッジリストの要素を削除
	public void remove(activeEdgeListEntry element){
		activeEdgeListEntry p, q;
		if (header == element){//削除の要素はリストの最初の要素
			header = element.next;
			if (tailer == element) tailer = header;
			return;
		}
		p = q = header;
		while (p != element){
			q = p; p = p.next;
			if (p == null) throw new NullPointerException(); //見つからなかった
		}
		if (element == tailer) tailer = q; //削除の要素は末尾の要素
		q.next = p.next;// ここで本当に削除
	}

	//アクティブエッジリストを走査し，必要に応じ要素をスワップ
	public void traverse(){
		activeEdgeListEntry p, q, r, tmp; p = r = header;
		while (p != null){
			q = p.next;	if (q == null) break;
			if (q.x < p.x){//スワップ
				tmp = q.next; if (p == header) header = q;
				else r.next = q; q.next = p; p.next = tmp;
			}
			r = p; p = p.next;
		}
		tailer = r;
	}
}
