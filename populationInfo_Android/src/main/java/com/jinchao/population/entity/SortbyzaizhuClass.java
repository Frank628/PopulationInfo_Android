package com.jinchao.population.entity;

import java.util.Comparator;

import com.jinchao.population.entity.RealPeopleinHouseBean.RealPeopleinHouseOne;

public class SortbyzaizhuClass implements Comparator{

	@Override
	public int compare(Object lhs, Object rhs) {
		RealPeopleinHouseOne realPeopleinHouseOnel=(RealPeopleinHouseOne) lhs;
		RealPeopleinHouseOne realPeopleinHouseOner=(RealPeopleinHouseOne) rhs;
		int flag =String.valueOf(realPeopleinHouseOner.status).trim().compareTo(String.valueOf(realPeopleinHouseOnel.status).trim());
		return flag;
	}

}
