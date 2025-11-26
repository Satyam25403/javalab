//collection framework provides a set of interfaces and classes to implement various datastructures and algorithms
//Collection(List,Set(SortedSet),Queue(Deque));Map(SortedMap);Iterator(ListIterator)    are some interfaces

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;


//collection interface methods:size,isEmpty,contains(object o):O(n),add(E e),remove(Object o/int index),bool containsAll(Collection<> c)
//bool addAll(Collection<> c):O(n), bool removeAll(Collection<> c){remove common elements}, bool retainAll(Collection<> c){to find intersection}, void clear:O(1), Object[] toArray
//all these are inherited by implementing classes and interfaces


public class CollectionsFramework {
    //List interface:implemented by 4 classes(ArrayList,LinkedList,Stack,Vector)
    public static void main(String[] args) {

        List<Integer> list=new ArrayList<>();       //since arraylist implements list, a ref of list can hold arraylist object
        list.add(20);list.add(10);list.add(30);
        //arraylist is used in case of traditional arrays where size of array is not known forehand: arraylists are dynamic in size
        System.out.println(list.size());
        List<Integer> list1=new ArrayList<>(); 
        list1.add(1);list1.add(2);list1.add(3);     
        list.addAll(list1);
        System.out.println(list);
        //the above functions work the same for Linked list also



        //list interface extends colle tion interface and adds methods that are specific to lists(which are ordered collections allowing duplicates)
        //some methods that are in List interface but not in Collection interface
        //get(int index),set(int index,E element){to replace},add(int index,E element):O(n),remove(int index)overridden method of collection interface,
        //indexOf(Object o):O(n){returns -1 if not present},lastIndexOf(Object o),listIterator(),listIterator(int index){iterator which starts at a particular index}
        //subList(int fromIndex(inclusive),int toIndex(exclusive))
        System.out.println(list.get(0));
        list.set(1,200);
        list.add(1,300);
        list.remove(3);
        System.out.println(list);

        List<String> fruits=new ArrayList<>();
        fruits.add("kiwi");fruits.add("Papaya");fruits.add("Mango");fruits.add("Apple");fruits.add("Orange");
        Iterator<String> fe=fruits.listIterator();      //return iterator pointing to before first element
        //we can also use iterator() in place of listIterator()
        while(fe.hasNext()){
            System.out.println(fe.next());
        }

        List<String> smallList=fruits.subList(1, 3);
        System.out.println(smallList);



        Queueimplement();
        setImplementation();
        mapImplementation();
        LC l=new LC();
        l.learnComparator();
        //ARRAYLIST allow us to create resizable arrays:when capacity of the array is reached,ArrayList creates a new larger array 
        //and copies the elements from old array to new one
        //resizing logic newsize=(oldsize*3)/2 +1

        

        //LINKEDLIST: doubly linked list;elements in the linked lists are not stored in sequence,they are scattered and connected 
        //through links(Prev and Next)
        //to see the usage of LinkedList, class just replace ArrayList with LinkedList and all other things remain same as in the above example



        //VECTOR:same like the LinkedList but Vector synchronizes each individual operation(whenever we try to perform some operation 
        //Vector class automatically applies lock to that operation).If one thread accesses a vector,and if other thread tries to access it at same time,
        //ConcurrentModificationException is generated.Hence continuous use of lock makes vectors less effecient
        //hence to avoid overhead, it is recommended to use ArrayLists instead of Vectors whenever possible as in arraylists methods are not synchronized


        //STACK:void push(<E> item),E pop(),E peek(),boolean isEmpty(){from list}/empty()

    }
    //QUEUE interface:provides functionality of queue data structure(Extends collection framework)
    //implemented by 3 classes:ArrayDeque,LinkedList,PriorityQueue
    //methods in queue interface: bool add(E e) throws exception,bool offer(E e) hence use this most of times   {both of them do same :add element}
    //E remove() throw exception if nothing there,E poll() return null if nothing there{both of them do same job}
    //E element() retrieve but not remove element at front(throws exception if empty),E peek() returns null if empty
    //hence use offer(),poll(),peek()

