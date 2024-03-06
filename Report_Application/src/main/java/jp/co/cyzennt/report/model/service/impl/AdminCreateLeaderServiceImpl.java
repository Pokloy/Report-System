package jp.co.cyzennt.report.model.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.UserInformationAccountEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;
import jp.co.cyzennt.report.model.logic.AdminCreateLeaderLogic;
import jp.co.cyzennt.report.model.logic.UserLogic;
import jp.co.cyzennt.report.model.service.AdminCreateLeaderService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.le.duke.common.util.ApplicationPropertiesRead;
import jp.co.le.duke.common.util.DateFormatUtil;

@Service
public class AdminCreateLeaderServiceImpl implements AdminCreateLeaderService {
	
	@Autowired
	LoggedInUserService loggedInUserService;
	
	@Autowired
	AdminCreateLeaderLogic createLeaderLogic;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserLogic userLogic;

	/**
	 * saves new leaderInfo
	 * @param inDto
	 * @author Christian
	 * 10/26/2023
	 */
	@Override
	public void saveNewLeader(UserCreationInOutDto inDto, String imageName) {
		// gets logged in user's info
		UserInformationEntity loggedInUser = loggedInUserService.getLoggedInUser();
		// today's timestamp
		Timestamp timeNow = DateFormatUtil.getSysDate();
		
		// create new leader userInfo entity
		UserInformationEntity leaderEntity = new UserInformationEntity();
		// set leader userName
		leaderEntity.setUsername(inDto.getUsername());
		// set leader email
		leaderEntity.setMailAddress(inDto.getMailAddress());
		// set leader firstName
		leaderEntity.setFirstName(inDto.getFirstName());
		//set leader lastName
		leaderEntity.setLastName(inDto.getLastName());
		// set leader role
		leaderEntity.setRole("LEADER");
		// set leader permissionId
		leaderEntity.setPermissionId("2");
		// set regId
		leaderEntity.setRegId(loggedInUser.getRole());
		// set regDate
		leaderEntity.setRegDate(timeNow);
		// set updateId
		leaderEntity.setUpdateId(loggedInUser.getRole());
		// set up// set updateIddateDate
		leaderEntity.setUpdateDate(timeNow);
		// set leader pic
		leaderEntity.setDisplayPicture("default");
		// save the new leader entity
		createLeaderLogic.saveUserInformation(leaderEntity);
		
		// gets info from the saved leader entity
		UserInformationEntity leaderInformation = userLogic.getUserInfo(leaderEntity.getUsername());
		// creates a new leader infoAccount entity
		UserInformationAccountEntity leaderAccountEntity = new UserInformationAccountEntity();
		// gets idPk from created leader idPk
		leaderAccountEntity.setUserIdPk(leaderInformation.getIdPk());
		// sets leader infoAccount password
		leaderAccountEntity.setPassword(passwordEncoder.encode(inDto.getPassword()));;
		// set regId
		leaderAccountEntity.setRegId(loggedInUser.getRole());
		// set regDate
		leaderAccountEntity.setRegDate(timeNow);
		// set updateId
		leaderAccountEntity.setUpdateId(loggedInUser.getRole());
		// set updateDate
		leaderAccountEntity.setUpdateDate(timeNow);
		// save the new leader infoAccount entity
		createLeaderLogic.saveUserInformationAccount(leaderAccountEntity);
		
		if(imageName != null) {
			// instantiate int groupIdPk 
			int userIdPk = leaderInformation.getIdPk();
			// retrieves the path for saving an image in application properties
			String baseDirectoryPath = ApplicationPropertiesRead.read("image.path");
			// path for saving group photos
			String userDirectoryPath = baseDirectoryPath + "user" + "\\" + userIdPk +  "\\";
			
			// setting the saved image to a string readable
			// format to be followed
			// get loggedInUser email without the domain
			String[] emailAddress = loggedInUser.getMailAddress().split("@");
			// set fileName
			String filename = "leader_pic_" + emailAddress[0] + ".jpg";
			
			// create the subfolder if it does not exist
			File userDirectory = new File(userDirectoryPath);
	     	if(!userDirectory.exists()) {
	     		userDirectory.mkdirs();
	     	}
	     	
	     	// construct the full path for the image
	     	String imagePath = userDirectoryPath + filename;
	     	
	     	// get the decoded bytes
	        byte[] decodedBytes = Base64.getDecoder().decode(imageName);
	        // surround the outputstreams in try catch block
	        try(FileOutputStream stream = new FileOutputStream(imagePath)) {
	            // write the stream with the array of bytes
	            stream.write(decodedBytes);
	        // fileoutputstream throws FileNotFoundException so we catch it
//	        } catch (FileNotFoundException e) {
//	            e.printStackTrace();
	        // stream.write() throws IOException
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        leaderEntity.setDisplayPicture(imagePath);
		} else {
			leaderEntity.setDisplayPicture("default");
		}
        // save the userInfoEntity
		createLeaderLogic.saveUserInformation(leaderEntity);
	}

}
