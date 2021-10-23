package com.zzp.excel.utils;

import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
import java.util.Set;  
  
import org.dom4j.Document;  
import org.dom4j.Element;  
import org.dom4j.io.SAXReader;

/**
 * 解析XML工具类
 *
 *
 * @author  zzp
 * @since  2019.04.17
 */
public class ParseXMLUtils {

     /**entity map对象，key:name ,value:entity的属性map集**/  
    public static Map entityMap = new HashMap();
      
    /**column map 对象，key:entityName_colName , value:column的属性map集 **/  
    public static Map columnMap = new HashMap();  
      
    /**rule map 对象，key:entityName_colName_ruleName, value: rule 的map集：找到一行rule**/  
    public static Map ruleMap = new HashMap() ;  
      
    /**rules map 对象, key:entityName_colName, value: rules 的map集:找到该column下所有的rule**/  
    public static Map  columnRulesMap = new HashMap() ;  
      
    /**entity--column map: key:entityName, value: column list:根据实体类名得到所有的列**/  
    public static Map columnListMap = new HashMap() ;  
      
   /**column list**/  
    public static List columnList = new ArrayList() ;  
    
    static{
    	try {
	    	SAXReader reader = new SAXReader();                  
	        Document doc = reader.read(ParseXMLUtils.class.getClassLoader().getResourceAsStream("templates.xml"));
	        Element root = doc.getRootElement();      
	        Iterator itEntity = root.elements("entity").iterator();  
	        while(itEntity.hasNext()){  
	            Element entity = (Element) itEntity.next();  
	            parseEntity(entity);
	        }
    	}catch(Exception e){  
            e.printStackTrace();  
        } 
    }
       
