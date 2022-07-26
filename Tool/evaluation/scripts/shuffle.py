#!/usr/bin/env python

from sys import argv, exit
from random import shuffle

info = '''
Author: Zhaopeng Tu
Date:   2013-09-19
Usage:  Shuffle the order of lines in a file
'''

if __name__=='__main__':
    print info
    if len(argv) != 2:
        print "./shuffle.py input\n"
        exit(1)
    
    lines = [line.strip() for line in open(argv[1], 'r').readlines()]
    shuffle(lines)
    
    print >> open(argv[1], 'w'), '\n'.join(lines)

