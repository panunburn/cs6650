#CS6650 Project 4
Weiran Guo

##How to build
Sample Usage for server:
javac *.java

java ServerAppRMI <port number> <start port> <end port>

For simplicity, I assume all servers have continuous port number, for example,
from 1234 to 1238. So to run server cluster, simply run

java ServerApp 1234 1234 1238
...
java ServerApp 1238 1234 1238

The 1234 is the port number you want to use for this server.

Sample Usage for Client:
java ClientApp localhost 1234

you can run as many clients as you want, but you need to first run the server.

for put/get/delete, simply enter following in the textbox:
put one 1
get one
delete one

For switching server, simply enter following in the textbox:
switch

then enter
<ip> <port>
e.g.
switch
localhost 1235

The operation remains same after switching the server.


To mimic the failure of server, you can let server sleep from client side.
e.g.
sleep 200000

One sample
![Alt Text](https://github.com/panunburn/cs6650/blob/main/project4/src/sample%20run.gif)
