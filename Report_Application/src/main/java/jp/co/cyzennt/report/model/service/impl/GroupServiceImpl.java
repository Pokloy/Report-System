package jp.co.cyzennt.report.model.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.common.constant.MessageConstant;
import jp.co.cyzennt.report.controller.dto.GroupCreationWebDto;
import jp.co.cyzennt.report.model.dao.entity.GroupEntity;
import jp.co.cyzennt.report.model.dao.entity.GroupUserViewEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
import jp.co.cyzennt.report.model.logic.AdminCreateGroupLogic;
import jp.co.cyzennt.report.model.logic.GroupLogic;
import jp.co.cyzennt.report.model.logic.GroupUserViewLogic;
import jp.co.cyzennt.report.model.logic.UserLogic;
import jp.co.cyzennt.report.model.service.GroupService;
import jp.co.cyzennt.report.model.service.LoggedInUserService;
import jp.co.le.duke.common.util.ApplicationPropertiesRead;
import jp.co.le.duke.common.util.DateFormatUtil;

/**
 * GroupRegistrationServiceImpl
 * @author Christian
 * created: 09/29/2023
 */

@Service
@Scope("prototype")
public class GroupServiceImpl implements GroupService{
	
	@Autowired
	private GroupLogic groupLogic;
	
	@Autowired
	private GroupUserViewLogic groupUserViewLogic;
	
	@Autowired
	private UserLogic userLogic;
	
	@Autowired
	private LoggedInUserService loggedInUserService;
	
	@Autowired
	AdminCreateGroupLogic createGroupLogic;

	@Override
	public GroupCreationInOutDto getUserPermission(GroupCreationInOutDto inDto) {
		// Get user info by idPk
		UserInformationEntity loggedInUser = loggedInUserService.getLoggedInUser();
		// Instantiate new GroupCreationInOutDto
		GroupCreationInOutDto outDto = new GroupCreationInOutDto();
		// get role entity by idPk
		UserInformationEntity entityPermissions = userLogic.getPermissionId(loggedInUser.getIdPk());
		// checking for role permission
		if (entityPermissions.getPermissionId().equals("3")) {
//			outDto.setGroupCreateFlg(true);
//			System.out.println(entityPermissions.getPermissionId());
		} else {
//			outDto.setGroupCreateFlg(false);
		}
		return outDto;
	}

	/**
	 * assigns leader to a group
	 * @param List<Integer> selectedGroupIdPk
	 * @param List<Integer> selectedUserIdPk
	 * @return outDto
	 */
	@Override
	public GroupCreationInOutDto assignLeaderToGroup(List<Integer> selectedGroupIdPk, List<Integer> selectedUserIdPk) {
		UserInformationEntity loggedInUser = loggedInUserService.getLoggedInUser();
		// today's timestamp
		Timestamp timeNow = DateFormatUtil.getSysDate();
		// create new instance of GroupInOutDto named outDto
		GroupCreationInOutDto outDto = new GroupCreationInOutDto();
		// creates a new groupUserView entity
		GroupUserViewEntity groupView = new GroupUserViewEntity();
		List<GroupEntity> groups = new ArrayList<GroupEntity>();
		List<UserInformationEntity> leaders = new ArrayList<UserInformationEntity>();
		
		// loop through the selected groupIdPk
		for (int i : selectedGroupIdPk) {
			// get the group entity
			GroupEntity group = groupLogic.getGroupInfo(i);
			// 
			groupView.setGroupIdPk(group.getIdPk());
			//
			groups.add(group);
		}
		
		for (int x : selectedUserIdPk) {
			// get the leader entity
			UserInformationEntity leader = userLogic.getUserByIdPk(x);
			//
			groupView.setUserIdPk(leader.getIdPk());
			//
			leaders.add(leader);
		}
		// set regId
		groupView.setRegId(loggedInUser.getRole());
		// set regDate
		groupView.setRegDate(timeNow);
		// set updateId
		groupView.setUpdateId(loggedInUser.getRole());
		// set updateDate
		groupView.setUpdateDate(timeNow);
		groupUserViewLogic.saveGroupUserViewInfo(groupView);
		
		// return outDto
		return outDto;
	}
	
	/**
	 * getting user information with respective group information
	 * return a user
	 * @author Karl James
	 * October 11,2023
	 * */
	@Override
	public GroupCreationInOutDto getUserInfoWithGroupInfoByUserIdPk(int userIdPk) {
		// create new outDto
		GroupCreationInOutDto outDto = new GroupCreationInOutDto();
		// get the group by idPk
		GroupEntity user = groupLogic.getUserInfoWithGroupInfoByUserIdPk(userIdPk);
		
		//if the entity is not null, set the groupName
		if (user != null)
		
	
		//set the groupName
		outDto.setGroupName(user.getGroupName());
		// return outDto
		return outDto;
	}

