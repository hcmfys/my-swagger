package org.springbus.comutergraphics.CG.C2_4.fillPolygon;


// activeEdgeList.java
//活动边缘列表和相关方法
//程序2-11

public class activeEdgeList {

	activeEdgeListEntry header = null;//第一要素
	activeEdgeListEntry tailer = null;//最后一个元素

	// 构造函数
	public activeEdgeList(activeEdgeListEntry element) {
		header = tailer = element;
	}

	// 插入排序
	public void insert(activeEdgeListEntry element) {
		activeEdgeListEntry sentinel;//哨兵
		if (element == null || this.header == null)
			throw new NullPointerException();
		sentinel = this.header;
		int xt = element.topx;
		int xtold = sentinel.topx;
		double oldDelta = sentinel.delta;
		double newDelta = element.delta;
		if ((xtold < xt) || ((xtold == xt) && (oldDelta < newDelta))) {
			//列出比存储桶数据更新的数据
			// の後ろ側にくる場合
			while (true) {
				if (sentinel.next == null) {//添加到末尾
					sentinel.next = element;
					this.tailer = element;
					break;
				}
				activeEdgeListEntry mp = sentinel.next;
				int xmt = mp.topx;
				double midDelta = mp.delta;
				if ((xmt < xt) || ((xmt == xt) && (midDelta < newDelta))) sentinel = mp;//新元素有待确定
				else { // 插入之间和结束
					sentinel.next = element;
					element.next = mp;
					break;//尾部保持不变
				}
			}
		} else {//插入存储桶的开头
			sentinel = this.header;
			this.header = element;
			element.next = sentinel;
		}
	}

	// 在结尾添加元素
	public void append(activeEdgeListEntry element) {
		if (element == null) throw new NullPointerException();
		if (tailer == null) {
			tailer = element;
			header = element;
		}//はじめての要素だった場合
		else {
			tailer.next = element;
			tailer = element;
		} //末尾に追加
	}

	// 合并排序两个边缘列表
	public activeEdgeList merge(activeEdgeList list, int y) {
		if (list == null) return this;
		activeEdgeListEntry a = this.header;//当前的activeHeader
		activeEdgeListEntry b = list.header;//バケットリスト
		activeEdgeListEntry save = null; // 合并指针
		activeEdgeList result = null;//用于存储返回值的变量
		activeEdgeListEntry anext, bnext;//用于临时
		while (true) {
			if (a == null && b == null) return (result);
			else if (a == null) {
				if (save != null) {
					save.next = b;
					result.tailer = list.tailer;
				}
				return (result);
			} else if (b == null) {
				if (save != null) {
					save.next = a;
					result.tailer = this.tailer;
				}
				return (result);
			}
			int xa = (int) (a.topx + (a.topy - y) * a.delta);
			int xb = b.topx;//铲斗侧的X值
			if (xa <= xb) {//将一个添加到结果列表
				anext = a.next;
				a.next = null;
				if (result == null) {
					result = new activeEdgeList(a);
					save = result.tailer;
				} else result.append(a);
				save = a;
				a = anext;
			} else {
				bnext = b.next;
				b.next = null;
				if (result == null) {
					result = new activeEdgeList(b);
					save = result.tailer;
				} else result.append(b);
				save = b;
				b = bnext;
			}
		}
	}

	// 删除活动边缘列表元素
	public void remove(activeEdgeListEntry element) {
		activeEdgeListEntry p, q;
		if (header == element) {//删除元素是列表中的第一个元素
			header = element.next;
			if (tailer == element) tailer = header;
			return;
		}
		p = q = header;
		while (p != element) {
			q = p;
			p = p.next;
			if (p == null) throw new NullPointerException(); //找不到
		}
		if (element == tailer) tailer = q; //删除的元素是最后一个元素
		q.next = p.next;// 真的在这里删除
	}

	//扫描活动边缘列表并根据需要交换元素
	public void traverse() {
		activeEdgeListEntry p, q, r, tmp;
		p = r = header;
		while (p != null) {
			q = p.next;
			if (q == null) break;
			if (q.x < p.x) {//交换
				tmp = q.next;
				if (p == header) header = q;
				else r.next = q;
				q.next = p;
				p.next = tmp;
			}
			r = p;
			p = p.next;
		}
		tailer = r;
	}
}
