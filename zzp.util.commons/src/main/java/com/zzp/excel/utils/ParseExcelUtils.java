package com.zzp.excel.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.houbb.paradise.common.util.StringUtil;
import com.zzp.date.utils.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 解析excel工具类
 *
 * @author  zzp
 * @since  2019.04.17
 */
public class ParseExcelUtils {
      
	private  FileInputStream fis ;  
	private XSSFWorkbook workBook;  
	private XSSFSheet sheet;  
	private StringBuffer errorString;  
      
    /**当前实体类的code**/  
	private String curEntityCode;  
    /**表头map对象：key:entityCode, value:headMap(index,headTitle)**/  
	private Map curEntityHeadMap ;  
      
    /**字段的必填：key:entityCode+headTitle, value:true(必填),false(不必填)**/  
	//private Map curEntityColRequired;  
      
    /**存放每一行的数据**/  
	private  List listDatas ;
    /** 标题行号 */
    private int titleRowNum;
    /** 数据行号 */
    private int dataRowNum;
    
    private String entityName;

    /**
     *
     * @param fis 导入文件输入流
     * @param titleRowNum 导入模板标题行号
     * @param dataRowNum 导入模板数据行号
     */
    public ParseExcelUtils(InputStream fis, int titleRowNum, int dataRowNum){
        try {  
            this.workBook = new XSSFWorkbook(fis);
        	this.errorString = new StringBuffer();
            this.titleRowNum = titleRowNum;
            this.dataRowNum = dataRowNum;
            readExcelData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  
        }catch (IOException e) {  
            e.printStackTrace();  
        }     
    }

    /**
     *
     * @param fis 导入文件输入流
     * @param titleRowNum 导入模板标题行号
     * @param dataRowNum 导入模板数据行号
     * @param entityName 导入模板的模板配置名称
     */
    public ParseExcelUtils(InputStream fis, int titleRowNum, int dataRowNum, String entityName){
        try {  
        	this.workBook = new XSSFWorkbook(fis);
        	this.errorString = new StringBuffer();
            this.titleRowNum=titleRowNum;
            this.dataRowNum = dataRowNum;
            this.entityName = entityName;
            readExcelData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  
        }catch (IOException e) {  
            e.printStackTrace();  
        }     
    }

    /**
     *
     * @param fis 导入文件输入流
     * @param titleRowNum 导入模板标题行号
     * @param dataRowNum 导入模板数据行号
     * @param entityName 导入模板的模板配置名称
     * @param sheetIndex sheet的下标
     */
    public ParseExcelUtils(InputStream fis, int titleRowNum, int dataRowNum, String entityName, int sheetIndex){
        try {  
        	this.workBook = new XSSFWorkbook(fis);  
        	this.errorString = new StringBuffer();
            this.titleRowNum=titleRowNum;
            this.dataRowNum = dataRowNum;
            this.entityName = entityName;
            readExcelData(sheetIndex);  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        }catch (IOException e) {  
            e.printStackTrace();  
        }     
    }
      
      
    /**开始从excel读取数据**/    
    public void readExcelData(){  
        int sheetSize = workBook.getNumberOfSheets();  
        for(int i=0;i<sheetSize;i++){  
            sheet = workBook.getSheetAt(i);  
            String sheetEntityName = workBook.getSheetName(i);
            if(StringUtil.isNotBlank(entityName) && !entityName.equals(sheetEntityName)){
            	errorString.append("请选择正确的模板进行导入！<br>");
                return; 
            }
            readSheetData(sheet,sheetEntityName);  
        }  
          
    }  
    
    /**
     * 重载readExcelData方法
     * 
     * @param sheetIndex
     */
    public void readExcelData(int sheetIndex){  
    	sheet = workBook.getSheetAt(sheetIndex);  
        String sheetEntityName = workBook.getSheetName(sheetIndex);
        if(StringUtil.isNotBlank(entityName) && !entityName.equals(sheetEntityName)){
        	errorString.append("请选择正确的模板进行导入！<br>");
            return; 
        }
        readSheetData(sheet,sheetEntityName);
    }  
      
