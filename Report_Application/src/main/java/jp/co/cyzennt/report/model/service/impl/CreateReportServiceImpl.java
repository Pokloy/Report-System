package jp.co.cyzennt.report.model.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.common.constant.MessageConstant;
import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;
import jp.co.cyzennt.report.model.dao.entity.SelfEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.logic.CreateReportLogic;
import jp.co.cyzennt.report.model.service.CreateReportService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.le.duke.common.util.ApplicationPropertiesRead;
import jp.co.le.duke.common.util.DateFormatUtil;

@Service
public class CreateReportServiceImpl implements CreateReportService {
	

	// Autowired annotation to inject the UserReportLogic bean dependency.
	@Autowired
	CreateReportLogic createReportLogic;

	// Autowired annotation to inject the LoggedInUserService bean dependency.
	@Autowired
	private LoggedInUserService loginUserService;
	
	/**
	 * save Report
	 * 
	 * @param eportInOutDto inDto
	 * @return ReportInOutDto
	 * 
	 */
	public ReportInOutDto saveReport(ReportInOutDto inDto) {
		ReportInOutDto outDto = new ReportInOutDto();
		// Obtain information about a logged-in user using a method called
		// getLoggedInUser()
		UserInformationEntity user = loginUserService.getLoggedInUser();
		// count
		int count = createReportLogic.countDailyReport(user.getIdPk(), inDto.getInputDate());
		if (count == 1) {
			// set retrun code
			outDto.setReturnCd(CommonConstant.RETURN_CD_INVALID);
			// set errmsg
			outDto.setErrMsg(MessageConstant.DAILY_REPORT_EXISTED);
			// return outDto
			return outDto;
		}
		// Create a new instance of the DailyReportEntity
		DailyReportEntity dailyReportEntity = new DailyReportEntity();
		// Set the current timestamp value
		Timestamp timeNow = DateFormatUtil.getSysDate();
		// Set the user ID in the DailyReportEntity
		dailyReportEntity.setUserIdPk(user.getIdPk());
		// Set the report date in the DailyReportEntity from the input DTO
		dailyReportEntity.setReportDate(inDto.getInputDate());
		// Set the target in the DailyReportEntity from the input DTO
		dailyReportEntity.setTarget(inDto.getTarget());
		// Set the status in the DailyReportEntity (assumed "0" as a default value)
		dailyReportEntity.setStatus("0");
		// Set the registration ID in the DailyReportEntity from the user's role
		dailyReportEntity.setRegId(user.getRole());
		// Set the registration date in the DailyReportEntity to the current timestamp
		dailyReportEntity.setRegDate(timeNow);
		// Set the update ID in the DailyReportEntity from the user's role
		dailyReportEntity.setUpdateId(user.getRole());
		// Set the update date in the DailyReportEntity to the current timestamp
		dailyReportEntity.setUpdateDate(timeNow);
		// Set the delete flag in the DailyReportEntity to false
		dailyReportEntity.setDeleteFlg(false);
		// Save the DailyReportEntity data and retrieve the result
		DailyReportEntity entity = createReportLogic.saveDailyReport(dailyReportEntity);
		if (entity == null) {
			return outDto;
		}
		outDto.setDailyReportIdPk(entity.getIdPk());
		// Create a new instance of SelfEvaluationEntity
		SelfEvaluationEntity selfEvaluationEntity = new SelfEvaluationEntity();
		// Set the DailyReportIdPk in the SelfEvaluationEntity from the previously saved
		// dailyReportEntity
		selfEvaluationEntity.setDailyReportIdPk(dailyReportEntity.getIdPk());
		// Set comments in the SelfEvaluationEntity from the input DTO
		selfEvaluationEntity.setComment(inDto.getComments());
		// Set rating in the SelfEvaluationEntity from the input DTO
		selfEvaluationEntity.setRating(inDto.getRatings());
		// Set registration ID in the SelfEvaluationEntity from the user's role
		selfEvaluationEntity.setRegId(user.getRole());
		// Set registration date in the SelfEvaluationEntity to the current timestamp
		selfEvaluationEntity.setRegDate(timeNow);
		// Set update ID in the SelfEvaluationEntity from the user's role
		selfEvaluationEntity.setUpdateId(user.getRole());
		// Set update date in the SelfEvaluationEntity to the current timestamp
		selfEvaluationEntity.setUpdateDate(timeNow);
		// Set delete flag in the SelfEvaluationEntity to false
		selfEvaluationEntity.setDeleteFlg(false);
		// Save data to selfEvaluationEntity
		createReportLogic.saveSelfEvaluation(selfEvaluationEntity);
		// set return code
		outDto.setReturnCd(CommonConstant.RETURN_CD_NOMAL);
		// return outDto
		return outDto;
	} 
	/**
	 * encode multipart image to string
	 * 
	 * @param inDto
	 * @return list of encoded strings
	 */
	// This method overrides a superclass method to encode image files in a list of
	// strings.
	@Override
	public ReportInOutDto  encodeImgFiles(ReportInOutDto inDto) {
		ReportInOutDto outDto = new ReportInOutDto();
		// Create a list to store the encoded image strings.
		List<String> imageStrings = new ArrayList<String>();
		// Iterate through the MultipartFile objects in the input DTO..
		for (MultipartFile file : inDto.getImages()) {
			if (file.isEmpty()) {
				continue; // Skip the empty file.
			}else {			
				 if (isValidImageFile(file)) {
					 try {						
							// Encode the MultipartFile to Base64 and add the encoded string to the list.
							imageStrings.add(Base64.getEncoder().encodeToString(file.getBytes()));
						} catch (IOException e) {
							// Handle any IOException that may occur during the encoding process.
							// TODO: Proper error handling should be implemented here.
							e.printStackTrace(); // Print the stack trace for debugging purposes.
						}				 
				 }else {
					  outDto.setErrMsgForFile(MessageConstant.IMAGE_VALIDATION);
				 }				
			}
		}
		outDto.setImageStrings(imageStrings);
		// Return the list of encoded image strings.
		return outDto;
	}
	
