package com.example.demo.enviroment;
<<<<<<< HEAD

import java.io.File;
=======
>>>>>>> upstream/master
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class environmentLoader {
<<<<<<< HEAD

    public static void load() {
        try {
            Properties properties = new Properties();

            // 从当前目录开始，向上查找 .env 文件（最多查 5 层）
            File current = new File(System.getProperty("user.dir"));
            File envFile = null;

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

            FileInputStream fis = new FileInputStream(envFile);
=======
    public static void load() {
        try {
            Properties properties = new Properties();
            String projectRoot = System.getProperty("user.dir");
            String envFilePath = projectRoot + "/../../../.env";
            FileInputStream fis = new FileInputStream(envFilePath);
>>>>>>> upstream/master
            properties.load(fis);
            fis.close();

            for (String key : properties.stringPropertyNames()) {
                String value = properties.getProperty(key);
                System.setProperty(key, value);
            }
<<<<<<< HEAD

            System.out.println("✅ 成功加载 .env 文件：" + envFile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("❌ 加载 .env 文件失败：" + e.getMessage());
        }
    }
}
=======
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

>>>>>>> upstream/master
