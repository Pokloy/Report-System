	package jp.co.cyzennt.report.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.util.Matrix;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



import jp.co.cyzennt.report.common.constant.CommonConstant;
import jp.co.cyzennt.report.common.util.ApplicationPropertiesRead;
import jp.co.cyzennt.report.common.util.CsvEditUtil;
import jp.co.cyzennt.report.common.util.ExcelConversionUtil;
import jp.co.cyzennt.report.common.util.FileIoUtil;
import jp.co.cyzennt.report.common.util.GetDirSeparatorUtil;
import jp.co.cyzennt.report.common.util.PreschoolJobLogWriter;


@Component
@StepScope
public class SamplePdfOutputLogic {
	
	@Value("#{jobParameters['report.username']}")
    String username;

	private static final String JOB_CD = "SamplePdfOutput";
	
	/* cell layout */
	private static final int A_TO_E_COLUMN_WIDTH = (int) (11.45 * 256);
    private static final int F_COLUMN_WIDTH = (int) (8.36 * 256);
    private static final int G_COLUMN_WIDTH = (int) (6.64 * 256);
    private static final float NAME_ROW_HEIGHT = 18.8f;
    private static final float HEADER_ROW_HEIGHT = 29.5f;
    private static final float RATING_ROW_HEIGHT = 57f;
    
    private CellStyle ratingStyle;
    private CellStyle finalRatingStyle;
    private CellStyle shadedRatingStyle;
    private CellStyle shadedFinalStyle;

