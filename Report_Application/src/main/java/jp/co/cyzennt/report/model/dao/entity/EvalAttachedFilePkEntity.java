package jp.co.cyzennt.report.model.dao.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.springframework.context.annotation.Scope;

import lombok.Data;

@Embeddable
@Data
@Scope("prototype")
public class EvalAttachedFilePkEntity  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//declare user Id Pk;
	private int dailyReportIdPk;
	
	// declare increment num
	private int incrementNum;
	
	//declare user Id Pk;
	private int uploaderIdPk;
	

}
