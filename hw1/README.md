## How to build it
> ant build

It will generate two jar file in build/ directory. 

## How to run it
> cat data/RCV1.small_train.txt | java -jar build/Train-1.0.0.jar | java -jar build/Test-1.0.0.jar data/RCV1.small_test.txt

## Sample Output: 
```
The prediction is CCAT compared with real label: C13,CCAT,GCAT,GDIS
The prediction is MCAT compared with real label: E12,ECAT
The prediction is CCAT compared with real label: C12,C13,C15,CCAT,GCAT,GCRIM
The prediction is CCAT compared with real label: C33,CCAT,M14,M143,MCAT
The prediction is CCAT compared with real label: C11,C41,C411,CCAT
...
```

## Reference
http://www.cs.cmu.edu/~yipeiw/TA605/hw1A/hashtable-nb_s15.pdf