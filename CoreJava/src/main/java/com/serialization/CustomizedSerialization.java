package com.serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CustomizedSerialization  {

    public static void main(String args[]) throws Exception{
        Account acc= new Account();
        acc.setName("Deepti");
        acc.setPassword("Password");
        FileOutputStream fos = new FileOutputStream("test.scr");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(acc);

        FileInputStream fis = new FileInputStream("test.scr");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Account acc1 = (Account) ois.readObject();
        System.out.println(acc1.getName()+"    "+acc1.getPassword());

    }


}

class Account implements Serializable {
    private String name;
    private transient String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    private void writeObject(ObjectOutputStream oos) throws Exception {
        oos.defaultWriteObject();
        String encryptedPassword = this.password +"123";
        oos.writeObject(encryptedPassword);
    }

    private void readObject(ObjectInputStream ois)throws Exception{
        ois.defaultReadObject();
        String encryptedPassword =(String)ois.readObject();
        this.password=encryptedPassword.substring(0,encryptedPassword.length()-3);
    }
}
