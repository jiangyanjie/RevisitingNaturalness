#!/bin/bash

bugid=(1)

projectName=(Knox_assertion_common)

for i in ${!bugid[@]}
do

./walk.py /file/${projectName}/${projectName}_${bugid[$i]}_buggy /file/${projectName}/output_${projectName}_${bugid[$i]}_buggy
./walk.py /file/${projectName}/${projectName}_${bugid[$i]}_fix /file/${projectName}/output_${projectName}_${bugid[$i]}_fix
./walk.py /file/${projectName}/${projectName}_${bugid[$i]}_free /file/${projectName}/output_${projectName}_${bugid[$i]}_free

done
