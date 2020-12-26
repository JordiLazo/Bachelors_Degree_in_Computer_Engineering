#!/bin/bash
while : ; do let "a = ($a + 1) % 10" ; echo -n "$a" ; done