    /**开始解析xml文件**/  
    public ParseXMLUtils(File xmlFilePath){
        FileInputStream in = null;  
        try {  
            if(xmlFilePath == null){  
                 throw new FileNotFoundException();  
            }  
            SAXReader reader = new SAXReader();                  
            in = new FileInputStream(xmlFilePath);                
            Document doc = reader.read(in);  
            Element root = doc.getRootElement();      
            Iterator itEntity = root.elements("entity").iterator();  
            while(itEntity.hasNext()){  
                Element entity = (Element) itEntity.next();  
                parseEntity(entity);  
            }  
              
            /**测试entityMap 是否正确  
            Map enMap = (Map) this.getEntityMap().get("用户表");  
            Set<?> set = enMap.keySet();  
            Iterator it = set.iterator();  
            while(it.hasNext()){  
                String uu = (String) it.next();  
                System.out.println("entity properties:"+uu+" = "+enMap.get(uu));  
            }  
              **/
/*          *//**测试column list是否正确**//* 
            List colList = (List) this.getColumnListMap().get("用户表"); 
            System.out.println("column size:"+colList.size()); 
             
            *//**测试columnMap是否正确**//* 
            Map colMap = (Map) this.getColumnMap().get("用户表_员工号");                       
                Set<?> coListSet = colMap.keySet(); 
                Iterator coListIt = coListSet.iterator(); 
                while(coListIt.hasNext()){ 
                    String coListKey = (String) coListIt.next(); 
                    System.out.println("column  properties: "+coListKey+" = "+colMap.get(coListKey)); 
                }        
            *//**测试ruleMap是否正确**//*  
            if(this.getColumnRulesMap() != null){ 
                List rulesValidList = (List) this.getColumnRulesMap().get("用户表_员工号");                
                for(int i=0;i<rulesValidList.size(); i++){ 
                    Map colRuleMap = (Map) rulesValidList.get(i); 
                    String ruleName = (String) colRuleMap.get("name"); 
                    Map ruleMa = (Map) this.getRuleMap().get("用户表_员工号_"+ruleName); //eg: 用户表_用户名_nullable 
                    String mess = (String) ruleMa.get("message"); 
                    System.out.println("Validate Rules"+i+" : "+mess);                           
                }  
            }*/  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
          
    }  
      
     /**开始解析entity**/  
    @SuppressWarnings("unchecked")  
    public static void parseEntity(Element entity){  
        if(entity != null){  
              
            /**对数据进行初始化设置  
            columnListMap = new HashMap();  
            columnMap = new HashMap();             
            ruleMap = new HashMap();  
            columnRulesMap = new HashMap();  
             **/ 
        	columnList = new ArrayList(); 
            setEntityMap(entity);             
            String entityName = entity.attributeValue("name");  
            Iterator itColumn = entity.elements("column").iterator();  
            while(itColumn.hasNext()){  
                Element column = (Element) itColumn.next();  
                setColumnMap(entityName,column);  
            }  
            columnListMap.put(entityName, columnList);
            
            
        }  
    }  
       
      
      
    /**将entity放入entityMap中**/  
    @SuppressWarnings("unchecked")  
    public static void setEntityMap(Element entity){         
        Map ent = new HashMap();  
        String name = entity.attributeValue("name");  
        String code = entity.attributeValue("code");  
        ent.put("name", name);  
        ent.put("code", code);  
        entityMap.put(name, ent);             
    }  
      
    /**将column放入columnMap中**/  
    @SuppressWarnings("unchecked")  
    public static void setColumnMap(String entityName,Element column){  
        if(column != null){       
            Map col = new HashMap();  
            String name = column.attributeValue("name");  
            String code = column.attributeValue("code");  
            String type = column.attributeValue("type");
            String valuechange = column.attributeValue("valuechange");
            String list = column.attributeValue("list");
            String defaultproperties = column.attributeValue("defaultproperties");
            col.put("name", name);  
            col.put("code", code);  
            col.put("type", type);  
            col.put("valuechange", valuechange);  
            col.put("list", list);  
            col.put("defaultproperties", defaultproperties);  
            String columnMapKey = entityName+"_"+name;    //eg:  用户表_用户名  
            columnMap.put(columnMapKey, col);         
            columnList.add(col);  
            Iterator ruleIt = column.elements("rules").iterator();  //获得rules  
            while(ruleIt.hasNext()){  
                Element rules = (Element)ruleIt.next();   
                Iterator rule  = rules.elements("rule").iterator();   //获得 rule  
                while(rule.hasNext()){  
                    Element ruleValid = (Element) rule.next();     //获得每一行rule  
                    setRuleMap(entityName,name,ruleValid);                    
                }  
            }  
        }  
    }  
          
    /**将 rule 验证规则放入ruleMap中**/  
    @SuppressWarnings("unchecked")  
    public static void setRuleMap(String entityName,String columnName,Element ruleValid){  
        if(ruleValid != null){            
            String ruleName = ruleValid.attributeValue("name");  
            String ruleMsg = ruleValid.attributeValue("message");  
            String value = ruleValid.attributeValue("value");  
            String condition = ruleValid.attributeValue("condition");  
            String min = ruleValid.attributeValue("min");  
            String max = ruleValid.attributeValue("max");  
            Map ruleValidMap = new HashMap();  
            ruleValidMap.put("name", ruleName);  
            ruleValidMap.put("message", ruleMsg);
            ruleValidMap.put("value", value);
            ruleValidMap.put("condition", condition);
            ruleValidMap.put("min", min==null?0:Integer.valueOf(min));
            ruleValidMap.put("max", max==null?0:Integer.valueOf(max));
            
            String ruleStrKey = entityName+"_"+columnName+"_"+ruleName;  
            String colStrKey = entityName+"_"+columnName;  
            if(columnRulesMap.containsKey(colStrKey)){  
                List valids = (List) columnRulesMap.get(colStrKey);  
                valids.add(ruleValidMap);  
            }else{  
                List valids = new ArrayList();  
                valids.add(ruleValidMap);  
                columnRulesMap.put(colStrKey, valids);  //将每个column下的所有rules存入该map中  
            }  
            ruleMap.put(ruleStrKey, ruleValidMap); //将每个column下的一条rule存入该map中  
        }  
    }
    
    public static void loadTemplate(){
        System.out.println("=============Excel的导入配置模板加载完成=============");
    }
      
    /**主方法**/  
    public static void main(String[] args) {  
    	/*URL url = ParseXMLUtil.class.getClassLoader().getResource("templates.xml");
        File file = new File(url.getFile());  
        ParseXMLUtil parseXMLUtil = new ParseXMLUtil(file);*/
        Map enMap = (Map) ParseXMLUtils.entityMap.get("空运");
        Set<?> set = enMap.keySet();
        Iterator it = set.iterator();
        while(it.hasNext()){
            String uu = (String) it.next();
            System.out.println("entity properties:"+uu+" = "+enMap.get(uu));
        }

        System.out.println(ParseXMLUtils.class.getClassLoader().getResource("templates.xml"));
    }  
  
     
  
}  