#!/usr/bin/env python

from sys import argv
from math import sqrt

def get_results(input):
    scores = [float(line.split(' ||| ')[0]) for line in open(input, 'r').readlines()]
    
    return scores

if len(argv) != 3:
    print './cal_significance.py baseline test'
else:
    baseline = get_results(argv[1])
    test = get_results(argv[2])
    
    greater_number = 0
    less_number = 0
    equal_number = 0
    
    for i in xrange(len(baseline)):
        if test[i] > baseline[i]:
            greater_number += 1
        elif test[i] < baseline[i]:
            less_number += 1
        else:
            equal_number += 1

    N = greater_number + less_number

    if greater_number > N/2:
        Z = greater_number - 0.5
    else:
        Z = greater_number + 0.5

    Z = (Z - N/2)/(sqrt(N)/2)

    print ''
    if Z >= 3.1:
        print 'Significance at p0.001 level'
    elif Z >= 2.33:
        print 'Significance at p0.01 level'
    elif Z >= 1.65:
        print 'Significance at p0.05 level'
    else:
        print 'Not significance'
    
    print '\n'
    print 'Z:', Z
    print 'N:', N
    print 'Greater:', greater_number
    print 'Less:', less_number

