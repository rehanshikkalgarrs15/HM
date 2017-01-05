package com.example.rehanr.hmcashew.Parsers;

import com.example.rehanr.hmcashew.Models.FactoryReport;

import org.json.JSONObject;

/**
 * Created by rehan r on 05-01-2017.
 */
public class FactoryReportParser {

    String employeeCutting,employeeGrading,employeeStaff,employeeTotal,
            boiling,
            cuttingGar,cuttingTukda,cuttingTotal,cuttingTukdaPercentage,cuttingAveragePerEmployee,cuttingCostPerKG,
            gradingGar,gradingTukda,gradingTotal,gradingAveragePerEmployee,gradingCostPerKG,
            packedTinsOpStock,packedTinsNewPacked,packedTinsSale,packedTinsCloseStock,
            rcnRawCashew;
    public FactoryReport parse(JSONObject object){
        FactoryReport factoryReport = null;
        try {
            if (object.has("emp_cutting")) {
                employeeCutting = object.getString("emp_cutting");
            }
            if (object.has("emp_grading")) {
                employeeGrading = object.getString("emp_grading");
            }
            if (object.has("emp_staff")) {
                employeeStaff = object.getString("emp_staff");
            }
            if (object.has("boiling")) {
                boiling = object.getString("boiling");
            }
            if (object.has("cutting_gar")) {
                cuttingGar = object.getString("cutting_gar");
            }
            if (object.has("cutting_tukda")) {
                cuttingTukda = object.getString("cutting_tukda");
            }
            if (object.has("cutting_average_per_employee")) {
                cuttingAveragePerEmployee = object.getString("cutting_average_per_employee");
            }
            if (object.has("cutting_cost_perkg")) {
                cuttingCostPerKG = object.getString("cutting_cost_perkg");
            }
            if (object.has("grading_gar")) {
                gradingGar = object.getString("grading_gar");
            }
            if (object.has("grading_tukda")) {
                gradingTukda = object.getString("grading_tukda");
            }
            if (object.has("grading_average_per_employee")) {
                gradingAveragePerEmployee = object.getString("grading_average_per_employee");
            }
            if (object.has("grading_cost_perkg")) {
                gradingCostPerKG = object.getString("grading_cost_perkg");
            }
            if (object.has("packed_tins_opstock")) {
                packedTinsOpStock = object.getString("packed_tins_opstock");
            }
            if (object.has("packed_tins_newpacked")) {
                packedTinsNewPacked = object.getString("packed_tins_newpacked");
            }
            if (object.has("packed_tins_sale")) {
                packedTinsSale = object.getString("packed_tins_sale");
            }
            if (object.has("packed_tins_sale_or_close")) {
                packedTinsCloseStock = object.getString("packed_tins_sale_or_close");
            }
            if (object.has("rcn_raw_cashew")) {
                rcnRawCashew = object.getString("rcn_raw_cashew");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        factoryReport = new FactoryReport(employeeCutting,employeeGrading,employeeStaff,boiling,cuttingGar,cuttingTukda,cuttingTukdaPercentage,
                cuttingAveragePerEmployee,cuttingCostPerKG,gradingGar,gradingTukda,gradingAveragePerEmployee,gradingCostPerKG,
                packedTinsOpStock,packedTinsNewPacked,packedTinsSale,packedTinsCloseStock,rcnRawCashew);
        return factoryReport;
    }
}
