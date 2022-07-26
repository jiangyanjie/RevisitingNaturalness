#!/usr/bin/python

import sys

info = '''
Author: Zhaopeng Tu
Date: 2013-09-19
Function: get accuracy of different projects
'''

#projects = ['ant', 'batik', 'cassandra', 'eclipse-e4', 'log4j lucene', 'maven2', 'maven3', 'xalan-j', 'xerces']
#projects = ['git', 'libgit2', 'macvim', 'nu', 'wax', 'iProxy', 'linux-2.6', 'mongrel2', 'redis', 'xbmc']

sizes = [0, 100, 200, 500, 1000, 3000, 5000, 7000, 9000, 10000000]

def get_value(line):
    pos = line.find('\t')
    return line[pos+1:].strip()


def get_accuracy(project_file, model):
    projects = [project.strip() for project in open(project_file).readlines()]

    accuracies = []
    accuracies.append(['%d' % size for size in sizes])
    for project in projects:
        project_accuracy = [[], []]
        for size in sizes:
            if size != 0:
                fin = open('%s/%s.%s.%d.dynamic.lambda.accuracy' % (project, project, model, size))
            else:
                fin = open('%s/%s.backoff.accuracy' % (project, project))
            lines = fin.readlines()
            for line in lines:
                if 'top1' in line:
                    project_accuracy[0].append(get_value(line))
                elif 'top5' in line:
                    project_accuracy[1].append(get_value(line))
    
        accuracies.extend(project_accuracy)
 
    for accuracy in accuracies:
        print '\t'.join(accuracy)


if __name__ == '__main__':
    print info + '\n'

    if len(sys.argv) < 3:
        print './get_accuracy.py projects model'
        sys.exit(-1)

    get_accuracy(sys.argv[1], sys.argv[2])
