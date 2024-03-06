package jp.co.cyzennt.report.model.dao.entity;

import java.sql.Timestamp;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User Information Entity
 * @author Mizuki
 *
 * created on: 20230928
 */
@Entity
@Table(name="m_user_information")
public class UserInformationEntity {

	//user idPk
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Setter
	@Getter
	private int idPk;

	//user id
	@Getter
	@Setter
	private String mailAddress;

	//Family name
	@Getter
	@Setter
	private String username;

	//first name
	@Getter
	@Setter
	private String role;

	//role name
	@Getter
	@Setter
	private String permissionId;

	//family name 
	@Getter
	@Setter
	private String lastName;

	//first name
	@Getter
	@Setter
	private String firstName;

	//displaypicture
	@Getter
	@Setter
	private String displayPicture;


	//registrar Id
	@Getter
	@Setter
	private String regId;

	//registered date
	@Getter
	@Setter
	private Timestamp regDate;

	//updater Id
	@Getter
	@Setter
	private String updateId;

	//updated date
	@Getter
	@Setter
	private Timestamp updateDate;

	//delete flage
	@Getter
	@Setter
	private boolean deleteFlg;
}