    /**读每个sheet页的数据**/  
    public void readSheetData(XSSFSheet sheet,String entityName){  
          
           int rowNumbers = sheet.getLastRowNum();  
           Map ent = (Map) ParseXMLUtils.entityMap.get(entityName);
           if(ent == null){
        	   errorString.append("excel中sheet名称不正确！<br>");
               return; 
           }
           this.setCurEntityCode((String) ent.get("code"));  
           if(rowNumbers < dataRowNum){  
               errorString.append("excel无可导入数据,请检查<br>");
               return;
           }            
	       List colList = (List) ParseXMLUtils.columnListMap.get(entityName);
	       int xmlRowNum = colList.size();  
           XSSFRow excelRow = sheet.getRow(titleRowNum);  
           int excelFirstRow = excelRow.getFirstCellNum();  
           int excelLastRow = excelRow.getLastCellNum();  
           if(xmlRowNum  != (excelLastRow-excelFirstRow)){  
                 errorString.append("系统配置xml文件列数与excel列数不相符，请检查<br>");
                 return;
           }
           readSheetHeadData(sheet,entityName);  
           if(errorString.length() > 0){
        	   return;
           }
           readSheetColumnData(sheet,entityName);  
             
           
                 
       }  
      
    /**读取sheet页中的表头信息**/
    @SuppressWarnings({ "unchecked", "static-access"})
    public void readSheetHeadData(XSSFSheet sheet,String entityName){  
         
           Map headMap = new HashMap();  
           curEntityHeadMap = new HashMap();  
           //curEntityColRequired = new HashMap();  
           XSSFRow excelheadRow = sheet.getRow(titleRowNum);  
           int excelLastRow = excelheadRow.getLastCellNum();  
           String headTitle = "";
           List colList = (List) ParseXMLUtils.columnListMap.get(entityName);
           for(int i=0;i<excelLastRow;i++){  
        	   XSSFCell cell = excelheadRow.getCell(i);  
               headTitle = this.getStringCellValue(cell).trim();  
               /*if(headTitle.endsWith("*")){  
                   curEntityColRequired.put(this.getCurEntityCode()+"_"+headTitle,true);  
               }else{  
                   curEntityColRequired.put(this.getCurEntityCode()+"_"+headTitle,false);  
               } */ 
               headMap.put(i, headTitle);
               Map<String,Object> tempTitle = (Map<String, Object>) colList.get(i);
               if(!headTitle.equals(tempTitle.get("name"))){
            	   errorString.append("excel格式不正确，请参照相应模板<br>");
                   return; 
               }
           }  
           curEntityHeadMap.put(this.getCurEntityCode(), headMap);  
       }  
     
