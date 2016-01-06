#!/usr/bin/env python
import json
import matplotlib.pyplot as plt
import numpy as np
import sys

from mpl_toolkits.mplot3d import Axes3D


TITLES_PATH = './movie_titles.txt'
MOVIES = [{  # dimension 0 - low:
    1569:  "The Bogus Witch Project (2000)",
    5213:  "Legion of the Dead (2002)",
    2426:  "Dark Harvest 2: The Maize (2004)",
    13354: "Vampires vs. Zombies (2004)",
    4437:  "Mari-Cookie and the Killer Tarantula (1998)",
  }, {  # dimension 0 - high:
    7751:  "Anne of Green Gables (1985)",
    13673: "Toy Story (1995)",
    17709: "A River Runs Through It (1992)",
    12162: "Close to Leo (2002)",
    12870: "Schindler's List (1993)",
  }, {  # dimension 1 - low:
    6143:  "Louisiana Story (1948)",
    17042: "Lamerica (1994)",
    4729:  "Ulysses (1967)",
    6225:  "The Magic Flute (1975)",
    1628:  "The Barchester Chronicles (1982)",
    9540:  "The Decalogue (1987)",
  }, {  # dimension 1 - high:
    5834:  "WWE: Royal Rumble 2003 (2003)",
    14518: "WWE: Unforgiven 2005 (2005)",
    9551:  "Drunks (1997)",
    10772: "Samurai Shodown (1994)",
    11744: "Dragon Ball: Tien Shinhan Saga (2002)",
    14762: "Devour (2005)",
    13234: "Mortal Kombat: Annihilation (1997)",
  }, {  # dimension 2 - low:
    4980:  "Dead or Alive 2 (2000)",
    14233: "Dirty Pretty Things (2002)",
    16447: "Another Woman's Husband (2000)",
    13258: "Gozu (2003)",
    7623:  "The Blonde (1992)",
    14657: "Tetsuo: The Iron Man (1992)",
  }, {  # dimension 2 - high:
    3455:  "Time Changer (2002)",
    9922:  "VeggieTales: Minnesota Cuke (2005)",
    3454:  "Kenny Chesney: Greatest Hits (2004)",
    8692:  "Shania Twain: Up Close and Personal (2004)",
    2036:  "'N Sync: PopOdyssey Live (2003)",
  }]


def main(feats, titles):
    colors = iter(['k', 'r', 'y', 'b', 'g', 'c', 'm'])
    fig = plt.figure()
    ax = Axes3D(fig)
    for chunk in MOVIES:
        color = next(colors)
        pts = [feats[key] for key in chunk]
        xs, ys, zs = zip(*pts)
        ax.scatter(xs, ys, zs, c=color)
        for key, val in chunk.iteritems():
            x, y, z = feats[key]
            ax.text(x + 0.1, y, z, val)
    ax.set_xlabel('dim 1') 
    ax.set_ylabel('dim 2')
    ax.set_zlabel('dim 3')
    plt.show()


def parse_files(feats_path, titles_path):
    feats = dict()
    for line in open(feats_path):
        vals = line.split(',')
        feats[int(vals[0])] = tuple(map(float, vals[1:]))
    titles = dict()
    for line in open(titles_path):
        idx, year, title = line.strip().split(',', 2)
        titles[int(idx)] = (title, year)
    return feats, titles


if __name__ == '__main__':
    if len(sys.argv) != 3:
        print "Usage: %s path/to/features path/to/titles" % sys.argv[0]
        exit()
    feats, titles = parse_files(sys.argv[1], sys.argv[2])
    main(feats, titles)
