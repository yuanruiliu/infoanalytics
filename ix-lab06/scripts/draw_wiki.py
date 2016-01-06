import matplotlib.pyplot as plt
import numpy as np
import networkx as nx
import sys

if (__name__ == '__main__'):

    if (len(sys.argv) != 5):
       print 'usage: python draw_wiki.py <wiki-graph> <wiki-communities> <wiki-nodeslist> <Wiki-article>'
       sys.exit(1)
    
    nodes = list()
    nodesnei = list()
    cnt = 0
    G=nx.MultiGraph()
    nodecomm = dict()
    vocab = dict()
    with open(sys.argv[3]) as infile:
        for line in infile:
            line = line.replace("\n", "")
            vocab[str(cnt)] = line
            cnt += 1
    with open(sys.argv[2]) as infile:
        for line in infile:
            line = line.replace('\n','')
            split = line.split('\t')
            nodecomm[vocab.get(split[0])] = int(split[1])
    cnt = 0


            
            
    with open(sys.argv[1]) as infile:
        for line in infile:
            line = line.replace('\n','')
            split = line.split('\t')
            if len(split) > 3:
                print 'Error in file format'
                sys.exit()
            if len(split) == 2:
                G.add_edge(vocab.get(split[0]),vocab.get(split[1]),weight=1)
            else:
                G.add_edge(vocab.get(split[0]),vocab.get(split[1]),weight=int(split[2]))

    if sys.argv[4] not in G.nodes():
        print 'The garph wikipedia for school does ont have this node.'
        sys.exit(1)
    G = nx.ego_graph(G, sys.argv[4] ,1,center=True, undirected=False, distance=None)
    nodes_pos = nx.spring_layout(G)
    node_color=[float(nodecomm.get(v)*3+1) for v in G]
    node_list=G.nodes()

        
    nodes_pos = nx.spring_layout(G)
    nx.draw_networkx_nodes(G,nodes_pos,alpha = 0.5,nodelist=node_list,node_size=200,node_color=node_color)
    nx.draw_networkx_labels(G,nodes_pos,font_size=10,font_weight='bold')
    plt.show()