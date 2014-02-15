#!/bin/bash
start_time=`date +%s`
for i in 1 2 3 4 5 6 7 8 9 10
do
    cat links.tiny.train | java -Xmx128m NBTrain |
                          sort -k1,1 -t ';' -T . | java -Xmx128m MergeCounts > /dev/null
done
end_time=`date +%s`
echo execution time was `(expr $end_time - $start_time)` s.