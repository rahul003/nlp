#!/bin/bash


for i in 1000 2000 3000 4000 5000 7000 10000 13000 17000 21000
do 
 echo br_`$i`k_wsj0222_wsj23
 echo dadapt -train brown $i -adapt wsj0222 -test wsj23
 echo "Done"
done