    /**读取sheet页里面的数据**/
    @SuppressWarnings({ "unchecked", "static-access" })
    public void readSheetColumnData(XSSFSheet sheet,String entityName){
         
	   XSSFRow excelheadRow = sheet.getRow(dataRowNum);  
       int excelLastcell = excelheadRow.getLastCellNum();   //excel总列数  
       int excelRowNum = sheet.getLastRowNum();  //excel总行数  
       Map headMap = (Map) this.getCurEntityHeadMap().get(this.getCurEntityCode());      
       Map colMap = ParseXMLUtils.columnMap;
       listDatas =new ArrayList();  
         
       for(int i=dataRowNum;i<excelRowNum+1;i++){//行循环   
    	   boolean rowValidate = true;
    	   XSSFRow columnRow = sheet.getRow(i);   
           if(columnRow != null){  
               Map curRowCellMap = new HashMap();  
               Map curRowCellMapSource = new HashMap();
               if(excelLastcell < headMap.size()){
            	   excelLastcell = headMap.size();
               }
               for(int j =0; j<excelLastcell;j++){ //列循环 
            	   System.out.println("xxxxxxxxxxxxxxxx=" + i + ",j=" + j);
            	   String value = null;
                   String headTitle ="";  
                   headTitle = headMap.get(j).toString();                        
                   Map curColMap =  (Map) colMap.get(entityName+"_"+headTitle);  
                   String curColCode = (String) curColMap.get("code");  
                   String curColType = (String) curColMap.get("type");  
                   String valuechange = (String) curColMap.get("valuechange");  
                   String collection = (String) curColMap.get("list");
                   String defaultproperties = (String) curColMap.get("defaultproperties");
                   String xmlColType = (String) curColMap.get("type");
                   XSSFCell colCell = columnRow.getCell(j);
                   if(xmlColType.equals("Date2")){
                	   value = this.getDate2CellValue(colCell);
                   }else{
                	   value =this.getStringCellValue(colCell);
                   }
                   
                   if(value != null){  
                       value = value.trim();  
                   }                
                   
                   curRowCellMapSource.put(curColCode, value);
                   /**验证cell数据**/  
                   boolean validate = validateCellData(i+1,j+1,colCell,entityName,headTitle,curColType,curRowCellMapSource);
                   if (rowValidate && !validate) {
                	   rowValidate = false;
                   }
                   if(value != null && validate && StringUtil.isNotBlank(value)){
                	   if(StringUtil.isNotBlank(valuechange)){
                		   value = changeVal(valuechange,value);
                	   }
                	   Map collectionMap = null;
                	   List<Map> collectionList = null;
                	   if(StringUtil.isNotBlank(collection)){
                		   collectionList = (List) curRowCellMap.get(collection);
                		   if(collectionList == null){
                			   collectionList = new ArrayList();
                			   curRowCellMap.put(collection, collectionList);
                		   }
                		   collectionMap = new HashMap();
                		   collectionList.add(collectionMap);
                		   if(StringUtil.isNotBlank(defaultproperties)){
                			   String[] props = defaultproperties.split("/");
                			   for(String prop:props){
                				   String[] keyValue = prop.split("=");
                				   collectionMap.put(keyValue[0], keyValue[1]);
                			   }
                		   }
                	   }
	                   if(xmlColType.equals("int")){
	                       int intVal = Integer.valueOf(value);
	                       if(StringUtil.isNotBlank(collection)){
	                    	   collectionMap.put(curColCode, intVal);
	                       }else{
	                    	   curRowCellMap.put(curColCode, intVal);  //将这一行的数据以code-value的形式存入map
	                       }
	                   }else if(xmlColType.equals("Double")){
	                	   if (StringUtil.isNotBlank(value)) {
	                		   Double doubleVal = Double.valueOf(value);
	                		   if(StringUtil.isNotBlank(collection)){
		                    	   collectionMap.put(curColCode, doubleVal);
		                       }else{
		                    	   curRowCellMap.put(curColCode, doubleVal);  //将这一行的数据以code-value的形式存入map
		                       }
	                	   }
	                	   
	                   }else if(xmlColType.equals("Date")){
	                	   Date date = DateUtils.convertStringToDate(value, "yyyy-MM-dd");
	                	   if(StringUtil.isNotBlank(collection)){
	                    	   collectionMap.put(curColCode, date);
	                       }else{
	                    	   curRowCellMap.put(curColCode, date);  //将这一行的数据以code-value的形式存入map
	                       }
	                   }else if(xmlColType.equals("Date2")){
	                	   Date date = DateUtils.convertStringToDate(value, "yyyy-MM-dd HH:mm:ss");
	                	   if(StringUtil.isNotBlank(collection)){
	                    	   collectionMap.put(curColCode, date);
	                       }else{
	                    	   curRowCellMap.put(curColCode, date);  //将这一行的数据以code-value的形式存入map
	                       }
	                   }else{
	                	   if(StringUtil.isNotBlank(collection)){
	                		   putCollectionData(curColCode,value,collectionMap,collectionList);
	                    	   
	                       }else{
	                    	   curRowCellMap.put(curColCode, value); 
	                       }
	                        
	                   }
                   }
               }  
               listDatas.add(curRowCellMap);  
           }  
           if (!rowValidate) {
        	   errorString.append("<br>");
           }
       }  
   }  
   

