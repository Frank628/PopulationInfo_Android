package com.jinchao.population.entity;

import java.util.Comparator;

import com.jinchao.population.entity.RealPeopleinHouseBean.RealPeopleinHouseOne;

public class SortbyRoomCodeClass implements Comparator{

	@Override
	public int compare(Object lhs, Object rhs) {
		RealPeopleinHouseOne realPeopleinHouseOnel=(RealPeopleinHouseOne) lhs;
		RealPeopleinHouseOne realPeopleinHouseOner=(RealPeopleinHouseOne) rhs;
		int flag =realPeopleinHouseOnel.roomCode.trim().compareTo(realPeopleinHouseOner.roomCode.trim());
		return flag;
	}

}
