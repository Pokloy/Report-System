package jp.co.cyzennt.report.model.service;

import jp.co.cyzennt.report.model.dto.GroupCreationInOutDto;
import jp.co.cyzennt.report.model.dto.ReportInOutDto;
//import jp.co.cyzennt.report.model.dto.UserCreationInOutDto;

public interface AdminTopService {
	/**
	 * 
	 * @param page
	 * @return list of all groups
	 */
	public GroupCreationInOutDto getAllGroup(int page);
	
	/**
	 * get one group by groupIdPk
	 * @param i
	 * @return
	 */
	public GroupCreationInOutDto getGroup(int idPk);
	
	/**
	 * delete selected groups
	 * @param selectedIdPk
	 * @return outDto
	 */
	public boolean deleteGroup(int groupIdPk);
	
	/**
	 * activates archived groups
	 * @param selectedIdPk
	 * @return outDto
	 */
	public boolean activateGroup(int groupIdPk);
	/**
	 * get list of all groups
	 * GroupCreationInOutDo
	 * @author Karl james
	 * created January 18, 2024
	 * */
	public GroupCreationInOutDto getListOfActiveGroups();
	
	/**
	 * 
	 * @return ReportInOutDto userInfo
	 */
	public ReportInOutDto getUserInfoByIdPk();
}
