package jp.co.cyzennt.report.model.dao.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name ="t_weekly_pdf")
public class WeeklyPdfEntity {

	//declare idPk
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idPk;
	
	//set userIdPk
	private int userIdPk;
	
	//declare file path
	private String filePath;
	
	//declare title
	private String title;
	
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
