package jp.co.cyzennt.report.model.dao.entity;
/**
 * LoggedIn user service
 * @author glaze
 * 
 * 9/29/2023
 */
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name ="t_daily_report")
public class DailyReportEntity {
	//declare idPk
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idPk;
	
	//set ReportIdPk
	private int userIdPk;
	
	//set ReporDate
	private String reportDate;
		
	
	//declare target
	private String target;
	
	//declare status
	private String status;
	
	//declare  regId
	private String regId;
	
	//declare regDate
	private Timestamp regDate;
	
	//declare updateId
	private String updateId;
	
	//declare updateDate
	private Timestamp updateDate;
	
	//declare deleteFlg
	private Boolean deleteFlg;
	
}
