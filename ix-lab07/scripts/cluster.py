import numpy
import sys

if (__name__ == '__main__'):
    if (len(sys.argv) != 5):
       print 'usage: python cluster.py <topic-document-distribution.txt> <corpus.txt> <topic-id-to-show> <number-doc-to-print>\n'
       sys.exit(1)
    
    data_file = sys.argv[1]
    text_file = sys.argv[2]
    topic = int(sys.argv[3])
    ndoc = int(sys.argv[4])
    
    with open(text_file) as f:
        content = f.readlines()
    data = numpy.loadtxt(data_file)
    
    m,n = numpy.shape(data)
    pdoc = 0
    for i in range(m):
        if pdoc < ndoc:
            if numpy.argmax(data[i,:]) == topic:
                if len(content[i]) < 1000:
                    print content[i]
                else:
                    print content[i][0:1000]
                print '\n'
                pdoc += 1