    static void Queueimplement(){
        Queue<Integer> q=new LinkedList<>();
        q.offer(12);
        q.offer(1);
        q.offer(3);
        q.offer(45);
        System.out.println(q.poll());
        System.out.println(q.peek());





        ArrayDeque<Integer> q1=new ArrayDeque<>();       //doubly ended queue:any of the end can be considered as front and the other as rear
        //addFirst(E e),offerFirst(E e),addLast(E e),offerLast(E e);
        //removeFirst(),pollFirst(),removeFirst(),pollLast();
        //getFirst(),peekFirst() start of queue; getlast(),peekLast() end of queue;      {to return elements}

        q1.offerFirst(10);
        q1.offerLast(20);
        System.out.println(q1.poll());
        System.out.println(q1.pollLast());

        //stack and queue operations in ARRAYDEQUE class:whenever we need to use a stack, it is recommended to use arraydeque
        //because Stack uses vector which is threadsafe locks need to be acquired hence is slow
        // stack operations:push(E e),pop(),peek()
        // queue operations:add(E e) or offer(E e) and remove() or poll()

        ArrayDeque<Integer> stack=new ArrayDeque<>();
        stack.push(10);stack.push(20);stack.push(30);
        System.out.println(stack.pop());

        ArrayDeque<Integer> queue=new ArrayDeque<>();
        queue.add(10);queue.add(20);queue.add(30);
        System.out.println(queue.poll());





        //PriorityQueue:elements added in any order but min(magnitude) priority comes out first:a min heap is followed
        Queue<Integer> pq=new PriorityQueue<>();
        pq.add(30);pq.add(40);pq.add(10);pq.add(20);
        System.out.println(pq);

        //if we dont want to use default property, we can pass comparator for the baasis of comparision
        Queue<Integer> pqr=new PriorityQueue<>((a,b)->b-a);//making higher magnitude higher priority
        pqr.add(30);pqr.add(40);pqr.add(10);pqr.add(20);
        System.out.println(pqr);

    }

    //SET INTERFACE:implemented by EnumSet,HashSet(most used because most methods have o(1) complexity),LinkedHashSet and TreeSet classes
    //provides features of mathematical set in java:dont allow duplicates
    //methods: add(),addAll(),remove(),removeAll(),retainAll() retain all elements from set that are present in specified set,clear(),size(),contains()
    static void setImplementation(){

        //Hashset is commonly used if we have to access elements randomly as elements are accessed using hashcodes.
        //hashcode is unique identity to identify an element in hash table.for same inputs hashcodes will be same in hashset 
        //if we are using the type as some custom class objects,even objects with same attributes will be treated different
        //to avoid this we will be overriding equals() and hashCode() method based on deciding parameter
        Set<Integer> set=new HashSet();     //when we use get we dont know what element comes:they are arranged in random order
        set.add(10);set.add(10);set.add(20);set.add(50);set.add(9);    //add(),remove(),contains() all have O(1) complexity
        System.out.println(set);

        Set<Integer> set1=new LinkedHashSet();      //to add elements in order implementing linked list internally
        set1.add(10);set1.add(10);set1.add(20);     //add O(1),remove O(n)
        System.out.println(set1);

        Set<Integer> set2=new TreeSet<>();          //implemented using binary search tree :most methods complexity O(logn)
        set2.add(10);set2.add(10);set2.add(20);set2.add(50);set2.add(9);
        System.out.println(set2.contains(9));
        System.out.println(set2);

        EnumSet<Color> enset=EnumSet.allOf(Color.class);    //not used generally
    }
    enum Color{
        RED,YELLOW,GREEN;
    }


    //MAP INTERFACE:maps are stored in key value pairs;keys are unique: each key is associated with a single value
    //we need to specify how two keys shold be unique overriding equals() and hashCode() methods

    //implemented by 5 classes:HashMap(Most useful),TreeMap,EnumMap,LinkedHashMap,WeakHashMap

    //Note:This doesnt inherits collection interface and hence collection interface methods are not available in it

