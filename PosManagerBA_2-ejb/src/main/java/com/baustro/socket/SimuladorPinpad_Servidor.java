/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ba0100063v
 */
public class SimuladorPinpad_Servidor extends Thread {

    @Override
    public void run() {
        try {
            System.out.println("Hilo arriba");
            escuchaPinpad();
        } catch (Exception ex) {
            Logger.getLogger(SimuladorPinpad_Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void escuchaPinpad() throws IOException {
        ServerSocket server = new ServerSocket(9001);
        boolean infinitoC = true;
        while (infinitoC) {
            System.out.println("Bucle infinito");
            try (Socket conexion = server.accept()) {
                System.out.println("__SOCKET LEVANTADO");
                server.setSoTimeout(5000);
                DataOutputStream outToClient = new DataOutputStream(conexion.getOutputStream());
                Random rnd = new Random();
                Integer numeroAleatorio = (int) (rnd.nextDouble() * 999999 + 100000);
                String numeroAleatorioString = String.valueOf(numeroAleatorio);
                String tramaPinpad = "0202PP000300AUTORIZACION OK.    0000050046001017402017092" + numeroAleatorioString + "612131434000000332976                                                                                                                                VISA                     03MAISINCHO/RODRIGO                       000000000030VISA CREDITO                                                                                 0200008000E800476397XXXXXX5009         2103F145E656952ACAAB0E27E897AC2819586B80F7015F59E3143013A2C4BB134331                           01AB6463DD21100A0A9BD80286CAE7D8";
                System.out.println("Mensaje simulador Pinpad: " + "Dirección: " + conexion.getLocalSocketAddress() + "     Trama: " + tramaPinpad);
                outToClient.writeBytes("Dirección: " + conexion.getLocalSocketAddress() + "     Trama: " + tramaPinpad);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }
}
