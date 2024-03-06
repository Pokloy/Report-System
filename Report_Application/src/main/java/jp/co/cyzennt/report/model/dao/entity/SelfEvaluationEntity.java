package jp.co.cyzennt.report.model.dao.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "t_self_evaluation")
public class SelfEvaluationEntity {
	
		//declare dailyReportIdPk
		@Id
		private int dailyReportIdPk;
		
		//declare  comment
		private String comment;
		
		//declare rating
		private int rating;
		
		//declare regId
		private String regId;
		
		//declare regDate
		private Timestamp regDate;
		
		//declare updateId
		private String updateId;
		
		//declare updateId
		private Timestamp updateDate;
		
		//declare deleteFlg
		private Boolean deleteFlg;
}
