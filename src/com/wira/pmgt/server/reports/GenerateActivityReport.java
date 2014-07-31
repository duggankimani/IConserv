package com.wira.pmgt.server.reports;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wira.pmgt.server.dao.biz.model.ProgramDetail;
import com.wira.pmgt.server.dao.helper.ProgramDaoHelper;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.server.db.DBTrxProvider;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.program.IsProgramDetail;
import com.wira.pmgt.shared.model.program.TargetAndOutcomeDTO;
import com.wira.pmgt.shared.requests.GetProgramsRequest;

public class GenerateActivityReport {

	private static final String[] titles = { "Overall LWF Goals", "Activity",
			"Targets", "Indicator", "Funding", "Status", "Remarks",
			"Monitoring test" };

	static Map<String, CellStyle> styles = null;
	static Map<String, Font> fonts = null;

	private Workbook wb = null;
	private String name = null;

	public GenerateActivityReport(Long programId, String code, Long outcomeId,
			Long activityId,Long periodId,String programType, String type) {

		ProgramDetailType programDetailType=ProgramDetailType.PROGRAM;
		if(programType==null){
			programDetailType = ProgramDetailType.valueOf(programType);
		}
		//Program Id must not be null	
		assert programId!=null;
		
		if (type != null && type.equals("xls")) {
			wb = new HSSFWorkbook();
		} else {
			wb = new XSSFWorkbook();
		}
		fonts = createFonts(wb);
		styles = createStyles(wb);
		
		
		List<IsProgramDetail> lst= new ArrayList<>();
		String label = "Summary";
		//Get Program to be the label
		if(programDetailType.equals(ProgramDetailType.OBJECTIVE)){
			label = "Objectives";
			lst = ProgramDaoHelper.getProgramsByType(programDetailType, true);
		}else if (periodId != null && code!=null) {
			// Period is not current period
			IsProgramDetail program = ProgramDaoHelper.getProgramByCode(code, periodId,true);
			label  = program.getName();
			lst.add(program);
		}else if (periodId!=null && programId==null){
			lst = ProgramDaoHelper.getPrograms(true);			
		}else if(programId!=null){
			IsProgramDetail program=null;
			if(outcomeId!=null){
				//get by outcome
				program =ProgramDaoHelper.getProgramById(programId,false);
				program.setProgramOutcomes(ProgramDaoHelper.loadById(programId,outcomeId,programDetailType,true));
			}else if(activityId!=null){
				program =ProgramDaoHelper.getProgramById(programId,false);
				program.setChildren(ProgramDaoHelper.loadById(activityId,null,programDetailType,true));
			}else{
				 program =ProgramDaoHelper.getProgramById(programId,true);
				 label = program.getName();				 
			}
			label = program.getName();
			lst.add(program);
		}else{
			//Load all programs
			lst = ProgramDaoHelper.getPrograms(true);
		}
		
		name = label+"_report_"+SimpleDateFormat.getDateInstance().format(new Date())+"."+type;
		
		// Write the output to a file
		//
		//Standardised structure
		/*
		 * --Program
		 * 		--Outcome
		 *  		--Activities
		 *  			--Tasks
		 */
		
		for (IsProgramDetail detail : lst) {
			Map<Long, IsProgramDetail> outcomeActivityMap = new HashMap<Long, IsProgramDetail>();
			
			//single page
			if (detail.getProgramOutcomes() != null) {
				for (IsProgramDetail outcome : detail
						.getProgramOutcomes()) {
					outcomeActivityMap.put(outcome.getId(), outcome);
				}

				List<IsProgramDetail> activities = detail
						.getChildren();
				if (activities != null) {
					for (IsProgramDetail activity : activities) {
						if (outcomeActivityMap.get(activity
								.getActivityOutcomeId()) != null) {
							outcomeActivityMap.get(
									activity.getActivityOutcomeId())
									.addChild(activity);
						}
					}
				}
			}
			List<IsProgramDetail> data = detail.getProgramOutcomes();
			if (data != null) {
				generate(wb, detail.getName(), data);
			}
		}

	}
	
	public String getName(){
		return name;
	}

	public byte[] getBytes() throws IOException {

		ByteArrayOutputStream byteo = new ByteArrayOutputStream();
		wb.write(byteo);
		return byteo.toByteArray();
	}

	public static void main(String[] args) throws Exception {
		setup();
		Long programId=1L;
		String code = null;
		Long outcomeId=23L;
		Long activityId=null;
		Long periodId=null;
		String programType="PROGRAM";
		String type="xlsx";
		
		byte[] bites = new GenerateActivityReport(programId, code, outcomeId,
				activityId,periodId,programType,type).getBytes();
		
		FileOutputStream os = new FileOutputStream(new File("programout.xlsx"));
		os.write(bites);
		os.close();
	}

