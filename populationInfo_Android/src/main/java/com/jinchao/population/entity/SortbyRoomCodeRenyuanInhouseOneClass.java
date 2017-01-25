package com.jinchao.population.entity;

import com.jinchao.population.entity.RenyuanInHouseBean.RenyuanInhouseOne;

import java.util.Comparator;

public class SortbyRoomCodeRenyuanInhouseOneClass implements Comparator{

	@Override
	public int compare(Object lhs, Object rhs) {
		RenyuanInhouseOne realPeopleinHouseOnel=(RenyuanInhouseOne) lhs;
		RenyuanInhouseOne realPeopleinHouseOner=(RenyuanInhouseOne) rhs;
		int flag =realPeopleinHouseOnel.shihao.trim().compareTo(realPeopleinHouseOner.shihao.trim());
		return flag;
	}

}
