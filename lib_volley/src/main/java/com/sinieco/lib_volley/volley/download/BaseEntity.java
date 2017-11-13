package com.sinieco.lib_volley.volley.download;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author BaiMeng on 2017/11/10.
 */

class BaseEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L ;
    public BaseEntity(){
    }

    public T copy(){
        ByteArrayOutputStream baos = null ;
        ObjectOutputStream oos = null ;
        try{
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(this);

            ByteArrayInputStream bis = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object result = ois.readObject() ;
            return (T)result ;
        }catch (IOException ioException){
            ioException.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        } finally {
            if(baos != null){
                try{
                    baos.close();
                }catch (IOException io){
                    io.printStackTrace();
                }
            }
            if(oos != null){
                try{
                    oos.close();
                }catch (IOException io){
                    io.printStackTrace();
                }
            }
        }
        return null ;
    }

}
