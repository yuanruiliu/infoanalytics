#!/usr/bin/env python
import sys
import matplotlib.pyplot as plt


PATH_LINPLOT = "degdist-lin.png"
PATH_LOGPLOT = "degdist-log.png"


def lin_plot(data):
    degrees, freqs = zip(*data)
    plt.bar(degrees, freqs, label="Degree distribution")
    plt.xlabel("Degree")
    plt.ylabel("Number of occurrences")
    plt.legend()
    plt.xlim([0, 5000])
    plt.ylim([0, 50000])
    plt.savefig(PATH_LINPLOT)
    plt.clf()


def log_plot(data):
    degrees, freqs = zip(*data)
    plt.scatter(degrees, freqs, s=1, label="Degree distribution")
    plt.xscale('log')
    plt.yscale('log')
    plt.xlabel("Degree")
    plt.ylabel("Number of occurrences")
    plt.legend()
    plt.savefig(PATH_LOGPLOT)
    plt.clf()


def main(fdegrees):
    data = list()
    with open(fdegrees) as f:
        for line in f:
            d, f = line.strip().split()
            data.append((int(d), int(f)))
    lin_plot(data)
    log_plot(data)
    print "Plots saved as '{}' and '{}'.".format(PATH_LINPLOT, PATH_LOGPLOT)


if __name__ == '__main__':
    if len(sys.argv) != 2:
        print "Usage: {} path/to/degree_file".format(sys.argv[0])
        exit()
    main(sys.argv[1])
