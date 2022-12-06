package com.shpp;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.shpp.dto.Category;
import com.shpp.dto.Goods;
import com.shpp.dto.Market;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class DocReader {

    public List<Goods> getGoods() {
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



