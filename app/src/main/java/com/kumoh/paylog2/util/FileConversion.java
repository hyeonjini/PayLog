package com.kumoh.paylog2.util;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileConversion {
    private Workbook workbook;
    private Sheet sheet;
    private Row row;
    private Cell cell;

    private Map<Integer, String> titleMap = new HashMap<>();

    // LOGO : Paylog
    private final static int LOGO_START_ROW = 0;

    // Conversion Date
    private final static int CONVERSION_DATE_START_ROW = 3;
    private final static int CONVERSION_DATE_START_COL = 4;

    // GROUP
    private final static int GROUP_START_ROW = 4;
    private final static int GROUP_START_COL = 4;

    // TITLE : 제목
    private final static int TITLE_START_ROW = 6;
    private final static int TITLE_START_COL = 0;

    // CONTEXT : 내용
    private final static int CONTEXT_START_ROW = 7;
    private final static int CONTEXT_START_COL = 0;

    public FileConversion() {
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet();

        // 각 열의 너비 초기화
        sheet.setColumnWidth(0, (8 * 500));
        sheet.setColumnWidth(1, (7 * 500));
        sheet.setColumnWidth(2, (10 * 500));
        sheet.setColumnWidth(3, (6 * 500));
        sheet.setColumnWidth(4, (6 * 500));
        sheet.setColumnWidth(5, (8 * 500));

        // title 초기화
        titleMap.put(0, "날짜");
        titleMap.put(1, "분류");
        titleMap.put(2, "내용");
        titleMap.put(3, "수입");
        titleMap.put(4, "지출");
        titleMap.put(5, "비고");
    }

    // PayLog Logo 삽입
    private void inputLogo() {
        row = sheet.createRow(LOGO_START_ROW);

        // 셀 병합
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, titleMap.size() - 1)); //열시작, 열종료, 행시작, 행종료 (자바배열과 같이 0부터 시작)

        cell = row.createCell(LOGO_START_ROW);
        cell.setCellValue("PayLog");
    }

    // 그룹 이름 삽입
    private void inputGroupName() {
        row = sheet.createRow(GROUP_START_ROW);

        // 셀 병합
        sheet.addMergedRegion(new CellRangeAddress(GROUP_START_ROW, GROUP_START_ROW, GROUP_START_COL, GROUP_START_COL+1));

        cell = row.createCell(GROUP_START_COL);
        cell.setCellValue("그룹 이름 : ");
    }

    // 변환 날짜 삽입
    private void inputConversionDate() {
        // 현재 날짜
        SimpleDateFormat format = new SimpleDateFormat( "yyyy년 MM월dd일 HH시mm분ss초");
        Date time = new Date();
        String currentTime = format.format(time);

        row = sheet.createRow(CONVERSION_DATE_START_ROW);

        // 셀 병합
        sheet.addMergedRegion(new CellRangeAddress(CONVERSION_DATE_START_ROW, CONVERSION_DATE_START_ROW, CONVERSION_DATE_START_COL, CONVERSION_DATE_START_COL+1));

        cell = row.createCell(CONVERSION_DATE_START_COL);
        cell.setCellValue("파일 변환 날짜 : " + currentTime);
    }

    //   제목 입력   * 건드릴 필요 없음 *
    //
    // - TITLE_START_ROW : 제목 시작 행 (멤버변수에서 초기화)
    // - titleMap : 초기화된 제목 Map (생성자에서 초기화)
    private void typeTitles() {
        row = sheet.createRow(TITLE_START_ROW);
        // (0, i)위치의 셀에 각 title 입력
        for (int i = TITLE_START_COL; i < titleMap.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(titleMap.get(i));
        }
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

    private void setCellStyle() {
        // 모든 cell style 적용
        // 가운데 정렬
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
//        // 글자 크기 20
//        Font font = workbook.createFont();
//        font.setFontHeightInPoints((short)20);
//        cell.getCellStyle().setFont(font);
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
        inputGroupName();
        inputConversionDate();
        typeTitles();
        typeContexts(mItems);
        setCellStyle();

        return saveFileToExternalStorage(context, fileName);
    }
}

