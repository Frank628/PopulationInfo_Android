package com.jinchao.population.entity;

import java.util.Comparator;

import com.jinchao.population.entity.RealPeopleinHouseBean.RealPeopleinHouseOne;

public class SortbyTimeClass implements Comparator{

	@Override
	public int compare(Object lhs, Object rhs) {
		RealPeopleinHouseOne realPeopleinHouseOnel=(RealPeopleinHouseOne) lhs;
		RealPeopleinHouseOne realPeopleinHouseOner=(RealPeopleinHouseOne) rhs;
		int flag =realPeopleinHouseOner.chkudt.compareTo(realPeopleinHouseOnel.chkudt);
		return flag;
	}

}
