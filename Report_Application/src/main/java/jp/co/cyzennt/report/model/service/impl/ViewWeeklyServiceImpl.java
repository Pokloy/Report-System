package jp.co.cyzennt.report.model.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.CSVWriter;

import jp.co.cyzennt.report.model.dao.entity.DailyReportEntity;
import jp.co.cyzennt.report.model.dao.entity.FinalEvaluationEntity;
import jp.co.cyzennt.report.model.dao.entity.UserInformationEntity;
import jp.co.cyzennt.report.model.logic.LeaderTopLogic;
import jp.co.cyzennt.report.model.logic.UserReportLogic;
import jp.co.cyzennt.report.model.logic.ViewReportLogic;
import jp.co.cyzennt.report.model.logic.ViewWeeklyLogic;
import jp.co.cyzennt.report.model.service.ViewWeeklyService;

@Service
public class ViewWeeklyServiceImpl implements ViewWeeklyService {
	//accessing the userReportLogic
		@Autowired 
		private UserReportLogic userReportLogic;
		
		//inject LeaderTopLogic
		@Autowired
		private LeaderTopLogic leaderTopLogic;
		
		//inject View Weekly Logic
		@Autowired
		private ViewWeeklyLogic viewWeeklyLogic;
		
		//inject view Report Logic
		@Autowired
		private ViewReportLogic viewReportLogic;
		
		
		//inject 
	/**
	 * generate pdfs output
	 * @throws DocumentException 
	 * @throws IOException 
	 * */
		
	@Override
	public void generatePDF(int groupIdPk,List<LocalDate> generatedArr, 
			LocalDate startDate, LocalDate endDate) throws DocumentException, IOException {
		//applying list to enable looping of information
		List<UserInformationEntity> submitters = leaderTopLogic.getListOfUsersBelongingUnderTheSameGroupIdPkAndBasedOnRole(groupIdPk, "USER");
		
		List<UserInformationEntity> evaluators = leaderTopLogic.getListOfUsersBelongingUnderTheSameGroupIdPkAndBasedOnRole(groupIdPk, "LEADER");
		
		if(submitters == null) {
			return;
		}
		String listOfEvaluators = "";
		
		if(evaluators != null) {
			for(UserInformationEntity evaluator: evaluators) {
				
				listOfEvaluators += evaluator.getFirstName() + ' ' + evaluator.getLastName() + ' ';
			}
		}
		
		for(UserInformationEntity user: submitters) {
			//set start date as string
			String startDateString = startDate.toString();
			//set end date as string
			String endDateString = endDate.toString();
			//get list of daily report
			List<DailyReportEntity> dailyReportList = viewWeeklyLogic.biweeklyEvaluatedReportBasedOnStartAndEndDatesAndUserIdPk(startDateString, endDateString, user.getIdPk());
			System.out.println(dailyReportList);
			//get sum
			List<FinalEvaluationEntity> finalReportList = viewWeeklyLogic.listOfFinalEvaluationEntityBasedOnStartDateandEndDateAndUerIdPk(startDateString, endDateString, user.getIdPk());
			List<String> array = new ArrayList<String>();
			
			for(DailyReportEntity item: dailyReportList) {
				array.add(item.getReportDate());
			}
			int sumFinalDailyRating = 0;
			for(FinalEvaluationEntity finalReport : finalReportList) {
				
				sumFinalDailyRating+= finalReport.getRating();
			}
			//get float 
			float calculatedSum = (float) sumFinalDailyRating / dailyReportList.size();
			//get file
			String file 
	            = "C:/report/"+user.getUsername()+"addingTableToPDF.pdf"; 
	  
			 Document document = new Document();
			 Paragraph title = new Paragraph();
			 Paragraph userIdentity = new Paragraph();
			 Paragraph evaluator = new Paragraph();
			 PdfWriter.getInstance(document, new FileOutputStream(file));
			
			 document.open();
			 
	
			 title.setAlignment(Element.ALIGN_CENTER);
			 title.setSpacingBefore(20);
			 title.add(new Phrase("EVALUATION"));
			 title.setSpacingAfter(20);
			 document.add(title);
			
			
			 userIdentity.setSpacingAfter(10);
			 userIdentity.add(new Phrase("Submitter Name: "+ user.getFirstName() + " "+user.getLastName()));
			 userIdentity.setSpacingBefore(10);
			 document.add(userIdentity);
			 
			 
			 evaluator.add(new Phrase("Evaluator/s: " + listOfEvaluators));
			 evaluator.setSpacingBefore(10);
			 evaluator.setSpacingAfter(30);
			 document.add(evaluator);
			 PdfPTable table = new PdfPTable(7);
			 table.setWidthPercentage((float) 90.5);
			 addTableHeader(table);
			 addRows(table,generatedArr,sumFinalDailyRating,calculatedSum,user.getIdPk(),array);
			
			 //addCustomRows(table);
			 document.add(table);
			 // close the document
			 document.close();
		}
	
	}         
	
