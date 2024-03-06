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
import jp.co.cyzennt.report.model.logic.AdminCreateAdminLogic;
import jp.co.cyzennt.report.model.logic.UserLogic;
import jp.co.cyzennt.report.model.service.AdminCreateAdminService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.le.duke.common.util.ApplicationPropertiesRead;
import jp.co.le.duke.common.util.DateFormatUtil;

@Service
public class AdminCreateAdminServiceImpl implements AdminCreateAdminService {
	
	@Autowired
	private LoggedInUserService loggedInUserService;
	
	@Autowired
	AdminCreateAdminLogic createAdminLogic;
	
	@Autowired
	UserLogic userLogic;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public void saveNewAdmin(UserCreationInOutDto inDto, String imageName) {
		// gets logged in user's info
		UserInformationEntity loggedInUser = loggedInUserService.getLoggedInUser();
		// today's timestamp
		Timestamp timeNow = DateFormatUtil.getSysDate();
		// create new instance of GroupInOutDto named outDto
		
		// create new admin userInfo entity
		UserInformationEntity adminEntity = new UserInformationEntity();
		
		// set admin username
		adminEntity.setUsername(inDto.getUsername());
		// set admin email
		adminEntity.setMailAddress(inDto.getMailAddress());
		// set admin firstName
		adminEntity.setFirstName(inDto.getFirstName());
		// set admin lastName
		adminEntity.setLastName(inDto.getLastName());
		// set admin role
		adminEntity.setRole("ADMIN");
		// set admin permissionId
		adminEntity.setPermissionId("3");
		// set admin regId
		adminEntity.setRegId(loggedInUser.getRole());
		// set admin regDate
		adminEntity.setRegDate(timeNow);
		// set admin updateId
		adminEntity.setUpdateId(loggedInUser.getRole());
		// set admin updateDate
		adminEntity.setUpdateDate(timeNow);
		// set admin pic
		adminEntity.setDisplayPicture("default");
		// save the new admin info
		createAdminLogic.saveUserInformation(adminEntity);
		
		// gets info from saved leader entity
		UserInformationEntity adminInfo = userLogic.getUserInfo(adminEntity.getUsername());
		
		// creates new admin infoAcct
		UserInformationAccountEntity adminAccountEntity = new UserInformationAccountEntity();
		
		// gets idPk from newly created admin
		adminAccountEntity.setUserIdPk(adminInfo.getIdPk());
		// sets and encodes the admins password
		adminAccountEntity.setPassword(passwordEncoder.encode(inDto.getPassword()));
		// set the regId
		adminAccountEntity.setRegId(loggedInUser.getRole());
		// set the regDate
		adminAccountEntity.setRegDate(timeNow);
		// set the updateId
		adminAccountEntity.setUpdateId(loggedInUser.getRole());
		// set the updateDate
		adminAccountEntity.setUpdateDate(timeNow);
		// save the new admin infoAcct
		createAdminLogic.saveUserInformationAccount(adminAccountEntity);
		
		if(imageName != null) {
			// instantiate int groupIdPk 
			int userIdPk = adminInfo.getIdPk();
			// retrieves the path for saving an image in application properties
			String baseDirectoryPath = ApplicationPropertiesRead.read("image.path");
			// path for saving group photos
			String userDirectoryPath = baseDirectoryPath + "user" + "\\" + userIdPk +  "\\";
			
			// setting the saved image to a string readable
			// format to be followed
			// get loggedInUser email without the domain
			String[] emailAddress = loggedInUser.getMailAddress().split("@");
			// set fileName
			String filename = "admin_pic_" + emailAddress[0] + ".jpg";
			
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
//	        // fileoutputstream throws FileNotFoundException so we catch it
//	        } catch (FileNotFoundException e) {
//	            e.printStackTrace();
	        // stream.write() throws IOException
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        adminEntity.setDisplayPicture(imagePath);
		} else {
			adminEntity.setDisplayPicture("default");
		}
        
        //
		createAdminLogic.saveUserInformation(adminEntity);
	}

}
