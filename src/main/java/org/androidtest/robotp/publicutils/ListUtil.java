package org.androidtest.robotp.publicutils;

import java.util.*;

public class ListUtil {
	/**
	 * 使用时，务必保证每个对象中实现了equals()方法。 如果是自己写的类，比如，Dog，Cat这些的，请重写Object中的equals方法！
	 *
	 * @param aList
	 *            左右顺序无所谓
	 * @param bList
	 *            左右顺序无所谓
	 * @return 尽可能避免相同的情况
	 *         <p>
	 *         https://blog.csdn.net/qq_35640964/article/details/87881714
	 */
	public static boolean equals(List aList, List bList) {

		if (aList == bList)
			return true;

		if (aList.size() != bList.size())
			return false;
		int n = aList.size();
		int i = 0;
		while (n-- != 0) {
			if (!aList.get(i).equals(bList.get(i)))
				return false;
			i++;
		}
		return true;
	}

	// 降序排序
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(
			Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				int compare = (o1.getValue()).compareTo(o2.getValue());
				return -compare;
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	// 升序排序
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueAscending(
			Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				int compare = (o1.getValue()).compareTo(o2.getValue());
				return compare;
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}