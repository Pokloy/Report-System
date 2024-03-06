package jp.co.cyzennt.report.model.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

/**
 * author: Karl James Arboiz
 * created on: September 28, 2023
 */

@Entity
@Table(name="m_user_info_account")
public class UserInformationAccountEntity {
	//user id FK
	@Id
	@Setter
	@Getter
	private int userIdPk;
	
	//password
	@Setter
	@Getter
	private String password;
	
	//register id
	@Setter
	@Getter
	private String regId;

	
	//register date
	@Setter
	@Getter
	private Timestamp regDate;
	
	//update id
	@Setter
	@Getter
	private String updateId;
	
	//update date
	@Setter
	@Getter
	private Timestamp updateDate;
	
		
	//delete flag
	@Getter
	@Setter
	private boolean deleteFlg;
	
}
