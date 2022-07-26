#!/usr/bin/env python

from sys import argv

def get_value(line):
    start = line.find(': ')
    end = line.find('')
    return float(line[start+2:])

fin = open(argv[1], 'rU')

count = 0
mrr_total = 0.0
top1_total = 0.0
top5_total = 0.0
top10_total = 0.0

while True:
    try:
        line = fin.next().strip()
    except StopIteration:
        break

    if 'Mean' in line:
        mrr_total += get_value(line)
    if 'Total top1 accuracy:' in line:
        top1_total += get_value(line)
    elif 'Total top5 accuracy:' in line:
        top5_total += get_value(line)
    elif 'Total top10 accuracy:' in line:
        top10_total += get_value(line)
        count += 1

print 'MRR:\t%.2f' % (mrr_total / count * 100)
print 'Top1 accuracy:\t%.2f' % (top1_total / count * 100)
print 'Top5 accuracy:\t%.2f' % (top5_total / count * 100)
print 'Top10 accuracy:\t%.2f' % (top10_total / count * 100)

