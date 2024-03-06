package jp.co.cyzennt.report.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;


@Service
public class ReusableFunctions {
	public String convertedDatabaseReportDateToAReadableFormat(String reportDate,
			String curDateFormat,
			String desiredDateFormat) {
		//initiating current format of that date
		 SimpleDateFormat inputFormat = new SimpleDateFormat(curDateFormat);
		//initiating desired format of that date
	     SimpleDateFormat outputFormat = new SimpleDateFormat(desiredDateFormat);
	     //initiating empty formattedDate;
	     String formattedDate = "";
		try {
			if(reportDate != null) {
			//initiating the date to parse the reportDate
			Date date = inputFormat.parse(reportDate);
			
			//filling in the empty formatted Date string
			formattedDate = outputFormat.format(date);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
		return formattedDate;
	}
	
	public String convertADateFromDatabaseToASpecificFormat(String evaluateReportDate) {
	

        // Define input and output date formats
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        
        //
        String result = "";
        try {
            // Parse the input date string
            Date date = inputDateFormat.parse(evaluateReportDate);

            // Format the date into the desired output format
            String outputDateString = outputDateFormat.format(date);

            result= outputDateString;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
       
	}
}
