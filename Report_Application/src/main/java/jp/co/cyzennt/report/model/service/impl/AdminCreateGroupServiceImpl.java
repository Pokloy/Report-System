package jp.co.cyzennt.report.model.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.entity.GroupEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
import jp.co.cyzennt.report.model.logic.AdminCreateGroupLogic;
import jp.co.cyzennt.report.model.service.AdminCreateGroupService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.le.duke.common.util.ApplicationPropertiesRead;
import jp.co.le.duke.common.util.DateFormatUtil;

@Service
@Scope("prototype")
public class AdminCreateGroupServiceImpl implements AdminCreateGroupService {
	
	@Autowired
	LoggedInUserService loggedInUserService;
	
	@Autowired
	AdminCreateGroupLogic createGroupLogic;

	@Override
	public GroupCreationInOutDto saveGroupInfo(GroupCreationInOutDto inDto, String imageName) {
		// get user info by pk
		UserInformationEntity loggedInUser = loggedInUserService.getLoggedInUser();
		// today's timestamp
		Timestamp timeNow = DateFormatUtil.getSysDate();
		// create new instance of GroupInOutDto named outDto
		GroupCreationInOutDto outDto = new GroupCreationInOutDto();
		
		// create new group entity
		GroupEntity entity = new GroupEntity();
		// set DeleteFlg
		entity.setDeleteFlg(false);
		// set groupName
		entity.setGroupName(inDto.getGroupName());
		// set regId
		entity.setRegId(loggedInUser.getRole());
		// set regDate
		entity.setRegDate(timeNow);
		// set updateId
		entity.setUpdateId(loggedInUser.getRole());
		// set updateDate
		entity.setUpdateDate(timeNow);
		// set display photo
		entity.setDisplayPhoto("default");
		// save the groupInfo entity
		createGroupLogic.saveGroupInfo(entity);
		// getting groupInfo by groupName
		GroupEntity groupInfo = createGroupLogic.getGroupInfo(entity.getGroupName());
		// instantiate int groupIdPk 
		int groupIdPk = groupInfo.getIdPk();
		// retrieves the path for saving an image in application properties
		String baseDirectoryPath = ApplicationPropertiesRead.read("image.path");
		// path for saving group photos
		String userDirectoryPath = baseDirectoryPath + "group" + "\\" + groupIdPk +  "\\";
		// setting the saved image to a string readable
		// format to be followed
		// get loggedInUser email without the domain
		String[] emailAddress = loggedInUser.getMailAddress().split("@");
		// set fileName
		String filename = "group_pic_" + emailAddress[0] + ".jpg";
		
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
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//         stream.write() throws IOException
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        
        
        entity.setDisplayPhoto(imagePath);
	
		// save the groupInfo entity
        createGroupLogic.saveGroupInfo(entity);
		
		return outDto;
	}
}
