package me.blockcat.clocker;

public enum Datam {

	JANUARI(1, 31),
	FEBRUARI(2, 28),
	MARCH(3, 31),
	APRIL(4, 30),
	MAY(5, 31),
	JUNE(6, 30),
	JULI(7, 31),
	AUGUST(8, 31),
	SEPTEMBER(9, 30),
	OCTOBER(10, 31),
	NOVEMBER(11, 30),
	DECEMBER(12, 31);
	
	int day;
	int month;
	
	Datam(int month, int maxDays) {
		this.day = maxDays;
		this.month = month;
	}
	
	public static int getDaysbyId(int m) {
		for (Datam months : Datam.values()) {
			if (months.month == m) {
				return months.day;
			} else {
				continue;
			}
		}
		return 30;
	}
}
