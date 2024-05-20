package com.xue;

import com.xue.mapper.PathMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * @author Xue
 * @create 2024-05-20 11:14
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringTest {

    @Autowired
    private PathMapper pathMapper;

    @Test
    public void test() throws IOException {
        /*String url = "E:\\path4.txt";
        Path path = Paths.get(url);
        List<String> list = Files.readAllLines(path);
        list.forEach(item -> {
            String[] split = item.split(",");
            pathMapper.insert(new com.xue.entity.Path(null, Double.valueOf(split[0]), Double.valueOf(split[1]), 4));
        });*/
    }

}
