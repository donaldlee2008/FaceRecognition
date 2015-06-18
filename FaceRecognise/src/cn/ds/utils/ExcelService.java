/**
 * Project             :FaceRecognise project
 * Comments            :管理控制类
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | dl 
 * 2 | 2013-5-02 | 增加addRow(int page, int yCell, String[] contents)方法，增加synchronized | jxm 
 */
package cn.ds.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelService {
	private String filePath;
	private String fileName;
	private File xlsFile;
	private boolean firstCreate;
	private WritableSheet sheet;
	private int yCell = 0;

	public ExcelService(String name) {
		this.fileName = name;
		firstCreate = true;
		xlsFile = new File(name);
	}

	public ExcelService(String path, String name) {
		firstCreate = true;
		this.filePath = path;
		this.fileName = name;
		xlsFile = new File(filePath + fileName + ".xls");
	}

	private Workbook getWorkBook() {
		Workbook wb = null;
		try {
			wb = Workbook.getWorkbook(xlsFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wb;
	}

	private WritableWorkbook getWritableWorkbook() {
		WritableWorkbook wBook = null;
		try {
			if (firstCreate) {
				wBook = Workbook.createWorkbook(xlsFile);
			} else {
				Workbook wb = getWorkBook();
				wBook = Workbook.createWorkbook(xlsFile, wb);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wBook;
	}


	public synchronized void addRow(int page, int yCell, String[] contents) {
		WritableWorkbook wBook = getWritableWorkbook();
		try {
			if (firstCreate) {
				sheet = wBook.createSheet("第" + page + "页", page);
				firstCreate = false;
			} else {
				sheet = wBook.getSheet(page);
			}
			for (int i = 0; i < contents.length; i++) {
				sheet.addCell(new Label(i, yCell, contents[i]));
			}
			wBook.write();
			wBook.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void addRow(int page, String[] contents) {
		WritableWorkbook wBook = getWritableWorkbook();
		try {
			if (firstCreate) {
				sheet = wBook.createSheet("第" + page + "页", page);
				firstCreate = false;
			} else {
				sheet = wBook.getSheet(page);
			}
			for (int i = 0; i < contents.length; i++) {
				sheet.addCell(new Label(i, yCell, contents[i]));
			}
			yCell += 1;
			wBook.write();
			wBook.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String[]> readAll(int sheetNum){
		List<String[]> al = new ArrayList<String[]>();
		
		Workbook wb = getWorkBook();
		Sheet sheet = wb.getSheet(sheetNum);
		int rowCount = sheet.getRows();
		int colCount = sheet.getColumns();
		String[] contents;
		for(int i = 0;i < rowCount;i++){
			Cell[] cells = sheet.getRow(i);
			contents = new String[colCount];
			for(int j = 0;j < cells.length;j++){
				contents[j] = cells[j].getContents();
			}
			
			for(int k = cells.length;k < colCount;k++){
				contents[k] = null;
			}
			al.add(contents);
		}
		return al;
	}
	public String getFileName() {
		return fileName;
	}

	public String getFilePath() {
		return filePath;
	}
}
