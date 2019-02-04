
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.util.ArrayList;
import java.util.List;

public class MybatisGeneratorTest {

    public static void main(String[] args) {
        List<String> warnings = new ArrayList<String>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = null;
        try {
            config = cp.parseConfiguration(MybatisGeneratorTest.class.getResourceAsStream("/generatorConfig.xml"));
            DefaultShellCallback shellCallback = new DefaultShellCallback(true);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
            myBatisGenerator.generate(null, null, null, true);
            System.out.println("warnings:" + warnings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
