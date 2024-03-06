package jp.co.cyzennt.report.model.service;

import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;

@Service
public interface AdminCreateGroupService {
	/**
	 * save group info
	 * @param inDto
	 * @return GroupInOutDto
	 */
	public GroupCreationInOutDto saveGroupInfo(GroupCreationInOutDto inDto, String imageName);
}
