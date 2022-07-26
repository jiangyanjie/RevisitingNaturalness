#!/usr/bin/env python

import os, sys

def walk_dir(input_dir, output_dir, topdown=True):
    files_list = []
    count = 0

    os.system('mkdir -p %s' % output_dir)
    for root, dirs, files in os.walk(input_dir, topdown):
        for name in files:
            if name.endswith('.java'):
                name = os.path.join(root,name)
                name = name.replace(' ', '\ ')
                output = '%s/%d.code.java' % (output_dir, count)
                print output
                count += 1
                os.system("cat %s | ./bin/remccoms3.sed | sed '/^\s*$/d' > %s" % (name, output))
                os.system('java -jar bin/LexJava-1.0.jar %s' % output)
                files_list.append(name)

    fout = open('%s/fileinfo.txt' % output_dir, 'w')
    for i in xrange(len(files_list)):
        print >> fout, '%d\t%s' % (i, files_list[i])
    fout.close()
        
    os.system('./bin/write_line.py %s' % output_dir)

    print 'all files:', len(files_list)
 

if __name__ == '__main__':
    if len(sys.argv) < 3:
        print './walk.py input_dir output_dir\n'
        sys.exit()
    
    '''
    topdown = False
    if len(sys.argv) > 3 and sys.argv[3] == 'topdown':
        topdown = True
    
    walk_dir(sys.argv[1], sys.argv[2], topdown)
    '''

    walk_dir(sys.argv[1], sys.argv[2])
