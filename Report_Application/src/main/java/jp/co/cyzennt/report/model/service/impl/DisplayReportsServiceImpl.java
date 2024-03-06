package jp.co.cyzennt.report.model.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.common.util.ReusableFunctions;
import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.logic.CreateReportLogic;
import jp.co.cyzennt.report.model.logic.DisplayReportsLogic;
import jp.co.cyzennt.report.model.logic.UserLogic;

import jp.co.cyzennt.report.model.object.DailyReportObj;
import jp.co.cyzennt.report.model.service.DisplayReportsService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
@Service
public class DisplayReportsServiceImpl implements DisplayReportsService {
	@Autowired 
	DisplayReportsLogic displayReportsLogic;		
	@Autowired
	private LoggedInUserService loginUserService;	
			
	@Autowired
	private ReusableFunctions rf;
	
	
	@Override
	public ReportInOutDto getUserReportList() {		
	    // Create a new instance of ReportInOutDto
		ReportInOutDto outDto = new ReportInOutDto();		
		// Retrieve the currently logged-in user's information
		UserInformationEntity user = loginUserService.getLoggedInUser();		
		// Retrieve a list of DailyReportEntity objects associated with the user
		List<DailyReportEntity> entityList =  displayReportsLogic.getUserDailyReportByUserIdPk(user.getIdPk());		
		// Create a new list to store DailyReportObj objects
		List<DailyReportObj> dailyReportObj = new ArrayList<DailyReportObj>();		
		// Check if the entityList is empty or null, and if so, return an empty outDto
		if(entityList == null) {
			return outDto;
		}		
		// Iterate through the list of DailyReportEntity objects and convert them to DailyReportObj objects
		for(DailyReportEntity entity : entityList) {
		// Formatting reportDate from "yyyyMMdd" to "MM/dd/yyyy" format.
			String formattedReportDate = rf.convertedDatabaseReportDateToAReadableFormat(
					entity.getReportDate(), "yyyyMMdd", "MM/dd/yyyy");
			// Create a new DailyReportObj
			DailyReportObj reportObj = new DailyReportObj();
			//set status in reportObj
	        reportObj.setStatus(entity.getStatus());
			// Set the ID of the report object using the ID from the entity
			reportObj.setIdPk(String.valueOf(entity.getIdPk()));			
			// Set the report date of the report object using the date from the entity
			reportObj.setReportDate(entity.getReportDate());
			//set userIdpk
			reportObj.setUserIdPk(entity.getUserIdPk());
			//set formattedReportDate
			reportObj.setFormattedReportDate(formattedReportDate);
			// Add the report object (reportObj) to the list (dailyReportObj)
			dailyReportObj.add(reportObj);
		}			
		// Set the list of DailyReportObj objects in the outDto
		outDto.setReportList(dailyReportObj);		
		// Return the outDto containing the user's report list
		return outDto;
	}
	
	@Override
	public ReportInOutDto getAllDailyReportByIdPkandWithStatus0() {
	// Create an instance of ReportInOutDto to store the result
    ReportInOutDto outDto = new ReportInOutDto();	    
    // Get the currently logged-in user information
    UserInformationEntity user = loginUserService.getLoggedInUser();	    
    // Retrieve a list of DailyReportEntity objects with status 1 for the user
    List<DailyReportEntity> entityList = displayReportsLogic.getAllDailyReportByIdPkandWithStatus0(user.getIdPk());	    
    // Create a list to store DailyReportObj objects
    List<DailyReportObj> dailyReportObj = new ArrayList<DailyReportObj>();	    
    // If the entityList is null, return an empty ReportInOutDto
    if (entityList == null) {
	//return outDto
    return outDto;
    }    
    // Iterate through the retrieved DailyReportEntity objects
    for (DailyReportEntity entity : entityList) {    
		// Formatting reportDate from "yyyyMMdd" to "MM/dd/yyyy" format.
		String formattedReportDate = rf.convertedDatabaseReportDateToAReadableFormat(entity.getReportDate(), "yyyyMMdd", "MM/dd/yyyy");		    	
	    // Create a new DailyReportObj
	    DailyReportObj reportObj = new DailyReportObj();
	    //set status in reportObj
	    reportObj.setStatus(entity.getStatus());						
	    // Set the ID from the entity as a string
	    reportObj.setIdPk(String.valueOf(entity.getIdPk()));	        
	    // Set the report date from the entity
	    reportObj.setReportDate(entity.getReportDate());
	    //set formattedReportDate
	    reportObj.setFormattedReportDate(formattedReportDate);		        
	    // Add the reportObj to the dailyReportObj list
	    dailyReportObj.add(reportObj);
	 }	    
    // Set the list of DailyReportObj objects in the outDto
    outDto.setReportList(dailyReportObj);    
    // Return the populated ReportInOutDto
	return outDto;
	}
    
	@Override
	public ReportInOutDto getAllDailyReportByIdPkandWithStatus1() {
	    // Create an instance of ReportInOutDto to store the result
	    ReportInOutDto outDto = new ReportInOutDto();		    
	    // Get the currently logged-in user information
	    UserInformationEntity user = loginUserService.getLoggedInUser();	    
	    // Retrieve a list of DailyReportEntity objects with status 1 for the user
	    List<DailyReportEntity> entityList = displayReportsLogic.getAllDailyReportByIdPkandWithStatus1(user.getIdPk());    
	    // Create a list to store DailyReportObj objects
	    List<DailyReportObj> dailyReportObj = new ArrayList<DailyReportObj>();	    
	    // If the entityList is null, return an empty ReportInOutDto
	    if (entityList == null) {
	       //return outDto
	    	return outDto;
	    }		    
	    // Iterate through the retrieved DailyReportEntity objects
	    for (DailyReportEntity entity : entityList) {		    	
	    	// Formatting reportDate from "yyyyMMdd" to "MM/dd/yyyy" format.
			String formattedReportDate = rf.convertedDatabaseReportDateToAReadableFormat(
					entity.getReportDate(), "yyyyMMdd", "MM/dd/yyyy");
	        // Create a new DailyReportObj
	        DailyReportObj reportObj = new DailyReportObj();
	        // Set the ID from the entity as a string
	        reportObj.setIdPk(String.valueOf(entity.getIdPk()));
	        //set status in reportObj
	        reportObj.setStatus(entity.getStatus());
	        // Set the report date from the entity
	        reportObj.setReportDate(entity.getReportDate());
	        //set formattedReportDate
	        reportObj.setFormattedReportDate(formattedReportDate);
	        // Add the reportObj to the dailyReportObj list
	        dailyReportObj.add(reportObj);
	    }
		    
	    // Set the list of DailyReportObj objects in the outDto
	    outDto.setReportList(dailyReportObj);	    
	    // Return the populated ReportInOutDto
	    return outDto;
		} 
}
