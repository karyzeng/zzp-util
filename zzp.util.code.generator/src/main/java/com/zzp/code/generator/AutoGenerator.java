package com.zzp.code.generator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * @Description 代码生成器
 * @Author karyzeng
 * @since 2021.07.23
 **/
public class AutoGenerator {

    private static final Logger logger = LoggerFactory.getLogger(AutoGenerator.class);

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public void general() {
        Properties prop = this.getProperties();

        // 代码生成器
        com.baomidou.mybatisplus.generator.AutoGenerator mpg = new com.baomidou.mybatisplus.generator.AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        final String projectPath = this.getCodePath(prop);
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(prop.getProperty("author"));
        gc.setOpen(false);
        gc.setSwagger2(false);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(prop.getProperty("datasource.url"));
        dsc.setDriverName(prop.getProperty("datasource.driver-class-name"));
        dsc.setUsername(prop.getProperty("datasource.username"));
        dsc.setPassword(prop.getProperty("datasource.password"));
        mpg.setDataSource(dsc);

        // 包配置
        final PackageConfig pc = new PackageConfig();
        pc.setModuleName(scanner("模块"));
        String packageParent = prop.getProperty("packageParent");
        String packageParentPath = packageParent.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
        pc.setParent(packageParent);
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
//                Map<String, Object> map = new HashMap<String, Object>();

            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        // 自定义domain配置
        focList.add(new FileOutConfig("/templates/domain.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/java/" + packageParentPath + "/" + pc.getModuleName() + "/" + pc.getEntity() + "/domain"
                        + "/" + tableInfo.getEntityName() + "Domain" + StringPool.DOT_JAVA;
            }
        });

        // 自定义validator配置
        focList.add(new FileOutConfig("/templates/validator.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/java/" + packageParentPath + "/" + pc.getModuleName() + "/" + pc.getService() + "/validator"
                        + "/" + tableInfo.getEntityName() + "ServiceValidator" + StringPool.DOT_JAVA;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(scanner("表名"));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix("t_");// 去除表名的前缀，这样就会在实体类加上@TableName注解了
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();

    }

    private Properties getProperties() {
        Properties prop = new Properties();
        try {
            // 加载默认配置
            prop.load(getClass().getResourceAsStream("/default-generator.properties"));
            URL fileUrl = getClass().getClassLoader().getResource("generator.properties");
            if (fileUrl != null) {
                // 加载自定义配置，会覆盖默认配置中相同key的内容
                prop.load(getClass().getResourceAsStream("/generator.properties"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    private String getCodePath(Properties prop) {
        // 获得配置的代码生成目录
        String codePath = prop.getProperty("codePath");
        if (codePath != null) {
            return codePath;
        }

        // 生成默认路径
        File file = new File(getClass().getClassLoader().getResource("").getFile());
        codePath = file.getParentFile().getParentFile().getPath() + File.separator + "code";
        return codePath;
    }

}
