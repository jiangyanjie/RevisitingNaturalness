#!/usr/bin/env python

import os
import sys

def write_single_line(input_file):
    words = []

    fin = open(input_file)
    fout = open(input_file+'.tt', 'w')
    while 1:
        try:
            line = fin.next().strip()
        except StopIteration:
            break

        words.extend(line.split())

        if len(words) > 40000:
            print >> fout, ' '.join(words)
            words = []
    
    if len(words) > 0:
        print >> fout, ' '.join(words)
    fin.close()
    fout.close()

    os.system('mv %s.tt %s' % (input_file, input_file))


def change(file_dir):
    for file in os.listdir(file_dir):
        if file.endswith('.tokens'):
            file = os.path.join(file_dir, file)
            os.system('cp %s %s.lines' % (file, file[:-7]))
            write_single_line(file)


if __name__ == '__main__':
    change(sys.argv[1])
