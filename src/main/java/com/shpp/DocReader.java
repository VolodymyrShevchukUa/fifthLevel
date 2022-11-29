package com.shpp;

import com.shpp.dto.Category;
import com.shpp.dto.Goods;
import com.shpp.dto.Market;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DocReader {

    public List<Goods> getGoods() {
        LinkedList<Goods> list = new LinkedList<>();
        try (FileInputStream fileInputStream = new FileInputStream(("test.xlsx"))) {
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    list.add(new Goods(cellIterator.next().getStringCellValue(),
                            new Category(cellIterator.next().getStringCellValue()),
                            Double.parseDouble(cellIterator.next().getStringCellValue().split(" ")[0].replace(",", "."))));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Market> getMarkets(){
        LinkedList<Market> markets = new LinkedList<>();
        try(Reader reader = new FileReader(new File("Markets.txt"))){
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while (line !=null){
                String[] lines = line.split(",");
                markets.add(new Market(lines[1],lines[0]));
                line = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return markets;
    }
}
