package oops;
//stack:variables,references,function calls in organized form(i.e primitive types)
//that is getting referred from the method:stack memory size is very less compared to heap memory
//contains method-specific values that are short lived and references to other objects in the heap
//stack memory is thread safe as each thread makes its own call stack(no sync needed)


//heap:unorganized form: objects etc(non-primitive types)
//to allocate memory to objects and jre classes.Any abject created in heap space has global access and can be referenced from 
//anywhere of the application
//heap memory is not thread safe as thread can be accessed anywhere(synchronization is needed)

//garbage collector:deletes periodically,unreferenced objects


class Data{
    int data;
    public void printData(){
        System.out.println(data);
    }
}
class childData extends Data{
    @Override
    public void printData(){
        System.out.println("overridden"+data);
    }
    public void insideChild(){

    }
}

public class oops8 {
    public static void main(String[] args) {
        int a=5;        //this wont go into heap area:goes into stack area


        //if an object is created,it is stored in heap area:and the changes will be affected since the actual object is being changed
        Data obj=new Data();        
        obj.data=5;

        changeValue(a,obj);
        System.out.println(a+" "+obj.data);      //since actual object has not been changed though function call,the value remained same

        





        //DYNAMIC METHOD DISPATCH: runtime polymorphism
        //relation is   :is-a relation        : childData is-a Data
        //left side:parent      right side:child        only methods of parent are allowed
        Data d;
        d=new childData();          //only access methods of Data
        //d.insidechild();      is invalid

        childData cd=new childData();
        cd.insideChild();           //is valid

        d.printData();      //here call to the overridden method is checked at rumtime 
        // which method of parent or childclass should be linked with the call

    }
    static void changeValue(int a,Data obj){
        a=10;

        obj.data=10;
    }

    
}
