package com.intomusic.musicplatform.common.utils;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;

import java.io.File;

/**
 * 音频工具类
 */
public class AudioUtil {

    private AudioUtil() {
        // 私有构造，防止实例化
    }

    /**
     * 获取音频文件的时长（秒）
     *
     * @param file 音频文件
     * @return 时长（秒），解析失败返回 0
     */
    public static int getDuration(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        try {
            AudioFile audioFile = AudioFileIO.read(file);
            AudioHeader header = audioFile.getAudioHeader();
            return header.getTrackLength();
        } catch (Exception e) {
            // 解析失败，返回默认值
            return 0;
        }
    }
}