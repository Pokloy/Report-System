package jp.co.cyzennt.report.model.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.itextpdf.text.DocumentException;

public interface ViewWeeklyService {
	
	public void generatePDF(int groupIdPk,List<LocalDate> generatedArr, LocalDate startDate, LocalDate endDate) throws DocumentException, IOException;
	/*
	 * generating CSV
	 * @param inDto 
	 * @author Karl James 
	 * November 23, 2023
	 * */
	public void generateCSV(int groupIdPk,
			String startDate,String endDate);
}
