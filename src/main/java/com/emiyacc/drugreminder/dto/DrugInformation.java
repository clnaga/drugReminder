package com.emiyacc.drugreminder.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @program: drugReminder
 * @description: 单个药物信息
 * @author: EmiyaCC
 * @create: 2022-06-10 18:43
 **/

@Data
public class DrugInformation {

    @JSONField
    private String drugName;
    @JSONField
    private String endTime;

    public DrugInformation() {

    }

    public DrugInformation(String drugName , String endTime) {
        this.drugName = drugName;
        this.endTime = endTime;
    }
}