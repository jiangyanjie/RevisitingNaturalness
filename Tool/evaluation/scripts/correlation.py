#!/usr/bin/env python

import math
import sys

def average(x):
    assert len(x) > 0
    return float(sum(x)) / len(x)

def pearson_def(x, y):
    assert len(x) == len(y)
    n = len(x)
    assert n > 0
    avg_x = average(x)
    avg_y = average(y)
    diffprod = 0
    xdiff2 = 0
    ydiff2 = 0
    for idx in range(n):
        xdiff = x[idx] - avg_x
        ydiff = y[idx] - avg_y
        diffprod += xdiff * ydiff
        xdiff2 += xdiff * xdiff
        ydiff2 += ydiff * ydiff

    return diffprod / math.sqrt(xdiff2 * ydiff2)

def read_data(input):
    stats = [float(num.strip()) for num in open(input, 'rU')]
    return stats

stats1 = read_data(sys.argv[1])
stats2 = read_data(sys.argv[2])

print ' '.join([str(num) for num in stats1])
print ' '.join([str(num) for num in stats2])
print pearson_def(stats1, stats2)
