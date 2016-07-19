package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class BaseController {
    /**
     * 分页读取weblogic控制台日志
     * @param logFileName 日志文件名字
     * @param curPage 当前读取内容的页码，文件从末尾向开始读取，末尾所在页码为1
     * @param row 每次读取内容行数
     * @param charSet 读取内容的字符编码
     * @param model
     * @return
     */

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String viewConsoleLog(@RequestParam(required = false, defaultValue = "/home/mi/log/boss-operations.log") String logFileName,
                                 @RequestParam(required = false, defaultValue = "1") int curPage,
                                 @RequestParam(required = false, defaultValue = "500") int row,
                                 @RequestParam(required = false, defaultValue = "-1") long pos,
                                 @RequestParam(required = false, defaultValue = "UTF-8") String charSet,
                                 Model model) {
        System.out.println(logFileName + "\t" + curPage + "\t" + row + "\t" + pos);
        List<String> list = new ArrayList<String>();
        RandomAccessFile logFile = null;
        try {
            //以只读方式打开日志文件
            logFile = new RandomAccessFile(new File(logFileName), "r");
            //计算要读取的行号，从末行开始读取，所以末行行号为0
            int startRow = (curPage - 1) * row;
            int endRow = curPage * row - 1;
            //当前行号
            int curRow = 0;
            //获取文件大小
            long len = logFile.length();
            System.out.println(startRow + "\t" + endRow + "\t" + curRow + "\t" + len);
            //读取行
            String line = null;
            if(len > 0) {
                System.out.println("reading from log.");
                //定位至文件尾部
                if (pos == -1) pos = len;
                while(pos-- > 0) {
                    logFile.seek(pos);//定位指针位置
                    if(curRow > endRow) { //如果已读行数达到指定行数，则不再读取
                        break;
                    }
                    if(logFile.readByte() == '\n') {
//                        if(curRow >= startRow && curRow <= endRow) {
                        if(curRow < row) {
                            line = logFile.readLine();//读取到换行符，这里的换行符是上一行的换行符，所以这里永远不会打印第一行的内容
                            if (line != null && !line.equals("")) {
                                curRow ++;
                                list.add(line);
                            }
                        }
                    }
                }
            }

            //读取文件首行
            if (pos == 0) {
                logFile.seek(pos);//定位指针至文件开头
                line = logFile.readLine();//读取到换行符，这里的换行符是上一行的换行符，所以这里永远不会打印第一行的内容
                if (line != null && !line.equals("")) {
                    curRow ++;
                    list.add(line);
                }
            }

            if (list.size() == 0) {  //没有读取到任何数据，表示已经加载过了所有内容
                model.addAttribute("isOver", true);
            } else {
                model.addAttribute("isOver", false);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            model.addAttribute("msg", "找不到日志文件: " + logFileName);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("msg", "读取日志文件失败: " + logFileName);
        } finally {
            try {
                if (logFile != null) logFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("The size of list: " + list.size());

        Collections.reverse(list);
        model.addAttribute("content", list);
        model.addAttribute("curPage", curPage + 1);
        model.addAttribute("row", row);
        model.addAttribute("pos", pos);
        return "index";
    }
}


