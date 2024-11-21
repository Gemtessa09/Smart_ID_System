import java.util.Scanner;
public class hd {
    public  static void main(String[] args)
    {
System.out.println("hello world");

for(int i=0;i<=5 ;i++)
System.out.println(i);
String get ="hello";
String name = "world";
String message = get + "" + name;
System.out.println(message);
System.out.println(message.length());
System.out.println(message.toUpperCase());
char fristchar = message.charAt(0);

System.out.println(fristchar);
String submessage = message.substring(7,10);
System.out.println(submessage);
boolean containWorld = message.contains("world");
System.out.println(containWorld);

Scanner sc =new Scanner(System.in);

int [] arr = new int [5];

for (int i = 0; i < 5; i++) {
    System.out.println("enter array"+(i+1));
    arr[i] = sc.nextInt();
    
}
System.out.println("entered element are:");
for (int i = 0; i < 5; i++) {
    System.out.println(arr[i] +" ");
    
}

    }

}
