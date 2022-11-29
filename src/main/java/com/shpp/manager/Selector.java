package com.shpp.manager;

import com.mongodb.client.MongoDatabase;
import com.shpp.dto.Category;
import com.shpp.dto.Goods;
import com.shpp.dto.Market;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class Selector {
    private final MongoDatabase connection;

    Logger logger = LoggerFactory.getLogger(Selector.class);

    public Selector(MongoDatabase connection) {
        this.connection = connection;
    }

//    public List<Category> getCategories() {
//        List<Category> list = new LinkedList<>();
//        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT category_name FROM category")) {
//            ResultSet result = preparedStatement.executeQuery();
//            while (result.next()) {
//                list.add(new Category(result.getString(1)));
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return list;
//    }
//
//    public List<Goods> getGoods() {
//        List<Goods> list = new LinkedList<>();
//        logger.info("Try to select goods");
//        try (PreparedStatement preparedStatement = connection.
//                prepareStatement("SELECT category.category_name , g.goods_name ,g.goods_price" +
//                        " FROM category INNER JOIN goods g on category.category_id = g.category_id")) {
//            ResultSet result = preparedStatement.executeQuery();
//            while (result.next()) {
//                list.add(new Goods(new Category(result.getString(1)), result.getString(2)
//                        , Double.parseDouble(result.getString(3))));
//            }
//        } catch (SQLException e) {
//            logger.error("Goods select get wrong",e);
//            throw new RuntimeException(e);
//        }
//        return list;
//    }
//
//    public List<Market> getMarkets() {
//        List<Market> list = new LinkedList<>();
//        logger.info("Try to select markets");
//        try (PreparedStatement preparedStatement = connection.
//                prepareStatement("SELECT market_name,market_adres FROM market;")) {
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                list.add(new Market(resultSet.getString(2), resultSet.getString(1)));
//            }
//        } catch (SQLException e) {
//            logger.error("Market select get wrong",e);
//            throw new RuntimeException(e);
//        }
//        return list;
//    }
//
//    public String getAddress(String typeOfGoods){
//        String result = "";
//        logger.info("Try to select adress");
//        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT mk.market_adres ," +
//                " COUNT(mk.market_name), market_name FROM goods_list gl INNER JOIN market mk ON mk.market_id = gl.market_id  \n" +
//                "INNER JOIN goods ON goods.goods_id = gl.goods_id" +
//                " INNER JOIN category ON category.category_id = goods.category_id WHERE category.category_name = (?)\n" +
//                "GROUP BY mk.market_adres, market_name ORDER BY count(*) DESC LIMIT 1;")) {
//            preparedStatement.setString(1,typeOfGoods);
//            ResultSet resultSet  = preparedStatement.executeQuery();
//            resultSet.next();
//            result = resultSet.getString(3).concat(" За адресою ").concat(resultSet.getString(1)).concat(". Кількість товарів складає : = ").concat(resultSet.getInt(2)+"");
//
//        } catch (SQLException e) {
//            logger.error("Adress select get wrong",e);
//            throw new RuntimeException(e);
//        }
//        return result;
//    }
}
