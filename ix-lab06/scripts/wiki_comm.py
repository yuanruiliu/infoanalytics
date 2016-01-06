import matplotlib.pyplot as plt
import numpy as np
import networkx as nx
import sys

if (__name__ == '__main__'):

    if (len(sys.argv) != 5):
       print 'usage: python draw_wiki.py <wiki-communities> <wiki-nodeslist> <Wiki-article> <nb-printed-articles>'
       sys.exit(1)
    
    nodes = list()
    nodesnei = list()
    cnt = 0
    G=nx.MultiGraph()
    nodecomm = dict()
    vocab = dict()
    wikivocab = dict()
    with open(sys.argv[2]) as infile:
        for line in infile:
            line = line.replace("\n", "")
            vocab[str(cnt)] = line
            wikivocab[line] = cnt
            cnt += 1
    with open(sys.argv[1]) as infile:
        for line in infile:
            line = line.replace('\n','')
            split = line.split('\t')
            nodecomm[vocab.get(split[0])] = int(split[1])
    cnt = 0
    
    
    if sys.argv[3] not in wikivocab:
        print 'The garph wikipedia for school does ont have this node.'
        sys.exit(1)
        
    ncom = nodecomm[sys.argv[3]]
    ncnt = 0
    for node, com in nodecomm.iteritems():
        if ncnt >= int(sys.argv[4]):
            break
        if com == ncom:
            print node
            ncnt+=1