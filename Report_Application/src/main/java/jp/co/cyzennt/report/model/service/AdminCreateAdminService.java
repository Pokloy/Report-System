package jp.co.cyzennt.report.model.service;

import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;

public interface AdminCreateAdminService {
	
	/**
	 * save newly created admin
	 * @param inDto
	 */
	public void saveNewAdmin (UserCreationInOutDto inDto, String imageName);

}
