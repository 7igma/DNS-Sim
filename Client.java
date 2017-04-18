/*
    Steven Nunes
    CS521 DNS Assignment
*/

import java.util.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.lang.NullPointerException;

public class Client
{
  public static void main(String[] args)
  {
    try
    {
      String input = "";
      String response = "";
      String serverAddress = "";
      InputStreamReader input_reader;
      BufferedReader input_in, server_in;
      Socket sock;

      //first asks the user to enter the IP address of the server (provided when you run Server.java)
      System.out.println("Client: DNS Resolving Simulation");
      input_reader = new InputStreamReader(System.in);
      input_in = new BufferedReader(input_reader);
      System.out.print("Enter IP Address of the DNS server: ");
      serverAddress = input_in.readLine();

      while (true)
      {
        //prompts client to enter a domain name
        input_reader = new InputStreamReader(System.in);
        input_in = new BufferedReader(input_reader);
        System.out.print("Enter domain name (e.g. 'google.com'): ");
        input = input_in.readLine();

        //if exit is typed, exit
        if (input.equals("exit"))
        {
          System.out.println("Client: Exiting");
          return;
        }
        //if other input is provided, request this domain name from the server
        else
        {
          if (!input.equals(""))
          {
            sock = new Socket(serverAddress, 4545);

            //Send the request to the server
            OutputStream os = sock.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            String message = input + "\n";
            bw.write(message);
            bw.flush();


            //get server response
            server_in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            response = server_in.readLine();
            System.out.println("Client: Server has responded with "+response);
            System.out.println("---");
          }
        }
      }
    }
    catch (IOException e)
    {
      System.out.println("Error - unable to connect to DNS server");
      return;
    }
    catch (NullPointerException e)
    {
      return;
    }
  }
}
