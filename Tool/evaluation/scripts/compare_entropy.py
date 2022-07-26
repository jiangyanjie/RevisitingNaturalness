#!/usr/bin/env python

from sys import argv

def get_value(line):
    start = line.rfind(':')
    return float(line[start+1:].strip())

fin1 = open(argv[1], 'rU')
fin2 = open(argv[2], 'rU')

count = 0
total1 = 0.0
total2 = 0.0
min_improve = 100
max_improve = -100

while True:
    try:
        line1 = fin1.next().strip()
        line2 = fin2.next().strip()
    except StopIteration:
        break

    accuracy1 = get_value(line1)
    accuracy2 = get_value(line2)

    total1 += accuracy1
    total2 += accuracy2

    improve = accuracy2 - accuracy1
    if improve < min_improve:
        min_improve = improve
    if improve > max_improve:
        max_improve = improve

    count += 1

print argv[2], 'improves over', argv[1]
print 'averaged:', (total2-total1)/count
print 'min:', min_improve
print 'max:', max_improve

