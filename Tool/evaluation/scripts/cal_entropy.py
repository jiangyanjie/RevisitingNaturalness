#!/usr/bin/env python

from sys import argv

def get_value(line):
    start = line.find(': ')
    return float(line[start+2:])

fin = open(argv[1], 'rU')

count = 0
total = 0.0

while True:
    try:
        line = fin.next().strip()
    except StopIteration:
        break

    if 'Entropy: ' in line:
        total += get_value(line)
        count += 1

print 'Entropy:\t%.2f' % (total / count)

