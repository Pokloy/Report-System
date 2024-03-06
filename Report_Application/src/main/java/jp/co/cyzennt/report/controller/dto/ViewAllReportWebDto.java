package jp.co.cyzennt.report.controller.dto;

import java.util.ArrayList;
import java.util.List;

import jp.co.cyzennt.report.model.object.ViewReportObj;
import lombok.Data;

@Data
public class ViewAllReportWebDto {
	//set page Number
	private int page;
	//set user id pk
	private int userIdPk;
	//set list of report obj
	private List<ViewReportObj> userReportObj;
	//set list of all reports
	private List<ViewReportObj> allReports;
	//set is changed 
	private boolean isChanged;
	//set list of specific reports
	private List<ViewReportObj> specReports;
	//set previous button value
	private String pageController;
	//set page Limit
	private int pageLimit1;
	//set page Limit
	private int pageLimit2;
	//set page limit
	private int pageLimit;
	//set pageNumbers1
	private ArrayList<Integer> pageNumbers1;
	//set pageNumebrs2
	private ArrayList<Integer> pageNumbers2;

}
