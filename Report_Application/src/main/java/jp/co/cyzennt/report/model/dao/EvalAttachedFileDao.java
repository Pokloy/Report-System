package jp.co.cyzennt.report.model.dao;
import java.util.List;

import org.springframework.dao.DataAccessException;
/**
 * DailyReportDo
 * @author glaze
 * 
 * 10/03/2023
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import jp.co.cyzennt.report.model.dao.entity.EvalAttachedFileEntity;


public interface EvalAttachedFileDao extends JpaRepository<EvalAttachedFileEntity ,String> {

	final String GET_DAILYREPORT_DETAILS_ATTACHED_FILE = 
		    "SELECT  e " +
		    "FROM EvalAttachedFileEntity e " +
		    "INNER JOIN DailyReportEntity dr ON e.dailyReportIdPk = dr.idPk " +
		    "WHERE dr.userIdPk = :userIdPk " +
		    "AND dr.reportDate = :reportDate " +
		    "AND dr.deleteFlg = false " +
		    "AND e.deleteFlg = false";
	
	final String GET_DAILYREPORT_IMAGES_BASED_ON_UPLOADER_ID_PK_AND_REPORT_ID_PK = 
			" SELECT e FROM EvalAttachedFileEntity e "
			+ " INNER JOIN DailyReportEntity dr "
			+ " ON e.dailyReportIdPk = dr.idPk "
			+ " WHERE dr.idPk = :reportIdPk "
			+ " AND e.uploaderIdPk = :userIdPk"
			+ " AND dr.deleteFlg = false "
			+ " AND e.deleteFlg = false ";
	


	/**
	 * get  report report details by userIdPk and reportDate
	 * @param userIdPk,reportDate
	 * @return EvalAttachedFileEntity
	 * @author glaze
	 * 10/09/2023
	 */	
	@Query(GET_DAILYREPORT_DETAILS_ATTACHED_FILE)	
	public List<EvalAttachedFileEntity> getDailyReportDetails(int userIdPk, String reportDate) throws DataAccessException;
	
	/**
	 * get list of images attached for a certain report based on 
	 * @param reportIdPk and userIdPk
	 * @return EvalAttachedFileEntity
	 * @author Karl James Arboiz
	 * 11/15/2023
	 */	
	@Query(value=GET_DAILYREPORT_IMAGES_BASED_ON_UPLOADER_ID_PK_AND_REPORT_ID_PK)	
	public List<EvalAttachedFileEntity> getListOfImagePathsBasedOnReportIdPkAndUserIdPk(int userIdPk, int reportIdPk) throws DataAccessException;
	
	


}
