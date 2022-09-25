package com.emiyacc.drugreminder.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @program: drugReminder
 * @description: 包含过期时间的药品信息组
 * @author: EmiyaCC
 * @create: 2022-06-11 10:56
 **/

@Data
public class DrugInformationInPage {

    @JSONField
    private DrugInformationGroup drugInformationGroup;
    @JSONField
    private long expirationDays;

    public DrugInformationInPage(
            DrugInformationGroup drugInformationGroup,
            long expirationDays) {
        this.drugInformationGroup = drugInformationGroup;
        this.expirationDays = expirationDays;
    }
}