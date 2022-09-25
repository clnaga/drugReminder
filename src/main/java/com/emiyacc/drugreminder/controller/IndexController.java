package com.emiyacc.drugreminder.controller;

import com.emiyacc.drugreminder.constant.BaseConstant;
import com.emiyacc.drugreminder.dto.DrugInformationInPage;
import com.emiyacc.drugreminder.service.DrugOperation;
import com.emiyacc.drugreminder.service.JsonOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * @program: drugReminder
 * @description: 管理页面访问
 * @author: EmiyaCC
 * @create: 2022-06-10 18:42
 **/

@Controller
public class IndexController {

    private final DrugOperation drugOperation;

    public IndexController(DrugOperation drugOperation) {
        this.drugOperation = drugOperation;
    }

    @GetMapping("/")
    public String index(
            Model model
    ) throws ParseException {
        List<DrugInformationInPage> drugInformationInPageList = drugOperation.getAllDrugExpirationInformation();
        model.addAttribute(BaseConstant.DRUG_INFORMATION_IN_PAGE_LIST, drugInformationInPageList);
        model.addAttribute("isShowAllDrug", true);
        return BaseConstant.INDEX_DEFINITION;
    }

    @GetMapping("/showAllDrug")
    public String showAllDrugInformation(
            Model model
    ) throws ParseException {
        List<DrugInformationInPage> drugInformationInPageList = drugOperation.getAllDrugInformation(Boolean.FALSE);
        model.addAttribute(BaseConstant.DRUG_INFORMATION_IN_PAGE_LIST, drugInformationInPageList);
        model.addAttribute("isShowAllDrug", false);
        return BaseConstant.INDEX_DEFINITION;
    }

    @RequestMapping(value = "/result", method = {RequestMethod.GET, RequestMethod.POST})
    public String result(
            Model model
    ) {
        return BaseConstant.REDIRECT_DEFINITION;
    }

    @PostMapping("/addDrug")
    public String addDrugInformation(
            Model model,
            @RequestParam(value = "drugName", defaultValue = "null") String drugName,
            @RequestParam(value = "endTime", defaultValue = "null") String endTime
    ) {

        boolean writeJsonFlag = JsonOperation.addDrugInformation(drugName, endTime);
        if (writeJsonFlag) {
            model.addAttribute("msg", "添加药品信息成果");
        } else {
            model.addAttribute("msg", "信息添加失败，请重试");
        }
        return BaseConstant.RESULT_DEFINITION;
    }

    @PostMapping("/edit")
    public String edit(
            Model model,
            @RequestParam(value = "drugName", defaultValue = "null") String drugName,
            @RequestParam(value = "stockInTime", defaultValue = "null") String stockInTime
    ) {
        Boolean deleteDrugFlag = JsonOperation.deleteDrugInformation(drugName, stockInTime);
        if (Boolean.TRUE.equals(deleteDrugFlag)) {
            return BaseConstant.REDIRECT_DEFINITION;
        } else {
            model.addAttribute("msg", "信息删除失败，请重试");
            return BaseConstant.RESULT_DEFINITION;
        }
    }
}