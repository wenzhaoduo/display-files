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

//    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String viewConsoleLog(@RequestParam(required = false, defaultValue = "/home/mi/log/boss-operations.log") String logFileName,
                                 @RequestParam(required = false, defaultValue = "1") int curPage,
                                 @RequestParam(required = false, defaultValue = "50") int row,
                                 @RequestParam(required = false, defaultValue = "UTF-8") String charSet,
                                 Model model) {
        List<String> list = new ArrayList<String>();
        RandomAccessFile logFile = null;
        try {
            //以只读方式打开日志文件
            logFile = new RandomAccessFile(new File(logFileName), "r");
            //计算要读取的行号，从末行开始读取，所以末行行号为0
            int startRow = (curPage - 1) * row;
            int endRow = curPage * row - 1;
//            System.out.println(startRow + "\t" + endRow);
            //当前行号
            int curRow = 0;
            //获取文件大小
            long len = logFile.length();
            //读取行
            String line = null;
            if(len > 0) {
                //定位至文件尾部
                long p = len;
                while(p-- > 0) {
                    logFile.seek(p);//定位指针位置
                    if(curRow > endRow) { //如果已读行数达到指定行数，则不再读取
                        break;
                    }
                    if(logFile.readByte() == '\n') {
                        if(curRow >= startRow && curRow <= endRow) {
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
            curRow++;
            if(curRow >= startRow && curRow <= endRow) {
                logFile.seek(0);//定位指针至文件开头
                line = logFile.readLine();//读取到换行符，这里的换行符是上一行的换行符，所以这里永远不会打印第一行的内容
                line = (line == null) ? "" : new String(line.getBytes("ISO-8859-1"), charSet);//如果是换行符,并且在指定行号内，就读取该行；如果是空行，这打印空行
                list.add(line);
            }
            if(list.size() == 0) {  //没有读取到任何数据，表示已经加载过了所有内容
                model.addAttribute("isOver", true);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            model.addAttribute("msg", "找不到日志文件: " + logFileName);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("msg", "读取日志文件失败: " + logFileName);
        } finally {
            try {
                if(logFile != null) logFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Collections.reverse(list);
        model.addAttribute("content", list);
        model.addAttribute("curPage", curPage);
        model.addAttribute("row", row);
        return "index";
    }
}


