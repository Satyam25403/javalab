import java.io.*;
class Book{
    BufferedReader b=new BufferedReader(new InputStreamReader(System.in));
    String BookName,author,publisher;
    int isbn;
    Book(){
        BookName="java";
        isbn=1000;
        author="satyam";
        publisher="hall";
    }
    //getters
    public void getbook_name()throws IOException{
        System.out.println("enter book name: ");
        BookName=b.readLine();
    }
    public void getisbn()throws IOException{
        System.out.println("enter isbn num: ");
        isbn=Integer.parseInt(b.readLine());
    }
    public void getauthor()throws IOException{
        System.out.println("enter author name: ");
        author=b.readLine();
    }
    public void getpubname()throws IOException{
        System.out.println("enter publishers name: ");
        publisher=b.readLine();
    }
    //setters
    public void setbook_name(String name){
        this.BookName=name;
    }
    public void setisbn(int name){
        this.isbn=name;
    }
    public void setauthor(String name){
        this.author=name;
    }
    public void setpub(String name){
        this.publisher=name;
    }
    public void getBookInfo(){
    System.out.println("book name: "+BookName);
    System.out.println("isbn number: "+isbn);
    System.out.println("author: "+author);
    System.out.println("publisher: "+publisher);        
    }
}
public class TestBook {
    public static void main(String args[])throws IOException{
        BufferedReader b= new BufferedReader(new InputStreamReader(System.in));
        Book[] book=new Book[30];
        System.out.println("number of boook: ");
        int n=Integer.parseInt(b.readLine());
        System.out.println("enter 1 for get methods and 2 for set methods:");
        int ch=Integer.parseInt(b.readLine());
        try{
            if(ch==1){
                for(int i=0;i<n;i++){
                    System.out.printf("enter %d book details:",i+1);
                    book[i]=new Book();
                    book[i].getbook_name();
                    book[i].getisbn();
                    book[i].getauthor();
                    book[i].getpubname();
                }
            }
            else{
                int i=0;
                book[i]=new Book();
                book[i].setbook_name("java");
                book[i].setisbn(43);
                book[i].setauthor("paul");
                book[i].setpub("mcgrawhill");
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        for(int i=0;i<n;i++){
                book[i].getBookInfo();
                System.out.println();
        }
    }
}

