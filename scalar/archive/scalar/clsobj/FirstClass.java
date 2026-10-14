package clsobj;

public class FirstClass {

    int a = 10;

    FirstClass() {
        System.out.println("FirstClass constructor");
        System.out.println("Value of a: " + a);
    }

    public static void main(String[] args) {
        FirstClass objFC = new FirstClass();
        objFC.printHello();

    }

    public void printHello() {
        System.out.println("Printint Hello...");
        System.out.println("Printing the value of from PH : " + this.a);
    }

}
