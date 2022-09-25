package com.emiyacc.drugreminder.dto;

import lombok.Data;

/**
 * @program: drugReminder
 * @description: 保存到Json文本的药物信息
 * @author: EmiyaCC
 * @create: 2022-06-10 18:44
 **/

@Data
public class DrugInformationGroup {

    private String drugName;
    private String stockInTime;
    private DrugInformation drugInformation;

    public DrugInformationGroup(String drugName, String stockInTime, DrugInformation drugInformation) {
        this.drugName = drugName;
        this.stockInTime = stockInTime;
        this.drugInformation = drugInformation;
    }
}