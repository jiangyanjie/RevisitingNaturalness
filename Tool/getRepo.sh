#!/bin/bash
bugid=(1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22)

projectName=(JxPath)
subName=(commons-geometry-core)

mkdir /file/${projectName}

for i in ${!bugid[@]}
do

defects4j checkout -p ${projectName} -v ${bugid[$i]}b -w /file/${projectName}/${projectName}_${bugid[$i]}_buggy # -s ${subName}
defects4j checkout -p ${projectName} -v ${bugid[$i]}f -w /file/${projectName}/${projectName}_${bugid[$i]}_fix  # -s ${subName}


done
