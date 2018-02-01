package com.example.lib_json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 对象中所有属性都应该定义get，set方法并且定义空参构造方法
 * Created by Administrator on 2017/12/9.
 */

public class FastJson {

    private static final int JSON_OBJECT = 1 ;
    private static final int JSON_ARRAY = 2 ;
    private static final int JSON_ERROR = 3 ;
    public static Object parseObject(String json , Class clazz){
        Object object = null ;
        Class<?> jsonClass = null ;
        //以[开头的是json数组，以{开头的是json对象
        if(json.charAt(0) == '['){
            try {
                object = toList(json,clazz);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else if(json.charAt(0) == '{') {
            try {
                object = clazz.newInstance();
                JSONObject jsonObject = new JSONObject(json);
                Iterator<?> iterator = jsonObject.keys();
                while (iterator.hasNext()){
                    String key = (String) iterator.next();
                    Object fieldValue = null ;
                    List<Field> fields = getAllFields(null,clazz);
                    for (Field field : fields) {
                        if(field.getName().equals(key)){
                            field.setAccessible(true);
                            fieldValue = getFieldValue(field,jsonObject,key);
                            if(fieldValue != null){
                                try {
                                    field.set(object,fieldValue);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                            field.setAccessible(false);
                        }
                    }
                }

            } catch (Exception e) {
                try {
                    throw new JSONException("json字符串不合法");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        } else {
            try {
                throw new IllegalAccessException("json字符串不合法");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return object ;
    }

    private static Object getFieldValue(Field field, JSONObject jsonObject, String key) throws JSONException {
        Object filedValue = null ;
        Class<?> fieldClass = field.getType();
//        String methodName = "get"+(fieldClass.getSimpleName().toUpperCase().substring(0,1)+fieldClass.getSimpleName().substring(1,fieldClass.getSimpleName().length()-1));
//        try {
//            Method method = JSONObject.class.getMethod(methodName,String.class);
//            if(method != null){
//
//            }
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
        if(fieldClass.getSimpleName().toString().equals("int")||
                fieldClass.getSimpleName().toString().equals("Integer")){
            filedValue = jsonObject.getInt(key);
        }else if(fieldClass.getSimpleName().toString().equals("String")){
            filedValue = jsonObject.getString(key);
        }else if(fieldClass.getSimpleName().toString().equals("Double") ||
                fieldClass.getSimpleName().toString().equals("double")){
            filedValue = jsonObject.getDouble(key) ;
        }else if(fieldClass.getSimpleName().toString().equals("boolean") ||
                fieldClass.getSimpleName().toString().equals("Boolean")){
            filedValue = jsonObject.getBoolean(key) ;
        }else if(fieldClass.getSimpleName().toString().equals("Long") ||
                fieldClass.getSimpleName().toString().equals("long")){
            filedValue = jsonObject.getLong(key) ;
        }else{
            //如果以上类型都不是，可能是List集合或者引用类型
            String jsonValue = jsonObject.getString(key);
            switch (getJSONType(jsonValue)){
                case JSON_ARRAY :
                    Type fieldType = field.getGenericType();
                    if(fieldType instanceof ParameterizedType){
                        ParameterizedType parameterizedType = (ParameterizedType) fieldType ;
                        Type[] fieldArgType = parameterizedType.getActualTypeArguments();
                        for (Type type : fieldArgType) {
                            Class<?> fieldArgClass = (Class<?>) type;
                            filedValue = toList(jsonValue,fieldArgClass);
                        }
                    }
                    break ;
                case JSON_OBJECT :
                    filedValue = parseObject(jsonValue,fieldClass);
                    break ;
                case JSON_ERROR :
                    break ;
            }
        }
        return filedValue;
    }

    private static Object toList(String json, Class<?> clazz) throws JSONException {
        List<Object> list = new ArrayList<>() ;
        JSONArray jsonArray = new JSONArray(json);
        for(int i = 0 ; i < jsonArray.length() ; i ++){
            String jsonValue = jsonArray.getJSONObject(i).toString();
            switch (getJSONType(jsonValue)){
                case JSON_ARRAY:
                    List<?> infoList = (List<?>) toList(jsonValue,clazz);
                    list.add(infoList);
                    break ;
                case JSON_OBJECT :
                    list.add(parseObject(jsonValue,clazz));
                    break ;
                case JSON_ERROR:
                    break;
            }
        }
        return list;
    }

    private static int getJSONType(String jsonValue) {
        char firstChar = jsonValue.charAt(0) ;
        if(firstChar == '{'){
            return JSON_OBJECT ;
        }else if(firstChar == '['){
            return JSON_ARRAY ;
        }
        return JSON_ERROR ;
    }


    /**
     * @param o   解析的对象
     * @return
     */
    public static String toJson(Object o){
        StringBuffer jsonBuffer = new StringBuffer();
        //首先判断是json对象还是json数组
        if(o instanceof List<?>){
            List<?> jsonList = (List<?>) o ;
            jsonBuffer.append("[");
            for (int i = 0 ; i < jsonList.size() ; i++) {
                addObjectToJson(jsonBuffer,jsonList.get(i));
                //每个json对象遍历完后加上",",最后一个不加
                if (i < jsonList.size() -1){
                    jsonBuffer.append(",");
                }
            }
        }else{
            addObjectToJson(jsonBuffer ,o);
        }
        return jsonBuffer.toString() ;
    }

    private static void addObjectToJson(StringBuffer jsonBuffer, Object o)  {
        jsonBuffer.append("{");
        List<Field> fields = new ArrayList<>();
        //获取当前对象的所有属性
        getAllFields(fields,o.getClass());
        for (int i = 0 ; i < fields.size() ;i ++ ) {
            Field field = fields.get(i) ;
            Method method = null ;
            Object fieldValue = null ;
            //获取当前属性的get方法
            String methodName = "get"+((char)(field.getName().charAt(0)-32)+field.getName().substring(1));
            //反射获取当前的方法
            try{
                method = o.getClass().getMethod(methodName);
            }catch (Exception e){
                if(!field.getName().startsWith("is")){
                    methodName = "is"+((char)(field.getName().charAt(0)-32)+field.getName().substring(1));
                    try {
                        method = o.getClass().getMethod(methodName);
                    }catch (Exception e1){
                        replaceChar(i ,fields ,jsonBuffer );
                        continue;
                    }
                }
            }
            //通过反射获取到对象属性的值
            if(method != null){
                try {
                    fieldValue = method.invoke(o);
                } catch (Exception e) {
                    replaceChar(i,fields,jsonBuffer);
                    continue;
                }
            }
            //获取到值后对值得类型进行判断，如果是基本类型，不需要双引号
            //String类型添加双引号，List类型，递归遍历,其他引用类型遍历其所有的属性
            if(fieldValue != null){
                jsonBuffer.append("\""+field.getName()+"\":");
                if(fieldValue instanceof Integer ||
                        fieldValue instanceof Long ||
                        fieldValue instanceof Double ||
                        fieldValue instanceof Boolean){
                    jsonBuffer.append(fieldValue.toString());
                }else if(fieldValue instanceof String){
                    jsonBuffer.append("\""+fieldValue.toString()+"\"");
                }else if(fieldValue instanceof List<?>){
                    addListToBuffer(jsonBuffer,fieldValue);
                }else {
                    addObjectToJson(jsonBuffer,fieldValue);
                }
                jsonBuffer.append(",");
            }
            if(i == fields.size() -1 && jsonBuffer.charAt(jsonBuffer.length()-1) == ','){
                replaceChar(i,fields,jsonBuffer);
            }
        }
        jsonBuffer.append("}");
    }

    private static void addListToBuffer(StringBuffer jsonBuffer, Object fieldValue) {
        List<?> list = (List<?>) fieldValue ;
        jsonBuffer.append("[");
        for (int i = 0 ; i < list.size() ; i++){
            addObjectToJson(jsonBuffer,list.get(i));
            if(i < list.size()-1){
                //每个对象遍历完添加","
                jsonBuffer.append(",");
            }
        }
        jsonBuffer.append("]");
    }

    private static void replaceChar(int i, List<Field> fields, StringBuffer jsonBuffer) {
        if(i == fields.size() -1 && jsonBuffer.charAt(jsonBuffer.length()-1) == ',' ){
            jsonBuffer.deleteCharAt(jsonBuffer.length()-1);
        }
    }

    private static List<Field> getAllFields(List<Field> fields, Class<?> clz) {
        if (fields == null){
            fields = new ArrayList<>();
        }
        //获取对象除了Object外所有的父类
        if(clz.getSuperclass()!=null){
            Field[] declaredFields = clz.getDeclaredFields();
            for (Field field : declaredFields) {
                //排除final修饰的属性
                if(!Modifier.isFinal(field.getModifiers())){
                    fields.add(field);
                }
            }
            //获取父类中的属性
            getAllFields(fields,clz.getSuperclass());
        }
        return fields ;
    }
}
