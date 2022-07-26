#!/usr/bin/python

import sys

info = '''
Author: Zhaopeng Tu
Date: 2013-09-30
Function: get mrr of different projects
'''

#projects = ['ant', 'batik', 'cassandra', 'eclipse-e4', 'log4j lucene', 'maven2', 'maven3', 'xalan-j', 'xerces']
#projects = ['git', 'libgit2', 'macvim', 'nu', 'wax', 'iProxy', 'linux-2.6', 'mongrel2', 'redis', 'xbmc']

sizes = [0, 100, 200, 500, 1000, 3000, 5000, 7000, 9000, 10000000]

def get_value(line):
    pos = line.find('\t')
    return line[pos+1:].strip()

def get_mrr(project_file, model):
    projects = [project.strip() for project in open(project_file).readlines()]

    mrrs = []
    mrrs.append(['%d' % size for size in sizes])
    for project in projects:
        project_mrr = []
        for size in sizes:
            if size != 0:
                fin = open('%s/%s.%s.%d.dynamic.lambda.accuracy' % (project, project, model, size))
            else:
                fin = open('%s/%s.backoff.accuracy' % (project, project))
            lines = fin.readlines()
            for line in lines:
                if 'MRR' in line:
                    project_mrr.append(get_value(line))
                        
        mrrs.append(project_mrr)
 
    for mrr in mrrs:
        print '\t'.join(mrr)


if __name__ == '__main__':
    print info + '\n'

    if len(sys.argv) < 3:
        print './get_mrr.py projects model'
        sys.exit(-1)

    get_mrr(sys.argv[1], sys.argv[2])
