Steven Nunes
CS 521 DNS Assignment Instructions

---

Compilation Command (from within this folder):

javac Client.java Server.java RootServer.java

---

Run all 3 components on 3 different command prompt windows:

java Client
java Server
java RootServer

---

Once the 3 are running, the server will provide its IP address. Enter this 
into the client.

You can now type website domain names and get responses from the server.

The server will first check its cache (which stores the 5 most recent requests)

If not found, the server asks the RootServer which has a database of 10 websites.


---

Websites that are in the RootServer database:

facebook.com
twitter.com
reddit.com
amazon.com
google.com
youtube.com
bing.com
yahoo.com
newegg.com
wikipedia.org
