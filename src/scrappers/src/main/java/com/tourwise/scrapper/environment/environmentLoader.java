package com.tourwise.scrapper.environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class environmentLoader {
    public static void load() {
        try {
            File currentDir = new File(System.getProperty("user.dir"));
            File envFile = null;

            // 向上最多 5 层目录查找 .env 文件
            for (int i = 0; i <= 5; i++) {
                File possibleEnv = new File(currentDir, ".env");
                if (possibleEnv.exists()) {
                    envFile = possibleEnv;
                    break;
                }
                currentDir = currentDir.getParentFile();
                if (currentDir == null) break;
            }

            if (envFile == null) {
                System.err.println("❗未找到 .env 文件（已向上查找 5 层）");
                return;
            }

            System.out.println("✅ 找到 .env 文件，路径：" + envFile.getAbsolutePath());

            Properties properties = new Properties();
            try (FileInputStream fis = new FileInputStream(envFile)) {
                properties.load(fis);
            }

            for (String key : properties.stringPropertyNames()) {
                String value = properties.getProperty(key);
                System.setProperty(key, value);
            }

        } catch (IOException e) {
            System.err.println("❗加载 .env 文件失败！");
            e.printStackTrace();
        }
    }
}
