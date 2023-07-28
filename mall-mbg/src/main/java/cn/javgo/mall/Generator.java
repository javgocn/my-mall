package cn.javgo.mall;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * MyBatisGenerator 生成器
 */
public class Generator {
    public static void main(String[] args) throws Exception {

        // 记录执行过程中的警告信息
        List<String> warnings = new ArrayList<>();

        // 当生成的代码重复时是否覆盖原代码
        boolean overwrite = true;

        // 读取 MyBatisGenerator 配置文件
        InputStream inputStream = Generator.class.getResourceAsStream("/generatorConfig.xml");
        // 解析配置文件
        ConfigurationParser configurationParser = new ConfigurationParser(warnings);
        Configuration configuration = configurationParser.parseConfiguration(inputStream);

        // 关闭输入流
        if (inputStream != null) inputStream.close();

        // 当生成的代码重复时是否覆盖原代码
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);

        // 创建 MyBatisGenerator
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(configuration, callback, warnings);

        // 执行生成代码
        myBatisGenerator.generate(null);

        // 输出警告信息
        for (String warning : warnings) {
            System.out.println(warning);
        }
    }
}
