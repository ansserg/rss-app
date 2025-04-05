package com.nexign.dmf.rss.rssvw.DAO;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletResponse;

@Component
public class GetData {


    public void createWb(HttpServletResponse response) throws   Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("отчет");
        XSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("FILE NAME");
        row.createCell(1).setCellValue("RAP FILE");
        row.createCell(2).setCellValue("CHARGE,SDR");
        row.createCell(3).setCellValue("TAX, SDR");


        ServletOutputStream ops =response.getOutputStream();


        workbook.write(ops);
        workbook.close();
        ops.flush();
        ops.close();

    }
}