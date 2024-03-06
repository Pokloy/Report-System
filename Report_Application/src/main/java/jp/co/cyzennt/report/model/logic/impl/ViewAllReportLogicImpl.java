package jp.co.cyzennt.report.model.logic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jp.co.cyzennt.report.model.dao.DailyReportDao;
import jp.co.cyzennt.report.model.dao.GroupUserViewDao;
import jp.co.cyzennt.report.model.dao.UserInformationDao;
import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.GroupUserViewEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.logic.ViewAllReportLogic;

@Service
public class ViewAllReportLogicImpl implements ViewAllReportLogic{
	@Autowired
	private UserInformationDao userInformationDao;
	@Autowired
	private GroupUserViewDao groupUserViewDao;
	
	@Autowired
	private DailyReportDao dailyReportDao;
	
	/** getting a list of group where leader belongs
	 * @params userIdpK
	 * return List
	 * @author Karl James
	 * November 8, 2023
	 */
	@Override
	public List<GroupUserViewEntity> getAListOfGroupWhereALeaderBelongsUsingUserIdPk(int userIdPk){
		return groupUserViewDao.getAListOfGroupWhereALeaderBelongsUsingUserIdPk(userIdPk);
	}
	

	
	/**
	 * get list of active users whose group id pk matches
	 * any group id pks of where the leader id pk belonged
	 * @param leaderidPk
	 * @return List<UserInformationEntity>
	 * @author Karl James
	 * created January 09, 2024
	 */
	
	@Override
	public List<UserInformationEntity> 
	getAListOfActiveUsersWhoseGroupIdPkMatchesGroupIdPksWhereLeaderBelong(int leaderIdPk) {
		return userInformationDao.getAListOfActiveUsersWhoseGroupIdPkMatchesGroupIdPksWhereLeaderBelong(leaderIdPk);
	}
	
	/**
	 * getting list of evaluated reports based on groupIdPks based on the leader id pk
	 *@param leaderIdPk
	 *@param Pageable page
	 * return List<DailyReportEntity>
	 * @author Karl James Arboiz
	 * 01/25/2024
	 */
	@Override
	public List<DailyReportEntity> getListOfEvaluatedReportsOFUsersBelongingToTheGroupIdPksWhereLeaderBelong(int leaderIdPk, int pageNumber){
		//get page number
		Pageable pageReq = PageRequest.of(pageNumber-1, 30);
		//generate report
		return dailyReportDao.getListOfEvaluatedReportsOFUsersBelongingToTheGroupIdPksWhereLeaderBelong(leaderIdPk,pageReq);
	}
	
	/**
	 * getting size of  list of evaluated reports based on groupIdPks based on the leader id pk
	 *@param leaderIdPk
	 * return List<DailyReportEntity>
	 * @throws DataAccessException
	 * @author Karl James Arboiz
	 * 02/06/2024
	 */
	@Override
	public List<DailyReportEntity> getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(int leaderIdPk) {
		return dailyReportDao.getSizeOfReturnedEvaluatedReportsOfUsersFromGroupsWhereLeaderExist(leaderIdPk);
	}
	/**
	 * getting size of  list of daily evaluated reports based user IdpK
	 *@param user Id pk
	 * return List<DailyReportEntity>
	 * @throws DataAccessException
	 * @author Karl James Arboiz
	 * 02/06/2024
	 */
	@Override
	public List<DailyReportEntity> getSizeOfDailyReportsWithStatus1BasedOnUserIdPk(int userIdPk) {
		return dailyReportDao.getSizeOfDailyReportsWithStatus1BasedOnUserIdPk(userIdPk);
	}
	
	/** 
	 * getting  list of daily evaluated reports based user IdpK with pagination
	 *@param userIdPk
	 *@param Pageable page
	 * return List<DailyReportEntity>
	 * @throws DataAccessException
	 * @author Karl James Arboiz
	 * 02/06/2024
	 */
	@Override
	public List<DailyReportEntity> getListOfDailyReportsWithStatus1BasedOnUserIdPkWithPagination(int userIdPk, int pageNumber){
		//get page number
		Pageable pageReq = PageRequest.of(pageNumber-1, 30);
		//generate report
		return dailyReportDao.getListOfDailyReportsWithStatus1BasedOnUserIdPkWithPagination(userIdPk, pageReq);
	}
}