	public static void generate(Workbook wb, String sheetName,
			List<IsProgramDetail> data) {

		Sheet sheet = wb.createSheet(sheetName);

		// sheet.setDisplayGridlines(false);
		// sheet.setPrintGridlines(false);
		sheet.setFitToPage(true);
		sheet.setHorizontallyCenter(true);
		PrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setLandscape(true);

		// the following three statements are required only for HSSF
		sheet.setAutobreaks(true);
		printSetup.setFitHeight((short) 1);
		printSetup.setFitWidth((short) 1);

		// the header row: centered text in 48pt font

		Row headingRow = sheet.createRow(0);
		headingRow.setHeightInPoints(30f);
		Cell headingCell = headingRow.createCell(0);
		headingCell.setCellStyle(styles.get("sheet_heading"));
		headingCell.setCellValue(sheetName);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

		Row headerRow = sheet.createRow(1);
		headerRow.setHeightInPoints(30f);
		for (int i = 0; i < titles.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(titles[i]);
			cell.setCellStyle(styles.get("header"));
		}

		CreationHelper helper = wb.getCreationHelper();
		int rownum = 2;
		paintRows(data, rownum, helper,sheet);

		sheet.setColumnWidth(0, 256 * 20);
		sheet.setColumnWidth(1, 256 * 60);
		sheet.setColumnWidth(2, 256 * 35);
		sheet.setColumnWidth(3, 256 * 35);
		sheet.setColumnWidth(4, 256 * 10);// Funding
		sheet.setColumnWidth(5, 256 * 10);// Status
		sheet.setColumnWidth(6, 256 * 15);// Remarks
		sheet.setColumnWidth(7, 256 * 10);// Monitoring test
		sheet.setZoom(3, 4);

	}

	private static int paintRows(List<IsProgramDetail> data, Integer rownum,
			CreationHelper helper, Sheet sheet) {
		if(data==null || data.isEmpty()){
			return rownum-1;
		}
		
		for (Integer i = 0; i < data.size(); i++, rownum++) {

			Row row = sheet.createRow(rownum);
			IsProgramDetail detail = data.get(i);
			bindData(detail, helper, sheet, row, i);
			
			rownum=paintRows(detail.getChildren(), rownum+1, helper, sheet);
		}
		
		return --rownum;
	}

	private static void bindData(IsProgramDetail detail, CreationHelper helper,
			Sheet sheet, Row row, int i) {

		boolean isHeader = detail.getType() == ProgramDetailType.OUTCOME;

		if (isHeader) {
			row.setHeightInPoints(60f);
		} else {
			row.setHeightInPoints(60f);
		}

		for (int j = 0; j < titles.length; j++) {
			Cell cell = row.createCell(j);
			String styleName = null;

			switch (j) {
			case 0:
				// "Overall LWF Goals"
				if (isHeader) {
					styleName = "cell_h";
					cell.setCellValue(detail.getName());
				} else {
					styleName = "cell_normal";
					// cell.setCellValue("");
				}
				break;
			case 1:
				// "Activity"
				if (isHeader) {
					// styleName = i == 0 ? "cell_h" : "cell_bb";
					styleName = "cell_h_sub";
				} else {
					styleName = "cell_normal";
				}
				cell.setCellValue(detail.getDescription());
				// sheet.autoSizeColumn(1);
				break;
			case 2:
				// "Targets"
				styleName = isHeader ? "cell_b" : "cell_normal";
				if (!isHeader) {
					generateAndFormatValue(helper, cell,
							detail.getTargetsAndOutcomes());
				}
				// sheet.autoSizeColumn(2);
				break;
			case 3:
				// "Indicator"
				styleName = isHeader ? "cell_b" : "cell_normal";
				if (!isHeader) {
					generateAndFormatValue(helper, cell,
							detail.getTargetsAndOutcomes(), true);
				}
				// sheet.autoSizeColumn(3);
				break;
			case 4: {
				// "Funding"
				styleName = "cell-currency";
				if (!isHeader)
					cell.setCellValue(detail.getBudgetAmount());
				break;
			}
			case 5: {
				// "Status"
				styleName = isHeader ? "cell_bg" : "cell_g";
				if (!isHeader){
					
					cell.setCellValue(detail.getStatus()==null? "": detail.getStatus().toString());
				}
				break;
			}
			case 6: {
				// "Remarks"
				styleName = isHeader ? "cell_b" : "cell_normal";
				break;
			}
			case 7: {
				// "Monitoring test"
				styleName = isHeader ? "cell_bg" : "cell_g";
				break;
			}
			default:
				styleName = "cell_normal";
			}

			cell.setCellStyle(styles.get(styleName));
		}

	}

	private static void generateAndFormatValue(CreationHelper helper,
			Cell cell, List<TargetAndOutcomeDTO> targetsAndOutcomes) {
		generateAndFormatValue(helper, cell, targetsAndOutcomes, false);
	}

