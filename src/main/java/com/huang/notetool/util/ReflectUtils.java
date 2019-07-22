//package com.huang.notetool.util;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// * 反射赋值
// *
// * @author 黄先生
// * @version 1.0
// * @Content 此处说明类的解释
// * @date 2019-06-28 16:36:20
// * @History 序号   日期         修改内容          修改进度
// * * * * * * 1.0    2019-06-28   创建项目           完成
// * @LeftoverProblem 此处说明遗留问题和潜在的问题
// */
//public class ReflectUtils {
//    public static void setValue(Object model, String setName, String setValue) {
//// 获取实体类的所有属性，返回Field数组
//        Field[] field = model.getClass().getDeclaredFields();
//        try {
//            // 遍历所有属性
//            for (Field field1 : field) {
//                // 获取属性的名字
//                String name = field1.getName();
//                if (name.equals(setName)) {
//                    // 将属性的首字符大写，方便构造get，set方法
//                    name = name.substring(0, 1).toUpperCase() + name.substring(1);
//                    // 获取属性的类型
//                    String type = field1.getGenericType().toString();
//                    // 如果type是类类型，则前面包含"class "，后面跟类名
//                    if ("class java.lang.String".equals(type)) {
//                        Method m = model.getClass().getMethod("get" + name);
//                        // 调用getter方法获取属性值
//                        String value = (String) m.invoke(model);
//                        if (value == null) {
//                            m = model.getClass().getMethod("set" + name, String.class);
//                            m.invoke(model, setValue);
//                        }
//                    }
//                    if ("class java.lang.Integer".equals(type)) {
//                        Method m = model.getClass().getMethod("get" + name);
//                        Integer value = (Integer) m.invoke(model);
//                        if (value == null) {
//                            m = model.getClass().getMethod("set" + name, Integer.class);
//                            m.invoke(model, Integer.parseInt(setValue));
//                        }
//                    }
//                    if ("class java.lang.Long".equals(type)) {
//                        Method m = model.getClass().getMethod("get" + name);
//                        Long value = (Long) m.invoke(model);
//                        if (value == null) {
//                            m = model.getClass().getMethod("set" + name, Integer.class);
//                            m.invoke(model, Long.parseLong(setValue));
//                        }
//                    }
//                    if ("class java.lang.Boolean".equals(type)) {
//                        Method m = model.getClass().getMethod("get" + name);
//                        Boolean value = (Boolean) m.invoke(model);
//                        if (value == null) {
//                            m = model.getClass().getMethod("set" + name, Boolean.class);
//                            m.invoke(model, Boolean.parseBoolean(setValue));
//                        }
//                    }
//                    if ("class java.util.Date".equals(type)) {
//                        Method m = model.getClass().getMethod("get" + name);
//                        Date value = (Date) m.invoke(model);
//                        if (value == null) {
//                            m = model.getClass().getMethod("set" + name, Date.class);
//                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                            m.invoke(model, sdf.parse(setValue));
//                        }
//                    }
//                    // 如果有需要,可以仿照上面继续进行扩充,再增加对其它类型的判断
//                    break;//跳出较少循环
//                }
//            }
//        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ParseException e) {
//            e.printStackTrace();
//        }
//    }
//}
