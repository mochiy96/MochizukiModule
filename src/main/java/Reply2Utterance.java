import java.io.*;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Reply2Utterance {

    public static ArrayList<Food> foodList;
    public static List<Integer> foundFoodIndex = new ArrayList<>();
    public static List<Integer> preFoodIndex = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        foodList = new ObjectMapper().readValue(readAll
                ("FoodInfo.json"), new TypeReference<ArrayList<Food>>(){});

        while(true){
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String receivedInput = reader.readLine();
            foundFoodIndex.clear();

            if(receivedInputHasFood(receivedInput)){
                containsFoodInfo(receivedInput);
            } else {
                checkReceivedInput(receivedInput);
            }

            if(!foundFoodIndex.isEmpty()){
                preFoodIndex = new ArrayList<>(foundFoodIndex);
            }

        }
    }

    static boolean receivedInputHasFood(String receivedInput){
        Tokenizer tokenizer = new Tokenizer.Builder().build();
        List<Token> tokens = tokenizer.tokenize(receivedInput);

        for(int i = 0; i < foodList.size(); i++){
            if(receivedInput.toUpperCase().contains(foodList.get(i).name)){
                foundFoodIndex.add(i);
                break;
            } else {
                for(Token token : tokens){
                    if(token.getPartOfSpeechLevel1().equals("名詞") &&
                            (foodList.get(i).name.contains(token.getBaseForm()))){
                        foundFoodIndex.add(i);
                    }
                }
            }
        }

        if(foundFoodIndex.isEmpty()){
            return false;
        } else {
            return true;
        }
    }

    static void checkReceivedInput(String receivedInput){
        if(receivedInput.contains("bye")){
            System.out.println("終了します");
            System.exit(1);
        }
        if(receivedInput.contains("それ") || receivedInput.contains("その")){
            if (receivedInput.contains("カロリー")) {
                for (int i : preFoodIndex){
                    System.out.println(foodList.get(i).name + "のカロリーは" +
                            foodList.get(i).nutrients.energy + "kcalです");
                }
            }
            if (receivedInput.contains("説明") || receivedInput.contains("詳細")) {
                for (int i : preFoodIndex){
                    System.out.println(foodList.get(i).name + "：" +
                            foodList.get(i).description);
                }
            }
            if (receivedInput.contains("値段")) {
                for (int i : preFoodIndex){
                    System.out.println(foodList.get(i).name + "の値段は" +
                            foodList.get(i).cost + "円です");
                }
            }
        }else {
            System.out.println("すみません、わかりません");
        }
    }

    static void containsFoodInfo(String receivedInput){
        boolean flag = true;
        if (receivedInput.contains("カロリー")) {
            for (int i : foundFoodIndex){
                System.out.println(foodList.get(i).name + "のカロリーは" +
                        foodList.get(i).nutrients.energy + "kcalです");
            }
            flag = false;
        }
        if (receivedInput.contains("説明") || receivedInput.contains("詳細")) {
            for (int i : foundFoodIndex){
                System.out.println(foodList.get(i).name + "：" +
                        foodList.get(i).description);
            }
            flag = false;
        }
        if (receivedInput.contains("値段")) {
            for (int i : foundFoodIndex){
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