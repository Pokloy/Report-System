package jp.co.cyzennt.report.model.dao.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Group Entity
 * @author : Christian
 * Created On: 09/28/2023
 */

@Entity
@Table(name="m_group")
public class GroupEntity {
	
	//group IdPk
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Setter
	@Getter
	private int idPk;
	
	// group_name
	@Getter
	@Setter
	private String groupName;
	
	// reg_id
	@Getter
	@Setter
	private String regId;
	
	// reg_date
	@Getter
	@Setter
	private Timestamp regDate;
	
	// update_id
	@Getter
	@Setter
	private String updateId;
	
	// update_date
	@Getter
	@Setter
	private Timestamp updateDate;
	
	// delete_flg
	@Getter
	@Setter
	private boolean deleteFlg;
	
	// display_photo
	@Getter
	@Setter
	private String displayPhoto;
}