    //methods:put(k,v)insert key k with value v,if key already present new value replaces old value,
    //putAll() put all entries of a map into this map,putIfAbsent(k,v),get(k) if key not found return null,
    //getOrDefault(k,defaultValue) return value associated with key and if not found, return default value
    //containsKey(k)-check if key is present,remove(k);containsValue(v);replace(k,v) replace value of key k with new value
    //replace(k,oldval,newval) replace value of key k with newval only if key k is associated with oldval
    //remove(k)remove entry associated with k,remove(k,v) remove entry from map with key k and value v
    //keySet() returns set of all keys,values() returns the set of all values,entrySet() return set of all key/value mappings present in map
    static void mapImplementation(){
        Map<Integer,String> m=new HashMap<>();          //mostly time complexity is O(1)
        m.put(1,"satyam");m.put(2,"nithish");m.put(3,"bhai");
        m.put(1,"gongali");
        m.remove(1);m.put(59,"ManchalaRaj");

        Set<Integer> s=m.keySet();
        Collection<String> values=m.values();     //here values() returns a collection 

        Set<Map.Entry<Integer, String>> entries=m.entrySet();       //keyvalue pairs are stored as entries
        for(Map.Entry<Integer,String> entry: entries){
            System.out.println(entry.getKey()+" "+entry.getValue());   
        }
        //we can iterate in keys as well as in values in similar manner

        System.out.println(s);
        System.out.println(values);
        System.out.println(m);
        System.out.println(entries);
    }
    
}











class Animal implements Comparable<Animal>{
//comparable interface is a functional interface that contains only one method named compareTo(object):to sort elements on basis of single data member
//return 0 if equal;positive integer if current object is greater than specified object;negative if less
    int age,weight;
    String name;
    Animal(int age,int weight,String name){
        this.age=age;
        this.weight=weight;this.name=name;
    }
    @Override
    public String toString(){
        return name+" "+age+" "+weight;
    }
    //getters
    public int getAge(){
        return this.age;
    }
    public String getName(){
        return this.name;
    }

    @Override
    public int compareTo(Animal that) {
        //current object is this; specified object is o
        if(this.age==that.age){
            //if age equal sort on basis of name
            //we are able to do this because string class implements comparable interface
            return this.name.compareTo(that.name);
        }
        return this.age-that.age;
    }
    
}
//Comparable and Comparator:
//To sort custom objects like students employees etc we need to explicitly provide sorting logic
//we can do this by implementing Comparable interface:comparable and comparator allow us
// to define custom sorting behaviour for objects including sorting based on multiple data members
//Comparable is used for natural sorting order:but code needs to be changed based on the usecase;
//Comparator is used for custom sorting order:versatile and flexible use

class MyCustomComparator implements Comparator<Animal>{
    @Override
    public int compare(Animal o1, Animal o2) {
        //we can sort on basis of any data member
        //this dont need to disturb the animal class
        return Integer.compare(o1.weight, o2.weight);
    }
}
class LC{
    void learnComparator(){
        Animal a1=new Animal(4,10,"leo");
        Animal a2=new Animal(3,90,"Bruno");
        Animal a3=new Animal(5,90,"jack");
        Animal a4=new Animal(1,8,"maxo");
        Animal a5=new Animal(3,4,"don");        

        List<Animal> dogs=new ArrayList<>();
        dogs.add(a1);dogs.add(a2);dogs.add(a3);dogs.add(a4);dogs.add(a5);
        System.out.println(dogs);

        //dogs are not comparable right now as they are custom class objects
        //so we need to make them comparable by implementing comparable interface
        Collections.sort(dogs,new MyCustomComparator());
        System.out.println(dogs);

        Collections.sort(dogs,new Comparator<Animal>() {
            @Override
            public int compare(Animal a,Animal b){
                return a.name.compareTo(b.name);
            }
        });
        //same thing can be done using lambda expression
        //Collections.sort(dogs,(o1,o2)-> o1.name.compareTo((o2.name)));
        System.out.println(dogs);

        Collections.sort(dogs,Comparator.comparing(Animal::getAge).thenComparing(Animal::getName));
    }
}
