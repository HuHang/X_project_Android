package com.iftech.car.utils;

import com.iftech.car.trace.bean.ShopBean;

import java.util.Comparator;

/**
 * 首个中文转字母
 * */
public class PinyinComparator implements Comparator<ShopBean> {

	public int compare(ShopBean o1, ShopBean o2) {
		if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}
}
