import numpy
import matplotlib
import matplotlib.pyplot as plt
import sys

if (__name__ == '__main__'):
    if (len(sys.argv) != 2):
       print 'usage: python loglikelihood.py <loglikelihood-text-file>\n'
       sys.exit(1)
    
    data_file = sys.argv[1]
    d = numpy.loadtxt(data_file)
    plt.plot(d)
    plt.xlabel('Iterations')
    plt.ylabel('Log-likelihood')
    plt.title('Evolution of the log-likelihood')
    plt.show() 