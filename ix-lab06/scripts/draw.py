import matplotlib.pyplot as plt
import numpy as np
import networkx as nx
import sys

if (__name__ == '__main__'):
    if (len(sys.argv) != 3):
       print 'usage: python draw.py <graph> <communities>'
       sys.exit(1)
    nodes = list()
    nodesnei = list()
    cnt = 0
    G=nx.MultiGraph()
    nodecomm = dict()
    with open(sys.argv[2]) as infile:
        for line in infile:
            line = line.replace('\n','')
            split = line.split('\t')
            nodecomm[split[0]] = int(split[1])
    cnt = 0


    with open(sys.argv[1]) as infile:
        for line in infile:
            line = line.replace('\n','')
            split = line.split('\t')
            if len(split) > 3:
                print 'Error in file format'
                sys.exit()
            if len(split) == 2:
                G.add_edge(split[0],split[1],weight=1)
            else:
                G.add_edge(split[0],split[1],weight=int(split[2]))
            
    nodes_pos = nx.spring_layout(G)
    node_color=[float(nodecomm.get(v)+1) for v in G]
    node_list=G.nodes()
    nx.draw_networkx_edges(G,nodes_pos,alpha=0.8,edge_color='k', width=0.6) 
    nx.draw_networkx_nodes(G,nodes_pos,alpha = 0.5,nodelist=node_list,node_size=200,node_color=node_color)
    nx.draw_networkx_labels(G,nodes_pos,font_size=10,font_weight='bold')
    plt.show()                           
    