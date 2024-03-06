package jp.co.cyzennt.report.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;

import jp.co.cyzennt.report.model.dto.ReportInOutDto;
import jp.co.cyzennt.report.model.service.ViewWeeklyReportService;


@Controller
@Scope("prototype")
public class ViewWeeklyReportController {
	// Inject the ViewWeeklyReportService using Spring's @Autowired annotation
	@Autowired
	ViewWeeklyReportService viewWeeklyReportService;
	 /**
	  * viewWeeklyReport
	  * @param model
	  * @return
	  * @author glaze
	  * 11/22/2023
	  */
	 @GetMapping ("/view-weekly-report")
		public String viewWeeklyReport(Model model,HttpSession session) {
		//set model for profile nav
		    model.addAttribute("profilePhoto", (String) session.getAttribute("profilePhoto"));
			return "user/WeeklyPdf";
		}
	 
	 @GetMapping("/show-pdf")
	 @ResponseBody
	 public ResponseEntity<byte[]> showWeeklyPdf() {
		// Retrieve the PDF file using the userReportService
		 File file = viewWeeklyReportService.retrievePdfFile();
		 // Set up HttpHeaders for the HTTP response
		 HttpHeaders headers = new HttpHeaders();	 
		 // declare file bytes 
		 byte[] fileBytes;	 
		 // set content contentn type
		 headers.setContentType(MediaType.APPLICATION_PDF);	 
		 try(InputStream is = new FileInputStream(file)) {		 
			 // get bytes
			 fileBytes = IOUtils.toByteArray(is);	 
			// Encode the file name using UTF-8 to handle special characters in the name
			 String encodedFileName = UriUtils.encode(file.getName(), StandardCharsets.UTF_8);			 
			 // add headers
			 headers.add("Content-Disposition", "inline; filename=" + encodedFileName);
			 
			
		} catch (IOException e) {
			// print the error
			e.printStackTrace();
			// assign null to filebytes
			fileBytes = null;
			
			// add ehadsfe
			 headers.add("Content-Disposition", "inline; filename=FILE_NOT_FOUND");
		}
		// Return a ResponseEntity with HTTP status OK
		// Set the headers for the HTTP response
		// Set the content length based on the length of the file bytes
		// Provide the file bytes as the body of the response
		 return ResponseEntity.ok().headers(headers).contentLength(fileBytes.length).body(fileBytes);
	 }
	


	 @PostMapping("/download-pdf")
	 public void downloadWeeklyPdf(HttpServletResponse response) {
		 
		 File file = viewWeeklyReportService.retrievePdfFile();
		 
		 try(InputStream is = new FileInputStream(file);
				 OutputStream os = response.getOutputStream()) {
			 
			 // get bytes
			 byte[] fileBytes = IOUtils.toByteArray(is);
			 
			 // set the content type
			 response.setContentType("application/pdf");
			 
			 // set header
			 response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
			 
			 // set the content length
			 response.setContentLength(fileBytes.length);
			 
			 // write os yey
			 os.write(fileBytes);
			 // flush
			 os.flush();
			 // close
			 os.close();
			
		} catch (IOException e) {
			// print the error
			e.printStackTrace();
			// set content type of response
	        response.setContentType("application/pdf");

	        // set header
	        response.setHeader("Content-Disposition", "attachment; filename=" + "file_not_found");

	        // set content length
	        response.setContentLength(0);
		}
	 }
}
