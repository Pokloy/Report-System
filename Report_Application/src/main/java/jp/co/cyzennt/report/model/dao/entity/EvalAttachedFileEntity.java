package jp.co.cyzennt.report.model.dao.entity;
/**
 * EvalAttachedFileEntity
 * @author glaze
 * 
 * 10/03/2023
 */

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name ="t_eval_attached_file")
@IdClass(EvalAttachedFilePkEntity.class)
public class EvalAttachedFileEntity {
			
		//set dailyReportIdPk
	    @Id
		private int dailyReportIdPk;
		
		//set file path
		private String filePath;
		
		// declare increment number
		private int incrementNum;
		
		//declare uploaderIdPk
		private int uploaderIdPk;
		
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