	private void addTableHeader(PdfPTable table) {
	    Stream.of("Mon", "Tue", "Wed","Thu","Fri","Actual Rating","Final Rating")
	      .forEach(columnTitle -> {
	    	   PdfPCell header = new PdfPCell();
	    	
	           header.setBackgroundColor(BaseColor.LIGHT_GRAY);
	           header.setPadding((float) 2.5);
	           header.setBorderWidth(2);
	           header.setPhrase(new Phrase(columnTitle));
	           header.setHorizontalAlignment(Element.ALIGN_CENTER);
	           table.addCell(header);
	    });
	}
	

	private void addRows(PdfPTable table,List<LocalDate> generatedArr,
			int sumFinalDailyRating,float calculatedSum,int userIdPk, List<String> array ) {
		
		System.out.println(array);
		//format of the start and end dates for query
		DateTimeFormatter queryFormatDate = DateTimeFormatter.ofPattern("yyyyMMdd");
		int actualArrSize = 21;
		int missingCol = actualArrSize - generatedArr.size();
		int initialVal = 0;
		while (missingCol > initialVal) {
			generatedArr.add(generatedArr.get(generatedArr.size()-1).plusDays(1));
			initialVal++;
		}
		
		HashMap<String,String> newMap = new HashMap<String,String>();
		
		for(String item: array) {
			FinalEvaluationEntity finalEval= viewReportLogic.getFinalEvalDetails(userIdPk, item);
			newMap.put(item, String.valueOf(finalEval.getRating()));
		}
		
		List<String> arrInStrings = new ArrayList<String>();
		
		for(LocalDate item : generatedArr) {
	
			String hello = item.format(queryFormatDate).toString();
			arrInStrings.add(hello);
		}
		
		System.out.println(newMap);
		
		for (int i = 0; i < arrInStrings.size(); i++) {
			
			if(i == 5 || i ==6 ||    
			i==12 || i==13) {
				arrInStrings.set(i, "");
			}
			
			if(i == 19 ) {
				arrInStrings.set(i,Integer.toString(sumFinalDailyRating));
			}
			
			if(i == 20) {
				arrInStrings.set(i,calculatedSum == 0.0 ? "": Float.toString(calculatedSum));
			
			}
		

			PdfPCell newCell = new PdfPCell();
			
			newCell.setFixedHeight((float) 70.5);
			
			Paragraph dateInString = new Paragraph();
			Paragraph rating = new Paragraph();
			rating.setAlignment(Element.ALIGN_CENTER);
			rating.add(new Phrase("Hello"));
			dateInString.add(new Phrase(arrInStrings.get(i)));
			
			dateInString.setSpacingAfter(20);
			newCell.addElement(dateInString);
			newCell.addElement(rating);
			table.addCell(newCell);
		}

	}
	

	
//	private void addCustomRows(PdfPTable table) 
//			  throws URISyntaxException, BadElementException, IOException {
//			    Path path = Paths.get(ClassLoader.getSystemResource("Java_logo.png").toURI());
//			    Image img = Image.getInstance(path.toAbsolutePath().toString());
//			    img.scalePercent(10);
//
//			    PdfPCell imageCell = new PdfPCell(img);
//			    table.addCell(imageCell);
//
//			    PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
//			    horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			    table.addCell(horizontalAlignCell);
//
//			    PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
//			    verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
//			    table.addCell(verticalAlignCell);
//			}

	
	/*
	 * generating CSV
	 * @param inDto
	 * @author Karl James 
	 * November 23, 2023
	 * */
	@Override
	public void generateCSV(int groupIdPk,
			String startDate, String endDate) {
		
		//getting list of users information by group id and role
		List<FinalEvaluationEntity> userReportInfo = 
				userReportLogic.listOfFinalEvaluatedReportsOfUsersBasedOnStartDateAndGroupIdPk(
						startDate,endDate,groupIdPk);		
		// create new file
		File csvFile = new File("C:\\report\\temporary\\example.csv");
	
	
		try {
			//check if the file already exist
			//if it exist, it will be deleted first
			if(csvFile.exists()) {
				csvFile.delete();
			}			
		    // create FileWriter object with file as parameter and utf-8 as the standard charset
		    FileWriter fw = new FileWriter(csvFile, StandardCharsets.UTF_8);

		    // create CSVWriter with fw as parameter
		    CSVWriter csvWriter = new CSVWriter(fw);

		    //instantiate new arrayList of stringArrays
		    List<String[]> allLines = new ArrayList<String[]>();

		    // create the header
		    String[] header = {"User Id Pk","Final Rating","Report Date"};
		    // add the header to all lines
		    allLines.add(header);
		    //put the information in the cells
			for(FinalEvaluationEntity item: userReportInfo) {
				//get user id pk
		    	DailyReportEntity submitterInfo = 
		    			userReportLogic.getReportInformationBasedOnReportIdPk(item.getDailyReportIdPk()); 
		    	//set the row values
		    	String[] line = {
		    			String.valueOf(submitterInfo.getUserIdPk()),
		    			String.valueOf(item.getRating()),
		    			submitterInfo.getReportDate()
		    	};
		    	//add each row
		    	allLines.add(line);
		    }
			
			// write all the lines
		    csvWriter.writeAll(allLines, false);
		    
		    // close the csv writer
		    csvWriter.close();
		} catch (IOException e) {
		    // TODO: handle exception
		    e.printStackTrace();
		}
	}
}
