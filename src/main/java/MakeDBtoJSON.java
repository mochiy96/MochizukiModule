import java.io.*;
import java.util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MakeDBtoJSON {

    public static void main(String[] args) {

        Connection connection = null;
        Statement statement = null;

        String databeseName = "Food-dbase.db";  //データベースファイル名
        String fileName= "FoodInfo.json";		//JSONファイル名

        String tableName = "food";              //データベースのテーブル名

        String[] column = {"name", "cost"};
        boolean hasGroup = true;
        Map<String, List<String>> map = new HashMap<>();
        String[] columnGroup = {"foodColors", "nutrients"};

        List<String> foodColors = new ArrayList<>();
        List<String> nutrients = new ArrayList<>();
        foodColors.addAll(Arrays.asList("red", "green", "yellow"));
        nutrients.addAll(Arrays.asList("energy", "protein", "lipid",
                "carbohydrate", "salt", "calcium", "vegetables"));

        map.put("foodColors", foodColors);
        map.put("nutrients", nutrients);

        /* ファイルの確認 */
        File file = new File(fileName);
        if(file.exists()) {	//書き込みファイル先の確認
            file.delete();
        }

        try {
            Class.forName("org.sqlite.JDBC");

            PrintWriter pw = new PrintWriter(new BufferedWriter(new
                    OutputStreamWriter(new FileOutputStream(file),"UTF-8")));

            connection = DriverManager.getConnection("jdbc:sqlite:" + databeseName);
            statement = connection.createStatement();
            String sql = "select * from " + tableName;
            ResultSet rs = statement.executeQuery(sql);

            int cnt = 0;

            pw.println("[");
            while (rs.next()) {
                if(cnt == 0){
                    pw.println("    {");
                    for(int i = 0; i < column.length; i++){
                        if(hasGroup){
                            pw.println("        \"" + column[i] + "\":\"" + rs.getString(column[i]) + "\",");
                        }else{
                            if(i == (column.length - 1)){
                                pw.println("        \"" + column[i] + "\":\"" + rs.getString(column[i]) + "\"");
                            }else {
                                pw.println("        \"" + column[i] + "\":\"" + rs.getString(column[i]) + "\",");
                            }
                        }
                    }
                    for(int i = 0; i < columnGroup.length; i++){
                        pw.println("        \"" + columnGroup[i] + "\":{");
                        for(int j = 0; j < map.get(columnGroup[i]).size(); j++){
                            if(j == (map.get(columnGroup[i]).size()-1)){
                                pw.println("            \"" + map.get(columnGroup[i]).get(j) + "\":\"" + rs.getString(map.get(columnGroup[i]).get(j)) + "\"");
                            }else{
                                pw.println("            \"" + map.get(columnGroup[i]).get(j) + "\":\"" + rs.getString(map.get(columnGroup[i]).get(j)) + "\",");
                            }
                        }
                        if(i == (columnGroup.length - 1)){
                            pw.println("        }");
                        }else{
                            pw.println("        },");
                        }
                    }
                    pw.print("    }");
                }else {
                    pw.println(",");
                    pw.println("    {");
                    for(int i = 0; i < column.length; i++){
                        if(hasGroup){
                            pw.println("        \"" + column[i] + "\":\"" + rs.getString(column[i]) + "\",");
                        }else{
                            if(i == (column.length - 1)){
                                pw.println("        \"" + column[i] + "\":\"" + rs.getString(column[i]) + "\"");
                            }else {
                                pw.println("        \"" + column[i] + "\":\"" + rs.getString(column[i]) + "\",");
                            }
                        }
                    }
                    for(int i = 0; i < columnGroup.length; i++){
                        pw.println("        \"" + columnGroup[i] + "\":{");
                        for(int j = 0; j < map.get(columnGroup[i]).size(); j++){
                            if(j == (map.get(columnGroup[i]).size()-1)){
                                pw.println("            \"" + map.get(columnGroup[i]).get(j) + "\":\"" + rs.getString(map.get(columnGroup[i]).get(j)) + "\"");
                            }else{
                                pw.println("            \"" + map.get(columnGroup[i]).get(j) + "\":\"" + rs.getString(map.get(columnGroup[i]).get(j)) + "\",");
                            }
                        }
                        if(i == (columnGroup.length - 1)){
                            pw.println("        }");
                        }else{
                            pw.println("        },");
                        }
                    }
                    pw.print("    }");
                }
                cnt++;
            }
            pw.println();
            pw.println("]");
            pw.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
