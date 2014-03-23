#!/usr/bin/python

# Christian Sherland
# Michael Scibor
# Ethan Lusterman
#
# 3-21-14
#
# generateData.py
#   Generates valid matrix data for this project. Each line of the specified
#   output file is one matrix. The format of each line is as follows:
#
#       [matrix id number] [matrix dim] [element(1,1)], ...  [element(1, dim)],
#                                       [element(2,1)], ...  [element(2, dim)],
#                                                       ...
#                                       [element(dim,1)] ...  [element(dim, dim)]
#
#   Where line breaks are only included here for readability, elements are separated
#   by spaces and every element is an integer.
#
#   The script will generate random invertible matrices of random dimension between
#   500 and 1000. Invertibility will be guarenteed by checking the determinant of
#   each matrix.
#
#   The number of matrices (<num_mats>) generated will be specified by the user.
#

import sys

if __name__ == "__main__":

    if len(sys.argv) is not 2:
        print "Usage Error: ./generateData <num_mats>"
        sys.exit(1)

    num_mats = int(sys.argv[1])


