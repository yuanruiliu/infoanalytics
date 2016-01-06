#!/usr/bin/env python
import matplotlib.pyplot as plt
import sys


PATH_PLOT = "giant-component.png"


def main(frand, ftarg=None):
    with open(frand) as f:
        randres = [int(line.strip()) for line in f]
    plt.plot(range(0, 100*len(randres), 100), randres, color='blue',
            label='Random edge removal')
    if ftarg is not None:
        with open(ftarg) as f:
            targres = [int(line.strip()) for line in f]
        plt.plot(range(0, 100*len(targres), 100), targres, color='green',
                label='Targeted edge removal')
    plt.xlabel("Number of edges removed")
    plt.ylabel("Size of the giant component")
    plt.legend()
    plt.savefig(PATH_PLOT)
    print "Plot saved as '{}'.".format(PATH_PLOT)


if __name__ == '__main__':
    if len(sys.argv) == 2:
        main(sys.argv[1])
    elif len(sys.argv) == 3:
        main(sys.argv[1], sys.argv[2])
    else:
        print "Usage: {} path/to/random [path/to/targeted]".format(sys.argv[0])