    private void putCollectionData(String curColCode, String value, Map collectionMap, List<Map> collectionList) {
	   if(value.contains("/")){
		   String[] vals = value.split("/");
		   collectionMap.put(curColCode, vals[0]);
		   for(int i=0; i<vals.length; i++){
			   if(i > 0){
				   collectionMap = new HashMap();
				   collectionMap.put(curColCode, vals[i]);
        		   collectionList.add(collectionMap);
			   }
		   }
	   }else{
		   collectionMap.put(curColCode, value);
	   }
   }


    private String changeVal(String valuechange, String value) {
        String[] changes = valuechange.split("/");
        for(String str : changes){
            if(str.contains(value)){
                return str.split("-")[1];
            }
        }
        return null;
    }


    /**验证单元格数据
     * @return **/
       @SuppressWarnings("static-access")
    public boolean validateCellData(int curRow,int curCol,XSSFCell colCell,String entityName,String headName,String curColType,Map curRowCellMapSource){
       boolean result = true;  
       List rulList = (List) ParseXMLUtils.columnRulesMap.get(entityName+"_"+headName);
       if(rulList != null && rulList.size()>0){ 
           for(int i=0 ; i<rulList.size() ; i++){  
               Map rulM = (Map) rulList.get(i);  
               String rulName = (String) rulM.get("name");  
               String rulMsg = (String) rulM.get("message");  
               String value = (String) rulM.get("value");  
               String condition = (String) rulM.get("condition");  
               int min = (Integer) rulM.get("min");  
               int max = (Integer) rulM.get("max");  
               String cellValue = "";
               
               if(rulName.equals("date2able")){
            	   cellValue = this.getDate2CellValue(colCell);
               }else{
            	   cellValue = this.getStringCellValue(colCell);
               }
               if(cellValue != null){
            	   cellValue = cellValue.trim();
               }
               
               String msg = "第"+curRow+"行,第"+curCol+"列:"+(rulMsg==null?"":rulMsg)+"<br>";
               if(rulName.equals("nullable")){ // 必填           
                   if(StringUtil.isBlank(cellValue)){  
                       errorString.append(msg);
                       result = false;
                   }  
               }else if(rulName.equals("valueable")){ // 值校验
            	   if(StringUtil.isNotBlank(cellValue)){
            		   String[] values = cellValue.split("/");
            		   for(String str : values){
            			   if(!value.contains(str)){
            				   errorString.append(msg);
                    		   result = false;
            			   }
            		   }
            	   }
               }else if(rulName.equals("ifnullable")) { // 通过其他列值判断当前列是否必填 
            	   String val = (String) curRowCellMapSource.get(condition);
            	   if(StringUtil.isNotBlank(val) && val.equals(value) && StringUtil.isBlank(cellValue)){
            		   errorString.append(msg);
            		   result = false;
            	   }
               }else if(rulName.equals("numbleable")) { // 数字 
            	   String regex = "^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$"; 
            	   if(StringUtil.isNotBlank(cellValue) && !cellValue.matches(regex)){
            		   errorString.append(msg);
            		   result = false;
            	   } 
               }else if(rulName.equals("dateable")) { // 时间 yyyy-MM-dd
            	   if(StringUtil.isNotBlank(cellValue) && !cellValue.matches(DAYPATTERN)){
            		   errorString.append(msg);
            		   result = false;
            	   }
               }else if(rulName.equals("date2able")){ // 时间 yyyy-MM-dd HH:mm:ss 
            	   if(StringUtil.isNotBlank(cellValue) && !cellValue.matches(DATE_VALIDATE)){
            		   errorString.append(msg);
            		   result = false;
            	   }
               }else if(rulName.equals("lengthable")) { // 长度 
            	   int length = 0;
            	   if(StringUtil.isNotBlank(cellValue)){
            		   length = cellValue.length();
            	   }
            	   if(length < Integer.valueOf(min) || length > Integer.valueOf(max)){
            		   errorString.append(msg); 
            		   result = false;
            	   }
               } else if (rulName.equals("loginName")) {//登录名，只允许字母、数字、下划线
                   String regex = "^[_a-zA-Z0-9]+$";
                   if(StringUtil.isNotBlank(cellValue) && !cellValue.matches(regex)){
                       errorString.append(msg);
                       result = false;
                   }
               } else if (rulName.equals("email")) {//匹配email
                   String regex = "^[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?$";
                   if(StringUtil.isNotBlank(cellValue) && !cellValue.matches(regex)){
                       errorString.append(msg);
                       result = false;
                   }
               } else if (rulName.equals("phone")) {//匹配手机号码
                   String regex = "^0?(13|14|15|17|18|19)[0-9]{9}$";
                   if(StringUtil.isNotBlank(cellValue) && !cellValue.matches(regex)){
                       errorString.append(msg);
                       result = false;
                   }
               }
           }  
       }
       return result;
   }  
  
