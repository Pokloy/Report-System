package jp.co.cyzennt.report.model.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.Base64;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.controller.dto.UserCreationWebDto;
import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.logic.CreateEditUserLogic;
import jp.co.cyzennt.report.model.logic.UserLogic;
import jp.co.cyzennt.report.model.object.UserInfoDetailsObj;
import jp.co.cyzennt.report.model.service.AdminEditLeaderInfoService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.le.duke.common.util.ApplicationPropertiesRead;
import jp.co.le.duke.common.util.DateFormatUtil;

@Service
public class AdminEditLeaderInfoServiceImpl implements AdminEditLeaderInfoService {

	@Autowired
	UserLogic userLogic;
	
	@Autowired
	LoggedInUserService loggedInUserService;
	
	@Autowired
	CreateEditUserLogic createEditUserLogic;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public UserCreationInOutDto getAUserInfoForEditByALeader(int userIdPk) {
		//instantiating usercreationinoutdto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		//finding the user from m_user_information
		UserInformationEntity userInformation = userLogic.getUserByIdPk(userIdPk);
		//finding the user from m_user_info_account
		UserInformationAccountEntity userInformationAcc = userLogic.getUserInfoByUserIdPk(userIdPk);
		//initiating obj to filled in with information to be shown on the edit page
		UserInfoDetailsObj obj = new UserInfoDetailsObj();
		 
		//getting the firstName of the user
		obj.setFirstName(userInformation.getFirstName());
		//getting the lastName of the user
		obj.setLastName(userInformation.getLastName());
		//getting the username
		obj.setUsername(userInformation.getUsername());
		//getting the password
		obj.setPassword(userInformationAcc.getPassword());
		//getting the display picture
		obj.setDisplayPicture(userInformation.getDisplayPicture());
		//getting email
		obj.setMailAddress(userInformation.getMailAddress());
		// getting idPk
		obj.setIdPk(userInformation.getIdPk());
		
		
		outDto.setUserInfo(obj);
		
		return outDto;
	}

	@Override
public boolean saveEditedLeaderInfo(UserCreationWebDto webDto, int leaderIdPk, HttpSession session) {
		
		// gets logged in users info
		UserInformationEntity loggedInUser = loggedInUserService.getLoggedInUser();
		// today's timestamp
		Timestamp timeNow = DateFormatUtil.getSysDate();
		// retrieve info from DB depending on the leaderIdPk
		UserInformationEntity updateInfo = userLogic.getUserByIdPk(leaderIdPk);
		// getting userInfoAcc 
		UserInformationAccountEntity updateInfoAcc = userLogic.getUserInfoByUserIdPk(leaderIdPk);
		updateInfo.setFirstName(webDto.getFirstName());
		updateInfo.setLastName(webDto.getLastName());
		updateInfo.setUsername(webDto.getUsername());
		updateInfo.setMailAddress(webDto.getMailAddress());
		updateInfo.setUpdateDate(timeNow);
		updateInfo.setUpdateId(loggedInUser.getRole());
		
		//checking if leader did indeed update the photo
		if(webDto.getDisplayPicture() != null && !webDto.getDisplayPicture().isEmpty()) {
			//combine the final path and the image
			updateInfo.setDisplayPicture(webDto.getDisplayPicture());
		} else {
			//retain the same photo saved in the database if no new image is uploaded
			updateInfo.setDisplayPicture(updateInfo.getDisplayPicture());
		}
		
		// declaration of new password
		String newPassword = webDto.getPassword();
		
		if(!newPassword.equals(updateInfoAcc.getPassword())) {
			// saves the new Password if false
			updateInfoAcc.setPassword(passwordEncoder.encode(newPassword));
			// set updateDate
			updateInfoAcc.setUpdateDate(timeNow);
			// set updateId
			updateInfoAcc.setUpdateId(loggedInUser.getRole());
			// save the updateInfoAcc
			createEditUserLogic.saveUserInformationAccount(updateInfoAcc);
		}
		
		// saving the updated leader info
		createEditUserLogic.saveUserInformation(updateInfo);
		
		return true;
	}

	@Override
	public UserCreationInOutDto movingTemporaryImageToFinalFolderUser(String imageName, UserCreationWebDto webDto, int userIdPk) {
		// instantiate new outDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		// 
		UserInformationEntity loggedInUser = loggedInUserService.getLoggedInUser();
	
		// Use the temporaryimage.path property from application.properties
		String baseDirectoryPath = ApplicationPropertiesRead.read("image.path");
		// Construct the local directory path
		String userDirectoryPath = baseDirectoryPath + "user" + "\\" + userIdPk +  "\\";
		
		//setting the saved image to a string readable
		//format to be followed
		// get loggedInUser email without the domain
		String[] emailAddress = loggedInUser.getMailAddress().split("@");
		
		// set filename
		String filename = "user_pic_" + emailAddress[0] + ".jpg";
	      
		// create the subfolder if it does not exist
		File userDirectory = new File(userDirectoryPath);
		if(!userDirectory.exists()) {
			userDirectory.mkdirs();
		}
		
		// Construct the full path for the image
		String imagePath = userDirectoryPath + filename;
		// get the decoded bytes
		byte[] decodedBytes = Base64.getDecoder().decode(imageName);
		// surround the outputstreams in try catch block
		try(FileOutputStream stream = new FileOutputStream(imagePath)) {
			// write the stream with the array of bytes
			stream.write(decodedBytes);
		// fileoutputstream throws FileNotFoundException so we catch it
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// stream.write() throws IOException
		} catch (IOException e) {
			e.printStackTrace();
		}
		
      outDto.setDisplayPicture(imagePath);
      
      return outDto;
	}

	@Override
	public String convertedImageFromTheDatabaseToBase64(File file) {
		//initiate empty string
		String convertedImage = "";
		//try catch the value of the converted image
		try {
			 byte[] fileContent = Files.readAllBytes(file.toPath());
			convertedImage = Base64.getEncoder().encodeToString(fileContent);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertedImage;
	}
}
