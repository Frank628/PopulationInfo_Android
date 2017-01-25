package com.jinchao.population.entity;


import java.util.Comparator;
import com.jinchao.population.entity.RenyuanInHouseBean.RenyuanInhouseOne;
public class SortbyTimeRenyuanInhouseOneClass implements Comparator{

	@Override
	public int compare(Object lhs, Object rhs) {
		RenyuanInhouseOne realPeopleinHouseOnel=(RenyuanInhouseOne) lhs;
		RenyuanInhouseOne realPeopleinHouseOner=(RenyuanInhouseOne) rhs;
		int flag =realPeopleinHouseOnel.write_time.compareTo(realPeopleinHouseOner.write_time);
		return flag;
	}

}
