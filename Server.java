/*
    Steven Nunes
    CS521 DNS Assignment
*/

import java.util.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server
{
  public static void main(String[] args)
  {
    try
    {
      InetAddress ip;

      //using a hashmap that removes oldest entry when full (size = 5) for the cache
      LinkedHashMap<String, String> cache = new LinkedHashMap<String, String>()
      {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, String> eldest)
        {
          return this.size() > 5;
        }
      };

      //setting up socket
      System.out.println("Server: DNS Resolving Simulation");
      ip = InetAddress.getLocalHost();
      System.out.println("Server: My IP Address is " + ip.getHostAddress());
      ServerSocket listener = new ServerSocket(4545);

      while (true)
      {
        //accepting client query
        Socket sock = listener.accept();
        InputStream is = sock.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String message = br.readLine();
        System.out.println("Server: Client wants to find "+message);

        //check cache for client request
        String response = checkCache(cache, message);
        if (response != null)
        {
          System.out.println("Server: Domain name found in cache with IP "+response);
          System.out.println("Server: Sending to client");
        }
        //if not in cache, ask the root server
        else
        {
          System.out.println("Server: Domain name not in cache, asking root server server");
          response = askRootServer(message);
          if (response == null)
          {
            System.out.println("Server: Root server cannot resolve the domain name");
          }
          else
          {
            System.out.println("Server: Root server says "+response+", adding to cache");
            cache = addToCache(cache, message, response);
          }
        }
        System.out.println("---");


        //returning response to client
        PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
        out.println(response);
        sock.close();
      }
    }
    catch (IOException e)
    {
      System.out.println("Error communicating with the client");
      return;
    }
  }

  //checks this server's personal cache for the response
  public static String checkCache(LinkedHashMap<String, String> cache, String name)
  {
    String result;
    if (cache.containsKey(name))
    {
      //we place this entry back at the front of the queue
      result = cache.get(name);
      cache.remove(name);
      cache.put(name, result);
      return result;
    }
    else
    {
      return null;
    }
  }

  //forwards the client request to the root server
  public static String askRootServer(String msg)
  {
    try
    {
      //sending domain name to root server
      InetAddress ip = InetAddress.getLocalHost();
      Socket sock = new Socket(ip.getHostAddress(), 4546); //assuming the root server is on the same address as this server for this simulation
      OutputStream os = sock.getOutputStream();
      OutputStreamWriter osw = new OutputStreamWriter(os);
      BufferedWriter bw = new BufferedWriter(osw);
      String message = msg + "\n";
      bw.write(message);
      bw.flush();

      //getting root server's response
      BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
      String response = br.readLine();
      if (response.equals("null"))
      {
        return null;
      }
      else
      {
        return response;
      }
    }
    catch (IOException e)
    {
      return null;
    }
  }

  //adding the record to the cache
  public static LinkedHashMap<String, String> addToCache(LinkedHashMap<String, String> cache, String name, String address)
  {
    cache.put(name, address);
    return cache;
  }
}
