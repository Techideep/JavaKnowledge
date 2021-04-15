package com.serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializationDemo {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Dog d1 = new Dog();
        FileOutputStream fos = new FileOutputStream("E:\\Resume\\abc");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(d1);
        
        FileInputStream fis = new FileInputStream("E:\\Resume\\abc");
        ObjectInputStream ois = new ObjectInputStream(fis);
        
        Dog d2 = (Dog) ois.readObject();
        System.out.println(d2.i + "........." + d2.j);
    }

}

class Dog implements Serializable{
    transient int i=10;
    static transient int j =20;
}
/*NotSerializableException:
class Dog {
    int i=10;
    int j =20;
}*/