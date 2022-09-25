package com.emiyacc.drugreminder.service;

import com.emiyacc.drugreminder.dto.DrugInformationGroup;
import com.emiyacc.drugreminder.dto.DrugInformationInPage;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: drugReminder
 * @description: 对药物信息的操作
 * @author: EmiyaCC
 * @create: 2022-06-10 18:45
 **/

@Service
public class DrugOperation {

    /**
     * 获取快要过期（有效期<10天）的所有药品信息
     * ref:
     * 两个日期相减：
     *      https://blog.csdn.net/u013305864/article/details/52385888
     */
    public List<DrugInformationInPage> getAllDrugExpirationInformation() throws ParseException {
        return this.getAllDrugInformation(Boolean.TRUE);
    }

    public List<DrugInformationInPage> getAllDrugInformation(Boolean isFilterExpire) throws ParseException {
        List<DrugInformationInPage> drugInformationInPageList = new ArrayList<>();

        List<DrugInformationGroup> drugInformationGroupList = JsonOperation.getAllDrugInformation();
        for (DrugInformationGroup drugInformationGroup : drugInformationGroupList) {
            String drugEndTime = drugInformationGroup.getDrugInformation().getEndTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date nowTime = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            long diff = JsonOperation.string2Date(drugEndTime).getTime() - nowTime.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (Boolean.TRUE.equals(isFilterExpire)) {
                if (diffDays < 10) {
                    drugInformationInPageList.add(new DrugInformationInPage(drugInformationGroup, diffDays));
                }
            } else {
                drugInformationInPageList.add(new DrugInformationInPage(drugInformationGroup, diffDays));
            }
        }
        return drugInformationInPageList;
    }
}