/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_thread_overview;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

public class WindowDialog implements ActionListener {

    private static JTextArea tf6;
    private static JTextField tf1, tf2, tf3, tf4, tf5;
    private JButton b1, b2;
    private PrimeChecker pc1;
    private boolean click = false;

    //creates window dialog
    WindowDialog() {
        JFrame f = new JFrame("Multithreaded Primary Digit Checker");
        //closes jvm on click of close button
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //position of dialog on window screen
        f.setBounds(550, 200, 600, 650);

        tf1 = new JTextField();
        tf2 = new JTextField();
        tf3 = new JTextField();
        tf4 = new JTextField();
        tf5 = new JTextField();
        tf6 = new JTextArea();

        Font font = tf1.getFont();
        float fontSize = font.getSize() + 5.0f;

        tf1.setFont(font.deriveFont(fontSize));
        tf1.setBounds(60, 50, 450, 50);
        tf1.setEditable(false);
        tf1.setText("Minimal prime found : ");

        tf2.setFont(font.deriveFont(fontSize));
        tf2.setBounds(60, 95, 450, 50);
        tf2.setEditable(false);
        tf2.setText("Maximal prime found : ");

        tf3.setFont(font.deriveFont(fontSize));
        tf3.setBounds(60, 140, 450, 50);
        tf3.setEditable(false);
        tf3.setText("Last processed file :");

        tf4.setFont(font.deriveFont(fontSize));
        tf4.setBounds(60, 185, 450, 50);
        tf4.setEditable(false);
        tf4.setText("Files processed : ");

        tf5.setFont(font.deriveFont(fontSize));
        tf5.setBounds(60, 230, 450, 50);
        tf5.setEditable(false);
        tf5.setText("Thread Count : ");

        tf6.setFont(font.deriveFont(fontSize));
        tf6.setEditable(true);
        tf6.setText("Thread Log..");
        JScrollPane scroll = new JScrollPane(tf6);
        scroll.setBounds(60, 282, 450, 245);

        f.add(scroll);
        f.add(tf1);
        f.add(tf2);
        f.add(tf3);
        f.add(tf4);
        f.add(tf5);

        b1 = new JButton("Run Job");
        b1.setBounds(430, 528, 80, 50);
        b1.addActionListener(this);
        f.add(b1);

        b2 = new JButton("+");
        b2.setBounds(380, 528, 50, 50);
        b2.addActionListener(this);
        b2.setToolTipText("Add Thread");
        f.add(b2);

        f.setLayout(null);
        f.setVisible(true);
    }

    /**
     * Action listener for buttons
     *
     * @param e action event
     */
    public void actionPerformed(ActionEvent e) {
        boolean plusBtn = e.getActionCommand().contains("+") ? true : false;
        boolean runJobBtn = e.getActionCommand().contains("Run Job") ? true : false;

        //on click + button call addtask method
        if (plusBtn) {
            addTask();
        }

        //on click of Run job button starts application
        if (!click && runJobBtn) {
            try {
                pc1 = new PrimeChecker();
                Runnable r1 = () -> {
                    pc1.producer();
                };
              
                Runnable r2 = () -> {
                    pc1.consumer();
                };
                Thread t1 = new Thread(r1, "Thread-1");
                Thread t2 = new Thread(r2, "Thread-2");
               
                t1.start();
                t1.join(1000);
                t2.start();
               
                click = true;
            } catch (Exception ex) {
                System.out.println("java_thread_overview.Window_Dialog.actionPerformed() Exceptione::" + ex.getMessage());;
            }
        }

        System.out.println("java_thread_overview.TextFieldExample.actionPerformed()");
    }

    /**
     * Adds a new Thread (Live Threads Adjustment ) only increment threads 1 producer and one consumer
     */
    private void addTask() {
       
        Runnable r1 = () -> {
            pc1.consumer();
        };
        new Thread(r1, "Thread - " + new Random().nextInt(90)).start();
    }

    public static JTextField getTf1() {
        return tf1;
    }

    public static JTextField getTf2() {
        return tf2;
    }

    public static JTextField getTf3() {
        return tf3;
    }

    public static JTextField getTf4() {
        return tf4;
    }

    public static JTextField getTf5() {
        return tf5;
    }

    public static JTextArea getTf6() {
        return tf6;
    }

    public PrimeChecker getPc1() {
        return pc1;
    }
}
