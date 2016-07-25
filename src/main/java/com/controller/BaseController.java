package com.controller;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class BaseController {

    public static String staticLogFileNmae = "";
    public final String logDir = "/home/" + System.getProperty("user.name") + "/log/";
    public final int row = 500;

    public static RandomAccessFile logFile = null;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index (Model model) {
        List<String> files = new ArrayList<>();
        getFiles(logDir, files);
        model.addAttribute("files", files);

        return "index";
    }

    private void getFiles(String logDir, List<String> files) {
        File file = new File(logDir);

        if (file.exists()) {
            File[] filesArr = file.listFiles();
            if (filesArr.length == 0) {
                return;
            }

            for (File file2: filesArr) {
                if (file2.isDirectory()) {
                    getFiles(file2.getAbsolutePath(), files);
                } else {
                    files.add(file2.getName());
                }
            }
        }
    }

    /**
     * 初始化读取日志
     * @param model
     * @return
     */

    @RequestMapping(value = "/viewLog", method = RequestMethod.GET)
    public String viewLog (@RequestParam String logFileName,
                           Model model) throws IOException {

        String proDir = logFileName.split("\\.")[0];
        staticLogFileNmae = logDir + proDir + "/" + logFileName;
        System.out.println(staticLogFileNmae);

        List<String> list = new ArrayList<>();

        long pos = -2;
        int curPage = 1;

        pos = readLog(staticLogFileNmae, pos, list);

        if (list.size() == 0 || pos == -1) {  //没有读取到任何数据，表示已经加载过了所有内容
            model.addAttribute("isOver", true);
        } else {
            model.addAttribute("isOver", false);
        }

        BufferedReader reader = new BufferedReader(new FileReader(staticLogFileNmae));
        String line = "";
        int totalLines = 0;

        while ((line = reader.readLine()) != null) {
            totalLines ++;
        }

        model.addAttribute("content", list);
        model.addAttribute("curPage", curPage);
        model.addAttribute("pos", pos);
        model.addAttribute("totalLines", totalLines);

        return "viewLog";
    }

    /**
     * 分页读取weblogic控制台日志
     * @param curPage 当前读取内容的页码，文件从末尾向开始读取，末尾所在页码为1
     * @param pos 当前读取内容的指针位置
     * @param model
     * @return
     */

    @RequestMapping(value = "/loadLog", method = RequestMethod.GET)
    @ResponseBody
    public String addLog (@RequestParam(required = false, defaultValue = "1") int curPage,
                          @RequestParam(required = false, defaultValue = "-2") long pos,
                          Model model) {
        List<String> list = new ArrayList<>();

        pos = readLog(staticLogFileNmae, pos, list);

        if (list.size() == 0 || pos == -1) {  //没有读取到任何数据，表示已经加载过了所有内容
            model.addAttribute("isOver", true);
        } else {
            model.addAttribute("isOver", false);
        }

        model.addAttribute("content", list);
        model.addAttribute("curPage", curPage + 1);
        model.addAttribute("pos", pos);

        return new Gson().toJson(model);
    }

    private long readLog( String logFileName, long pos, List<String> list) {

        try {
            //以只读方式打开日志文件
            logFile = new RandomAccessFile(staticLogFileNmae, "r");
            //当前行号
            int curRow = 0;
            //读取行
            String line = null;
            //定位至文件尾部
            if (pos == -2) pos = logFile.length();
            while (pos-- > 0) {
                logFile.seek(pos);//定位指针位置
                if (logFile.readByte() == '\n' && curRow < row) {
                    line = logFile.readLine();//读取到换行符，这里的换行符是上一行的换行符，所以这里永远不会打印第一行的内容
                    if (line != null) {
                        line = new String(line.getBytes("ISO-8859-1"), "utf-8");//如果是换行符,并且在指定行号内，就读取该行；如果是空行，这打印空行
                        curRow++;
                        list.add(line);
                        if (curRow == row) { //如果已读行数达到指定行数，则不再读取
                            break;
                        }
                    }
                }
            }

            //读取文件首行
            if (pos == -1) {
                logFile.seek(0);//定位指针至文件开头
                line = logFile.readLine();//读取到换行符，这里的换行符是上一行的换行符，所以这里永远不会打印第一行的内容
                if (line != null && !line.equals("")) {
                    curRow ++;
                    list.add(line);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (logFile != null) logFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Collections.reverse(list);
        return pos;
    }
}


