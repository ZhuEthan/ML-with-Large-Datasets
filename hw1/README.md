## How to build it
> ant build
It will generate two jar file in build/ directory. 

## How to run it
> cat data/RCV1.small_train.txt | java -jar build/Train-1.0.0.jar | java -jar build/Test-1.0.0.jar data/RCV1.small_test.txt

## Reference
http://www.cs.cmu.edu/~yipeiw/TA605/hw1A/hashtable-nb_s15.pdf