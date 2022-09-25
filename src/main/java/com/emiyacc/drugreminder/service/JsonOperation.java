package com.emiyacc.drugreminder.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.emiyacc.drugreminder.constant.BaseConstant;
import com.emiyacc.drugreminder.dto.DrugInformation;
import com.emiyacc.drugreminder.dto.DrugInformationGroup;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @program: drugReminder
 * @description: 控制对Json文件的读写操作
 * @author: EmiyaCC
 * @create: 2022-06-10 18:45
 *  * ref:
 *  * 读取Json文件：
 *  *      https://blog.51cto.com/u_3664660/3213643
 *  *      https://blog.csdn.net/baidu_37107022/article/details/71194577
 *  * 写Json文件：
 *  *      https://www.runoob.com/w3cnote/fastjson-intro.html
 *  * 使用总结：
 *  *      https://www.jianshu.com/p/b9794f3d9862
 **/

@Service
public class JsonOperation {

    private static final String PATH = BaseConstant.FILE_PATH1;

    /**
     * 获取Json文件中全部的药品信息
     */
    public static List<DrugInformationGroup> getAllDrugInformation() {

        List<DrugInformationGroup> drugInformationGroupList = new ArrayList<>();

        String s = JsonOperation.readJsonFile(PATH);
        assert s != null;
        JSONArray jsonArray = JSONArray.parseArray(s);

        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i ++) {
                JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(i).get("drugInformation");
                String drugNameTmp = (String) jsonObject.get("drugName");
                String endTimeTmp = (String) jsonObject.get("endTime");
                drugInformationGroupList.add(new DrugInformationGroup(
                        drugNameTmp,
                        (String) jsonArray.getJSONObject(i).get("stockInTime"),
                        new DrugInformation(drugNameTmp, endTimeTmp)));
            }
        }

        return drugInformationGroupList;
    }

    /**
     * 添加药品信息到json文件中
     */
    public static Boolean addDrugInformation(String drugName, String endTime) {

        String endYear = endTime.trim().substring(0, 4);
        String endMonth = endTime.trim().substring(4, 6);
        String endDay = endTime.trim().substring(6);
        String endTimeSplice = endYear + "-" + endMonth + "-" + endDay;

        List<DrugInformationGroup> drugInformationGroupList = new ArrayList<>();
        drugInformationGroupList.add(new DrugInformationGroup(
                drugName,
                JsonOperation.getCurrentDate(),
                new DrugInformation(drugName, endTimeSplice)));

        String s = JsonOperation.readJsonFile(PATH);
        assert s != null;
        JSONArray jsonArray = JSONArray.parseArray(s);

        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i ++) {
                JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(i).get("drugInformation");
                String drugNameTmp = (String) jsonObject.get("drugName");
                if (Objects.equals(drugNameTmp, drugName)) continue;
                String endTimeTmp = (String) jsonObject.get("endTime");
                drugInformationGroupList.add(new DrugInformationGroup(
                        drugNameTmp,
                        JsonOperation.getCurrentDate(),
                        new DrugInformation(drugNameTmp, endTimeTmp)));
            }
        }

        String jsonOutput= JSON.toJSONString(drugInformationGroupList);
        return writeJsonFile(jsonOutput);
    }

    /**
     * 删除药品信息
     */
    public static Boolean deleteDrugInformation(String drugName, String stockInTime) {

        String s = JsonOperation.readJsonFile(PATH);
        assert s != null;
        JSONArray jsonArray = JSONArray.parseArray(s);
        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.get("drugName").toString().trim().equals(drugName)
                        && jsonObject.get("stockInTime").toString().trim().equals(stockInTime)) {
                    jsonArray.remove(i);
                    break;
                }
            }
        }

        String jsonOutput = JSON.toJSONString(jsonArray);
        return writeJsonFile(jsonOutput);
    }

    /**
     * 读取Json文件，返回Json串
     */
    public static String readJsonFile(String fileName) {
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(jsonFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8);
            int ch = 0;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }

            assert fileReader != null;
            fileReader.close();
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 写Json文件，返回boolean
     */
    public static Boolean writeJsonFile(String jsonOutput) {

        try {
            File file = new File(PATH);
            // 创建上级目录
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            // 如果文件存在，则删除文件
            if (file.exists()) {
                file.delete();
            }
            // 创建文件
            file.createNewFile();
            // 写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            write.write(jsonOutput);
            write.flush();
            write.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * String 转 Date
     */
    public static Date string2Date(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.parse(date);
    }

    /**
     * 获取当前的日期，格式为 yyyy-MM-dd
     */
    public static String getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dtf.format(LocalDateTime.now());
    }
}