/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pablito
 */
public class MsgFile {

    private static final String FILENAME = "Temporal.txt";

    public static void write(String s) {

        FileWriter aWriter = null;
        try {

            aWriter = new FileWriter(System.getProperty("jboss.server.config.dir") + "/" + FILENAME, true);

            aWriter.write(s + "\n");
            aWriter.flush();
        } catch (Exception ex) {
        } finally {
            if (aWriter != null) {
                try {
                    aWriter.close();
                } catch (IOException ex) {
                }
            }
        }

    }

    public static String getText() {
        File fichaEntrada = new File(System.getProperty("jboss.server.config.dir") + "/" + FILENAME);
        FileInputStream canalEntrada = null;
        try {
            canalEntrada = new FileInputStream(fichaEntrada);
            byte[] bt = new byte[(int) fichaEntrada.length()];
            canalEntrada.read(bt);
            return new String(bt);
        } catch (IOException e) {

        } finally {
            if (canalEntrada != null) {
                try {
                    canalEntrada.close();
                } catch (IOException ex) {
                    Logger.getLogger(MsgFile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return "";
    }

    public static void deleteFile() {
        try {

            File file = new File(System.getProperty("jboss.server.config.dir") + "/" + FILENAME);

            if (file.delete()) {
                System.out.println(file.getName() + " is deleted!");
            } else {
                System.out.println("Delete operation is failed.");
            }

        } catch (Exception e) {
            System.err.println("---------------- ERROR: " + e.getMessage());

            e.printStackTrace();

        }
    }
}
