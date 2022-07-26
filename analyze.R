library(tidyverse)
library(rstatix)
library(ggpubr)
library(dplyr)
library("data.table")
library(beanplot)

# calculate the relation between buggy line and fixed line in GrowingBugs

#prepare data
data1 <- read.table("/Data/GrowingBugs/buggy Lines.txt", header = T)
data2 <- read.table("/Data/GrowingBugs/fixed Lines.txt", header = T)
data <- rbind(data1,data2)
# Bean Plot
beanplot(entropy ~ type, data = data, ll =0, method = "jitter", ylim = c(-2,3), ylab="Entropy")

# Calculate Metrics (for example: buggy lines in GrowingBugs)
length(data1)
mean(data1)
max(data1)
min(data1)
quantile(data1)

#Wilcoxon non-parametric test
stat.test <- data
  wilcox_test(entropy ~ type) 
  add_significance()

stat.test
wilcox_effsize(entropy ~ type)

#------------------------------------------------------------------------

# # calculate the relation between bug-free line and buggy line in GrowingBugs

# # prepare data

# data1 <- read.table("/Data/GrowingBugs/bug-free Lines.txt", header = T)
# data2 <- read.table("/Data/GrowingBugs/buggy Lines.txt", header = T)
# data <- rbind(data1,data2)

# # Bean Plot

# beanplot(entropy ~ type, data = data, ll =0, method = "jitter", ylim = c(-2,3), ylab="Entropy")

# # Calculate Metrics (for example: bug-free lines in GrowingBugs)

# length(data1)
# mean(data1)
# max(data1)
# min(data1)
# quantile(data1)

#--------------------------------------------------------------------------------

# calculate the relation between buggy line and fixed line in Defects4J

#prepare data
# data1 <- read.table("/Data/Defects4J/buggy Lines.txt", header = T)
# data2 <- read.table("/Data/Defects4J/fixed Lines.txt", header = T)
# data <- rbind(data1,data2)
# # Bean Plot
# beanplot(entropy ~ type, data = data, ll =0, method = "jitter", ylim = c(-2,3), ylab="Entropy")

# # Calculate Metrics (for example: buggy lines in Defects4J)
# length(data1)
# mean(data1)
# max(data1)
# min(data1)
# quantile(data1)

# #Wilcoxon non-parametric test
# stat.test <- data
#   wilcox_test(entropy ~ type) 
#   add_significance()

# stat.test
# wilcox_effsize(entropy ~ type)

#------------------------------------------------------------------------

# # calculate the relation between bug-free line and buggy line in Defects4J

# # prepare data

# data1 <- read.table("/Data/Defects4J/bug-free Lines.txt", header = T)
# data2 <- read.table("/Data/Defects4J/buggy Lines.txt", header = T)
# data <- rbind(data1,data2)

# # Bean Plot

# beanplot(entropy ~ type, data = data, ll =0, method = "jitter", ylim = c(-2,3), ylab="Entropy")

# # Calculate Metrics (for example: bug-free lines in Defects4J)

# length(data1)
# mean(data1)
# max(data1)
# min(data1)
# quantile(data1)

#--------------------------------------------------------------------------------