	public void samplePdfOutputLogicMain() throws RuntimeException {
		// excel file path
		String directory = ApplicationPropertiesRead.read("weekly.excel.path") + username + GetDirSeparatorUtil.getDirSeparator();
		// excel file
		String path = directory + "Weekly_Evaluation_" + username + "_SAMPLE_TEMPLATE" + ".xlsx";
		// the directory as file
		File dirFile = new File(directory);
		// create a new folder if the directory does not exist
		if (!dirFile.exists()) {
			dirFile.mkdir();
			dirFile.setReadable(true);
			dirFile.setWritable(true);
			dirFile.setExecutable(true);
		}
		
		// create new file from path
		File file = new File(path);
		 FileOutputStream fileOutput = null;
		try(Workbook workbook = new XSSFWorkbook()) {
			// evaluation sheet for the excel
			Sheet sheet = workbook.createSheet("Evaluation");
            
			// set the column width of a to e
            for (int i = 0; i < 5; i++) {
                sheet.setColumnWidth(i, A_TO_E_COLUMN_WIDTH);
            }
            
            // set F column width
            sheet.setColumnWidth(5, F_COLUMN_WIDTH);
            // set G column width
            sheet.setColumnWidth(6, G_COLUMN_WIDTH);

            // the first row
            Row row = sheet.createRow(0);

            // member row
            Row memberRow = sheet.createRow(2);
            // set the height for member row
            memberRow.setHeightInPoints(NAME_ROW_HEIGHT);
            
            // evaluator row
            Row evaluatorRow = sheet.createRow(4);
            // set the height for evaluator row
            evaluatorRow.setHeightInPoints(NAME_ROW_HEIGHT);

            // header row
            Row header = sheet.createRow(6);
            // set the height for header row
            header.setHeightInPoints(HEADER_ROW_HEIGHT);

            // merging the cells
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 4));
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 4));
            sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 4));

            // bold style
            CellStyle boldStyle = workbook.createCellStyle();
            // bold font
            Font boldFont = workbook.createFont();
            // setting bold to true
            boldFont.setBold(true);
            // setting font for bold style
            boldStyle.setFont(boldFont);

            CellStyle wrapText = workbook.createCellStyle();
            wrapText.setWrapText(true);
            wrapText.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.cloneStyleFrom(boldStyle);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle nameStyle = workbook.createCellStyle();
            nameStyle.setBorderBottom(BorderStyle.THIN);
            nameStyle.setAlignment(HorizontalAlignment.CENTER);

            ratingStyle = workbook.createCellStyle();
            ratingStyle.setBorderTop(BorderStyle.MEDIUM);
            ratingStyle.setBorderBottom(BorderStyle.MEDIUM);
            ratingStyle.setBorderLeft(BorderStyle.THIN);
            ratingStyle.setBorderRight(BorderStyle.THIN);
            ratingStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            Color color = new XSSFColor(new byte[]{(byte)255, (byte)204, (byte)204});

            finalRatingStyle = workbook.createCellStyle();
            finalRatingStyle.setBorderTop(BorderStyle.MEDIUM);
            finalRatingStyle.setBorderBottom(BorderStyle.MEDIUM);
            finalRatingStyle.setBorderLeft(BorderStyle.MEDIUM);
            finalRatingStyle.setBorderRight(BorderStyle.MEDIUM);
            finalRatingStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            finalRatingStyle.setFillForegroundColor(color);
            finalRatingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            CellStyle finalHeaderStyle = workbook.createCellStyle();
            finalHeaderStyle.cloneStyleFrom(finalRatingStyle);
            finalHeaderStyle.setWrapText(true);
            finalHeaderStyle.setFont(boldFont);

            shadedRatingStyle = workbook.createCellStyle();
            shadedRatingStyle.cloneStyleFrom(ratingStyle);
            shadedRatingStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
            shadedRatingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            shadedFinalStyle = workbook.createCellStyle();
            shadedFinalStyle.cloneStyleFrom(finalRatingStyle);
            shadedFinalStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
            shadedFinalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.cloneStyleFrom(ratingStyle);
            headerStyle.setWrapText(true);
            headerStyle.setFont(boldFont);
            // headerStyle.cloneStyleFrom(boldStyle);
            
            Cell title = row.createCell(1);
            title.setCellValue("Weekly Evaluation");
            title.setCellStyle(titleStyle);

            Cell memberLabel = memberRow.createCell(0);
            memberLabel.setCellValue("Member's Name");
            memberLabel.setCellStyle(wrapText);

            Cell memberName = memberRow.createCell(1);
            memberName.setCellValue("Julius Basas");
            memberName.setCellStyle(nameStyle);
            setStyles(memberRow, nameStyle, 2, 4);

            Cell evaluatorLabel = evaluatorRow.createCell(0);
            evaluatorLabel.setCellValue("Evaluator's Name");
            evaluatorLabel.setCellStyle(wrapText);
            setStyles(evaluatorRow, nameStyle, 2, 4);

            Cell evaluatorName = evaluatorRow.createCell(1);
            evaluatorName.setCellValue("M.Higashishita, H.Goto");
            evaluatorName.setCellStyle(nameStyle);

            String[] headerTitles = {
                "Mon",
                "Tue",
                "Wed",
                "Thu",
                "Fri",
                "Actual Rating",
                "Final Rating"
            };

            for (int i = 0; i < headerTitles.length; i++) {
                Cell headerTitle = header.createCell(i);
                headerTitle.setCellValue(headerTitles[i]);
                if (i < headerTitles.length - 1) {
                    headerTitle.setCellStyle(headerStyle);
                } else {
                    headerTitle.setCellStyle(finalHeaderStyle);
                }
            }

            fileOutput = new FileOutputStream(file);
            workbook.write(fileOutput);
            editSheet(workbook);
            
            workbook.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
        	try {
				fileOutput.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		
		
	}
	

	
	public void editSheet(Workbook workbook) throws Exception {
		// excel file path
		String directory = ApplicationPropertiesRead.read("weekly.excel.path") + username + GetDirSeparatorUtil.getDirSeparator();
		// pdf directory
		String pdfDirectory = ApplicationPropertiesRead.read("weekly.pdf.path") + username + GetDirSeparatorUtil.getDirSeparator();
		// excel file
		String path = directory + "Weekly_Evaluation_" + username + "_EDITED_SAMPLE" + ".xlsx";
		// pdf file
		String pdfPath = pdfDirectory + "Weekly_Evaluation_" + username + "_PDF_SAMPLE" + ".pdf";

		// date time formatter
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		
		String csvFile = "path/to/csvFile.csv";

//		// declare list of strings for csvfilestream
//		List<String> csvFileStream = null;
//		try {
//		    csvFileStream = FileIoUtil.getFileStream(csvFile);
//		} catch (IOException e) {
//		    // error stuff
//		    // ...
//		    // throw runtimeexception
//		   
//		}
//
//		for (String line : csvFileStream) {
//		    // get csv data per column
//		    List<String> csvData = CsvEditUtil.split(line);
//		    
//		    // loop through csvData,
//		    
//		    for(String item: csvData) {
//		    	System.out.println(item);
//		    }
//		}
		
		// the directory as file
		File dirFile = new File(pdfDirectory);
		// create a new folder if the directory does not exist
		if (!dirFile.exists()) {
			dirFile.mkdir();
			dirFile.setReadable(true);
			dirFile.setWritable(true);
			dirFile.setExecutable(true);
		}
//		FileInputStream fileInput = new FileInputStream(path);
//        Workbook workbook = new XSSFWorkbook(fileInput);
        Sheet sheet = workbook.getSheetAt(0);

        int lastRowNum = sheet.getLastRowNum();
        Row lastRow = sheet.getRow(lastRowNum);
      //getting the current month
      	LocalDate currentDate = LocalDate.now();
      	 //gettin the last day of the month
      //getting the 15h or half of the month
      	LocalDate halfDayOfMonth = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 15);
      	 //gettin the last day of the month
		LocalDate lastDayOfMonth = halfDayOfMonth.withDayOfMonth(1).plusMonths(1).minusDays(1);
      //getting the date of the day
      	int dateInt = LocalDate.now().getDayOfMonth();
      	
      //initiate startDate
  		LocalDate startDate = null;
  		//initiate end date
  		LocalDate endDate = null;
      	
      	if( dateInt > 1 && dateInt <= 15 ) {
			//getting the end date which is the current month's 10th day date
 			endDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 10);
		}else if(dateInt > 15 && dateInt <= lastDayOfMonth.getDayOfMonth()) {
			//getting the end date which is the current month's 25th day date
			endDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 25);
			}
	
        LocalDate today = endDate;
        LocalDate lastDate = currentDate;
        LocalDate currDate = endDate;

        long daysBetween = ChronoUnit.DAYS.between(today, lastDate);
        int weekDay = today.getDayOfWeek().getValue();

        int cellIndex = weekDay - 1;
        int totalRating = 0;
        int daysPassed = 0;
        double avgRating = 0;
        Row dateRow = sheet.createRow(++lastRowNum);
        Row ratingRow = sheet.createRow(++lastRowNum);
        ratingRow.setHeightInPoints(RATING_ROW_HEIGHT);
        
        boolean finalRateFlg = false;

        if (weekDay > 1) {
            setStyles(dateRow, shadedRatingStyle, 0, weekDay);
            setStyles(ratingRow, shadedRatingStyle, 0, weekDay);
            if (weekDay == 7) {
                setStyles(dateRow, shadedFinalStyle, 6, 7);
                setStyles(ratingRow, shadedFinalStyle, 6, 7);
            }

            finalRateFlg = isCutoffInSameWeek(currDate);
        }

        boolean isCutOffWeekDay = false;

        for (int i = 0; i < daysBetween + weekDay; i++) {
            // day
            int day = currDate.getDayOfMonth();

            // when cell index is 0
            if (cellIndex == 0) {
                if (isCutOffWeekDay) {
                    int incIndex = currDate.getDayOfWeek().getValue() - 1;
                    setStyles(ratingRow, shadedRatingStyle, 0, incIndex - 1);
                    setStyles(dateRow, shadedRatingStyle, 0, incIndex - 1);
                    cellIndex += incIndex;
                    isCutOffWeekDay = false;
                    finalRateFlg = false;
                } else {
                    finalRateFlg = isCutoffInSameWeek(currDate);
                }
            }
            Cell dateCell = dateRow.createCell(cellIndex);
            Cell ratingCell = ratingRow.createCell(cellIndex);

            if (day == 10 || day == 25) {
                int currWeekDay = currDate.getDayOfWeek().getValue();

                if (currWeekDay < 5) {
                    int between = 5 - currWeekDay;
                    setStyles(ratingRow, shadedRatingStyle, currWeekDay - 1, currWeekDay + between);
                    setStyles(dateRow, shadedRatingStyle, currWeekDay - 1, currWeekDay + between);
                    cellIndex += between;
                    isCutOffWeekDay = true;
                }
            }

            if (cellIndex < 5) {
                int currRating = rng();
                totalRating += currRating;
                dateCell.setCellStyle(ratingStyle);
                dateCell.setCellValue(formatter.format(currDate));
                ratingCell.setCellStyle(ratingStyle);
                ratingCell.setCellValue(currRating);
                ++daysPassed;
                ++cellIndex;
                if (isCutOffWeekDay) {
                    currDate = currDate.plusDays(1);
                }
            } else {
                // add a day to currdate
                if (!isCutOffWeekDay) {
                    currDate = currDate.plusDays(1);
                }
                dateCell.setCellStyle(shadedFinalStyle);
                dateCell = dateRow.createCell(++cellIndex);
                dateCell.setCellStyle(shadedFinalStyle);
                if (finalRateFlg) {
                    avgRating = totalRating / (double)daysPassed;
                    ratingCell.setCellStyle(ratingStyle);
                    ratingCell.setCellValue(avgRating);
                    totalRating = 0;
                    daysPassed = 0;
                    
                    ratingCell = ratingRow.createCell(cellIndex);
                    ratingCell.setCellStyle(finalRatingStyle);
                    ratingCell.setCellValue(Math.round(avgRating));
                } else {
                    ratingCell.setCellStyle(shadedRatingStyle);
                    ratingCell = ratingRow.createCell(cellIndex);
                    ratingCell.setCellStyle(shadedFinalStyle);
                }
                dateRow = sheet.createRow(++lastRowNum);
                ratingRow = sheet.createRow(++lastRowNum);
                // set the height in points
                ratingRow.setHeightInPoints(RATING_ROW_HEIGHT);

                cellIndex = 0;
            }
            // add a day to currdate 
            if (!isCutOffWeekDay) currDate = currDate.plusDays(1);
        }

        lastRowNum = sheet.getLastRowNum();
        int lastCellNum = sheet.getRow(lastRowNum).getLastCellNum();

        if (lastCellNum != 7) {
            setStyles(dateRow, shadedRatingStyle, lastCellNum, 5);
            setStyles(ratingRow, shadedRatingStyle, lastCellNum, 5);
            setStyles(dateRow, shadedFinalStyle, 6, 6);
            setStyles(ratingRow, shadedFinalStyle, 6, 6);
        }

        FileOutputStream outFile = new FileOutputStream(new File(path));
        workbook.write(outFile);
        
        ExcelConversionUtil.convertToPDF(path, pdfPath);
	}
	
	/**
	 * helper method to set the styles of multiple cells in a row
	 * @param row
	 * @param style
	 * @param start
	 * @param end
	 */
	private static void setStyles(Row row, CellStyle style, int start, int end) {

        for (int i = start; i < end + 1; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(style);
        }
    }
	
	private static int rng() {
        Random random = new Random(System.currentTimeMillis());

        return random.nextInt(5)+1;
    }

    private static boolean isCutoffInSameWeek(LocalDate date) {
        // check the day of month
        int dayOfMonth = date.getDayOfMonth();

        if (dayOfMonth == 10 || dayOfMonth == 25) {
            // the date is the cutoff day
            return true;
        } else {
            // check day
            LocalDate checkTenthDay = date.withDayOfMonth(10);
            
            // tenth start date
            LocalDate tenthStartDate = checkTenthDay.minusDays(checkTenthDay.getDayOfWeek().getValue());
            // tenth end date
            LocalDate tenthEndDate = tenthStartDate.plusDays(8);
            
            // check if date is within the week of tenth day
            if (date.isAfter(tenthStartDate) && date.isBefore(tenthEndDate)) {
                return true;
            } else {
                // check 25th day
                LocalDate checkLastDay = date.withDayOfMonth(25);
                
                // 25th start date
                LocalDate lastStartDate = checkLastDay.minusDays(checkLastDay.getDayOfWeek().getValue());
                // 25th end date
                LocalDate lastEndDate = lastStartDate.plusDays(8);
                
                // return if date is within 25th or not
                return date.isAfter(lastStartDate) && date.isBefore(lastEndDate);
            }
            
        }
    }
}
