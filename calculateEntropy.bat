# clone source code of each bug in involved repositories

./Tool/getRepo.sh

# calculate entropies based on employed tool

./Tool/preprocess/auto.sh

./Tool/evaluation/trainTest.sh

# adjust entropies to balance the entropies of different statements

javac Revisiting.java
java Revisiting
