package com.john.server.test.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-01-01 15:44
 * @since jdk1.8
 */
@Slf4j
public class ExportTest {

    @Test
    public void expr9() throws IOException {
//        ExcelExportUtil.exportExcel(TemplateExportParams params, Map<String, Object> map)
//        TemplateExportParams params = new TemplateExportParams();
//        Map<String,Object> keyValue=new HashMap<>(16);
//        ExcelExportUtil.exportExcel(params, keyValue);
        List<CourseEntity> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CourseEntity entity = new CourseEntity();
            entity.setId("id=" + i);
            entity.setName(RandomStringUtils.random(2, "语文数学英语" + i));
            TeacherEntity entity1 = new TeacherEntity();
            entity1.setId("tid=" + i);
            entity1.setName("教师=" + i);
            entity.setMathTeacher(entity1);
            List<StudentEntity> stus = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                StudentEntity studentEntity = new StudentEntity();
                studentEntity.setId("学生" + i);
                studentEntity.setBirthday(new Date());
                studentEntity.setRegistrationDate(new Date());
                studentEntity.setSex(1);
                studentEntity.setName(RandomStringUtils.random(3, "qwerttyuuio346"));
                stus.add(studentEntity);
            }
            entity.setStudents(stus);
            list.add(entity);
        }
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("2412312", "测试", "测试"),
                CourseEntity.class, list);
        FileOutputStream outputStream = new FileOutputStream(new File("/Users/ligeit/Desktop/工作簿1.xlsx"));
        workbook.write(outputStream);
//        workbook.
    }

    @Test
    public void temlate() throws IOException {

        // 查询数据,此处省略
        Workbook workbook = get();
        FileOutputStream outputStream = new FileOutputStream(new File("/Users/ligeit/Desktop/工作簿1.xlsx"));
        workbook.write(outputStream);
    }

    private Workbook get() {
        // 学生信息
        User user1 = new User("张三", "男", 20, "北京市东城区", "多个");
        User user2 = new User("李四", "男", 17, "北京市西城区", "多个");
        User user3 = new User("淑芬", "女", 34, "北京市丰台区", "多个");
        User user4 = new User("仲达", "男", 55, "北京市昌平区", "多个");
        // sheet2内容
        List<User> userList = new ArrayList<User>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        // 存放数据map
        Map<String, Object> map1 = new HashMap<>();
        map1.put("lists", userList); // 设置导出配置
        map1.put("total", 123); // 设置导出配置
        // 获取导出excel指定模版
        TemplateExportParams params = new TemplateExportParams("/Users/ligeit/Desktop/导出 2.xlsx", true);
        // 设置sheetName，若不设置该参数，则使用得原本得sheet名称
//        String[] sheetNameArray = {"sadgsad"} ;
//        params.setSheetName(sheetNameArray);
        // 导出excel
        return ExcelExportUtil.exportExcel(params, map1);

    }

    @Test
    public void exportkpi() throws IOException {
        // 查询数据,此处省略
        Workbook workbook = get2();
        FileOutputStream outputStream = new FileOutputStream(new File("/Users/ligeit/Desktop/导出.xlsx"));
        workbook.write(outputStream);
    }

    private Workbook get2() {
// 学生信息
        List<KpiDailyReport> list = new ArrayList<>();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < 5; i++) {
            KpiDailyReport dailyReport = new KpiDailyReport();
            dailyReport.setDate(new Date());
            dailyReport.setGmv(new BigDecimal(random.nextDouble()));
            dailyReport.setGmvChain(random.nextDouble());
            dailyReport.setPrevGmv(new BigDecimal(random.nextDouble()));
            dailyReport.setSaleroom(new BigDecimal(random.nextDouble()));
            dailyReport.setPrevSaleroom(new BigDecimal(random.nextDouble()));
            dailyReport.setSaleroomChain(random.nextDouble());
            dailyReport.setServiceCharge(new BigDecimal(random.nextDouble()));
            dailyReport.setPrevServiceCharge(new BigDecimal(random.nextDouble()));
            dailyReport.setServiceChargeChain(random.nextDouble());
            list.add(dailyReport);
        }

        // 存放数据map
        Map<String, Object> map1 = new HashMap<>();
        map1.put("lists", list); // 设置导出配置
        map1.put("total", 123); // 设置导出配置
        // 获取导出excel指定模版
        TemplateExportParams params = new TemplateExportParams("/Users/ligeit/Desktop/团巴拉业绩日报表.xlsx", true);
        params.setHeadingRows(4);
        // 设置sheetName，若不设置该参数，则使用得原本得sheet名称
