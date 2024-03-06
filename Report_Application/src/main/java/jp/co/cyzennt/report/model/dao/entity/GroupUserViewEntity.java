package jp.co.cyzennt.report.model.dao.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="t_group_user_view")
@Data
@IdClass(GroupUserViewPkEntity.class)
public class GroupUserViewEntity {
	
	//declare group Id Pk
	@Id
	private int groupIdPk;
	
	//declare user Id Pk;
	@Id
	private int userIdPk;	
	
	//declare registration Id
	private String regId;
	
	//declare registration Date
	private Timestamp regDate;
	
	//declare update Id
	private String updateId;
	
	//declare update date
	private Timestamp updateDate;
	
	//
	private boolean deleteFlg;
}
