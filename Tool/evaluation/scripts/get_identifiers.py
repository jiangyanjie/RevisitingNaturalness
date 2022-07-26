#!/usr/bin/env python

from sys import argv

fin = open(argv[1])

fout = open(argv[2], 'w')

identifiers = set()

while True:
    try:
        line = fin.next().strip()
    except StopIteration:
        break

    items = line.split()

    for item in items:
        try:
            token, type = item.split('<=>')
            if type == 'Identifier' and (token != 'STRLIT' and token != 'CHARLIT'):
                identifiers.add(token)
        except:
            pass

print >> fout, '\n'.join(list(identifiers))

