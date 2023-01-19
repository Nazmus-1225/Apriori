package apriori;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Apriori {
    public static void main(String[] args) throws IOException {
        int minimumSupportCount;
        System.out.println("Enter minimum support count: ");
        Scanner sc=new Scanner(System.in);
        minimumSupportCount=sc.nextInt();
        ArrayList<Set<String>> transactionDatabase = readFile();
        Set<Set<String>> uniqueItemset = new HashSet<Set<String>>();
        HashMap<Set<String>, Integer> result=generateUniqueItems(transactionDatabase,uniqueItemset,minimumSupportCount);
        generateCandidateKeys(transactionDatabase,result,minimumSupportCount);
        generateConfidence(result);
    }
    public static ArrayList<Set<String>> readFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("Book1.csv"));
        ArrayList <Set<String>> transactionDatabase = new ArrayList<>();
        String line;
        while((line = reader.readLine()) != null){
            String[] items = line.split(",");
            Set<String> transaction = new HashSet<>();
            for (String item : items) {
                transaction.add(item);
            }

            transactionDatabase.add(transaction);
        }
        reader.close();
        return transactionDatabase;
    }

    public static HashMap<Set<String>, Integer> generateUniqueItems(ArrayList <Set<String>> transactionDatabase,Set<Set<String>> uniqueItemset,int minimumSupportCount){
        HashMap<Set<String>, Integer> result=new HashMap<Set<String>, Integer>();
        Iterator<Set<String>> itr = transactionDatabase.iterator();
        while(itr.hasNext()){
            Iterator<String> itr2=itr.next().iterator();
            while(itr2.hasNext()){
                Set<String> item=new HashSet<String>();
                item.add(itr2.next());
                if(!result.containsKey(item)) result.put(item,1);
                else{
                    result.put( item,(result.get(item))+1);
                }
            }}
        uniqueItemset=result.keySet();
        Iterator itr3=result.entrySet().iterator();
        while(itr3.hasNext()) {
            Map.Entry mapElement = (Map.Entry) itr3.next();
            if ((int) mapElement.getValue() < minimumSupportCount) {
                itr3.remove();
            }
        }
        return result;
    }

    public static void generateCandidateKeys(ArrayList <Set<String>> transactionDatabase, HashMap<Set<String>, Integer> result,int minimumSupportCount){
       int k=1;
       
        while(true){HashMap<Set<String>, Integer> temp=new HashMap<>();
        temp=result;
        HashMap<Set<String>, Integer> temp2=new HashMap<>();
        Iterator itr4=temp.entrySet().iterator();
        while(itr4.hasNext()){
            Map.Entry mapElement=(Map.Entry)itr4.next();
            Set<String> X= (Set<String>) mapElement.getKey();

            Iterator itr5=temp.entrySet().iterator();
            while(itr5.hasNext()){
                Map.Entry mapElement1=(Map.Entry)itr5.next();
                Set<String> Y= (Set<String>) mapElement1.getKey();
                Set<String> item=new HashSet<>();
                item.addAll(X);
                item.addAll(Y);
                if(item.size()>k) {
                temp2.put(item,0);}
            }
        }
        
        
        HashMap<Set<String>, Integer> temp3=new HashMap<>();
        temp3=temp2;
        

        Iterator<Set<String>> itr = transactionDatabase.iterator();

        while(itr.hasNext()){
            Set<String> item=itr.next();
            Iterator itr2=temp2.entrySet().iterator();
            
            while(itr2.hasNext()){
            	Map.Entry mapElement=(Map.Entry)itr2.next();
                Set<String> item2= (Set<String>) mapElement.getKey();
                if(item.containsAll(item2)) {
                	temp3.put(item2,temp3.get(item2)+1);}
            }}
        
        Iterator itr3=temp3.entrySet().iterator();
        while(itr3.hasNext()){
            Map.Entry mapElement=(Map.Entry)itr3.next();
            if((int)mapElement.getValue()<minimumSupportCount){
                itr3.remove();}
        }
        if(temp3.size()==0) {break;}
        result.putAll(temp3);
        k++;}
        
        Iterator itr3=result.entrySet().iterator();
        while(itr3.hasNext()){
            Map.Entry mapElement=(Map.Entry)itr3.next();
            System.out.println(mapElement.getKey()+"  "+mapElement.getValue());
        }
    }
    
    public static void generateConfidence(HashMap<Set<String>, Integer> result) {
    	String premise,conclusion;
    	Scanner sc=new Scanner(System.in);
    	Set<String> combined=new HashSet<String>(); 
    	System.out.println("Enter premises: ");
        premise=sc.nextLine();
        String[] items=premise.split(",");
        Set<String> premises=new HashSet<String>();
        for(String item:items) {
        	premises.add(item);
        	combined.add(item);
        }
        System.out.println("Enter conclusion: ");
        conclusion=sc.nextLine();
        items=conclusion.split(",");
        Set<String> conclusions=new HashSet<String>();
        for(String item:items) {
        	conclusions.add(item);
        	combined.add(item);
        }
        double confidence=0,premiseSupport=0,combinedSupport=0;
        Iterator itr=result.entrySet().iterator();
        while(itr.hasNext()) {
        	Map.Entry mapElement=(Map.Entry)itr.next();
        	if(mapElement.getKey().equals(premises)) {premiseSupport=(int)mapElement.getValue();}
        	if(mapElement.getKey().equals(combined)) {combinedSupport=(int)mapElement.getValue();}
        }
        if(premiseSupport!=0) {confidence=combinedSupport/premiseSupport;}
        System.out.println("Confidence of "+premises+" ----->  "+conclusions+":  "+confidence*100+"%");
    }
}
