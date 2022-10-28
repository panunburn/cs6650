# cs6650
##HW2
##Weiran Guo
###How to run
javac *.java

For Single Thread Server and Client, e.g.
java SingleThreadServer 32000
java Client localhost 32000

Then type in any string to get a reverse string.

For multi-thread server and client, e.g.
java MultithreadServer 32000

Then you can create any number of clients you want.

For Sort List of Java RMI, e.g. run
java SortListServer

then the server will use port 1099, then run 

java SortListClient

it will return sorted list of 10,9,8...3,2,1

