#!/usr/bin/make -f
COMP=fsc

all:
	$(COMP) *.scala

clean:
	rm -rf *.class org/