   private static final String JANPATTERN = "(0?[13578]|1[02])-(0?[1-9]|[12][0-9]|3[01])";  
   private static final String FEBPATTERN = "0?2-(0?[1-9]|[12][0-9])";  
   private static final String APRPATTERN = "(0?[469]|11)-(0?[1-9]|[12][0-9]|30)";  
   private static final String DAYPATTERN = String.format("^2[0-9]{3}-(%s|%s|%s)$", JANPATTERN, FEBPATTERN, APRPATTERN);
   private static final String DATE_VALIDATE = "((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
   
   /** 
     * 获得单元格字符串 
     * @throws
     */  
    public static String getStringCellValue(XSSFCell cell) {  
        if (cell == null){  
            return null;  
        }  
  
        String result = "";  
        switch (cell.getCellType()) {  
            case HSSFCell.CELL_TYPE_BOOLEAN:  
                result = String.valueOf(cell.getBooleanCellValue());  
                break;  
            case HSSFCell.CELL_TYPE_NUMERIC:  
                if (HSSFDateUtil.isCellDateFormatted(cell)) {  
                    java.text.SimpleDateFormat TIME_FORMATTER = new java.text.SimpleDateFormat(  
                            "yyyy-MM-dd");
                    result = TIME_FORMATTER.format(cell.getDateCellValue());
                }  
                else{  
                	BigDecimal big=new BigDecimal(cell.getNumericCellValue());
                	result = big.toString();
      				//解决1234.0  去掉后面的.0
      				if(null!=result&&!"".equals(result.trim())){
      				     String[] item = result.split("[.]");
      				     if(1<item.length&&"0".equals(item[1])){
      				    	result=item[0];
      				     }
      				}
                }  
                break;  
            case HSSFCell.CELL_TYPE_STRING:  
                if (cell.getRichStringCellValue() == null){  
                    result = null;  
                }  
                else{  
                    result = cell.getRichStringCellValue().getString().trim();  
                }  
                break;  
            case HSSFCell.CELL_TYPE_BLANK:  
                result = null;  
                break;  
            case HSSFCell.CELL_TYPE_FORMULA:  
                try{  
                    result = String.valueOf(cell.getNumericCellValue());     
                }catch(Exception e){  
                    result = cell.getRichStringCellValue().getString();  
                }  
                break;  
            default:  
                result = "";  
        }  
          
        return result;  
    }  
    
