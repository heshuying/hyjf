package com.hyjf.api.operationaldata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiasq
 * @version InvestorSexAgeDataVO, v0.1 2018/1/16 17:55
 */
public class InvestorSexAgeDataVO {

	// 出借人男人占比（小数点后两位）
	private String investorRegionMenRate;
	// 出借人女人占比（小数点后两位）
	private String investorRegionWoMenRate;

	private List<InvestorAgeDistribution> investorAgeList;

	public static class InvestorAgeDistribution {
		private String name;
		private String value;
		private String color;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}
	}

	public String getInvestorRegionMenRate() {
		return investorRegionMenRate;
	}

	public void setInvestorRegionMenRate(String investorRegionMenRate) {
		this.investorRegionMenRate = investorRegionMenRate;
	}

	public String getInvestorRegionWoMenRate() {
		return investorRegionWoMenRate;
	}

	public void setInvestorRegionWoMenRate(String investorRegionWoMenRate) {
		this.investorRegionWoMenRate = investorRegionWoMenRate;
	}

	public List<InvestorAgeDistribution> getInvestorAgeList() {
		return investorAgeList;
	}

	public void setInvestorAgeList(List<InvestorAgeDistribution> investorAgeList) {
		this.investorAgeList = investorAgeList;
	}
}
