package com.controller;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class BaseController {

    public final String logFileName = "/home/mi/log/boss-operations.log";
    public final int row = 500;

    /**
     * 初始化读取日志
     * @param model
     * @return
     */

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String viewLog (Model model) {
        List<String> list = new ArrayList<>();

        long pos = -2;
        int curPage = 1;


        pos = readLog(logFileName, pos, list);

        if (list.size() == 0 || pos == -1) {  //没有读取到任何数据，表示已经加载过了所有内容
            model.addAttribute("isOver", true);
        } else {
            model.addAttribute("isOver", false);
        }

//        Collections.reverse(list);

        model.addAttribute("content", list);
        model.addAttribute("curPage", curPage);
        model.addAttribute("pos", pos);

        return "index";
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
        List<String> list = new ArrayList<String>();
        pos = readLog(logFileName, pos, list);

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
        RandomAccessFile logFile = null;

        try {
            //以只读方式打开日志文件
            logFile = new RandomAccessFile(new File(logFileName), "r");
            //当前行号
            int curRow = 0;
            //获取文件大小
            long len = logFile.length();
//            System.out.println(startRow + "\t" + endRow + "\t" + curRow + "\t" + len);
            //读取行
            String line = null;
            if(len > 0) {
                //定位至文件尾部
                if (pos == -2) pos = len;
                while(pos-- > 0) {
                    logFile.seek(pos);//定位指针位置
                    if(logFile.readByte() == '\n' && curRow < row) {
                        line = logFile.readLine();//读取到换行符，这里的换行符是上一行的换行符，所以这里永远不会打印第一行的内容
                        if (line != null && !line.equals("")) {
                            curRow++;
                            list.add(line);
                            if (curRow == row) { //如果已读行数达到指定行数，则不再读取
                                break;
                            }
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
//            model.addAttribute("msg", "找不到日志文件: " + logFileName);
        } catch (IOException e) {
            e.printStackTrace();
//            model.addAttribute("msg", "读取日志文件失败: " + logFileName);
        } finally {
            try {
                if (logFile != null) logFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Collections.reverse(list);
        System.out.println(pos);
        return pos;
    }
}


