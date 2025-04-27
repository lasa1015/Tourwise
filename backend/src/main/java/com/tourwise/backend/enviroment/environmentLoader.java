package com.tourwise.backend.enviroment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


// 一个自定义的配置加载器，让你在 Java 项目中像 Node.js 一样用 .env 文件。
// 它在程序启动前运行，把 .env 文件中的内容加载进 Java 的系统属性中。

// 在 Spring Boot 中，其实推荐的是用 application.properties 或 application.yml 来写配置
// 但有时你可能希望保持敏感配置（如数据库密码）不写在代码仓库里
// 那么 .env 文件就非常好用，而这个类就是专门为兼容 .env 文件写的。

public class environmentLoader {

    public static void load() {
        try {
            Properties properties = new Properties();

            // 获取当前项目运行的路径
            File current = new File(System.getProperty("user.dir"));
            File envFile = null;

            // 从当前文件夹开始找 .env 文件,  如果没找到，就往上找一层目录, 最多找 5 层
            for (int i = 0; i < 5; i++) {
                File tryFile = new File(current, ".env");
                if (tryFile.exists()) {
                    envFile = tryFile;
                    break;
                }
                current = current.getParentFile(); // 向上一级
            }

            if (envFile == null) {
                System.err.println("❌ 没有找到 .env 文件！");
                return;
            }

            //读取 .env 内容
            FileInputStream fis = new FileInputStream(envFile);
            properties.load(fis);
            fis.close();

            for (String key : properties.stringPropertyNames()) {
                String value = properties.getProperty(key);

                // 注入系统变量
                System.setProperty(key, value);
            }

            System.out.println("✅ 成功加载 .env 文件：" + envFile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("❌ 加载 .env 文件失败：" + e.getMessage());
        }
    }
}
