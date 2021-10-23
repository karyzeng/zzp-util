package com.zzp.excel.utils;

import com.zzp.file.utils.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @Description 合并excel工具
 * @Author Garyzeng
 * @since 2019.10.15
 **/
public class ExcelMergeUtils {

    public static void merge(String excelFolderPath, String destinationFile, PrintSetupOption printSetupOption) throws Exception {
        File folder = new File(excelFolderPath);
        if (!folder.exists()) {
            throw new FileNotFoundException(excelFolderPath + "找不到对应目录");
        }

        if (!folder.isDirectory()) {
            throw new FileNotFoundException(excelFolderPath + "不是目录");
        }

        // 获取文件夹下的excel文件
        List<File> excelFiles = FileUtils.getFiles(folder);
        if (excelFiles == null || excelFiles.size() == 0) {
            throw new FileNotFoundException(excelFolderPath + "找不到excel文件");
        }

        // 合成后的excel文件
        HSSFWorkbook destinationExcel = new HSSFWorkbook();
        for (int i = 0; i < excelFiles.size(); i++) {
            File excelFile = excelFiles.get(i);
            InputStream in = new FileInputStream(excelFile);
            HSSFWorkbook excel = new HSSFWorkbook(in);
            for (int j = 0; j < excel.getNumberOfSheets(); j++) {
                HSSFSheet oldSheet = excel.getSheetAt(j);
                String sheetName = excelFile.getName().substring(0, excelFile.getName().lastIndexOf("."));
                HSSFSheet newSheet = destinationExcel.createSheet(sheetName);
                if (printSetupOption != null) {
                    setPrintOptions(newSheet, printSetupOption);
                }
                PoiUtils.copySheet(newSheet, oldSheet, destinationExcel, excel);
            }
        }

        FileOutputStream outputStream = new FileOutputStream(destinationFile);
        destinationExcel.write(outputStream);
        outputStream.flush();
        outputStream.close();

    }

    /**
     * 设置打印参数
     * @param newSheet
     * @param option
     */
    private static void setPrintOptions(HSSFSheet newSheet, PrintSetupOption option) {
        HSSFPrintSetup ps = newSheet.getPrintSetup();
        if (option.getLandscape() != null) {
            ps.setLandscape(option.getLandscape().booleanValue()); // 打印方向，true：横向，false：纵向
        }

        if (option.getPaperSize() != null) {
            ps.setPaperSize(option.getPaperSize().shortValue()); //纸张
        }

        if (option.getTopMargin() != null) {
            newSheet.setMargin(HSSFSheet.TopMargin, option.getTopMargin().doubleValue());// 页边距（上）
        }

        if (option.getBottomMargin() != null) {
            newSheet.setMargin(HSSFSheet.BottomMargin, option.getBottomMargin().doubleValue());// 页边距（下）
        }

        if (option.getLeftMargin() != null) {
            newSheet.setMargin(HSSFSheet.LeftMargin, option.getLeftMargin().doubleValue());// 页边距（左）
        }

        if (option.getRightMargin() != null) {
            newSheet.setMargin(HSSFSheet.RightMargin, option.getRightMargin().doubleValue());// 页边距（右）
        }

        if (option.getHeaderMargin() != null) {
            newSheet.setMargin(HSSFSheet.HeaderMargin, option.getHeaderMargin().doubleValue());// 页眉
        }

        if (option.getFooterMargin() != null) {
            newSheet.setMargin(HSSFSheet.FooterMargin, option.getFooterMargin().doubleValue());// 页脚
        }

        if (option.getHorizontallyCenter() != null) {
            newSheet.setHorizontallyCenter(option.getHorizontallyCenter().booleanValue());//设置打印页面为水平居中
        }

        if (option.getVerticallyCenter() != null) {
            newSheet.setVerticallyCenter(option.getVerticallyCenter().booleanValue());//设置打印页面为垂直居中使用POI输出Excel时打印页面的设置
        }

        if (option.getRepeatingRows() != null) {
            newSheet.setRepeatingRows(option.getRepeatingRows());// 设置打印标题
        }
    }

    public static void main(String[] args) {
        try {
            PrintSetupOption printSetupOption = new PrintSetupOption();
            printSetupOption.setLandscape(true);
            printSetupOption.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
            printSetupOption.setTopMargin(0.436);
            printSetupOption.setBottomMargin(0.52);
            printSetupOption.setLeftMargin(0.316);
            printSetupOption.setRightMargin(0.244);
            printSetupOption.setHeaderMargin(0.304);
            printSetupOption.setFooterMargin(0.304);
            printSetupOption.setHorizontallyCenter(true);
            CellRangeAddress cellRangeAddress = new CellRangeAddress(20, 20, 0, 0);
            printSetupOption.setRepeatingRows(cellRangeAddress);
            merge("E:\\scmtemp\\mergeexcel\\temp", "E:\\scmtemp\\mergeexcel\\merge.xls", printSetupOption);
            System.out.println("合并完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
