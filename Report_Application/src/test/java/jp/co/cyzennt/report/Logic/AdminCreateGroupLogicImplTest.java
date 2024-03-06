package jp.co.cyzennt.report.Logic;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jp.co.cyzennt.report.model.dao.GroupDao;
import jp.co.cyzennt.report.model.dao.entity.GroupEntity;
import jp.co.cyzennt.report.model.logic.impl.AdminCreateGroupLogicImpl;


/**
 * This class test the GroupServiceImpl
 * @author Alier Torrenueva
 * 02/09/2024
 */

//These annotations enable Mockito and Spring support in JUnit 5.
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class AdminCreateGroupLogicImplTest {
	AutoCloseable closeable;
	
	@InjectMocks
	AdminCreateGroupLogicImpl mockAdminCreateGroupLogicImpl;
	
	@Mock
	GroupDao groupDao;
	

	@BeforeEach
	public void openMocks() {
		closeable = MockitoAnnotations.openMocks(this);
	}
	
	@AfterEach
	public void releaseMocks() throws Exception {
		closeable.close();
	}
	
	@Test
	public void TestSaveGroupInfo() {
		// Setup
		GroupEntity mockGroupEntity = mock(GroupEntity.class);
		
		// Mocks
		when(groupDao.save(mockGroupEntity)).thenReturn(mockGroupEntity);
		
		// Invoke
		mockAdminCreateGroupLogicImpl.saveGroupInfo(mockGroupEntity);
	}
	
	
	@Test
	public void TestGetGroupInfo() {
		// Setup
		String mockGroupName = "sample Group";
		GroupEntity mockGroupEntity = mock(GroupEntity.class);
		
		// Mocks
		when(groupDao.getGroupInfoByGroupName(mockGroupName)).thenReturn(mockGroupEntity);
		
		// Invoke
		mockAdminCreateGroupLogicImpl.getGroupInfo(mockGroupName);
	}
	
}