	/**
	 * saves existing user (excluding users with ADMIN role) as Admin
	 * @param List<Integer> selectedUserIdPk
	 * @return GroupCreationInOutDto
	 */
	@Override
	public GroupCreationInOutDto saveExistingUserAsAdmin(List<Integer> selectedUserIdPk) {
		// get the user info by idPk
		UserInformationEntity loggedInUser = loggedInUserService.getLoggedInUser();
		// today's timestamp
		Timestamp timeNow = DateFormatUtil.getSysDate();
		// create a new outDto object
		GroupCreationInOutDto outDto = new GroupCreationInOutDto();
		//
		List<UserInformationEntity> usersList = new ArrayList<UserInformationEntity>();
		
		// loop through the selected userIdPK
		for (int i : selectedUserIdPk) {
			// get user entity
			UserInformationEntity user = userLogic.getUserByIdPk(i);
			// if user is null
			if (user == null) {
				// continue
				continue;
			}
			// set new updateDate
			user.setUpdateDate(timeNow);
			// set new updateId
			user.setUpdateId(loggedInUser.getRole());
			// set new permissionId
			user.setPermissionId("3");
			// set new role
			user.setRole("ADMIN");
			// 
			usersList.add(user);
		}
		// save the user lists
		userLogic.saveAllUsers(usersList);
		// set success message
		outDto.setSuccessMsg(MessageConstant.GROUP_DELETED);
		// set the return code to normal
		outDto.setReturnCd(CommonConstant.RETURN_CD_NOMAL);
		// return outDto
		return outDto;
	}
	
	/**
	 * removes leader from a group
	 * @param leaderIdPk
	 * @return outDto
	 */
	public GroupCreationInOutDto removeLeaderFromGroup (int leaderIdPk) {
		// get the logged in user info
		UserInformationEntity loggedInUser = loggedInUserService.getLoggedInUser();
		// today's timestamp
		Timestamp timeNow = DateFormatUtil.getSysDate();
		// create a new outDto object
		GroupCreationInOutDto outDto = new GroupCreationInOutDto();
		
		// get groupUserViewInfo by userIdPk
		GroupUserViewEntity guvLeader = groupUserViewLogic.findUserInfoByUserIdPk(leaderIdPk);
		
		// set the update date
		guvLeader.setUpdateDate(timeNow);
		// set the update id
		guvLeader.setUpdateId(loggedInUser.getRole());
		// archives the leader from group
		guvLeader.setDeleteFlg(true);
		// save the groupUserView
		groupUserViewLogic.saveGroupUserViewInfo(guvLeader);
		// return outDto
		return outDto;
	}

	/** checks if group name already exists
	 * @param groupName
	 * @return boolean
	 */
	@Override
	public boolean isGroupExist(String groupName) {
		// the number of groups with the same name
		int count = groupLogic.countByGroupName(groupName);
		
		// when count is more than 0
		if(count > 0) {
			return true;
		}
		return false;
	}

	@Override
	public String convertMultipartImageToStrings(MultipartFile filename, GroupCreationWebDto groupWebDto) {
		//
		String encodedImg = "";
		// encoding the uploaded image to string. In this case, "file" is the uploaded image
		try {
			byte[] bytes = filename.getBytes();
		    encodedImg = Base64.getEncoder().encodeToString(bytes);
		    
		    groupWebDto.setDisplayPicture(encodedImg);

		    // the encoder catches ioexception
		} catch (IOException e) {
		    e.printStackTrace();
		}
	
		return encodedImg;
	}

	@Override
	public GroupCreationInOutDto movingTemporaryImageToFinalFolder(String imageName, GroupCreationWebDto groupWebDto) {
		GroupCreationInOutDto outDto  = new GroupCreationInOutDto();
		// retrieves the path for saving an image in application properties
		String baseDirectoryPath = ApplicationPropertiesRead.read("image.path");
		// path for saving group photos
		String userDirectoryPath = baseDirectoryPath + "group" + "\\";
	
		// setting the saved image to a string readable
		// format to be followed

		// get user info by pk
		UserInformationEntity loggedInUser = loggedInUserService.getLoggedInUser();
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
      //  } catch (FileNotFoundException e) {
       //     e.printStackTrace();
        // stream.write() throws IOException
        } catch (IOException e) {
            e.printStackTrace();
        }
        outDto.setDisplayPhoto(imagePath);
		
     	return outDto;
	}

	@Override
	public String convertedImageFromTheDatabaseToBase64(File file) {
		String convertedImage = "";
		
		try {
			 byte[] fileContent = Files.readAllBytes(file.toPath());
			convertedImage = Base64.getEncoder().encodeToString(fileContent);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertedImage;
	}

	/** get information of the randomly selected group information
	 * @return int
	 * @author Karl James Arboiz
	 * November 22, 2023
	 */
	
	@Override
	public int randomSelectedGroupIdPkWhereLeaderBelonged() {
		
		//get randomly selected group
		GroupEntity groupInfo = groupLogic.randomSelectedGroupWhereLeaderBelonged(loggedInUserService.getLoggedInUser().getIdPk());
		if(groupInfo == null) {
			return 0;
		}
		//return value		
		return groupInfo.getIdPk();
	}
	
	
	
}
