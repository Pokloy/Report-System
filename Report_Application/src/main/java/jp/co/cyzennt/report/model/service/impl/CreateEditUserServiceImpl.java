package jp.co.cyzennt.report.model.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.controller.dto.UserCreationWebDto;
import jp.co.cyzennt.report.model.dao.entity.GroupUserViewEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.logic.CreateEditUserLogic;
import jp.co.cyzennt.report.model.logic.GroupUserViewLogic;
import jp.co.cyzennt.report.model.service.CreateEditUserService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.le.duke.common.util.ApplicationPropertiesRead;
 
@Service
public class CreateEditUserServiceImpl implements CreateEditUserService{
	//inject groupUserViewLogic 
	@Autowired
	private GroupUserViewLogic groupUserViewLogic;
	
	@Autowired
	private LoggedInUserService loggedInUserService;
	
	//inject createEditUserLogic
	@Autowired
	private CreateEditUserLogic createEditUserLogic; 

	
	/**
	 * checks if username already exists
	 * @param username
	 * @param userIdPk
	 * @return boolean
	 */
	@Override
	public boolean isUsernameExist(String username) {
		// the number of usernames with the same name
		int count = createEditUserLogic.countByUsernameWithoutUserIdPk(username);
		
		// when count is more than 0
		if(count > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * checks if email address already exists
	 * @param username
	 * @param userIdPk
	 * @return boolean
	 */
	@Override
	public boolean isEmailExist(String mailAddress) {
		// the number of email with the same name
		int count = createEditUserLogic.countByEmailWithoutUserIdPk(mailAddress);

		// when count is more than 0
		if(count > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * changing the multipart image to string to make it readable when rendered on the confirmation page
	 * @param inDto
	 * @author Karl James Arboiz
	 * 11/03/2023
	 */
	
	@Override 
	public String convertMultipartImageToStrings(MultipartFile filename
			,UserCreationWebDto webDto) {
		String encodedImg = "";
		// encoding the uploaded image to string. In this case, "file" is the uploaded image
		try {
			//declaring the byte[]
			byte[] bytes = filename.getBytes();
			//encoding the image to base64
		    encodedImg = Base64.getEncoder().encodeToString(bytes);

		    // the encoder catches ioexception
		} catch (IOException e) {
		    e.printStackTrace();
		}
	
		return encodedImg;
	}
	
	/*
	 * uploading the image from being encoded to base64 to something readable
	 * @params String imageName, webDto
	 * @author Karl James
	 * @updated
	 * November 03, 2023
	 * */
	
	@Override 
	public UserCreationInOutDto movingSessionImageToFinalFolder(String imageName) {
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		// Use the temporaryimage.path property from application.properties
	  String baseDirectoryPath = ApplicationPropertiesRead.read("image.path");
	  // Construct the local directory path
	  String userDirectoryPath = baseDirectoryPath;
	  //setting the saved image to a string readable
	//format to be followed
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		//local date format
		LocalDateTime now = LocalDateTime.now(); // Get the current date and time
		// Format the current date and time as a string
		String timestamp = now.format(formatter);
	// set filename
      String filename = timestamp + ".jpg";
      
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
	
	/**saving information of the user
	 * @param webDto
	 * @author Karl James Arboiz
	 * 10/02/2023
	 */
	@Override
	public UserCreationInOutDto saveUser(UserCreationInOutDto webDto) {
		
		UserInformationEntity user = loggedInUserService.getLoggedInUser();
		//instatiate UserCreationInOutDto
		UserCreationInOutDto outDto = new UserCreationInOutDto();
		//setting new User to save
		UserInformationEntity newUser = new UserInformationEntity();
//		//setting the information of the loggedin user
//		UserInformationEntity user = loggedInUserService.getLoggedInUser();
		//setting timestamp
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		//set mail address
		newUser.setMailAddress(webDto.getMailAddress());
		//set username
		newUser.setUsername(webDto.getUsername());
		//set role
		newUser.setRole("USER");
		//set permission id
		newUser.setPermissionId("1");
		//set lastname
		newUser.setLastName(webDto.getLastName());
		//set first name
		newUser.setFirstName(webDto.getFirstName());
		//set display picture
		newUser.setDisplayPicture(webDto.getDisplayPicture());
		//set updateId
		newUser.setUpdateId(user.getUpdateId());
		//set updateDate
		newUser.setUpdateDate(timestamp);
		//set regId
		newUser.setRegId(user.getRegId());
		//set regDate
		newUser.setRegDate(timestamp);
		//saving data to m_user_information
		createEditUserLogic.saveUserInformation(newUser);
		
		//this section is for saving data to m_user_info_account
		UserInformationAccountEntity newUserAcc = new UserInformationAccountEntity();
		//set useridpk
		newUserAcc.setUserIdPk(newUser.getIdPk());
		//set password
		newUserAcc.setPassword(webDto.getPassword());
		//set regId
		newUserAcc.setRegId(user.getRegId());
		//set registerDate
		newUserAcc.setRegDate(timestamp);
		//set updateId
		newUserAcc.setUpdateId(user.getUpdateId());
		//set update date
		newUserAcc.setUpdateDate(timestamp);
		//set deleteflag
		newUserAcc.setDeleteFlg(false);
		//saving data to m_user_information
		createEditUserLogic.saveUserInformationAccount(newUserAcc);
	
		
		//condition set to block groupIdpk equals 0
		if(webDto.getGroupIdPk() != CommonConstant.PK_NOT_INCLUDED) {
			//this section is for saving data to t_group_user_view
			//initiating groupUserViewEntity 
			GroupUserViewEntity groupUserViewEntity = new GroupUserViewEntity();
			//set groupIdpk
			groupUserViewEntity.setGroupIdPk(webDto.getGroupIdPk());
			//set regDate
			groupUserViewEntity.setRegDate(timestamp);
			//set UpdateDate
			groupUserViewEntity.setUpdateDate(timestamp);
			//set UpdateDate
			groupUserViewEntity.setRegId(user.getRegId());;
			//set UpdateId
			groupUserViewEntity.setUpdateId(user.getUpdateId());
			//set UpdateDate
			groupUserViewEntity.setUserIdPk(newUser.getIdPk());
			//saving data to t_group_user_view
			groupUserViewLogic.saveGroupUserViewInfo(groupUserViewEntity);
		} 
		
		//set return cd in outDto
		outDto.setReturnCd(CommonConstant.RETURN_CD_NOMAL);
		//return outDto
		return outDto;
	}
}
