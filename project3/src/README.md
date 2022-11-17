#CS6650 Project 3
Weiran Guo

##How to build
Sample Usage for server:
javac *.java

java ServerAppRMI <port number> <coordinator ip> <coordinator port>

For creating a coordinator, simply run
java ServerAppRMI 1234
The 1234 is the port number you want to use for coordinator, ignore the last two arguments.

For creating a replica server, run
java ServerAppRMI 1235 localhost 1234
The 1235 is the port number you want to use for this replica server, 
Localhost is the coordinator IP address, and 1235 is the coordinator port number.

You can run as many server as you want, and it can run on different machines with valid public ip and port.

Sample Usage for Client:
java ClientAppRMI localhost 1234

you can run as many clients as you want, but you need to first run the server.

for put/get/delete, simply enter following in the textbox:
put one 1
get one
delete one

For switching server, simply enter following in the textbox:
switch <ip> <port>
e.g.
switch localhost 1235

The operation remains same after switching the server.

![Alt Text](https://github.com/panunburn/cs6650/blob/main/project3/src/cmd%20sample.gif)