    /**
     * 格式化类型为Date2的值, 精确到时分秒  
     * @param cell
     * @return
     */
    private String getDate2CellValue(XSSFCell cell) {
 	   
    	if (cell == null){  
            return null;  
        }  
        
        String result = "";  
        switch (cell.getCellType()) {  
            case HSSFCell.CELL_TYPE_BOOLEAN:
                result = String.valueOf(cell.getBooleanCellValue());  
                break;  
            case HSSFCell.CELL_TYPE_NUMERIC:  
                if (HSSFDateUtil.isCellDateFormatted(cell)) {  
                    java.text.SimpleDateFormat TIME_FORMATTER = new java.text.SimpleDateFormat(  
                            "yyyy-MM-dd HH:mm:ss");
                    result = TIME_FORMATTER.format(cell.getDateCellValue());
                }  
                else{  
                    double doubleValue = cell.getNumericCellValue();  
                    result = "" + doubleValue;  
                }  
                break;  
            case HSSFCell.CELL_TYPE_STRING:  
                if (cell.getRichStringCellValue() == null){  
                    result = null;  
                }  
                else{  
                    result = cell.getRichStringCellValue().getString().trim();  
                }  
                break;  
            case HSSFCell.CELL_TYPE_BLANK:  
                result = null;  
                break;  
            case HSSFCell.CELL_TYPE_FORMULA:  
                try{  
                    result = String.valueOf(cell.getNumericCellValue());     
                }catch(Exception e){  
                    result = cell.getRichStringCellValue().getString();  
                }  
                break;  
            default:  
                result = "";  
        }  
          
        return result; 
    }
    
    
    /**主方法**/  
    public static void main(String[] args) {
        ParseExcelUtils excel =  new ParseExcelUtils(ParseExcelUtils.class.getClassLoader().getResourceAsStream("test.xlsx"),1,5);
        if(excel.getErrorString().length() ==0){//如果没有任何错误，就保存  
            //saveExcelData(entityName);  
            System.out.println("导入数据成功！");  
        }else{  
            //清理所有的缓存clearMap();现在暂时未清理  
            String[] strArr = excel.getErrorString().toString().split("<br>");  
            for(String s: strArr){  
                System.out.println(s);  
            }  
              
        }   
    }  
  
      
      
    public String getCurEntityCode() {  
        return curEntityCode;  
    }  
    public void setCurEntityCode(String curEntityCode) {  
        this.curEntityCode = curEntityCode;  
    }  
    public Map getCurEntityHeadMap() {  
        return curEntityHeadMap;  
    }  
    public void setCurEntityHeadMap(Map curEntityHeadMap) {  
        this.curEntityHeadMap = curEntityHeadMap;  
    }  
    /*public Map getCurEntityColRequired() {  
        return curEntityColRequired;  
    }  
    public void setCurEntityColRequired(Map curEntityColRequired) {  
        this.curEntityColRequired = curEntityColRequired;  
    } */ 
    public List getListDatas() {  
        return listDatas;  
    }  
    public void setListDatas(List listDatas) {  
        this.listDatas = listDatas;  
    }  
    public StringBuffer getErrorString() {  
        return errorString;  
    }  
    public void setErrorString(StringBuffer errorString) {  
        this.errorString = errorString;  
    }


	public FileInputStream getFis() {
		return fis;
	}


	public void setFis(FileInputStream fis) {
		this.fis = fis;
	}


	public XSSFWorkbook getWorkBook() {
		return workBook;
	}


	public void setWorkBook(XSSFWorkbook workBook) {
		this.workBook = workBook;
	}


	public XSSFSheet getSheet() {
		return sheet;
	}


	public void setSheet(XSSFSheet sheet) {
		this.sheet = sheet;
	}


	public int getTitleRowNum() {
		return titleRowNum;
	}


	public void setTitleRowNum(int titleRowNum) {
		this.titleRowNum = titleRowNum;
	}


	public int getDataRowNum() {
		return dataRowNum;
	}


	public void setDataRowNum(int dataRowNum) {
		this.dataRowNum = dataRowNum;
	}  
  
  
}  