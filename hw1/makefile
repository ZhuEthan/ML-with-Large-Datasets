DS_PATH = .

VS_TRAIN = $(DS_PATH)/RCV1.very_small_train.txt
VS_TEST = $(DS_PATH)/RCV1.very_small_test.txt

S_TRAIN = $(PDS_PATHA)/RCV1.small_train.txt
S_TEST = $(DS_PATH)/RCV1.small_test.txt

FULL_TRAIN = $(DS_PATH)/RCV1.full_train.txt
FULL_TEST = $(DS_PATH)/RCV1.full_test.txt

.SUFFIXES: .java .class 

.java.class:
	javac $<

CLASSES = NB.class NBTrain.class NBTest.class

all: $(CLASSES)

NBTrain: NBTrain.class

NBTest: NBTest.class

demo: NBTrain NBTest
	cat $(VS_TRAIN) | java NBTrain | java NBTest $(VS_TEST)

demo_small: NBTrain NBTest
	cat $(S_TRAIN) | java NBTrain | java NBTest $(S_TEST)

demo_full: NBTrain NBTest
	cat $(FULL_TRAIN) | java NBTrain | java NBTest $(FULL_TEST)

test: NBTrain NBTest
	cat $(VS_TRAIN) | java NBTrain | java NBTest $(TESTFILE)

clean:
	rm $(CLASSES) *~
