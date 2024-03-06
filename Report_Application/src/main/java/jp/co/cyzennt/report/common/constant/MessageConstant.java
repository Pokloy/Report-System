package jp.co.cyzennt.report.common.constant;

/**
 * MessageConstants
 * @initialized by Christian
 * 09/29/2023
 */

public class MessageConstant {
	// create group
	public static final String GROUP_CREATED = "Group created successfully!";
	// edit group
	public static final String GROUP_EDITED = "Group updated successfully!";
	// group delete error
	public static final String GROUP_ERROR_DELETE = "Error deleting group. Please try again.";
//	// group is linked
//	public static final String GROUP_ERROR_LINKED = "The selected group is currently in use and cannot be deleted.";
	// group deleted
	public static final String GROUP_DELETED = "The selected group is deleted successfully!";
	
//	when leader successfully created a new member
	public static final String USER_CREATED = "User is successfully created";
	
	//when report is submitted successfully
	public static final String DAILY_REPORT_CREATED = "You have submitted the report successfully!!";
	//when report already exist
	public static final String DAILY_REPORT_EXISTED = "DAILY REPORT ALREADY EXIST!";
	//when report is already evaluated and user tried to edit report
	public static final String DAILY_REPORT_EVALUATED ="Report has been evaluated and is now locked for further edits!";
	//SUCCESSFUL EDIT
	public static final String DAILY_REPORT_EDITED =" YOU HAVE SUCCESSFULLY EDITED THE REPORT!!";
	//SUCCESSFUL EDIT
	public static final String DAILY_REPORT_EVALUATED_SUCCESSFULLY =" You have successfully evaluated the report!!";
	//SUCCESSFUL USERPROFILE EDIT
	public static final String USER_PROFILE_EDITED =" YOU HAVE SUCESSFULLY EDITED YOUR PROFILE";
	
	//SUCCESSFUL EDIT Evaluation
	public static final String DAILY_REPORT_EVALUATION_EDITED_SUCCESSFULLY =" You have successfully edited the report evaluation!!";
	
	//SUCCESSFUL USERPROFILE EDIT
		public static final String USER_DELETE_REPORT ="REPORT DELETED SUCCESSFULLY.";
	//image validation 
	public static final String IMAGE_VALIDATION ="Invalid file format. Must be jpeg, jpg or png files.";
}
