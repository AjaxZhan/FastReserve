package cn.cagurzhan.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Cagur
 * @version 1.0
 */
public class BeanCopyUtils {
    //空参私有构造
    private BeanCopyUtils(){

    }
    /**
     * 通过反射创建对象，返回VO
     * 技术：泛型、反射
     * */
    public static <V> V copyBean(Object source, Class<V> clazz) {
        V vo = null;
        try {
            vo = clazz.newInstance();
            BeanUtils.copyProperties(source, vo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vo;
    }

    /**
     * 返回集合
     * 技术：Stream流、泛型
     */
    public static <O,V>List<V> copyBeanList(List<O> sourceList , Class<V> clazz){
        return sourceList.stream()
                .map(o -> copyBean(o, clazz)).
                collect(Collectors.toList());
    }

}
