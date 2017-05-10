#  Verteilte Systeme Praktikum
## Call nameservice
java -cp Lego.jar rmi.nameserver.NameServer [-p \<port\>]
### info
Parameter:<br />
-p \<port\> default: 8888
## Call provider
java -cp CaDSBase.jar:./Lego.jar impl.server.Controller [-n \<namespace\>] [-h \<hostname\>] [-p \<port\>] [-sim {0,1}]
### info
Parameter:<br />
-n \<namespace\> default: Robot[random number]<br />
-h \<hostname\> default: 255.255.255.255<br />
-p \<port\> default: 8888<br />
-sim {0,1} 0: simulation off, 1: simulation on, default: 0
## Call consumer
java -cp CaDSBase.jar:./Lego.jar impl.client.Controller [-h \<hostname\>] [-p \<port\>]
### info
Parameter:<br />
-h \<hostname\> default: 255.255.255.255<br />
-p \<port\> default: 8888<br />
