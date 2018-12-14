import java.io.*;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import org.apache.lucene.search.spell.JaroWinklerDistance;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Reply2Utterance {

    public static ArrayList<Food> foodList;
    public static List<Integer> found_food = new ArrayList<>();
    public static List<Integer> pre_food = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        foodList = new ObjectMapper().readValue(readAll("FoodInfo.json"), new TypeReference<ArrayList<Food>>(){});

        while(true){
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String user = reader.readLine();
            found_food.clear();

            if(has_food(user)){
                checkfoodinfo(user);
            } else {
                checksentence(user);
            }

            pre_food = new ArrayList<>(found_food);

        }
    }


    static boolean has_food(String user){
        Tokenizer tokenizer = new Tokenizer.Builder().build();
        List<Token> tokens = tokenizer.tokenize(user);
        JaroWinklerDistance jaroWinklerDistance = new JaroWinklerDistance();

        for(int i = 0; i < foodList.size(); i++){
            if(user.toUpperCase().contains(foodList.get(i).name)){
                found_food.add(i);
                break;
            } else {
                for(Token token : tokens){
                    if(token.getPartOfSpeechLevel1().equals("名詞") &&
                            ((jaroWinklerDistance.getDistance(token.getBaseForm(), foodList.get(i).name)) > 0.8)){
                        found_food.add(i);
                    }
                }
            }
        }

        if(found_food.isEmpty()){
            return false;
        } else {
            return true;
        }
    }

    static void checksentence(String user){
        if(user.contains("bye")){
            System.out.println("終了します");
            System.exit(1);
        }
        if(user.contains("それ") || user.contains("その")){
            if (user.contains("カロリー")) {
                for (int i : pre_food){
                    System.out.println(foodList.get(i).name + "のカロリーは" +
                            foodList.get(i).nutrients.energy + "kcalです");
                }
            }
            if (user.contains("説明") || user.contains("詳細")) {
                for (int i : pre_food){
                    System.out.println(foodList.get(i).name + "：" +
                            foodList.get(i).description);
                }
            }
            if (user.contains("値段")) {
                for (int i : pre_food){
                    System.out.println(foodList.get(i).name + "の値段は" +
                            foodList.get(i).cost + "円です");
                }
            }
        }else {
            System.out.println("すみません、わかりません");
        }
    }

    static void checkfoodinfo(String user){
        boolean flag = true;
        if (user.contains("カロリー")) {
            for (int i : found_food){
                System.out.println(foodList.get(i).name + "のカロリーは" +
                        foodList.get(i).nutrients.energy + "kcalです");
            }
            flag = false;
        }
        if (user.contains("説明") || user.contains("詳細")) {
            for (int i : found_food){
                System.out.println(foodList.get(i).name + "：" +
                        foodList.get(i).description);
            }
            flag = false;
        }
        if (user.contains("値段")) {
            for (int i : found_food){
                System.out.println(foodList.get(i).name + "の値段は" +
                        foodList.get(i).cost + "円です");
            }
            flag = false;
        }
        if(flag){
            System.out.println("すみません、わかりません");
        }
    }
    static String readAll(final String path) throws IOException {
        return Files.lines(Paths.get(path), Charset.forName("UTF-8"))
                .collect(Collectors.joining(System.getProperty("line.separator")));
    }
}