package com.kumoh.paylog2;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.kumoh.paylog2.db.History;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileConversion {
    private Workbook workbook;
    private Sheet sheet;
    private Row row;
    private Cell cell;

    private Map<Integer, String> titleMap = new HashMap<>();

    private final static int LOGO_START_ROW = 0;

    private final static int TITLE_START_ROW = 3;
    private final static int TITLE_START_COL = 0;

    private final static int CONTEXT_START_ROW = 4;
    private final static int CONTEXT_START_COL = 0;

    public FileConversion() {
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet();

        // title 초기화
        titleMap.put(0, "날짜");
        titleMap.put(1, "유형");
        titleMap.put(2, "내용");
        titleMap.put(3, "가격");
    }

    // PayLog Logo 삽입
    private void inputLogo() {
        row = sheet.createRow(LOGO_START_ROW);


        workbook.getNumCellStyles();

//        CellStyle cellStyle = workbook.getCellStyleAt(7);
//
//        cellStyle.setAlignment(HorizontalAlignment.CENTER);
//        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        cell = row.createCell(0);
        cell.setCellValue("PayLog");

        //셀 병합
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 10)); //열시작, 열종료, 행시작, 행종료 (자바배열과 같이 0부터 시작)

        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        Font font = workbook.createFont();
        font.setFontHeightInPoints((short)20);
        cell.getCellStyle().setFont(font);
    }

    // 제목 입력
    private void typeTitles() {
        row = sheet.createRow(TITLE_START_ROW);
        // (0, i)위치의 셀에 각 title 입력
        for (int i = TITLE_START_COL; i < titleMap.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(titleMap.get(i));
        }

        Font font = workbook.createFont();
        font.setFontHeightInPoints((short)10);
        cell.getCellStyle().setFont(font);
    }

    // 데이터 입력
    private void typeContexts(List<History> mItems) {
        int startRow = CONTEXT_START_ROW;
        for (int i = 0; i < mItems.size(); i++) {
            // (시작지점 + i) 번째 행
            row = sheet.createRow(startRow + i);

            // (i, 0)위치의 셀에 mItems 의 날짜 입력
            cell = row.createCell(0);
            cell.setCellValue(mItems.get(i).getDate());

            // (i, 1)위치의 셀에 mItems 의 유형(지출, 수입) 입력
            cell = row.createCell(1);

            String kind;
            int IKind = mItems.get(i).getKind();
            if(IKind == 1) kind = "수입";
            else if(IKind == -1) kind = "지출";
            else kind = "알 수 없는 종류";
            cell.setCellValue(kind);

            // (i, 0)위치의 셀에 mItems 의 내용 입력
            cell = row.createCell(2);
            cell.setCellValue(mItems.get(i).getDescription());

            // (i, 3)위치의 셀에 mItems 의 가격 입력
            cell = row.createCell(3);
            cell.setCellValue(mItems.get(i).getAmount());
        }
    }

    private File saveFileToExternalStorage(Context context, String fileName) {
        File file = new File(context.getExternalFilesDir(null), fileName);

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            workbook.write(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (workbook != null) workbook.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return file;
    }

    public File saveExcelFile(Context context, List<History> mItems, String fileName) {
        inputLogo();
        typeTitles();
        typeContexts(mItems);

        return saveFileToExternalStorage(context, fileName);
    }
}

