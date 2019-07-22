//package com.huang.notetool.tool;
//
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.prefs.Preferences;
//
///**
// * 获取注册表信息
// *
// * @author 黄先生
// * @version 1.0
// * @Content 此处说明类的解释
// * @date 2019-05-29 17:36:52
// * @History 序号   日期         修改内容          修改进度
// * * * * * * 1.0    2019-05-29   创建项目           完成
// * @LeftoverProblem 此处说明遗留问题和潜在的问题
// */
//public class WinRegistryMsg {
//
//
//    public static final int HKEY_CURRENT_USER = 0x80000001;
//    public static final int HKEY_LOCAL_MACHINE = 0x80000002;
//    public static final int REG_SUCCESS = 0;
//    public static final int REG_NOTFOUND = 2;
//    public static final int REG_ACCESSDENIED = 5;
//
//    private static final int KEY_ALL_ACCESS = 0xf003f;
//    private static final int KEY_READ = 0x20019;
//    private static final Preferences USER_ROOT = Preferences.userRoot();
//    private static final Preferences SYSTEM_ROOT = Preferences.systemRoot();
//    private static final Class<? extends Preferences> USER_CLASS = USER_ROOT.getClass();
//    private static final Method REG_OPEN_KEY;
//    private static final Method REG_CLOSE_KEY;
//    private static final Method REG_QUERY_VALUE_EX;
//    private static final Method regEnumValue;
//    private static final Method regQueryInfoKey;
//    private static final Method regEnumKeyEx;
//    private static final Method regCreateKeyEx;
//    private static final Method regSetValueEx;
//    private static final Method regDeleteKey;
//    private static final Method regDeleteValue;
//
//    static {
//        try {
//            REG_OPEN_KEY = USER_CLASS.getDeclaredMethod("WindowsRegOpenKey",
//                    int.class, byte[].class, int.class);
//            REG_OPEN_KEY.setAccessible(true);
//            REG_CLOSE_KEY = USER_CLASS.getDeclaredMethod("WindowsRegCloseKey",
//                    int.class);
//            REG_CLOSE_KEY.setAccessible(true);
//            REG_QUERY_VALUE_EX = USER_CLASS.getDeclaredMethod("WindowsRegQueryValueEx",
//                    int.class, byte[].class);
//            REG_QUERY_VALUE_EX.setAccessible(true);
//            regEnumValue = USER_CLASS.getDeclaredMethod("WindowsRegEnumValue",
//                    int.class, int.class, int.class);
//            regEnumValue.setAccessible(true);
//            regQueryInfoKey = USER_CLASS.getDeclaredMethod("WindowsRegQueryInfoKey1",
//                    int.class);
//            regQueryInfoKey.setAccessible(true);
//            regEnumKeyEx = USER_CLASS.getDeclaredMethod(
//                    "WindowsRegEnumKeyEx", int.class, int.class,
//                    int.class);
//            regEnumKeyEx.setAccessible(true);
//            regCreateKeyEx = USER_CLASS.getDeclaredMethod(
//                    "WindowsRegCreateKeyEx", int.class,
//                    byte[].class);
//            regCreateKeyEx.setAccessible(true);
//            regSetValueEx = USER_CLASS.getDeclaredMethod(
//                    "WindowsRegSetValueEx", int.class,
//                    byte[].class, byte[].class);
//            regSetValueEx.setAccessible(true);
//            regDeleteValue = USER_CLASS.getDeclaredMethod(
//                    "WindowsRegDeleteValue", int.class,
//                    byte[].class);
//            regDeleteValue.setAccessible(true);
//            regDeleteKey = USER_CLASS.getDeclaredMethod(
//                    "WindowsRegDeleteKey", int.class,
//                    byte[].class);
//            regDeleteKey.setAccessible(true);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private WinRegistryMsg() {
//    }
//
//    /**
//     * Read a value from key and value name
//     *
//     * @param hkey      HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
//     * @param key
//     * @param valueName
//     * @return the value
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     * @throws InvocationTargetException
//     */
//    public static String readString(int hkey, String key, String valueName)
//            throws IllegalArgumentException, IllegalAccessException,
//            InvocationTargetException {
//        if (hkey == HKEY_LOCAL_MACHINE) {
//            return readString(SYSTEM_ROOT, hkey, key, valueName);
//        } else if (hkey == HKEY_CURRENT_USER) {
//            return readString(USER_ROOT, hkey, key, valueName);
//        } else {
//            throw new IllegalArgumentException("hkey=" + hkey);
//        }
//    }
//
//    /**
//     * Read value(s) and value name(s) form given key
//     *
//     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
//     * @param key
//     * @return the value name(s) plus the value(s)
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     * @throws InvocationTargetException
//     */
//    public static Map<String, String> readStringValues(int hkey, String key)
//            throws IllegalArgumentException, IllegalAccessException,
//            InvocationTargetException {
//        if (hkey == HKEY_LOCAL_MACHINE) {
//            return readStringValues(SYSTEM_ROOT, hkey, key);
//        } else if (hkey == HKEY_CURRENT_USER) {
//            return readStringValues(USER_ROOT, hkey, key);
//        } else {
//            throw new IllegalArgumentException("hkey=" + hkey);
//        }
//    }
//
//    /**
//     * Read the value name(s) from a given key
//     *
//     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
//     * @param key
//     * @return the value name(s)
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     * @throws InvocationTargetException
//     */
//    public static List<String> readStringSubKeys(int hkey, String key) throws IllegalArgumentException
//            , IllegalAccessException,
//            InvocationTargetException {
//        if (hkey == HKEY_LOCAL_MACHINE) {
//            return readStringSubKeys(SYSTEM_ROOT, hkey, key);
//        } else if (hkey == HKEY_CURRENT_USER) {
//            return readStringSubKeys(USER_ROOT, hkey, key);
//        } else {
//            throw new IllegalArgumentException("hkey=" + hkey);
//        }
//    }
//
//    public static List<String> findAll() {
//        return null;
//    }
//
//    /**
//     * Create a key
//     *
//     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
//     * @param key
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     * @throws InvocationTargetException
//     */
//    public static void createKey(int hkey, String key)
//            throws IllegalArgumentException, IllegalAccessException,
//            InvocationTargetException {
//        int[] ret;
//        if (hkey == HKEY_LOCAL_MACHINE) {
//            ret = createKey(SYSTEM_ROOT, hkey, key);
//            REG_CLOSE_KEY.invoke(SYSTEM_ROOT, ret[0]);
//        } else if (hkey == HKEY_CURRENT_USER) {
//            ret = createKey(USER_ROOT, hkey, key);
//            REG_CLOSE_KEY.invoke(USER_ROOT, ret[0]);
//        } else {
//            throw new IllegalArgumentException("hkey=" + hkey);
//        }
//        if (ret[1] != REG_SUCCESS) {
//            throw new IllegalArgumentException("rc=" + ret[1] + "  key=" + key);
//        }
//    }
//
//    /**
//     * Write a value in a given key/value name
//     *
//     * @param hkey
//     * @param key
//     * @param valueName
//     * @param value
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     * @throws InvocationTargetException
//     */
//    public static void writeStringValue
//    (int hkey, String key, String valueName, String value)
//            throws IllegalArgumentException, IllegalAccessException,
//            InvocationTargetException {
//        if (hkey == HKEY_LOCAL_MACHINE) {
//            writeStringValue(SYSTEM_ROOT, hkey, key, valueName, value);
//        } else if (hkey == HKEY_CURRENT_USER) {
//            writeStringValue(USER_ROOT, hkey, key, valueName, value);
//        } else {
//            throw new IllegalArgumentException("hkey=" + hkey);
//        }
//    }
//
//    /**
//     * Delete a given key
//     *
//     * @param hkey
//     * @param key
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     * @throws InvocationTargetException
//     */
//    public static void deleteKey(int hkey, String key)
//            throws IllegalArgumentException, IllegalAccessException,
//            InvocationTargetException {
//        int rc = -1;
//        if (hkey == HKEY_LOCAL_MACHINE) {
//            rc = deleteKey(SYSTEM_ROOT, hkey, key);
//        } else if (hkey == HKEY_CURRENT_USER) {
//            rc = deleteKey(USER_ROOT, hkey, key);
//        }
//        if (rc != REG_SUCCESS) {
//            throw new IllegalArgumentException("rc=" + rc + "  key=" + key);
//        }
//    }
//
//    /**
//     * delete a value from a given key/value name
//     *
//     * @param hkey
//     * @param key
//     * @param value
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     * @throws InvocationTargetException
//     */
//    public static void deleteValue(int hkey, String key, String value)
//            throws IllegalArgumentException, IllegalAccessException,
//            InvocationTargetException {
//        int rc = -1;
//        if (hkey == HKEY_LOCAL_MACHINE) {
//            rc = deleteValue(SYSTEM_ROOT, hkey, key, value);
//        } else if (hkey == HKEY_CURRENT_USER) {
//            rc = deleteValue(USER_ROOT, hkey, key, value);
//        }
//        if (rc != REG_SUCCESS) {
//            throw new IllegalArgumentException("rc=" + rc + "  key=" + key + "  value=" + value);
//        }
//    }
//
//    // =====================
//
//    private static int deleteValue
//            (Preferences root, int hkey, String key, String value)
//            throws IllegalArgumentException, IllegalAccessException,
//            InvocationTargetException {
//        int[] handles = (int[]) REG_OPEN_KEY.invoke(root, new Object[]{
//                new Integer(hkey), toCstr(key), new Integer(KEY_ALL_ACCESS)});
//        if (handles[1] != REG_SUCCESS) {
//            return handles[1];  // can be REG_NOTFOUND, REG_ACCESSDENIED
//        }
//        int rc = ((Integer) regDeleteValue.invoke(root,
//                new Object[]{
//                        new Integer(handles[0]), toCstr(value)
//                })).intValue();
//        REG_CLOSE_KEY.invoke(root, new Object[]{new Integer(handles[0])});
//        return rc;
//    }
//
//    private static int deleteKey(Preferences root, int hkey, String key)
//            throws IllegalArgumentException, IllegalAccessException,
//            InvocationTargetException {
//        int rc = ((Integer) regDeleteKey.invoke(root,
//                new Object[]{new Integer(hkey), toCstr(key)})).intValue();
//        return rc;  // can REG_NOTFOUND, REG_ACCESSDENIED, REG_SUCCESS
//    }
//
//    private static String readString(Preferences root, int hkey, String key, String value)
//            throws IllegalArgumentException, IllegalAccessException,
//            InvocationTargetException {
//        int[] handles = (int[]) REG_OPEN_KEY.invoke(root, new Object[]{
//                new Integer(hkey), toCstr(key), new Integer(KEY_READ)});
//        if (handles[1] != REG_SUCCESS) {
//            return null;
//        }
//        byte[] valb = (byte[]) REG_QUERY_VALUE_EX.invoke(root, new Object[]{
//                new Integer(handles[0]), toCstr(value)});
//        REG_CLOSE_KEY.invoke(root, new Object[]{new Integer(handles[0])});
//        return (valb != null ? new String(valb).trim() : null);
//    }
//
//    private static Map<String, String> readStringValues
//            (Preferences root, int hkey, String key)
//            throws IllegalArgumentException, IllegalAccessException,
//            InvocationTargetException {
//        HashMap<String, String> results = new HashMap<String, String>();
//        int[] handles = (int[]) REG_OPEN_KEY.invoke(root, new Object[]{
//                new Integer(hkey), toCstr(key), new Integer(KEY_READ)});
//        if (handles[1] != REG_SUCCESS) {
//            return null;
//        }
//        int[] info = (int[]) regQueryInfoKey.invoke(root,
//                new Object[]{new Integer(handles[0])});
//
//        int count = info[0]; // count
//        int maxlen = info[3]; // value length max
//        for (int index = 0; index < count; index++) {
//            byte[] name = (byte[]) regEnumValue.invoke(root, new Object[]{
//                    new Integer
//                            (handles[0]), new Integer(index), new Integer(maxlen + 1)});
//            String value = readString(hkey, key, new String(name));
//            results.put(new String(name).trim(), value);
//        }
//        REG_CLOSE_KEY.invoke(root, handles[0]);
//        return results;
//    }
//
//    private static List<String> readStringSubKeys
//            (Preferences root, int hKey, String key)
//            throws IllegalArgumentException, IllegalAccessException,
//            InvocationTargetException {
//        List<String> results = new ArrayList<>();
//        int[] handles = (int[]) REG_OPEN_KEY.invoke(root, new Object[]{
//                hKey, toCstr(key), KEY_READ
//        });
//        if (handles[1] != REG_SUCCESS) {
//            return null;
//        }
//        int[] info = (int[]) regQueryInfoKey.invoke(root,
//                new Object[]{handles[0]});
//        // Fix: info[2] was being used here with wrong results. Suggested by davenpcj, confirmed by Petrucio
//        int count = info[0];
//        // value length max
//        int maxlen = info[3];
//        for (int index = 0; index < count; index++) {
//            byte[] name;
//            name = (byte[]) regEnumKeyEx.invoke(root, new Object[]{
//                    handles[0], index, maxlen + 1
//            });
//            results.add(new String(name).trim());
//        }
//        REG_CLOSE_KEY.invoke(root, handles[0]);
//        return results;
//    }
//
//    private static int[] createKey(Preferences root, int hkey, String key)
//            throws IllegalArgumentException, IllegalAccessException,
//            InvocationTargetException {
//        return (int[]) regCreateKeyEx.invoke(root,
//                new Object[]{new Integer(hkey), toCstr(key)});
//    }
//
//    private static void writeStringValue
//            (Preferences root, int hkey, String key, String valueName, String value)
//            throws IllegalArgumentException, IllegalAccessException,
//            InvocationTargetException {
//        int[] handles = (int[]) REG_OPEN_KEY.invoke(root, new Object[]{
//                new Integer(hkey), toCstr(key), new Integer(KEY_ALL_ACCESS)});
//
//        regSetValueEx.invoke(root,
//                new Object[]{
//                        new Integer(handles[0]), toCstr(valueName), toCstr(value)
//                });
//        REG_CLOSE_KEY.invoke(root, new Object[]{new Integer(handles[0])});
//    }
//
//    // utility
//    private static byte[] toCstr(String str) {
//        byte[] result = new byte[str.length() + 1];
//
//        for (int i = 0; i < str.length(); i++) {
//            result[i] = (byte) str.charAt(i);
//        }
//        result[str.length()] = 0;
//        return result;
//    }
//
//
//    // 测试
//    public static void main(String[] args) {
//        try {
//            //获取 args  中 所携带的参数
//            //String str = (String)args[0];
//            String str = "www.baidu.com";
//
//            //获取 Firefox的 版本号
//            String cversion = WinRegistryMsg.readString(WinRegistryMsg.HKEY_LOCAL_MACHINE, "SOFTWARE\\Mozilla\\Mozilla Firefox", "CurrentVersion");
//
//            //注册表路径拼接
//            String rUrl = "SOFTWARE\\Mozilla\\Mozilla Firefox\\" + cversion + "\\Main";
//
//            //获取 当前路径下 key(PathToExe)  的value
//            String pathValue = WinRegistryMsg.readString(WinRegistryMsg.HKEY_LOCAL_MACHINE, rUrl, "PathToExe");
//
//            // 用火狐打开指定路径
//            ProcessBuilder proc = new ProcessBuilder(pathValue, str);
//            proc.start();
//
//        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//}
