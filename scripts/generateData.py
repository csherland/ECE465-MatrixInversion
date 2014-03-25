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
#    The output file location can be specified by <output file>
#
#    Further work could allow the user to specify dimenion range and number range
#    for randomly generated data
#

import sys
import random

if __name__ == "__main__":

    if len(sys.argv) is not 4:
        print "Usage Error: ./generateData <num_mats> <mat_size> <output file>"
        sys.exit(1)

    num_mats = int(sys.argv[1])

    # Open output file
    out = open(sys.argv[3], 'w')
    out.write(str(num_mats) + '\n')

    # Generate matrices
    for mat_num in range(num_mats):
        print "Generating matrix " + str(mat_num) + " of " + str(num_mats)

        # Calculate random dimension
        dimension = int(sys.argv[2]) #random.randint(500,1000)

        out.write(str(mat_num) + " " + str(dimension) + " ")
        for i in range(dimension):
            for j in range(dimension):
                out.write(str(random.randint(400, 3000)) + " ")

        out.write('\n')
