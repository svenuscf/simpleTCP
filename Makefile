JAVA=javac

all: simpleTCPserver simpleTCPclient

simpleTCPserver: simpleTCPserver.java
	$(JAVA) simpleTCPserver.java 

simpleTCPclient: simpleTCPclient.java
	$(JAVA) simpleTCPclient.java
