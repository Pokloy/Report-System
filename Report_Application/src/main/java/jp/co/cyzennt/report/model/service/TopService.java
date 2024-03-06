package jp.co.cyzennt.report.model.service;

import org.springframework.stereotype.Service;

@Service
public interface TopService {

	/**
	 *
	 * @param username
	 * @return
	 */
	String getUserRoleByUsername(String username);

}
