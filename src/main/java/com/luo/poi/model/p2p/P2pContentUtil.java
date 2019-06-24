package com.luo.poi.model.p2p;

import com.luo.poi.utilnew.ExcelReadUtilNew;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luosen
 * @version 0.0.1
 * @date 2018/9/8
 * @time 15:46
 * @function 功能:
 * @describe 版本描述:
 * @modifyLog 修改日志:
 */
public class P2pContentUtil {

    public static void main(String[] args) throws IOException {
        List<BorrowInfo> testList = ExcelReadUtilNew.read("C:\\Users\\Administrator\\Desktop\\p2pcontent.xlsx",
                0, 0, null, null, BorrowInfo.class);


        if (!CollectionUtils.isEmpty(testList)) {
            System.out.println("总条数：" + testList.size());
            List<List<BorrowInfo>> lists = averageAssign(testList, 2);
            System.out.println("拆分数：" + lists.size());
            int size = 1;
//            for(List<BorrowInfo> list : lists) {
//                List<BorrowSql> borrowSqls = new ArrayList<>();
//                list.forEach(test -> {
//                    String content = getContent(test);
//                    String sql = "UPDATE borrow_content SET content = '" + content + "' where borrowId = (select id from borrow where borrowNo = " + test.getBorrowNo() + ");";
//                    //System.out.println(content);
//                    String creditRating = null;
//                    if("AAA".equals(test.getCreditRating())){
//                        creditRating = "1";
//                    } else if("AA".equals(test.getCreditRating())){
//                        creditRating = "2";
//                    } else if("A".equals(test.getCreditRating())){
//                        creditRating = "3";
//                    } else if("BBB".equals(test.getCreditRating())){
//                        creditRating = "4";
//                    } else if("BB".equals(test.getCreditRating())){
//                        creditRating = "5";
//                    }
//
//                    String sql2 = "UPDATE borrow SET creditRating = '" + creditRating + "' where borrowNo = " + test.getBorrowNo() + ";";
//                    BorrowSql borrowSql = new BorrowSql(test.getBorrowNo(), sql , sql2);
//                    borrowSqls.add(borrowSql);
//                });
//                System.out.println("文档[" + size + "]开始写入：" + borrowSqls.size());
//                try {
//                    ExcelWriteUtil.write("C:\\Users\\Administrator\\Desktop\\test"+ size+".xlsx", null, 20000, null, null, borrowSqls);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                size++;
//            }      //1.8
        }
    }

    /**
     * 将一个list均分成n个list,主要通过偏移量来实现的
     *
     * @param source
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int n) {
        int size = 0;
        List<List<T>> result = new ArrayList<List<T>>();
        int remaider = source.size() % n;  //(先计算出余数)
        int number = source.size() / n;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
            size++;
        }
        System.out.println("size:" + size);
        return result;
    }



}
