#!/usr/bin/python

import sys
import string

info = '''
Author: Zhaopeng Tu
Date: 2013-09-18
Function: duplicate the command for different projects
'''

#projects = ['ant', 'batik', 'cassandra', 'eclipse-e4', 'log4j lucene', 'maven2', 'maven3', 'xalan-j', 'xerces']
#projects = ['git', 'libgit2', 'macvim', 'nu', 'wax', 'iProxy', 'linux-2.6', 'mongrel2', 'redis', 'xbmc']

def duplicate(input, project_file, output):
    fin = open(input, 'r')

    projects = [project.strip() for project in open(project_file).readlines()]

    fout = open(output, 'w')

    print >> fout, '#!/bin/bash\n#$ -cwd\n#$ -j y\n#$ -S /bin/bash'

    lines = [line.strip() for line in fin.readlines()]

    for project in projects:
        for line in lines:
            print >> fout, line.replace('sample', project)


if __name__ == '__main__':
    print info + '\n'

    if len(sys.argv) < 3:
        print './build_pb.py sample_file projects output_file'
        sys.exit(-1)

    duplicate(sys.argv[1], sys.argv[2], sys.argv[3])
