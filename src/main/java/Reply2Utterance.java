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
    public static List<Integer> foundFoodsIndex = new ArrayList<>();
    public static List<Integer> preFoodsIndex = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        foodList = new ObjectMapper().readValue(readAll
                ("FoodInfo.json"), new TypeReference<ArrayList<Food>>(){});

        while(true){
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String recvdInput = reader.readLine();      // recvd = received
            foundFoodsIndex.clear();

            if(recvdInputHasFood(recvdInput)){
                checkHasFoodInfo(recvdInput);
            } else {
                checkRecvdInput(recvdInput);
            }

            preFoodsIndex = new ArrayList<>(foundFoodsIndex);

        }
    }


    static boolean recvdInputHasFood(String recvdInput){
        Tokenizer tokenizer = new Tokenizer.Builder().build();
        List<Token> tokens = tokenizer.tokenize(recvdInput);
        JaroWinklerDistance jaroWinklerDistance = new JaroWinklerDistance();

        for(int i = 0; i < foodList.size(); i++){
            if(recvdInput.toUpperCase().contains(foodList.get(i).name)){
                foundFoodsIndex.add(i);
                break;
            } else {
                for(Token token : tokens){
                    if(token.getPartOfSpeechLevel1().equals("名詞") &&
                            ((jaroWinklerDistance.getDistance
                                    (token.getBaseForm(), foodList.get(i).name)) > 0.8)){
                        foundFoodsIndex.add(i);
                    }
                }
            }
        }

        if(foundFoodsIndex.isEmpty()){
            return false;
        } else {
            return true;
        }
    }

    static void checkRecvdInput(String recvdInput){
        if(recvdInput.contains("bye")){
            System.out.println("終了します");
            System.exit(1);
        }
        if(recvdInput.contains("それ") || recvdInput.contains("その")){
            if (recvdInput.contains("カロリー")) {
                for (int i : preFoodsIndex){
                    System.out.println(foodList.get(i).name + "のカロリーは" +
                            foodList.get(i).nutrients.energy + "kcalです");
                }
            }
            if (recvdInput.contains("説明") || recvdInput.contains("詳細")) {
                for (int i : preFoodsIndex){
                    System.out.println(foodList.get(i).name + "：" +
                            foodList.get(i).description);
                }
            }
            if (recvdInput.contains("値段")) {
                for (int i : preFoodsIndex){
                    System.out.println(foodList.get(i).name + "の値段は" +
                            foodList.get(i).cost + "円です");
                }
            }
        }else {
            System.out.println("すみません、わかりません");
        }
    }

    static void checkHasFoodInfo(String recvdInput){
        boolean flag = true;
        if (recvdInput.contains("カロリー")) {
            for (int i : foundFoodsIndex){
                System.out.println(foodList.get(i).name + "のカロリーは" +
                        foodList.get(i).nutrients.energy + "kcalです");
            }
            flag = false;
        }
        if (recvdInput.contains("説明") || recvdInput.contains("詳細")) {
            for (int i : foundFoodsIndex){
                System.out.println(foodList.get(i).name + "：" +
                        foodList.get(i).description);
            }
            flag = false;
        }
        if (recvdInput.contains("値段")) {
            for (int i : foundFoodsIndex){
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