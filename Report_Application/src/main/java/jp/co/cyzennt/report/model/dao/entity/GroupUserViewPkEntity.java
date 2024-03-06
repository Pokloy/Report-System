package jp.co.cyzennt.report.model.dao.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.springframework.context.annotation.Scope;

/**
 * GroupUserViewEntityPk
 * @author Karl James
 * October 5, 2023
 */

@Embeddable
@Scope("Prototype")
public class GroupUserViewPkEntity implements Serializable{
	
	//declare user Id Pk;
	private int groupIdPk;
	
	//declare user Id Pk;
	private int userIdPk;
}
