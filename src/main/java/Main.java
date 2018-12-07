import java.io.*;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        // ObjectMapperでjson => Sampleへの変換を行う
        ArrayList<Food> foodList = new ObjectMapper().readValue(readAll("FoodInfo.json"), new TypeReference<ArrayList<Food>>(){});

        System.out.println(foodList);
    }

    static String readAll(final String path) throws IOException {
        return Files.lines(Paths.get(path), Charset.forName("UTF-8"))
                .collect(Collectors.joining(System.getProperty("line.separator")));
    }
}