/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_thread_overview;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Hp
 */
public class PrimeChecker {
    //folder location where files are being kept

    final File folderLoc = new File("C:\\Users\\Hp\\Documents\\upwork_projects\\project1\\work\\Test Files");
    final int fileCount = folderLoc.listFiles().length;
    // C:\\Users\\Hp\\Documents\\upwork_projects\\project1\\work\\Test Files
    //C:\\Users\\Hp\\Documents\\upwork_projects\\project1\\rand_files
    //concurrent class Blocking queue
    BlockingQueue<Long> qNum = new LinkedBlockingQueue<>();
    BlockingQueue<String> qFiles = new LinkedBlockingQueue<>();
    //Using Atomic classes as data is changing between thread
    AtomicLong minPrime = new AtomicLong();
    AtomicLong maxPrime = new AtomicLong();
    AtomicBoolean setIstPrime = new AtomicBoolean(false);
    AtomicInteger fileProcessCnt = new AtomicInteger(0);

    //Text fields of window dialog
    static JTextField tf1 = WindowDialog.getTf1();
    static JTextField tf2 = WindowDialog.getTf2();
    static JTextField tf3 = WindowDialog.getTf3();
    static JTextField tf4 = WindowDialog.getTf4();
    static JTextField tf5 = WindowDialog.getTf5();
    static JTextArea tf6 = WindowDialog.getTf6();

    /**
     * Check a number is prime number or not.
     *
     * @param num number to be checked
     * @return true is number is prime else return false
     */
    public static boolean isPrime(long num) {
        try {
            for (int i = 2; i <= num / i; i++) {
                if (num % i == 0) {
                    return false;
                }
            }

        } catch (Exception e) {
            System.out.println("java_thread_overview.PrimeChecker.isPrime()::" + e.getMessage());
        }
        return num > 1;
    }
    //Producer/Consumer Design Pattern

    /**
     * Producer Task read files from directory and put in queue
     */
    public void producer() {
        for (File file : folderLoc.listFiles()) {
            qFiles.add(file.getAbsolutePath());
            tf6.append("\nProducer Produces: " + file.getAbsolutePath() + " Thread Name: " + Thread.currentThread().getName());
            System.out.println("java_thread_overview.PrimeChecker.producer()" + file.getAbsolutePath());
            sleepThread(1000);
        }
        System.out.println("PRODUCER THREAD OUT::" + Thread.currentThread().getName());
    }

    /**
     * Consumer task read file and find prime
     */
    public void consumer() {
        BufferedReader br;
        String filePath;

        try {
            filePath = qFiles.poll();
            tf3.setText("Last File Updated: " + filePath);
            //if there are no files in producer queue this will throw error and terminate thread
            br = new BufferedReader(new FileReader(filePath));
            String text;
            //read files content line by line
            while ((text = br.readLine()) != null) {
                long number = Integer.parseInt(text);
                //put number from file into queu
                if (!setIstPrime.get() && isPrime(number)) {
                    setIstPrime.set(true);
                    minPrime.set(number);
                    maxPrime.set(number);
                }
                //Display Max and Min Found Primary Number
                //check for min prime number
                if (isPrime(number) && number <= minPrime.get()) {
                    minPrime.set(number);
                    tf1.setText("Minimal prime found: " + String.valueOf(minPrime.get()));
                }
                //check for maximum prime number
                if (isPrime(number) && number >= maxPrime.get()) {
                    maxPrime.set(number);
                    tf2.setText("Maximal prime found: " + String.valueOf(maxPrime.get()));
                }
                //Update window dialog text view
                tf5.setText("Thread Count : " + String.valueOf(Thread.activeCount()));
                tf6.append("\nConsumer Consumed: " + number + " Thread Name: " + Thread.currentThread().getName());
                tf6.setCaretPosition(tf6.getDocument().getLength());
                sleepThread(300);
            }
            //update file count
            fileProcessCnt.set((fileProcessCnt.get() + 1));
            //Display Number of Files Done 
            tf4.setText("Files Processed: " + String.valueOf(fileProcessCnt.get()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Consumer THREAD OUT::" + Thread.currentThread().getName());
    }

    /**
     * Sleep thread upto certain duration
     *
     * @param ms time in milisecond
     */
    public void sleepThread(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
