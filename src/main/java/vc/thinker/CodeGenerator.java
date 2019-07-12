package vc.thinker;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @Author: HeTongHao
 * @Date: 2019-06-23 17:02
 */
public class CodeGenerator {
    /**
     * JDBC相关配置
     */
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://pgm-wz9006t1oky2042t3o.pg.rds.aliyuncs.com:3432/smart_parking?useUnicode=true&characterEncoding=utf8";
    private static final String USER_NAME = "bull";
    private static final String PASSWORD = "vvAFXHrxKe672Rc*Y43T*Fv4wPkrxTiV";

    /**
     * 生成在哪个包下
     */
    private static final String PARENT_PACKAGE_NAME = "vc.thinker.dal";
    /**
     * 代码生成者
     */
    private static final String AUTHOR = "YZM";

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

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java")
                .setAuthor(AUTHOR)
                .setOpen(false)
                //实体属性 Swagger2 注解
                .setSwagger2(true);
        mpg.setGlobalConfig(gc);
        // 数据源配置
        mpg.setDataSource(dataSourceConfig());
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(scanner("模块名"))
                .setParent(PARENT_PACKAGE_NAME);
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
//        focList.add(new FileOutConfig(templatePath) {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
//                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
//                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
//            }
//        });
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录");
                return false;
            }
        });
        */
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        //templateConfig.setController("templates/controller.java");
        templateConfig.setEntity("templates/entity.java");
        // templateConfig.setService();

        //templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setEntityLombokModel(true)
                .setRestControllerStyle(true)
                .setInclude(scanner("表名，多个英文逗号分割").split(","))
//                .setSuperEntityColumns("id")
                .setControllerMappingHyphenStyle(false)
                .setTablePrefix(pc.getModuleName() + "_");
        //strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
        mpg.setStrategy(strategy);

        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    /**
     * 数据源配置
     *
     * @return
     */
    private static DataSourceConfig dataSourceConfig() {
        return new DataSourceConfig().setDriverName(DRIVER).setUrl(URL).setUsername(USER_NAME).setPassword(PASSWORD);
    }
}
