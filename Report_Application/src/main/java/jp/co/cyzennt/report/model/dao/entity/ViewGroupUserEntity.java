package jp.co.cyzennt.report.model.dao.entity;

import org.springframework.context.annotation.Scope;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * View group user entity
 * @author christian
 * 20231006
 */
@Data
@AllArgsConstructor
@Scope("prototype")
public class ViewGroupUserEntity {
	
	// Constructor for object type conversion
		public ViewGroupUserEntity(Object[] objects) {
			
			this(
					(String) objects[0]
					,(String) objects[1]
					);
		}
	    // leader first name
	    private String firstName;
	    // leader last name
	    private String lastName;
}
