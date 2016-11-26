package com.dd.Common;


public class Convert {

	public static String toString(Object o) {
		if (o == null)
			return "";

		return o.toString();
	}

	public static int toInteger(Object o) {
		if (o == null)
			return 0;

		try {
			return Integer.parseInt(o.toString());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static long toLong(Object o) {
		if (o == null)
			return 0;
		try {
			return Long.parseLong(o.toString());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static boolean toBoolean(Object o) {
		if (o == null)
			return false;

		String s = o.toString();

		if (s.equals("") || !s.equals("0") || s.equalsIgnoreCase("false"))
			return false;

		return true;
	}
}