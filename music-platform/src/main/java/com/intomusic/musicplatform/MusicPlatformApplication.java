package com.intomusic.musicplatform;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

// 1. 排除默认数据源 (防止冲突)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
// 2. 关键操作：强制提前加载动态数据源配置！
@Import(DynamicDataSourceAutoConfiguration.class)
// 3. 扫描 Mapper (这里可以放心扫描了，因为上面那行保证了数据源已经就绪)
@MapperScan("com.intomusic.musicplatform.modules.**.mapper")
public class MusicPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicPlatformApplication.class, args);
    }
}