	private static void generateAndFormatValue(CreationHelper helper,
			Cell cell, List<TargetAndOutcomeDTO> targetsAndOutcomes,
			boolean indicatorsOnly) {
		int count = 0;

		if (targetsAndOutcomes == null) {
			return;
		}

		int[] sizes = new int[targetsAndOutcomes.size()];
		int i = 0;
		StringBuffer buffer = new StringBuffer();
		for (TargetAndOutcomeDTO targetAndOutcome : targetsAndOutcomes) {
			Double actualOutcome = targetAndOutcome.getActualOutcome();
			Double target = targetAndOutcome.getTarget();
			String indicator = targetAndOutcome.getMeasure();
			String label = null;

			if (indicatorsOnly) {
				label = indicator;
			} else {
				if (actualOutcome != null) {
					label = actualOutcome + "/" + target + " " + indicator;
				} else {
					label = "NoData - " + indicator;
				}
			}

			buffer.append(label + ", ");
			sizes[i] = label.length();
			++i;
		}
		String out = buffer.toString();
		if (out.isEmpty()) {
			return;
		}

		RichTextString richText = helper.createRichTextString(out.substring(0,
				out.length() - 2));

		count = -2;
		for (i = 0; i < sizes.length; i++) {
			String font_style = "";
			if (indicatorsOnly) {
				font_style = "default";
			} else {
				TargetAndOutcomeDTO dto = targetsAndOutcomes.get(i);
				font_style = dto.getActualOutcome() == null ? "_nodata" : dto
						.getActualOutcome() > dto.getTarget() ? "font-success"
						: "font-failure";
			}

			richText.applyFont(count += 2, count = count + sizes[i],
					fonts.get(font_style));
		}
		cell.setCellValue(richText);

	}

	/**
	 * create a library of cell styles
	 */
	private static Map<String, CellStyle> createStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
		DataFormat df = wb.createDataFormat();

		CellStyle style;

		Font sheetHeadingFont = wb.createFont();
		sheetHeadingFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		sheetHeadingFont.setFontHeightInPoints((short) 16);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		// style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
		// style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(sheetHeadingFont);
		styles.put("sheet_heading", style);

		Font headerFont = wb.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setFontHeightInPoints((short) 14);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE
				.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(headerFont);
		styles.put("header", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE
				.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(headerFont);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("header_date", style);

		Font font1 = wb.createFont();
		font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(font1);
		style.setWrapText(true);
		styles.put("cell_b", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font1);
		styles.put("cell_b_centered", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setFont(font1);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_b_date", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		// style.setFont(font1);
		style.setDataFormat(df.getFormat("#,##0.00"));
		styles.put("cell_currency", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setFont(font1);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_g", style);

		Font font2 = wb.createFont();
		font2.setColor(IndexedColors.BLUE.getIndex());
		font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(font2);
		styles.put("cell_bb", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setFont(font1);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_bg", style);

		Font font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 14);
		font3.setColor(IndexedColors.DARK_BLUE.getIndex());
		font3.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(font3);
		style.setWrapText(true);
		styles.put("cell_h", style);

		Font subHeaderFont = wb.createFont();
		subHeaderFont.setItalic(true);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(subHeaderFont);
		style.setWrapText(true);
		styles.put("cell_h_sub", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		style.setWrapText(true);
		styles.put("cell_normal", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setWrapText(true);
		styles.put("cell_normal_centered", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setWrapText(true);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_normal_date", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setIndention((short) 1);
		style.setWrapText(true);
		styles.put("cell_indented", style);

		style = createBorderedStyle(wb);
		style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styles.put("cell_blue", style);

		return styles;
	}

	private static CellStyle createBorderedStyle(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		// style.setBorderRight(CellStyle.BORDER_THIN);
		// style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		// style.setBorderBottom(CellStyle.BORDER_THIN);
		// style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		// style.setBorderLeft(CellStyle.BORDER_THIN);
		// style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		// style.setBorderTop(CellStyle.BORDER_THIN);
		// style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		return style;
	}

	public static Map<String, Font> createFonts(Workbook wb) {
		fonts = new HashMap<>();
		Font headerFont = wb.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fonts.put("header", headerFont);

		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		// font.setColor(Font.COLOR_RED);
		font.setColor(IndexedColors.GREEN.index);
		fonts.put("font-success", font);

		font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setColor(Font.COLOR_RED);
		fonts.put("font-failure", font);

		font = wb.createFont();
		font.setColor(IndexedColors.VIOLET.index);
		fonts.put("_nodata", font);

		font = wb.createFont();
		font.setColor(IndexedColors.BLACK.index);
		fonts.put("default", font);

		return fonts;
	}

	public static void setup() {
		DBTrxProvider.init();
		DB.beginTransaction();
	}

}