	@Override
	 public boolean isValidImageFile(MultipartFile file) {
	        // Get the file name and convert it to lowercase for case-insensitive comparison.
	        String fileName = file.getOriginalFilename().toLowerCase();
	        // Check if the file has a valid image extension (png or jpeg/jpg).
	        return fileName.endsWith(".png") || fileName.endsWith(".jpeg") || fileName.endsWith(".jpg");
	    }
	/**
	 * 
	 * method for counting dailyReport
	 * 
	 * @author glaze
	 * @param inDto
	 * @return 11/15/2023
	 */
	@Override
	public boolean countDailyReport(ReportInOutDto inDto) {
		// Obtain information about a logged-in user using a method called
		// getLoggedInUser()
		UserInformationEntity user = loginUserService.getLoggedInUser();
		// count
		int count = createReportLogic.countDailyReport(user.getIdPk(), inDto.getInputDate());
		// Return true if count is greater than 0, otherwise return false
		return count > 0;
	}

	/**
	 * save attached file to local directory
	 * 
	 * @param inDto ReportInOutDto
	 * @return ReportInOutDto
	 * @author glaze 10/25/2023
	 */
	@Override
	public ReportInOutDto saveAttachedToLocalDiretory(ReportInOutDto inDto, int dailyReportIdPk) {
		// instantiate ReportInOutDto
		ReportInOutDto outDto = new ReportInOutDto();
		// Create a list to store the paths of saved images
		List<String> savedImages = new ArrayList<>();
		UserInformationEntity user = loginUserService.getLoggedInUser();
		// Retrieve the dailyReportIdPk and userIdPk from the saved ReportInOutDto
		int userIdPk = user.getIdPk();
		// Initialize the increment number to 1
		int incrementNumber = 1;
		// Use the temporaryimage.path property from application.properties
		String baseDirectoryPath = ApplicationPropertiesRead.read("image.path.daily");
		// Set reportDate value from inDto
		String reportDate = inDto.getReportDate();
		// Construct the local directory path
		String userDirectoryPath = baseDirectoryPath + userIdPk + "\\";
		// Create the userIdPk subfolder if it doesn't exist
		File userDirectory = new File(userDirectoryPath);
		if (!userDirectory.exists()) {
			userDirectory.mkdirs(); // Create the subfolder if it doesn't exist
		}
		// Check if any required variables are null
		if (inDto.getEncodedString() == null) {
			// Handle the null case, perhaps by throwing an exception or logging an error
			// For example:
			return outDto;
		}
		// Save each attached image with the increment number in the filename
		for (String encImage : inDto.getEncodedString()) {
			// set filename
			String filename = reportDate + "_" + dailyReportIdPk + "_" + incrementNumber + ".jpg";
			// Construct the full path for the image
			String imagePath = userDirectoryPath + filename;
			// get the decoded bytes
			byte[] decodedBytes = Base64.getDecoder().decode(encImage);
			// surround the outputstreams in try catch block
			try (FileOutputStream stream = new FileOutputStream(imagePath)) {
				// write the stream with the array of bytes
				stream.write(decodedBytes);				
			} catch (IOException e) {
				e.printStackTrace();
			} 
			// add the images to saved images
			savedImages.add(imagePath);
			// Increment the number for the next image
			incrementNumber++;
		}
		outDto.setFilePaths(savedImages);
		return outDto;
	}

