package jp.co.cyzennt.report.controller;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itextpdf.text.DocumentException;

import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;

import jp.co.cyzennt.report.model.service.GroupConfigureService;
import jp.co.cyzennt.report.model.service.LeaderTopService;
import jp.co.cyzennt.report.model.service.ViewWeeklyService;

/* 
 * building routes for leader controller
 * 
 * author Karl James Arboiz
 * 10/03/2023*/
 
@Controller
@RequestMapping("/view/leader")
public class ViewWeeklyController {
//	//private UserService userService
//	@Autowired
//	private UserService userService;
	@Autowired
	private GroupConfigureService groupConfigureService;
	
	@Autowired
	private LeaderTopService leaderTopService;
	
	@Autowired
	private ViewWeeklyService viewWeeklyService;
	
	
	

	
	/**
	 * generate bi-weekly report html
	 * @param model
	 * @param requestparam groupIdPk
	 * @param requestparam userIdPk
	 * @author Karl James
	 * 11/14/2023
	 * @throws DocumentException 
	 * @throws IOException 
	 */ 
	
	@PostMapping(path="/generate-report",params="biweekly-report")
	public String generateBiWeeklyReport(
			@RequestParam("groupIdPk") int groupIdPk,
			@RequestParam("randomUserIdPk") int randomIdPk,
			Model model) throws DocumentException, IOException {
		//initiating outDto
		UserCreationInOutDto outDto = leaderTopService.getListOfMembersUnderTheSameGroupBasedOnGroupIdPkAndRole(groupIdPk,"USER");
		//getting the current month
		LocalDate currentDate = LocalDate.now().withDayOfMonth(1);
		//getting the date of the day
		int dateInt = LocalDate.now().getDayOfMonth();
		//getting the 15h or half of the month
		LocalDate halfDayOfMonth = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 15);
        //gettin the last day of the month
		LocalDate lastDayOfMonth = halfDayOfMonth.withDayOfMonth(1).plusMonths(1).minusDays(1);
		//format of the start and end dates for query
		DateTimeFormatter queryFormatDate = DateTimeFormatter.ofPattern("yyyyMMdd");
		//getting random user info by the random id pk selected
		UserCreationInOutDto userInfo = groupConfigureService.getUserInfo(randomIdPk);
		
		//initiate startDate
		LocalDate startDate = null;
		//initiate end date
		LocalDate endDate = null;
		
		if( dateInt > 1 && dateInt <= 15 ) {
			//getting the previous month
			LocalDate previousDate = LocalDate.now().minusMonths(1);
			//getting the date of the 26th day of the previous month as the start date
			startDate =LocalDate.of(previousDate.getYear(), previousDate.getMonth(), 26);
			//getting the end date which is the current month's 10th day date
 			endDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 10);
		
		}else if(dateInt > 15 && dateInt <= lastDayOfMonth.getDayOfMonth()) {
			//getting the date of the 11th day of the current month as the start date
			startDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 11);
			//getting the end date which is the current month's 25th day date
			endDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 25);
		}
		viewWeeklyService.generateCSV(groupIdPk,startDate.format(queryFormatDate),endDate.format(queryFormatDate));
	
		LocalDate mondayStartDate = startDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate fridayEndDate = endDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.FRIDAY));
	
		List<LocalDate> generatedArr = generateArr(mondayStartDate,fridayEndDate);
	
		viewWeeklyService.generatePDF(groupIdPk, generatedArr,mondayStartDate,fridayEndDate);
		//model for showing all users
		model.addAttribute("users",outDto.getUsers()); 
		//mode attri for randomly choosen user Id pk for default option in view weekly report
		model.addAttribute("randomIdPk",randomIdPk);
		//model for user infor
		model.addAttribute("userInfo",userInfo);
		//model attri for group
		model.addAttribute("groupIdPk",groupIdPk);
	
		return "leader/viewweeklyreport";
	}
	
	

	
	public List<LocalDate>  generateArr(LocalDate startDate,LocalDate endDate) {

        // Define a list to store the dates
        List<LocalDate> datesInRange = new ArrayList<>();

        // Iterate over the range of dates and add them to the list
        LocalDate currentDate = startDate;
   
        while (!currentDate.isAfter(endDate)) {
            datesInRange.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        return datesInRange;
       
	}
	

	/**
	 * toggle among individual's report
	 * @param model
	 * @param requestparam groupIdPk
	 * @param requestparam userIdPk
	 * @author Karl James
	 * 11/14/2023
	 * @throws DocumentException 
	 * @throws IOException 
	 */ 
	@PostMapping(path="/generate-report")
	public String getIndividualBiWeeklyReport(
			@RequestParam("groupIdPk") int groupIdPk,
			@RequestParam("userIdPk") int userIdPk,
//			@RequestParam("startDate") String startDate,
//			@RequestParam("endDate") String endDate,
			Model model) throws DocumentException, IOException {
		//return to page for weekly report
		return generateBiWeeklyReport(groupIdPk,userIdPk,model);
	}
	
}
