package jp.co.cyzennt.report.model.service;

import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;

@Service
public interface AdminCreateLeaderService {
	/**
	 * save newly created leader
	 * @param inDto
	 */
	public void saveNewLeader (UserCreationInOutDto inDto, String imageName);
}
