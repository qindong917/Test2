package com.qd.cjb.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @version : 1.0
 * @auther : hjx
 * @Date : 2021/2/24
 * @Description : nuc.edu.hjx.utils
 */
public class MergeVideoBgm {

    private String ffmpeg;

    public MergeVideoBgm(String ffmpeg) {
        super();
        this.ffmpeg = ffmpeg;
    }

    /**
     * 对音频视频进行合并
     * @param videoInputPath
     * @param bgmInputPath
     * @param videoOutputPath
     * @param secoud
     * @throws Exception
     */
    public void convert(String videoInputPath, String bgmInputPath,String videoOutputPath, double secoud )
    throws Exception{

        List<String> command = new ArrayList<>();
        command.add(ffmpeg);
        command.add("-i");
        command.add(videoInputPath);
        command.add("-i");
        command.add(bgmInputPath);
        command.add("-t");
        command.add(String.valueOf(secoud));
        command.add("-y");
        command.add(videoOutputPath);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        InputStream errorStream = process.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        while (bufferedReader.readLine() != null) { }
        if (bufferedReader != null){
            bufferedReader.close();
        }if (inputStreamReader != null){
            inputStreamReader.close();
        }if (errorStream != null){
            errorStream.close();
        }
    }

    /**
     * 生成视频封面，截取视频第一秒
     * @param videoInputPath
     * @param coverOutputPath
     * @throws IOException
     */
    public void getcover(String videoInputPath, String coverOutputPath) throws IOException {
        List<String> command = new ArrayList();
        command.add(ffmpeg);
        command.add("-ss");
        command.add("00:00:01");
        command.add("-y");
        command.add("-i");
        command.add(videoInputPath);
        command.add("-vframes");
        command.add("1");
        command.add(coverOutputPath);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        InputStream errorStream = process.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        while (bufferedReader.readLine() != null) { }
        if (bufferedReader != null){
            bufferedReader.close();
        }if (inputStreamReader != null){
            inputStreamReader.close();
        }if (errorStream != null){
            errorStream.close();
        }
    }

}
