@echo off
start java -cp lib/Lego.jar rmi.nameserver.NameServer -p 8888
start java -cp lib/Lego.jar impl.server.Controller -p 8888 -n Robot_1 -sim 1
start java -cp lib/Lego.jar impl.client.Controller -p 8888