	/**
	 * saving the attached file paths in the Database
	 * 
	 * @param ReportInOutDto inDto
	 * @return ReportInOutDt 10/26/2023
	 */
	@Override
	public ReportInOutDto saveAttachedFileFilePathsToDatabase(ReportInOutDto inDto) {

		// Create an output data transfer object (DTO) to store the result.
		ReportInOutDto outDto = new ReportInOutDto();
		// increment num
		int incrementNum = 0;
		// Iterate through the list of file paths provided in the input DTO.
		for (String filePath : inDto.getFilePaths()) {
			// Retrieve information about the currently logged-in user.
			UserInformationEntity user = loginUserService.getLoggedInUser();
			// Get the current system timestamp for record creation.
			Timestamp timeNow = DateFormatUtil.getSysDate();
			// created a new instance of the EvalAttachedFileEntity
			EvalAttachedFileEntity evalAttachedEntity = new EvalAttachedFileEntity();
			// Check if the operation is for creating a new report
			if (inDto.isForCreateReport() == true) {
				// Get the daily report for the current user
				DailyReportEntity report = createReportLogic.getDailyReportByUserIdPk(user.getIdPk());
				// Set the DailyReportIdPk in the evaluation attached entity
				evalAttachedEntity.setDailyReportIdPk(report.getIdPk());
			} else if (inDto.isForEditReport() == true) {
				// The operation is not for creating a new report
				// Get the list of daily reports for the current user on a specific date
				List<DailyReportEntity> dailyReport = createReportLogic.getDailyReportByUserIdAndDate(user.getIdPk(),
						inDto.getReportDate());
				// Set the DailyReportIdPk in the evaluation attached entity using the first
				// report in the list
				evalAttachedEntity.setDailyReportIdPk(dailyReport.get(0).getIdPk());
			} else if (inDto.isForEvaluation() == true) {
				// Get the list of daily reports for the current user on a specific date
				List<DailyReportEntity> dailyReport = createReportLogic.getDailyReportByUserIdAndDate(inDto.getUserIdPk(), inDto.getReportDate());
				// Set the DailyReportIdPk in the evaluation attached entity using the first
				// report in the list
			
				evalAttachedEntity.setDailyReportIdPk(dailyReport.get(0).getIdPk());
			}
			// set increment num
			evalAttachedEntity.setIncrementNum(++incrementNum);
			// set uploaderIdPk
			evalAttachedEntity.setUploaderIdPk(user.getIdPk());
			// set RegId
			evalAttachedEntity.setRegId(user.getRole());
			// set RegDate
			evalAttachedEntity.setRegDate(timeNow);
			// set UpdateId
			evalAttachedEntity.setUpdateId(user.getRole());
			// set UpdateDate
			evalAttachedEntity.setUpdateDate(timeNow);
			// set DeleteFlg
			evalAttachedEntity.setDeleteFlg(false);
			// set Filepath value
			evalAttachedEntity.setFilePath(filePath);
			// Call userReportLogic for saving attached file data in the database
			createReportLogic.saveEvalAttachedFile(evalAttachedEntity);
		}
		outDto.setReturnCd(CommonConstant.RETURN_CD_NOMAL);
		// return outDto
		return outDto;
	}

	 

}