//        String[] sheetNameArray = {"sadgsad"} ;
//        params.setSheetName(sheetNameArray);
        // 导出excel
        return ExcelExportUtil.exportExcel(params, map1);

    }

    /**
     * @throws IOException 解析类路径下面的文件，这个方法本地没问题，在正式环境会报错，因为类路径下的文件被打在jar包里面，
     *                     通过下面方法 getPath 是在文件系统路径去找，找不到
     */
    @Test
    public void resourceResolver() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("demo", ClassUtils.getDefaultClassLoader());
        String absolutePath = classPathResource.getFile().getAbsolutePath();
        System.out.println(absolutePath);
        InputStream inputStream = classPathResource.getInputStream();
        byte[] data = new byte[1024];
        inputStream.read(data);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        ByteBuffer put = byteBuffer.put(data);
        byteBuffer.flip();

        String path = classPathResource.getPath();
        System.out.println(path);
    }

    /**
     * 正式环境解析 类路径下文件
     */
    @Test
    public void resourceResolverOnMaster() throws IOException {
        ClassPathResource pathResource = new ClassPathResource("xxx", ClassUtils.getDefaultClassLoader());

//        pathResource.clo
        //把文件流写入一个临时文件里面
        Path path = Paths.get("xxdir/xxx.xlsx");
        try (InputStream inputStream = pathResource.getInputStream()) {
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        }

    }

    /**
     * 创建一个新文件，往里面写数据
     */
    @Test
    public void nioFile() throws IOException {
        String key = "dsafasg";
        Path path = Paths.get("/Users/ligeit/Documents/project/fballfans/fballfans-server/src/main/java/com/john/server/test/excel/", "sdf.pem");
        Files.createFile(path);
        Files.write(path, key.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);

    }

    /**
     * 获取所有redis  满足某个通配符匹配的 key
     */
    @Test
    public void getAllkeys() {
        RedisTemplate redisTemplate = new RedisTemplate();
        Set keys = redisTemplate.keys("dgdg*");
        redisTemplate.opsForSet().add("k1", "v1");
        redisTemplate.opsForSet().add("k1", "v2");

        redisTemplate.opsForHyperLogLog().add("hyperlog", "log1", "log2");

        redisTemplate.opsForHash().put("BRAND_DATA" + 1, "PAGE-0,SIZE-10", "pageContent");
        //        redisTemplate.opsForCluster().keys()
//        redisTemplate.opsForSet().members("ke")
        Cursor cursor = redisTemplate.opsForSet().scan("k*", ScanOptions.scanOptions().count(5).match("k*").build());
        //或者使用。。。,没见过这样用的。
        Set<Object> objects = Collections.newSetFromMap(new HashMap<>(16));
        redisTemplate.execute((RedisCallback) connetion -> {
//            connetion.keys()
            Cursor<byte[]> scan = connetion.scan(ScanOptions.scanOptions().count(Long.MAX_VALUE).match("ddxx*").build());
            scan.forEachRemaining(item -> {
                String keuy = new String(item, StandardCharsets.UTF_8);
                objects.add(keuy);
            });

            return null;
        });
    }

    @Test
    public void enumutile() {
        Map<String, FruitEnum> enumMap = EnumUtils.getEnumMap(FruitEnum.class);
        enumMap.forEach((k,v)->log.info("key={},value={}", k,v));

        List<FruitEnum> enumList = EnumUtils.getEnumList(FruitEnum.class);
        System.out.println(enumList.size());
        boolean validEnum = EnumUtils.isValidEnum(FruitEnum.class, "zsjg");
        System.out.println(validEnum);
        boolean apple = EnumUtils.isValidEnumIgnoreCase(FruitEnum.class, "APPLE");
        System.out.println("isValidEnumIgnoreCase  "+apple);
    }

    @Test
    public void lettuce() {

        RedisURI redisUri = RedisURI.Builder.redis("localhost")
                .withSsl(true)
                .withPassword("authentication")
                .withDatabase(2)
                .build();
        RedisClient client = RedisClient.create(redisUri);
    }


}
