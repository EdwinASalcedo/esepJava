package com.example.helloworld;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class InMemoryDB {
    private Map<String, Integer> data;
    private boolean inTransaction;
    private Map<String, Integer> transactionData;

    public InMemoryDB() {
        this.data = new HashMap<>();
        this.inTransaction = false;
        this.transactionData = new HashMap<>();
    }
    public void begin_transaction() {
        if(!inTransaction)
        {
            transactionData.clear();
            inTransaction = true;
        }
    }
    public void put(String key, int value) {
        if (!inTransaction)
            throw new IllegalStateException("Transaction not in progress");
        // Otherwise transaction IS in progress
        transactionData.put(key, value);
    }
    public int get(String key) {
        if (data.containsKey(key))
            return data.get(key);
        return -1; // doc says null but get() has to return int
    }
    public void commit() {
        if (inTransaction) {
            // commit changes
            data.putAll(transactionData);
            transactionData.clear();
            inTransaction = false;
        }
        else
            throw new IllegalStateException("Transaction not in progress");
    }
    public void rollback() {
        if (inTransaction) {
            transactionData.clear();
            inTransaction = false;
        }
        else
            throw new IllegalStateException("Transaction not in progress");
    }

    public static void main(String[] args) {
        InMemoryDB db = new InMemoryDB();
        System.out.println("ESEP Extra Credit Assignment!\nPrinting out commands in order of Fig 2 from the assignment document.\n");

        //System.out.println();
        System.out.println(db.get("A")); //return null or -1

//        db.put("A", 5); // throw error because inTransaction = false

        db.begin_transaction(); // starts transaction process so now inTransaction = true
        db.put("A", 5); // set value of A to 5 but not committed yet!
        System.out.println(db.get("A")); // return null or -1 since A is not committed yet!
        db.put("A", 6); //update A's value to 6 within the transaction
        db.commit(); // commits the open transaction and sets inTransaction = false
        System.out.println(db.get("A")); // returns 6

//        db.commit(); // throws error because no open transaction
//        db.rollback(); // throws error because no open transaction

        System.out.println(db.get("B")); // return null because B does not exist

        db.begin_transaction();
        db.put("B", 10);
        db.rollback(); // B should not exist still
        System.out.println(db.get("B")); // Should return null because B was rolled back
    }
}