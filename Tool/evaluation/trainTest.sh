#!/bin/bash

projectName=(Closure)
bugid=(33 34 127)
version=(fix) #free buggy 


for i in ${!bugid[@]}
do

for j in ${!version[@]}
do


mkdir -p /home1/empirical4j/${projectName}/${projectName}_${bugid[$i]}_${version[$j]}_entropy


for LINE in `cat /home1/empirical4j/${projectName}/output_${projectName}_${bugid[$i]}_${version[$j]}/fileinfo1.txt`
do   
   str=$LINE
   OLD_IFS="$IFS"
   IFS=","
   arr=($str)
   IFS="$OLD_IFS"
   P1=${arr[0]}
   
mkdir -p ./data/sample_project/files

cp -a /home1/empirical4j/${projectName}/output_${projectName}_${bugid[$i]}_${version[$j]}/. data/sample_project/files

cp data/sample_project/files/${P1}.code.java.tokens data/sample_project 

rm data/sample_project/files/${P1}.code.java.tokens

./bin/train.py data/sample_project 3

mv ./data/sample_project/${P1}.code.java.tokens ./data/sample_project/files/

cat /dev/null > ./data/sample_project/fold0.test #clear the test list

echo 'data/sample_project/files/'${P1}'.code.java.tokens' > ./data/sample_project/fold0.test

rm -r results
./entropy.bat

mkdir -p /home1/empirical4j/${projectName}/${projectName}_${bugid[$i]}_${version[$j]}_entropy/${P1}

mv ./results/entropy/sample_project/file.cache.ngram.order.file.dynamic.lambda.log /home1/empirical4j/${projectName}/${projectName}_${bugid[$i]}_${version[$j]}_entropy/${P1}/

rm -rf ./data/sample_project/*

done

done

done


