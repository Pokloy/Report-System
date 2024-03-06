package jp.co.cyzennt.report.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.cyzennt.report.controller.dto.ViewAllReportWebDto;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.cyzennt.report.model.service.ViewAllReportService;


@Controller
@RequestMapping("/view/leader")
public class ViewAllReportController { 
	//autowired loggedinuserservice
	@Autowired
	private LoggedInUserService loggedInUserService;
	//autowired viewallreportservice
	@Autowired
	private ViewAllReportService viewAllReportService;
	//autowired httpsession
	@Autowired
	private HttpSession session;
	/**
	 * this is to get all reports of the members where leader have authority to evaluate
	 * @param model
	 * @author Karl James
	 * 10/11/2023
	 */
	//route for viewallreports
	@PostMapping(path="/viewallreports",params="viewallreports")
	public String getAllReports(
			ViewAllReportWebDto webDto,Model model) {
		//get page number
		int pageNumber = webDto.getPage();
		//get id pk
		int leaderIdPk = loggedInUserService.getLoggedInUser().getIdPk();
		//get divided report size
		float reportSizeDivided = viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(leaderIdPk);
		//get page list from helperFunction
		ArrayList<Integer> pageList = helperFunction1(reportSizeDivided);
		//initiating outDto
		ReportInOutDto outDto = viewAllReportService.getAllUsersInformationUnderTheLoggedInLeader(leaderIdPk);
		//initiating ReportInOutDto
		ReportInOutDto reports = viewAllReportService.getAllUsersReport(leaderIdPk,pageNumber);
		//set list view report obj
		webDto.setUserReportObj(outDto.getViewReportList());
		//model attri for pageNumber
		webDto.setPage(pageNumber);
		//set reports
		webDto.setAllReports(reports.getViewReportList());
		//wen
		webDto.setPageLimit1(pageList.size());
		//set pageNumbers1
		webDto.setPageNumbers1(pageList);
		//getting profile picture
		model.addAttribute("profilePhoto",(String) session.getAttribute("profilePhoto"));		
		//model for default user id pk
		return "/leader/viewallreports";
	}
	
	/**
	 * this is to get specific report details
	 * @param model
	 * @param pathvariable
	 * @author Karl James
	 * 10/11/2023
	 */
	
	//mapping route for specificreport
	@PostMapping(path="/viewallreports")
	public String viewUserReports(
			ViewAllReportWebDto webDto,
			Model model) {
		
		//get user Id Pk
		int userIdPk = webDto.getUserIdPk();
		//get page number
		int pageNumber = webDto.getPage();
		//get id pk
		int leaderIdPk = loggedInUserService.getLoggedInUser().getIdPk();
		//model attri for selected
		model.addAttribute("selected",userIdPk);
		//model attri for pageNumber
		model.addAttribute("pageNumber",pageNumber);
		if(userIdPk == 0) {
			//set isChanged 
			webDto.setChanged(false);
			//initiating ReportInOutDto
			ReportInOutDto reports = viewAllReportService.getAllUsersReport(leaderIdPk,pageNumber);
			//get divided report size
			float reportSizeDivided = viewAllReportService.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(leaderIdPk);
			//get page list from helperFunction
			ArrayList<Integer> pageList = helperFunction1(reportSizeDivided);
			//set reports
			webDto.setAllReports(reports.getViewReportList());
			//set page limit
			webDto.setPageLimit1(pageList.size());
			//list changed
			webDto.setUserIdPk(userIdPk);
			//returning to the same html and at the same attaching the model
			webDto.setPage(pageNumber > pageList.size() ? pageList.size() : pageNumber);
			//set pageNumbers1
			webDto.setPageNumbers1(pageList);
		}else {//set isChanged 
			//set isChanged 
			webDto.setChanged(true);
			//initiating ReportInOutDto
			ReportInOutDto specificUserReports = viewAllReportService.getSpecificUserReportList(userIdPk,pageNumber);
			//get divided report size
			float reportSizeDivided = viewAllReportService.getSizeOfDailyReportsWithStatus1BasedOnUserIdPk(userIdPk);
			//get page list from helperFunction
			ArrayList<Integer> pageList = helperFunction1(reportSizeDivided);
			//set reports
			webDto.setSpecReports(specificUserReports.getViewReportList());
			//set page limit
			webDto.setPageLimit2(pageList.size());
			//set userIdPk
			webDto.setUserIdPk(userIdPk);
			//model attri for pageNumber
			webDto.setPage(pageNumber > pageList.size() ? pageList.size() : pageNumber);
			//set pageNumbers1
			webDto.setPageNumbers2(pageList);
		}
		
		return getAllReports(webDto,model);
	}
	
	/**
	 * this page controller function represented by PREV and NEXT buttons
	 * @param @RequestParam String pageController
	 * @param webDto
	 * @param model
	 * @author Karl James
	 * 02/07/2024
	 */
	@PostMapping(value="/viewallreports",params="pageController")
	public String changeReportPage(
			@RequestParam("pageController") String pageController,
			ViewAllReportWebDto webDto,
			Model model) {
		//get page Number
		int page = webDto.getPage();
		//get pageLimit
		int pageLimit = webDto.getPageLimit();
		//set page limit
		webDto.setPageLimit(pageLimit);
		//check pageCOntroller value
		if(pageController.equalsIgnoreCase("prev")) {
			//if pageController value if PREV
			//subtract page
			page--;
			//set page with page value
			webDto.setPage(page == 0 ? 1: page);
		}else {
			//increase page number
			page++;
			//set page number
			webDto.setPage(page == pageLimit ? pageLimit: page);
		}
		
		//model attri for pageNumber
		model.addAttribute("pageNumber",page);
		//return to view User Reports
		return viewUserReports(webDto, model);
	}
	
	/**
	 * this page controller function in the dropdown in between PREV and NEX 
	 * @param @RequestParam String pageController
	 * @param webDto
	 * @param model
	 * @author Karl James
	 * 02/07/2024
	 */
	
	@PostMapping(value="/viewallreports",params="pageselect")
	public String pageSelectChangeReport
	(@RequestParam("pageselect") int pageselect,
	ViewAllReportWebDto webDto, 
	Model model) {

		//get pageLimit
		int pageLimit = webDto.getPageLimit();
		//set page limit
		webDto.setPageLimit(pageLimit);
		//set page
		webDto.setPage(pageselect);
		//model attri for pageNumber
		model.addAttribute("pageNumber",pageselect);
		//return to view user report 
		return viewUserReports(webDto, model);
	}
	
	
	/**
	 * helper function to calculate and produce arraylist for number of pages
	 * @param float reportSizeDivided
	 * @author Karl James
	 * 02/07/2024
	 */
	 
	
	public ArrayList<Integer> helperFunction1(float reportSizeDivided){
		
		//initial value compare
		int initialValue = 0;
		//initialize an array list of display a page select in viewallreport html
		ArrayList<Integer> pageList = new ArrayList<Integer>();
		//loop thru the number of Pages
		while(initialValue < reportSizeDivided) {
			//increase initial value
			++initialValue;
			//add initial value
			pageList.add(initialValue);			
		}
		return pageList;
	}
}
