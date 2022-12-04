package com.shpp;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.shpp.dto.Category;
import com.shpp.dto.Goods;
import com.shpp.dto.Market;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class DocReader {


    public LinkedList<Goods> getGoods() {
        CsvMapper csvMapper = new CsvMapper();

        LinkedList<Goods> list = new LinkedList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("product.csv"))) {
            String result = bufferedReader.readLine();
            String[] array;
            while (result != null) {
                array = result.split(",");
                list.add(new Goods(array[1],
                        new Category(array[0]),
                        Double.parseDouble(array[2])));
                result = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Market> getMarkets() {
        LinkedList<Market> markets = new LinkedList<>();
        try (Reader reader = new FileReader(("Markets.txt"))) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] lines = line.split(",");
                markets.add(new Market(lines[1], lines[0]));
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


//    public static void main(String[] args) {
//        FileWriter file;
//        try {
//            file = new FileWriter("product.csv");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        CsvMapper csvMapper = new CsvMapper();
//        DocReader docReader = new DocReader();
//        CsvSchema csvSchema = CsvSchema.builder().build().withHeader();
//        List<Goods> goods = docReader.getGoods();
//        String csv;
//        DocReader.nestedGoods nestedGoods = docReader.new nestedGoods();
//        LinkedList<nestedGoods> nestedGoods1 = new LinkedList<>();
////        for (Goods good : goods) {
////
////            nestedGoods =  docReader.new nestedGoods().setName(good.getName()).setCategory(good.getCategory().getName()).setPrice(good.getPrice());
////            nestedGoods1.add(nestedGoods);
////        }
//
//        for (Goods good : goods) {
//            nestedGoods.setName(good.getName()).setCategory(good.getCategory().getName()).setPrice(good.getPrice());
//            try {
//                Object[] obj = new Object[]{
//                        good.getCategory().getName(),
//                        good.getName(),
//                        good.getPrice()
//                };
////                csvMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
//                csv = csvMapper.writeValueAsString(obj);
//                BufferedWriter bufferedWriter = new BufferedWriter(file);
//                bufferedWriter.write(csv);
//                bufferedWriter.flush();
//                //               File file = new File("product.csv");
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//    }
//
//    public List<Goods> getGoods() {
//        LinkedList<Goods> list = new LinkedList<>();
//        try (FileInputStream fileInputStream = new FileInputStream(("test.xlsx"))) {
//            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
//            XSSFSheet sheet = workbook.getSheetAt(0);
//            for (Row row : sheet) {
//                Iterator<Cell> cellIterator = row.cellIterator();
//                while (cellIterator.hasNext()) {
//                    list.add(new Goods(cellIterator.next().getStringCellValue(),
//                            new Category(cellIterator.next().getStringCellValue()),
//                            Double.parseDouble(cellIterator.next().getStringCellValue().split(" ")[0].replace(",", "."))));
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return list;
//    }


