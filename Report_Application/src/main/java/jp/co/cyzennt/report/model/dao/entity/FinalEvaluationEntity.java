package jp.co.cyzennt.report.model.dao.entity;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name ="t_final_evaluation")
public class FinalEvaluationEntity {
		@Id
		//set DailyIdPk
		private int dailyReportIdPk;
		 
		//set Evaluation
		private int evaluatorIdPk;
		 
		//set comments
		private String comment;
		
		//set Rating
		private int rating;
		 
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
