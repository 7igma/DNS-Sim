/*
    Steven Nunes
    CS521 DNS Assignment
*/

import java.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class RootServer
{
  public static void main(String[] args)
  {
    try
    {
      InetAddress ip;
      ArrayList<ArrayList<String>> registry = loadRegistry();

      System.out.println("Root Server: DNS Resolving Simulation");
      ServerSocket listener = new ServerSocket(4546);

      while (true)
      {
        //accepting client query
        Socket sock = listener.accept();
        InputStream is = sock.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String message = br.readLine();
        System.out.println("Root Server: Server wants to find "+message);

        //checking for domain name
        String response = lookUp(registry, message);

        //responding to server
        if (response == null)
        {
          System.out.println("Root Server: Unknown domain name '"+message+"'");
        }
        else
        {
          System.out.println("Root Server: Address is "+response+", sending to server");
        }

        System.out.println("---");

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

  //looks up the given domain name in the registry to find the ip address
  public static String lookUp(ArrayList<ArrayList<String>> registry, String name)
  {
    int i;
    ArrayList<String> record;
    for (i = 0; i < registry.size(); i++)
    {
      record = registry.get(i);

      //if the name is found, return the corresponding address
      if (record.get(0).equals(name))
      {
        return record.get(1);
      }
    }
    return null; //if the domain name is not found return null
  }

  //loads the registry with some example websites
  public static ArrayList<ArrayList<String>> loadRegistry()
  {
    int i;
    ArrayList<String> record;
    ArrayList<ArrayList<String>> arr = new ArrayList<ArrayList<String>>();

    //the example websites are added to the server
    arr.add(new ArrayList<String>(Arrays.asList("facebook.com", "69.171.224.11")));
    arr.add(new ArrayList<String>(Arrays.asList("twitter.com", "199.59.149.230")));
    arr.add(new ArrayList<String>(Arrays.asList("reddit.com", "72.247.244.88")));
    arr.add(new ArrayList<String>(Arrays.asList("amazon.com", "72.21.211.176")));
    arr.add(new ArrayList<String>(Arrays.asList("google.com", "74.125.157.99")));
    arr.add(new ArrayList<String>(Arrays.asList("youtube.com", "74.125.65.91")));
    arr.add(new ArrayList<String>(Arrays.asList("bing.com", "65.55.175.254")));
    arr.add(new ArrayList<String>(Arrays.asList("yahoo.com", "98.137.149.56")));
    arr.add(new ArrayList<String>(Arrays.asList("newegg.com", "216.52.208.187")));
    arr.add(new ArrayList<String>(Arrays.asList("wikipedia.org", "208.80.152.201")));

    return arr;
  }
}
